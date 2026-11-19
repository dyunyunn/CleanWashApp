package laundry.model;

/**
 * [PEWARISAN / SUBCLASS 2]
 * Turunan dari kelas abstrak Layanan untuk cucian per potong (pcs).
 */
public class LayananSatuan extends Layanan implements EstimasiWaktu {
    public static final double TARIF_REGULER = 15000.0;
    public static final double TARIF_EXPRESS = 22500.0;

    // [CONSTRUCTOR] Menggunakan super() untuk memanggil konstruktor kelas induk
    public LayananSatuan(double kuantitas, String jenisPaket) throws CucianInvalidException {
        super(kuantitas, jenisPaket);
    }

    // [POLIMORFISME - METHOD OVERRIDING] Tarif khusus layanan satuan
    @Override
    public double hitungBiaya() {
        double tarif = jenisPaket.equals("Express") ? TARIF_EXPRESS : TARIF_REGULER;
        return kuantitas * tarif;
    }

    @Override
    public String getTipeLayanan() {
        return "Satuan";
    }

    // [IMPLEMENTASI INTERFACE EstimasiWaktu]
    @Override
    public String getEstimasiSelesai() {
        return jenisPaket.equals("Express") ? "1 Hari Selesai" : "2 Hari Selesai";
    }
}
