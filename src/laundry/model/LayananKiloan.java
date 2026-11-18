package laundry.model;

/**
 * [KELAS TURUNAN / SUBCLASS]
 * Menginduk ke Layanan dan mengimplementasikan antarmuka EstimasiWaktu.
 */
public class LayananKiloan extends Layanan implements EstimasiWaktu {
    
    // [CONSTRUCTOR] Meneruskan objek Entitas Pelanggan ke kelas Induk (super)
    public LayananKiloan(Pelanggan pelanggan, double kuantitas, String jenisPaket) {
        super(pelanggan, kuantitas, jenisPaket);
    }

    /**
     * [METHOD OVERRIDING] 
     * Menghitung total biaya khusus tarif kiloan.
     */
    @Override
    public double hitungBiaya() {
        double tarif = jenisPaket.equals("Express") ? 11000 : 6000;
        return kuantitas * tarif;
    }

    /**
     * [IMPLEMENTASI INTERFACE]
     */
    @Override
    public String getEstimasiSelesai() {
        return jenisPaket.equals("Express") ? "1 Hari Selesai" : "3 Hari Selesai";
    }
}
