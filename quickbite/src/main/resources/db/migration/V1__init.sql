CREATE TABLE pelanggan (
    id_pelanggan VARCHAR(64) PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    nomor_telepon VARCHAR(30),
    alamat TEXT
);

CREATE TABLE menu (
    id_menu VARCHAR(64) PRIMARY KEY,
    nama_menu VARCHAR(100) NOT NULL,
    harga DOUBLE PRECISION NOT NULL,
    stok INT NOT NULL
);

CREATE TABLE pesanan (
    id_pesanan VARCHAR(64) PRIMARY KEY,
    tanggal TIMESTAMP NOT NULL,
    total_biaya DOUBLE PRECISION NOT NULL,
    status VARCHAR(30) NOT NULL,
    id_pelanggan VARCHAR(64) NOT NULL REFERENCES pelanggan(id_pelanggan)
);

CREATE TABLE item_pesanan (
    id_item VARCHAR(64) PRIMARY KEY,
    jumlah INT NOT NULL,
    subtotal DOUBLE PRECISION NOT NULL,
    id_pesanan VARCHAR(64) NOT NULL REFERENCES pesanan(id_pesanan),
    id_menu VARCHAR(64) NOT NULL REFERENCES menu(id_menu)
);

CREATE TABLE pembayaran (
    id_pembayaran VARCHAR(64) PRIMARY KEY,
    jumlah_bayar DOUBLE PRECISION NOT NULL,
    metode VARCHAR(50) NOT NULL,
    status_bayar VARCHAR(30) NOT NULL,
    kode_referensi VARCHAR(100),
    id_pesanan VARCHAR(64) NOT NULL UNIQUE REFERENCES pesanan(id_pesanan)
);

-- Data Seeder Sesuai Contoh Pengujian Slide Hal. 31-32
INSERT INTO pelanggan (id_pelanggan, nama, email, nomor_telepon, alamat)
VALUES ('P-01', 'Budi Santoso', 'budi@ub.ac.id', '081234567890', 'Jl. Veteran No. 1, Malang');

INSERT INTO menu (id_menu, nama_menu, harga, stok) VALUES
('M-01', 'Nasi Ayam Geprek QuickBite', 25000.0, 20),
('M-02', 'Kopi Susu Gula Aren', 25000.0, 10);
