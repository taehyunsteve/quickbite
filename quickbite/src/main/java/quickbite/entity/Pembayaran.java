package quickbite.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/** «entity» Pembayaran — tabel pembayaran (DCD 8b). */
@Entity
@Table(name = "pembayaran")
public class Pembayaran {
    @Id
    @Column(name = "id_pembayaran", length = 64)
    private String idPembayaran;

    @Column(name = "jumlah_bayar", nullable = false)
    private double jumlahBayar;

    @Column(name = "metode", length = 50, nullable = false)
    private String metode;

    @Column(name = "status_bayar", length = 30, nullable = false)
    private String statusBayar;

    @Column(name = "kode_referensi", length = 100)
    private String kodeReferensi;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pesanan", nullable = false, unique = true)
    private Pesanan pesanan;

    protected Pembayaran() { } // wajib untuk JPA

    public Pembayaran(String id, Pesanan pesanan, double jumlahBayar,
                      String metode, String statusBayar,
                      String kodeReferensi) {
        this.idPembayaran = id;
        this.pesanan = pesanan;
        this.jumlahBayar = jumlahBayar;
        this.metode = metode;
        this.statusBayar = statusBayar;
        this.kodeReferensi = kodeReferensi;
    }

    /** +prosesVerifikasi() : boolean pada DCD 8b. */
    public boolean prosesVerifikasi() {
        return "SUKSES".equalsIgnoreCase(statusBayar)
                && kodeReferensi != null;
    }

    public String getIdPembayaran() { return idPembayaran; }
    public double getJumlahBayar() { return jumlahBayar; }
    public String getMetode() { return metode; }
    public String getStatusBayar() { return statusBayar; }
    public String getKodeReferensi() { return kodeReferensi; }
    public Pesanan getPesanan() { return pesanan; }
}
