package quickbite.boundary;

/** «boundary» eksternal PaymentGatewayAPI (outbound) pada DCD 8b. */
public interface PaymentGatewayAPI {
    /** [5] +mintaTransaksiPembayaran(total : double, tokenKartu : String) : String */
    String mintaTransaksiPembayaran(double total, String tokenKartu);
}
