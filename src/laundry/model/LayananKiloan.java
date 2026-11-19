package laundry.model;

/**
 * [PEWARISAN / SUBCLASS 1]
 * Turunan dari kelas abstrak Layanan dan mengimplementasikan interface EstimasiWaktu.
 */
public class LayananKiloan extends Layanan implements EstimasiWaktu {
    public static final double TARIF_REGULER = 6000.0;
    public static final double TARIF_EXPRESS = 11000.0;

    // [CONSTRUCTOR] Menggunakan super() untuk memanggil konstruktor kelas induk
    public LayananKiloan(double kuantitas, String jenisPaket) throws CucianInvalidException {
        super(kuantitas, jenisPaket);
    }

    // [POLIMORFISME - METHOD OVERRIDING] Tarif khusus layanan kiloan
    @Override
    public double hitungBiaya() {
        double tarif = jenisPaket.equals("Express") ? TARIF_EXPRESS : TARIF_REGULER;
        return kuantitas * tarif;
    }

    @Override
    public String getTipeLayanan() {
        return "Kiloan";
    }

    // [IMPLEMENTASI INTERFACE EstimasiWaktu]
    @Override
    public String getEstimasiSelesai() {
        return jenisPaket.equals("Express") ? "1 Hari Selesai" : "3 Hari Selesai";
    }
}
