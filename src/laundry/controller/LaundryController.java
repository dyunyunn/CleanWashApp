package laundry.controller;

import laundry.model.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// MVC Controller: penghubung antara lapisan View (GUI) dengan lapisan Model dan FileStorage
public class LaundryController {

    // Encapsulation, Collection & Generics: penyimpanan objek pelanggan dan transaksi secara dinamis di memori
    private final List<Pelanggan> daftarPelanggan = new ArrayList<>();
    private final Map<String, Pelanggan> petaPelanggan = new LinkedHashMap<>();
    private final List<Transaksi> daftarTransaksi = new ArrayList<>();

    // Constructor default (public): langsung memuat data permanen dari file saat controller dibuat
    public LaundryController() {
        muatSemuaData();
    }

    // Return instance method (public): memeriksa kecocokan username dan password petugas
    public boolean validasiLogin(String username, String password) {
        return username.equals("admin") && password.equals("12345");
    }

    // Void instance method (public), Exception Handling, & File I/O: membaca ulang data pelanggan dan transaksi dari file .txt
    public void muatSemuaData() {
        daftarPelanggan.clear();
        petaPelanggan.clear();
        daftarTransaksi.clear();

        // Exception Handling (try-catch): menangkap kegagalan pembacaan file agar aplikasi tidak berhenti mendadak
        try {
            List<Pelanggan> hasilPlg = FileStorage.muatSemuaPelanggan();
            for (Pelanggan p : hasilPlg) {
                daftarPelanggan.add(p);
                petaPelanggan.put(p.getIdPelanggan(), p);
            }

            List<Transaksi> hasilTrx = FileStorage.muatSemuaTransaksi(petaPelanggan);
            daftarTransaksi.addAll(hasilTrx);
        } catch (IOException e) {
            System.err.println("Gagal memuat data awal: " + e.getMessage());
        }
    }

    // Getter / Return instance method (public) & Generics: mengambil seluruh daftar pelanggan
    public List<Pelanggan> getDaftarPelanggan() {
        return daftarPelanggan;
    }

