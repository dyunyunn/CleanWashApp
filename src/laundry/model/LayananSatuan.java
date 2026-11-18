package laundry.model;

/**
 * Kelas LayananSatuan merupakan kelas turunan (subclass) dari kelas induk Layanan.
 * Kelas ini menghitung biaya cucian berdasarkan jumlah barang (satuan/pcs).
 */
public class LayananSatuan extends Layanan implements EstimasiWaktu {

    /**
     * Constructor turunan yang menggunakan 'super' untuk mewarisi data dari kelas induk.
     */
    public LayananSatuan(String namaPelanggan, double kuantitas, String jenisPaket) {
        super(namaPelanggan, kuantitas, jenisPaket);
    }

    /**
     * Method hitungBiaya (Overriding):
     * Menghitung total biaya khusus layanan satuan per item/pcs.
     * Tarif Reguler: Rp 10.000 per pcs
     * Tarif Express: Rp 15.000 per pcs
     */
    @Override
    public double hitungBiaya() {
        double tarifPerPcs = jenisPaket.equals("Express") ? 15000 : 10000;
        return kuantitas * tarifPerPcs;
    }

    /**
     * Method getEstimasiSelesai (Implementasi dari Interface EstimasiWaktu):
     * Menentukan estimasi waktu pengerjaan untuk layanan satuan.
     */
    @Override
    public String getEstimasiSelesai() {
        return jenisPaket.equals("Express") ? "1 Hari Selesai" : "2 Hari Selesai";
    }
}
