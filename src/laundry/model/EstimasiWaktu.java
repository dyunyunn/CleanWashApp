package laundry.model;

/**
 * [ABSTRAKSI - INTERFACE]
 * Kontrak perilaku untuk menghitung estimasi waktu pengerjaan laundry.
 * Diimplementasikan oleh lebih dari satu kelas (LayananKiloan & LayananSatuan).
 */
public interface EstimasiWaktu {
    String getEstimasiSelesai();
}