    // Return instance method (private) & Exception Handling: membuat ID pelanggan otomatis berurutan (PLG-001, dst.)
    private String buatIdPelangganBaru() {
        int max = 0;
        for (Pelanggan p : daftarPelanggan) {
            try {
                int angka = Integer.parseInt(p.getIdPelanggan().replace("PLG-", ""));
                if (angka > max) max = angka;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("PLG-%03d", max + 1);
    }

    // Return instance method (public), Exception Handling (throws), & File I/O: menambah pelanggan baru ke List/Map lalu simpan ke file (CRUD Create)
    public Pelanggan tambahPelanggan(String nama, String noHp, String alamat)
            throws CucianInvalidException, IOException {
        Pelanggan baru = new Pelanggan(buatIdPelangganBaru(), nama, noHp, alamat);
        daftarPelanggan.add(baru);
        petaPelanggan.put(baru.getIdPelanggan(), baru);
        FileStorage.simpanSemuaPelanggan(daftarPelanggan);
        return baru;
    }

    // Void instance method (public), Setter, Exception Handling (throw/throws), & File I/O: memperbarui data pelanggan lewat setter dan menyinkronkan file (CRUD Update)
    public void ubahPelanggan(String idPelanggan, String nama, String noHp, String alamat)
            throws CucianInvalidException, IOException {
        Pelanggan target = petaPelanggan.get(idPelanggan);
        if (target == null) {
            // Custom Exception (throw): melempar pengecualian jika ID pelanggan tidak ditemukan
            throw new CucianInvalidException("Data pelanggan tidak ditemukan!");
        }
        // Pemanggilan Setter pada model Pelanggan untuk mengubah sekaligus memvalidasi atribut private
        target.setNama(nama);
        target.setNoHp(noHp);
        target.setAlamat(alamat);
        FileStorage.simpanSemuaPelanggan(daftarPelanggan);
        // Menyinkronkan file transaksi agar perubahan nama pelanggan ikut tersimpan
        FileStorage.simpanSemuaTransaksi(daftarTransaksi);
    }

    // Void instance method (public), Exception Handling (throw/throws), & File I/O: menghapus pelanggan dari Map/List dan file (CRUD Delete)
    public void hapusPelanggan(String idPelanggan) throws CucianInvalidException, IOException {
        Pelanggan target = petaPelanggan.remove(idPelanggan);
        if (target == null) {
            throw new CucianInvalidException("Data pelanggan tidak ditemukan!");
        }
        daftarPelanggan.remove(target);
        FileStorage.simpanSemuaPelanggan(daftarPelanggan);
    }

    // Return instance method (public), Collection/Generics, & Getter: menyaring daftar pelanggan berdasarkan kata kunci
    public List<Pelanggan> cariPelanggan(String kataKunci) {
        if (kataKunci == null || kataKunci.trim().isEmpty()) {
            return new ArrayList<>(daftarPelanggan);
        }
        String key = kataKunci.trim().toLowerCase();
        List<Pelanggan> hasil = new ArrayList<>();
        for (Pelanggan p : daftarPelanggan) {
            if (p.getIdPelanggan().toLowerCase().contains(key) ||
                p.getNama().toLowerCase().contains(key) ||
                p.getNoHp().toLowerCase().contains(key) ||
                p.getAlamat().toLowerCase().contains(key)) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    // Getter / Return instance method (public) & Generics: mengambil seluruh daftar transaksi
    public List<Transaksi> getDaftarTransaksi() {
        return daftarTransaksi;
    }

    // Return instance method (private) & Exception Handling: membuat ID transaksi otomatis berurutan (TRX-001, dst.)
    private String buatIdTransaksiBaru() {
        int max = 0;
        for (Transaksi t : daftarTransaksi) {
            try {
                int angka = Integer.parseInt(t.getIdTransaksi().replace("TRX-", ""));
                if (angka > max) max = angka;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("TRX-%03d", max + 1);
    }

    // Return instance method (private) & Polymorphism (Upcasting): mengembalikan objek turunan LayananKiloan/LayananSatuan sebagai tipe induk Layanan
    private Layanan buatObjekLayanan(String tipeLayanan, double kuantitas, String jenisPaket)
            throws CucianInvalidException {
        if (tipeLayanan.equals("Kiloan")) {
            return new LayananKiloan(kuantitas, jenisPaket);
        } else {
            return new LayananSatuan(kuantitas, jenisPaket);
        }
    }

    // Return instance method (public) & Polymorphism (Overloading): menghitung estimasi biaya lewat method hitungBiaya berparameter diskon dan antar-jemput
    public double hitungEstimasiBiaya(String tipeLayanan, double kuantitas, String jenisPaket,
                                      boolean antarJemput, double diskonPersen) throws CucianInvalidException {
        Layanan layanan = buatObjekLayanan(tipeLayanan, kuantitas, jenisPaket);
        return layanan.hitungBiaya(diskonPersen, antarJemput);
    }

    // Return instance method (public), Exception Handling (throws), & File I/O: membuat transaksi baru lalu menyimpannya ke file (CRUD Create)
    public Transaksi tambahTransaksi(Pelanggan pelanggan, String tipeLayanan, double kuantitas,
                                     String jenisPaket, boolean antarJemput, double diskonPersen)
            throws CucianInvalidException, IOException {
        Layanan layanan = buatObjekLayanan(tipeLayanan, kuantitas, jenisPaket);
        Transaksi baru = new Transaksi(buatIdTransaksiBaru(), pelanggan, layanan, antarJemput, diskonPersen);
        daftarTransaksi.add(baru);
        FileStorage.simpanSemuaTransaksi(daftarTransaksi);
        return baru;
    }

    // Void instance method (public), Setter, Exception Handling (throw/throws), & File I/O: mengubah data transaksi lewat setter lalu hitung ulang (CRUD Update)
    public void ubahTransaksi(String idTransaksi, Pelanggan pelanggan, String tipeLayanan,
                              double kuantitas, String jenisPaket, boolean antarJemput, double diskonPersen)
            throws CucianInvalidException, IOException {
        Transaksi target = cariTransaksiById(idTransaksi);
        if (target == null) {
            throw new CucianInvalidException("Data transaksi tidak ditemukan!");
        }
        Layanan layananBaru = buatObjekLayanan(tipeLayanan, kuantitas, jenisPaket);
        // Pemanggilan Setter pada model Transaksi untuk memperbarui data secara terkontrol
        target.setPelanggan(pelanggan);
        target.setLayanan(layananBaru);
        target.setAntarJemput(antarJemput);
        target.setDiskonPersen(diskonPersen);
        target.hitungUlang();
        FileStorage.simpanSemuaTransaksi(daftarTransaksi);
    }

    // Void instance method (public), Exception Handling (throw/throws), & File I/O: menghapus transaksi terpilih dari List dan file (CRUD Delete)
    public void hapusTransaksi(String idTransaksi) throws CucianInvalidException, IOException {
        Transaksi target = cariTransaksiById(idTransaksi);
        if (target == null) {
            throw new CucianInvalidException("Data transaksi tidak ditemukan!");
        }
        daftarTransaksi.remove(target);
        FileStorage.simpanSemuaTransaksi(daftarTransaksi);
    }

    // Return instance method (public): mencari satu objek Transaksi berdasarkan ID secara persis
    public Transaksi cariTransaksiById(String idTransaksi) {
        for (Transaksi t : daftarTransaksi) {
            if (t.getIdTransaksi().equals(idTransaksi)) {
                return t;
            }
        }
        return null;
    }

    // Return instance method (public), Collection/Generics, & Getter: menyaring daftar transaksi sesuai kata kunci pencarian
    public List<Transaksi> cariTransaksi(String kataKunci) {
        if (kataKunci == null || kataKunci.trim().isEmpty()) {
            return new ArrayList<>(daftarTransaksi);
        }
        String key = kataKunci.trim().toLowerCase();
        List<Transaksi> hasil = new ArrayList<>();
        for (Transaksi t : daftarTransaksi) {
            if (t.getIdTransaksi().toLowerCase().contains(key) ||
                t.getPelanggan().getNama().toLowerCase().contains(key) ||
                t.getLayanan().getTipeLayanan().toLowerCase().contains(key) ||
                t.getLayanan().getJenisPaket().toLowerCase().contains(key)) {
                hasil.add(t);
            }
        }
        return hasil;
    }

    // Getter / Return instance method (public): mengembalikan jumlah total pelanggan untuk Dashboard
    public int getTotalPelanggan() {
        return daftarPelanggan.size();
    }

    // Getter / Return instance method (public): mengembalikan jumlah total transaksi untuk Dashboard
    public int getTotalTransaksi() {
        return daftarTransaksi.size();
    }

    // Getter / Return instance method (public): menghitung akumulasi total pendapatan dari semua transaksi
    public double getTotalPendapatan() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) {
            total += t.getTotalBiaya();
        }
        return total;
    }
}
