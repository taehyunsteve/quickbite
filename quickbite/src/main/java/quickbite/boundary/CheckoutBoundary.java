package quickbite.boundary;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quickbite.boundary.dto.CheckoutRequest;
import quickbite.boundary.dto.CheckoutResponse;
import quickbite.controller.CheckoutController;
import quickbite.controller.dto.CheckoutResult;

/**
 * «boundary» CheckoutBoundary — realisasi CheckoutUI pada DCD 8b.
 * Hanya menangani protokol HTTP, validasi input, dan konversi DTO -> Command.
 */
@RestController
@RequestMapping("/api/checkout")
public class CheckoutBoundary {
    private final CheckoutController checkoutController;

    public CheckoutBoundary(CheckoutController checkoutController) {
        this.checkoutController = checkoutController;
    }

    /** [1] klikTombolCheckout(keranjangData) dari aktor Pelanggan. */
    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(
            @Valid @RequestBody CheckoutRequest request) {
        // [2] prosesCheckout(idPelanggan, keranjangData)
        CheckoutResult hasil = checkoutController.prosesCheckout(request.toCommand());
        // [7] checkoutBerhasil(idPesanan, total) -> HTTP 200 + CheckoutResponse
        return ResponseEntity.ok(new CheckoutResponse(hasil.idPesanan(), hasil.total()));
    }
}
