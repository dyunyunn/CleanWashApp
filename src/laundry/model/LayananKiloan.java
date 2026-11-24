package laundry.model;

// Inheritance & Interface Implementation: mewarisi kelas abstrak Layanan dan mengimplementasikan EstimasiWaktu
public class LayananKiloan extends Layanan implements EstimasiWaktu {

    // Konstanta (public static final): tarif per kilogram untuk paket Reguler dan Express
    public static final double TARIF_REGULER = 6000.0;
    public static final double TARIF_EXPRESS = 11000.0;

    // Constructor berparameter (public): memanggil konstruktor kelas induk Layanan lewat super()
    public LayananKiloan(double kuantitas, String jenisPaket) throws CucianInvalidException {
        super(kuantitas, jenisPaket);
    }

    // Polymorphism (Method Overriding) - Return instance method (public): menghitung biaya khusus layanan kiloan
    @Override
    public double hitungBiaya() {
        double tarif = jenisPaket.equals("Express") ? TARIF_EXPRESS : TARIF_REGULER;
        return kuantitas * tarif;
    }

    // Polymorphism (Method Overriding) - Getter / Return instance method (public): mengembalikan tipe "Kiloan"
    @Override
    public String getTipeLayanan() {
        return "Kiloan";
    }

    // Interface Implementation (Method Overriding) - Getter / Return instance method (public): mengembalikan estimasi selesai kiloan
    @Override
    public String getEstimasiSelesai() {
        return jenisPaket.equals("Express") ? "1 Hari Selesai" : "3 Hari Selesai";
    }
}
