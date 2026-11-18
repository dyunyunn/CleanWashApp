package laundry.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Kelas FileStorage menangani penyimpanan data transaksi secara permanen 
 * ke dalam file teks eksternal (data_laundry.txt).
 */
public class FileStorage {
    private static final String FILE_NAME = "data_laundry.txt";

    public static void simpanData(String barisData) {
        // Menggunakan mode append (true) agar data baru ditambahkan di bawahnya (tidak menimpa)
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(barisData);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Gagal menyimpan file: " + e.getMessage());
        }
    }
}