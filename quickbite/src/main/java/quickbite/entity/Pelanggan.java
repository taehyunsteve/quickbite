package quickbite.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/** «entity» Pelanggan — tabel pelanggan (DCD 8b). */
@Entity
@Table(name = "pelanggan")
public class Pelanggan {
    @Id
    @Column(name = "id_pelanggan", length = 64)
    private String idPelanggan;

    @Column(name = "nama", length = 100, nullable = false)
    private String nama;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "nomor_telepon", length = 30)
    private String nomorTelepon;

    @Column(name = "alamat")
    private String alamat;

    @OneToMany(mappedBy = "pelanggan")
    private List<Pesanan> daftarPesanan = new ArrayList<>();

    protected Pelanggan() { } // wajib untuk JPA

    public Pelanggan(String id, String nama, String email,
                     String telepon, String alamat) {
        this.idPelanggan = id;
        this.nama = nama;
        this.email = email;
        this.nomorTelepon = telepon;
        this.alamat = alamat;
    }

    public String getInfoPelanggan() {
        return nama + " <" + email + ">";
    }

    public void ubahAlamat(String alamatBaru) {
        this.alamat = alamatBaru;
    }

    public String getIdPelanggan() { return idPelanggan; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getNomorTelepon() { return nomorTelepon; }
    public String getAlamat() { return alamat; }
    public List<Pesanan> getDaftarPesanan() { return daftarPesanan; }
}
