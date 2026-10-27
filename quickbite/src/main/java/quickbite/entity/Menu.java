package quickbite.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** «entity» Menu — tabel menu; aturan stok dienkapsulasi di domain (DCD 8b). */
@Entity
@Table(name = "menu")
public class Menu {
    @Id
    @Column(name = "id_menu", length = 64)
    private String idMenu;

    @Column(name = "nama_menu", length = 100, nullable = false)
    private String namaMenu;

    @Column(name = "harga", nullable = false)
    private double harga;

    @Column(name = "stok", nullable = false)
    private int stok;

    protected Menu() { } // wajib untuk JPA

    public Menu(String idMenu, String namaMenu,
                double harga, int stok) {
        this.idMenu = idMenu;
        this.namaMenu = namaMenu;
        this.harga = harga;
        this.stok = stok;
    }

    /** [3] +validasiStok(qty : int) : boolean pada sequence 8b. */
    public boolean validasiStok(int qty) {
        return qty > 0 && stok >= qty;
    }

    /** [6] +kurangiStok(qty : int) : boolean pada sequence 8b. */
    public boolean kurangiStok(int qty) {
        if (!validasiStok(qty)) return false;
        this.stok -= qty;
        return true;
    }

    public String getIdMenu() { return idMenu; }
    public String getNamaMenu() { return namaMenu; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }
}
