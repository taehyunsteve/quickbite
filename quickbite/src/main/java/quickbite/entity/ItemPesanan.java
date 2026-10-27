package quickbite.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/** «entity» ItemPesanan — komposisi dengan Pesanan, agregasi dengan Menu (DCD 8b). */
@Entity
@Table(name = "item_pesanan")
public class ItemPesanan {
    @Id
    @Column(name = "id_item", length = 64)
    private String idItem;

    @Column(name = "jumlah", nullable = false)
    private int jumlah;

    @Column(name = "subtotal", nullable = false)
    private double subtotal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pesanan", nullable = false)
    private Pesanan pesanan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_menu", nullable = false)
    private Menu menu;

    protected ItemPesanan() { } // wajib untuk JPA

    public ItemPesanan(String idItem, int jumlah, Menu menu) {
        this.idItem = idItem;
        this.jumlah = jumlah;
        this.menu = menu;
    }

    /** +hitungSubtotal() : double pada DCD 8b. */
    public double hitungSubtotal() {
        this.subtotal = menu.getHarga() * jumlah;
        return subtotal;
    }

    void setPesanan(Pesanan pesanan) {
        this.pesanan = pesanan;
    }

    public String getIdItem() { return idItem; }
    public int getJumlah() { return jumlah; }
    public double getSubtotal() { return subtotal; }
    public Pesanan getPesanan() { return pesanan; }
    public Menu getMenu() { return menu; }
}
