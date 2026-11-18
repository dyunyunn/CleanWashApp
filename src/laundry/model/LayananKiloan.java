package laundry.model;

/**
 * Kelas LayananKiloan merupakan kelas turunan (subclass) dari kelas induk Layanan.
 * Kelas ini mengimplementasikan interface EstimasiWaktu untuk menentukan durasi pengerjaan cucian kiloan.
 */
public class LayananKiloan extends Layanan implements EstimasiWaktu {

    /**
     * Constructor untuk menginisialisasi atribut LayananKiloan.
     * Menggunakan kata kunci 'super' untuk meneruskan data ke constructor kelas induk (Layanan).
     */
    public LayananKiloan(String namaPelanggan, double kuantitas, String jenisPaket) {
        super(namaPelanggan, kuantitas, jenisPaket);
    }

    /**
     * Method hitungBiaya (Overriding):
     * Mengimplementasikan dan mengubah logika perhitungan biaya khusus untuk layanan kiloan.
     * Tarif Reguler: Rp 5.000 per kg
     * Tarif Express: Rp 7.000 per kg (dihitung dari kuantitas dikali tarif)
     */
    @Override
    public double hitungBiaya() {
        // Menentukan tarif berdasarkan jenis paket yang dipilih menggunakan ternary operator
        double tarif = jenisPaket.equals("Express") ? 7000 : 5000;
        return kuantitas * tarif;
    }

    /**
     * Method hitungBiaya dengan parameter tambahan (Overloading):
     * Digunakan jika ada fitur hitung biaya yang memperhitungkan diskon (persen).
     */
    public double hitungBiaya(double persenDiskon) {
        double totalAwal = hitungBiaya(); // Memanggil method hitungBiaya() di atas
        double potongan = totalAwal * (persenDiskon / 100);
        return totalAwal - potongan;
    }

    /**
     * Method getEstimasiSelesai (Implementasi dari Interface EstimasiWaktu):
     * Menentukan estimasi lama pengerjaan berdasarkan jenis paket kiloan.
     */
    @Override
    public String getEstimasiSelesai() {
        // Paket Express selesai dalam 1 hari, paket Reguler selesai dalam 2 hari
        return jenisPaket.equals("Express") ? "1 Hari Selesai" : "2 Hari Selesai";
    }
}