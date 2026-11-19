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

/**
 * [LAPISAN DATA ACCESS / FILE I/O]
 * Menangani pembacaan dan penulisan data permanen untuk entitas Pelanggan dan Transaksi
 * ke dalam file teks menggunakan pemisah titik koma (;).
 */
public class FileStorage {
    private static final String FILE_PELANGGAN = "data_pelanggan.txt";
    private static final String FILE_TRANSAKSI = "data_transaksi.txt";

    /**
     * [FILE I/O - WRITE] Menyimpan seluruh objek List<Pelanggan> ke file teks.
     */
    public static void simpanSemuaPelanggan(List<Pelanggan> daftarPelanggan) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PELANGGAN, false))) {
            for (Pelanggan p : daftarPelanggan) {
                bw.write(p.getIdPelanggan() + ";" + p.getNama() + ";" + p.getNoHp() + ";" + p.getAlamat());
                bw.newLine();
            }
        }
    }

    /**
     * [FILE I/O - READ & GENERICS] Memuat data pelanggan dari file ke dalam List<Pelanggan>.
     */
    public static List<Pelanggan> muatSemuaPelanggan() throws IOException {
        List<Pelanggan> hasil = new ArrayList<>();
        File file = new File(FILE_PELANGGAN);
        if (!file.exists()) {
            return hasil;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                String[] data = baris.split(";");
                if (data.length == 4) {
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

    /**
     * [FILE I/O - WRITE] Menyimpan seluruh objek List<Transaksi> ke file teks.
     */
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

    /**
     * [FILE I/O - READ & GENERICS] Memuat data transaksi dari file dan menghubungkannya
     * kembali dengan objek Pelanggan melalui Map<String, Pelanggan>.
     */
    public static List<Transaksi> muatSemuaTransaksi(Map<String, Pelanggan> petaPelanggan) throws IOException {
        List<Transaksi> hasil = new ArrayList<>();
        File file = new File(FILE_TRANSAKSI);
        if (!file.exists()) {
            return hasil;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                String[] data = baris.split(";");
                if (data.length == 8) {
                    try {
                        String idTrx = data[0];
                        String idPlg = data[1];
                        String namaPlg = data[2];
                        String tipe = data[3];
                        String paket = data[4];
                        double kuantitas = Double.parseDouble(data[5]);
                        boolean antarJemput = Boolean.parseBoolean(data[6]);
                        double diskon = Double.parseDouble(data[7]);

                        // Cari objek Pelanggan dari Map; jika sudah dihapus, buat objek arsip sementara
                        Pelanggan pelanggan = petaPelanggan.get(idPlg);
                        if (pelanggan == null) {
                            pelanggan = new Pelanggan(idPlg, namaPlg, "00000000", "-");
                        }

                        // [POLIMORFISME] Instansiasi subclass berdasarkan tipe layanan
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