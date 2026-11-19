package laundry.model;

/**
 * [CUSTOM EXCEPTION]
 * Exception khusus untuk menangani kesalahan input bisnis pada aplikasi CleanWashApp
 * (seperti nama kosong, berat <= 0, nomor HP tidak valid, atau diskon di luar 0-100%).
 */
public class CucianInvalidException extends Exception {

    // [CONSTRUCTOR] Meneruskan pesan error ke kelas induk Exception
    public CucianInvalidException(String pesan) {
        super(pesan);
    }
}