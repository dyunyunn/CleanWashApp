package laundry.model;

// Custom Exception & Inheritance: mewarisi Exception untuk menangani error validasi input bisnis laundry
public class CucianInvalidException extends Exception {

    // Konstanta serialisasi (private static final): menghilangkan peringatan warning di Eclipse
    private static final long serialVersionUID = 1L;

    // Constructor berparameter (public): meneruskan pesan error ke kelas induk Exception lewat super()
    public CucianInvalidException(String pesan) {
        super(pesan);
    }
}