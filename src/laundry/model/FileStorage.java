package laundry.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Data Access Layer (File I/O): menangani penyimpanan dan pembacaan permanen data ke file teks (.txt)
public class FileStorage {

    // Konstanta (private static final): nama file penyimpanan lokal untuk pelanggan dan transaksi
    private static final String FILE_PELANGGAN = "data_pelanggan.txt";
    private static final String FILE_TRANSAKSI = "data_transaksi.txt";

    // Static void method (public), File I/O (Write), Generics, & Try-With-Resources: menulis seluruh List<Pelanggan> ke file .txt
    public static void simpanSemuaPelanggan(List<Pelanggan> daftarPelanggan) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PELANGGAN, false))) {
            for (Pelanggan p : daftarPelanggan) {
                bw.write(p.getIdPelanggan() + ";" + p.getNama() + ";" + p.getNoHp() + ";" + p.getAlamat());
                bw.newLine();
            }
        }
    }

    // Static return method (public), File I/O (Read), Collection/Generics, Array, & Exception Handling: membaca data pelanggan dari file .txt
    public static List<Pelanggan> muatSemuaPelanggan() throws IOException {
        List<Pelanggan> hasil = new ArrayList<>();
        File file = new File(FILE_PELANGGAN);
        if (!file.exists()) {
            return hasil;
        }

        // Try-With-Resources: menutup BufferedReader otomatis setelah pembacaan file selesai
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                // Array (String[]): memecah satu baris teks menjadi 4 kolom berdasarkan pemisah titik koma (;)
                String[] data = baris.split(";");
                if (data.length == 4) {
                    // Exception Handling (try-catch): melewati baris rusak agar pembacaan baris lain tetap berjalan
                    try {
                        hasil.add(new Pelanggan(data[0], data[1], data[2], data[3]));
                    } catch (CucianInvalidException e) {
                        System.err.println("Melewati baris pelanggan tidak valid: " + e.getMessage());
                    }
                }
            }
        }
        return hasil;
    }

    // Static void method (public), File I/O (Write), Generics, & Try-With-Resources: menulis seluruh List<Transaksi> ke file .txt
    public static void simpanSemuaTransaksi(List<Transaksi> daftarTransaksi) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_TRANSAKSI, false))) {
            for (Transaksi t : daftarTransaksi) {
                bw.write(
                    t.getIdTransaksi() + ";" +
                    t.getPelanggan().getIdPelanggan() + ";" +
                    t.getPelanggan().getNama() + ";" +
                    t.getLayanan().getTipeLayanan() + ";" +
                    t.getLayanan().getJenisPaket() + ";" +
                    t.getLayanan().getKuantitas() + ";" +
                    t.isAntarJemput() + ";" +
                    t.getDiskonPersen()
                );
                bw.newLine();
            }
        }
    }

    // Static return method (public), File I/O (Read), Collection/Generics, Array, & Polymorphism: membaca transaksi dan menautkannya ke Map<String, Pelanggan>
    public static List<Transaksi> muatSemuaTransaksi(Map<String, Pelanggan> petaPelanggan) throws IOException {
        List<Transaksi> hasil = new ArrayList<>();
        File file = new File(FILE_TRANSAKSI);
        if (!file.exists()) {
            return hasil;
        }

        // Try-With-Resources: membaca file transaksi baris demi baris secara aman
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                // Array (String[]): memecah baris teks transaksi menjadi 8 kolom data
                String[] data = baris.split(";");
                if (data.length == 8) {
                    // Exception Handling (multi-catch): menangkap error validasi data maupun konversi angka
                    try {
                        String idTrx = data[0];
                        String idPlg = data[1];
                        String namaPlg = data[2];
                        String tipe = data[3];
                        String paket = data[4];
                        double kuantitas = Double.parseDouble(data[5]);
                        boolean antarJemput = Boolean.parseBoolean(data[6]);
                        double diskon = Double.parseDouble(data[7]);

                        // Mengambil objek Pelanggan dari Map atau membuat objek arsip sementara jika pelanggan sudah dihapus
                        Pelanggan pelanggan = petaPelanggan.get(idPlg);
                        if (pelanggan == null) {
                            pelanggan = new Pelanggan(idPlg, namaPlg, "00000000", "-");
                        }

                        // Polymorphism (Upcasting): menyimpan objek LayananKiloan atau LayananSatuan pada referensi kelas induk Layanan
                        Layanan layanan = tipe.equals("Kiloan")
                                ? new LayananKiloan(kuantitas, paket)
                                : new LayananSatuan(kuantitas, paket);

                        hasil.add(new Transaksi(idTrx, pelanggan, layanan, antarJemput, diskon));
                    } catch (CucianInvalidException | NumberFormatException e) {
                        System.err.println("Melewati baris transaksi tidak valid: " + e.getMessage());
                    }
                }
            }
        }
        return hasil;
    }
}
