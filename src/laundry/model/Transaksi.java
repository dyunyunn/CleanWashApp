package laundry.model;

/**
 * Kelas Transaksi menghubungkan data Pelanggan dengan jenis Layanan yang dipilih.
 */
public class Transaksi {
    private Pelanggan pelanggan;
    private Layanan layanan;

    public Transaksi(Pelanggan pelanggan, Layanan layanan) {
        this.pelanggan = pelanggan;
        this.layanan = layanan;
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public Layanan getLayanan() {
        return layanan;
    }

    public double getTotalBayar() {
        return layanan.hitungBiaya(); 
    }
}
