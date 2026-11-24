package laundry.model;

// Abstraction (Interface): kontrak perilaku untuk menentukan estimasi waktu selesai cucian
public interface EstimasiWaktu {

    // Abstract return method (public abstract implisit): wajib diimplementasikan oleh kelas layanan
    String getEstimasiSelesai();
}