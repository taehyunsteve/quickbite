package quickbite.boundary;

import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import quickbite.controller.exception.PaymentFailedException;

/** Adapter outbound «boundary» untuk PaymentGatewayAPI. */
@Component
public class MidtransPaymentGateway implements PaymentGatewayAPI {
    private final RestClient restClient;
    private final boolean mockMode;

    public MidtransPaymentGateway(
            RestClient.Builder builder,
            @Value("${quickbite.payment.base-url:https://api.midtrans.com}") String baseUrl,
            @Value("${quickbite.payment.mock-enabled:true}") boolean mockMode) {
        this.restClient = builder.baseUrl(baseUrl).build();
        this.mockMode = mockMode;
    }

    @Override
    public String mintaTransaksiPembayaran(double total, String token) {
        if ("invalid_card".equalsIgnoreCase(token) || "tok_gagal".equalsIgnoreCase(token)) {
            throw new PaymentFailedException("Gateway menolak transaksi");
        }
        if (mockMode) {
            return "MID-REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        PaymentResponse res = restClient.post()
                .uri("/v1/transactions")
                .body(Map.of("total", total, "token", token))
                .retrieve()
                .body(PaymentResponse.class);
        if (res == null || !res.success()) {
            throw new PaymentFailedException("Gateway menolak transaksi");
        }
        return res.reference();
    }
}

record PaymentResponse(boolean success, String reference) { }
