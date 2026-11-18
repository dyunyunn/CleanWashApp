package laundry.model;

/**
 * [KELAS TURUNAN / SUBCLASS]
 */
public class LayananSatuan extends Layanan implements EstimasiWaktu {
    
    // [CONSTRUCTOR]
    public LayananSatuan(Pelanggan pelanggan, double kuantitas, String jenisPaket) {
        super(pelanggan, kuantitas, jenisPaket);
    }

    /**
     * [METHOD OVERRIDING]
     * Menghitung total biaya khusus tarif satuan/pcs.
     */
    @Override
    public double hitungBiaya() {
        double tarifPerPcs = jenisPaket.equals("Express") ? 15000 * 1.5 : 15000;
        return kuantitas * tarifPerPcs;
    }

    @Override
    public String getEstimasiSelesai() {
        return jenisPaket.equals("Express") ? "1 Hari Selesai" : "3 Hari Selesai";
    }
}
