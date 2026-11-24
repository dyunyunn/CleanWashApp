package laundry.model;

// Inheritance & Interface Implementation: mewarisi kelas abstrak Layanan dan mengimplementasikan EstimasiWaktu
public class LayananSatuan extends Layanan implements EstimasiWaktu {

    // Konstanta (public static final): tarif per potong untuk paket Reguler dan Express
    public static final double TARIF_REGULER = 15000.0;
    public static final double TARIF_EXPRESS = 22500.0;

    // Constructor berparameter (public): memanggil konstruktor kelas induk Layanan lewat super()
    public LayananSatuan(double kuantitas, String jenisPaket) throws CucianInvalidException {
        super(kuantitas, jenisPaket);
    }

    // Polymorphism (Method Overriding) - Return instance method (public): menghitung biaya khusus layanan satuan
    @Override
    public double hitungBiaya() {
        double tarif = jenisPaket.equals("Express") ? TARIF_EXPRESS : TARIF_REGULER;
        return kuantitas * tarif;
    }

    // Polymorphism (Method Overriding) - Getter / Return instance method (public): mengembalikan tipe "Satuan"
    @Override
    public String getTipeLayanan() {
        return "Satuan";
    }

    // Interface Implementation (Method Overriding) - Getter / Return instance method (public): mengembalikan estimasi selesai satuan
    @Override
    public String getEstimasiSelesai() {
        return jenisPaket.equals("Express") ? "1 Hari Selesai" : "2 Hari Selesai";
    }
}