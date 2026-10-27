package quickbite.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quickbite.boundary.PaymentGatewayAPI;
import quickbite.controller.dto.CheckoutCommand;
import quickbite.controller.dto.CheckoutResult;
import quickbite.controller.exception.ResourceNotFoundException;
import quickbite.controller.exception.StokTidakCukupException;
import quickbite.entity.ItemPesanan;
import quickbite.entity.Menu;
import quickbite.entity.Pelanggan;
import quickbite.entity.Pembayaran;
import quickbite.entity.Pesanan;
import quickbite.entity.StatusPesanan;
import quickbite.repository.MenuRepository;
import quickbite.repository.PelangganRepository;
import quickbite.repository.PesananRepository;

/**
 * «controller» / «control» CheckoutController pada DCD 8b.
 * Direalisasikan sebagai @Service (bukan @RestController) karena bertugas
 * mengoordinasikan alur use case checkout dan batas transaksi.
 */
@Service
public class CheckoutController {
    private final PelangganRepository pelangganRepo;
    private final MenuRepository menuRepo;
    private final PesananRepository pesananRepo;
    private final PaymentGatewayAPI paymentGateway;

    public CheckoutController(
            PelangganRepository pelangganRepo,
            MenuRepository menuRepo,
            PesananRepository pesananRepo,
            PaymentGatewayAPI paymentGateway) {
        this.pelangganRepo = pelangganRepo;
        this.menuRepo = menuRepo;
        this.pesananRepo = pesananRepo;
        this.paymentGateway = paymentGateway;
    }

    /** [2] +prosesCheckout(cmd : CheckoutCommand) : CheckoutResult */
    @Transactional
    public CheckoutResult prosesCheckout(CheckoutCommand cmd) {
        // Persiapan: ambil entity Pelanggan dari repository
        Pelanggan pelanggan = pelangganRepo
                .findById(cmd.idPelanggan())
                .orElseThrow(() -> new ResourceNotFoundException("Pelanggan " + cmd.idPelanggan()));

        // [3] Loop setiap item keranjang: ambil Menu (pessimistic lock) & validasiStok(qty)
        List<ResolvedItem> items = new ArrayList<>();
        for (CheckoutCommand.Item ci : cmd.items()) {
            Menu menu = menuRepo
                    .findByIdForUpdate(ci.idMenu())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu " + ci.idMenu()));
            if (!menu.validasiStok(ci.qty())) {
                throw new StokTidakCukupException(ci.idMenu()); // alur alternatif: stok tidak cukup
            }
            items.add(new ResolvedItem(menu, ci.qty()));
        }

        // [4] new Pesanan + tambahItem(ItemPesanan) + hitungTotal()
        Pesanan pesanan = new Pesanan(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                pelanggan);

        for (ResolvedItem ri : items) {
            pesanan.tambahItem(new ItemPesanan(
                    UUID.randomUUID().toString(),
                    ri.qty(),
                    ri.menu()));
        }

        double total = pesanan.hitungTotal();

        // [5] mintaTransaksiPembayaran(total, tokenKartu) ke Boundary eksternal
        String kodeRef = paymentGateway.mintaTransaksiPembayaran(total, cmd.tokenKartu());

        // [6] new Pembayaran, kurangiStok(qty), updateStatus(DIPROSES), dan simpan Pesanan
        Pembayaran pembayaran = new Pembayaran(
                UUID.randomUUID().toString(),
                pesanan,
                total,
                "MIDTRANS",
                "SUKSES",
                kodeRef);
        pesanan.setPembayaran(pembayaran);

        for (ResolvedItem ri : items) {
            ri.menu().kurangiStok(ri.qty());
        }
        pesanan.updateStatus(StatusPesanan.DIPROSES);
        pesananRepo.save(pesanan);

        // [7] Kembalikan hasil checkout ke Boundary
        return new CheckoutResult(pesanan.getIdPesanan(), total);
    }

    private record ResolvedItem(Menu menu, int qty) { }
}
