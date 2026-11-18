package laundry.model;

public abstract class Layanan {
    // Variabel utama yang akan diwariskan ke kelas anak (Kiloan & Satuan)
    protected String namaPelanggan;
    protected double kuantitas;
    protected String jenisPaket;

    // Constructor induk untuk menerima data transaksi
    public Layanan(String namaPelanggan, double kuantitas, String jenisPaket) {
        this.namaPelanggan = namaPelanggan;
        this.kuantitas = kuantitas;
        this.jenisPaket = jenisPaket;
    }

    // Method abstrak (wajib di-override oleh kelas anak untuk polimorfisme)
    public abstract double hitungBiaya();

    // Getter dan Setter standar
    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public double getKuantitas() {
        return kuantitas;
    }

    public String getJenisPaket() {
        return jenisPaket;
    }
}
