package laundry.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Kelas FileStorage berfungsi untuk menangani penyimpanan data transaksi 
 * secara permanen ke dalam file teks (.txt) di komputer.
 */
public class FileStorage {
    // Nama file tempat data transaksi akan disimpan
    private static final String FILE_NAME = "data_laundry.txt";

    /**
     * Method statis untuk menulis dan menyimpan data baris transaksi baru ke file.
     * Menggunakan FileWriter dengan mode 'append' (true) agar data lama tidak tertimpa.
     */
    public static void simpanData(String barisData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(barisData);
            bw.newLine(); // Membuat baris baru di dalam file txt
        } catch (IOException e) {
            System.out.println("Gagal menyimpan file: " + e.getMessage());
        }
    }
}
