package laundry.model;

/**
 * [KELAS INDUK / ABSTRACT CLASS]
 * Menjadi blueprint dasar untuk setiap jenis layanan laundry.
 */
public abstract class Layanan {
    // [RELASI ENTITAS / HAS-A RELATIONSHIP]
    // Menggunakan objek Pelanggan (Entitas Kedua), bukan sekadar String biasa
    protected Pelanggan pelanggan; 
    protected double kuantitas;
    protected String jenisPaket;

    // [CONSTRUCTOR]
    public Layanan(Pelanggan pelanggan, double kuantitas, String jenisPaket) {
        this.pelanggan = pelanggan;
        this.kuantitas = kuantitas;
        this.jenisPaket = jenisPaket;
    }

    // [METHOD ABSTRAK] Wajib di-override oleh kelas turunannya
    public abstract double hitungBiaya();

    // [GETTER]
    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public double getKuantitas() {
        return kuantitas;
    }

    public String getJenisPaket() {
        return jenisPaket;
    }
}
