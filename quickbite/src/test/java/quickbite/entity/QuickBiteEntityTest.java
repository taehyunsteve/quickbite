package quickbite.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/**
 * Unit test method entity yang dipanggil control pada sequence diagram
 * (Sesuai standar Slide 53 pada 10a. Implementasi RSI Oke.pdf).
 * Berjalan tanpa database dan server.
 */
class QuickBiteEntityTest {

    @Test
    void validasiStok_dan_kurangiStok_pada_Menu() {
        Menu menu = new Menu("M-01", "Nasi Ayam Geprek", 25000.0, 2);

        assertTrue(menu.validasiStok(2));
        assertFalse(menu.validasiStok(3));
        assertFalse(menu.validasiStok(0));

        assertTrue(menu.kurangiStok(2));
        assertEquals(0, menu.getStok());
        assertFalse(menu.kurangiStok(1));
    }

    @Test
    void tambahItem_dan_hitungTotal_pada_Pesanan_dan_ItemPesanan() {
        Pelanggan pelanggan = new Pelanggan("P-01", "Budi", "budi@ub.ac.id", "08123", "Malang");
        Menu m1 = new Menu("M-01", "Ayam Geprek", 25000.0, 10);
        Menu m2 = new Menu("M-02", "Kopi Susu", 15000.0, 10);

        Pesanan pesanan = new Pesanan("ORD-01", LocalDateTime.now(), pelanggan);
        pesanan.tambahItem(new ItemPesanan("IT-01", 2, m1));
        pesanan.tambahItem(new ItemPesanan("IT-02", 1, m2));

        double total = pesanan.hitungTotal();
        assertEquals(65000.0, total);
        assertEquals(StatusPesanan.MENUNGGU_BAYAR, pesanan.getStatus());

        pesanan.updateStatus(StatusPesanan.DIPROSES);
        assertEquals(StatusPesanan.DIPROSES, pesanan.getStatus());
    }

    @Test
    void prosesVerifikasi_pada_Pembayaran() {
        Pelanggan pelanggan = new Pelanggan("P-01", "Budi", "budi@ub.ac.id", "08123", "Malang");
        Pesanan pesanan = new Pesanan("ORD-01", LocalDateTime.now(), pelanggan);

        Pembayaran sukses = new Pembayaran("PAY-01", pesanan, 50000.0, "MIDTRANS", "SUKSES", "REF-123");
        assertTrue(sukses.prosesVerifikasi());

        Pembayaran gagal = new Pembayaran("PAY-02", pesanan, 50000.0, "MIDTRANS", "GAGAL", null);
        assertFalse(gagal.prosesVerifikasi());
    }
}
