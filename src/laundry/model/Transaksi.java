package laundry.model;

// MVC Model (Entitas 2) & Komposisi (Has-A): menghubungkan objek Pelanggan dan Layanan dalam satu transaksi
public class Transaksi {

    // Encapsulation (private) & Komposisi: menyimpan referensi objek Pelanggan, Layanan, dan rincian transaksi
    private String idTransaksi;
    private Pelanggan pelanggan;
    private Layanan layanan;
    private boolean antarJemput;
    private double diskonPersen;
    private double totalBiaya;
    private String estimasiSelesai;

    // Constructor berparameter (public) & Exception Handling (throws): menginisialisasi data lewat setter lalu menghitung biaya awal
    public Transaksi(String idTransaksi, Pelanggan pelanggan, Layanan layanan,
                     boolean antarJemput, double diskonPersen) throws CucianInvalidException {
        setIdTransaksi(idTransaksi);
        setPelanggan(pelanggan);
        setLayanan(layanan);
        setAntarJemput(antarJemput);
        setDiskonPersen(diskonPersen);
        hitungUlang();
    }

    // Void instance method (public) & Polymorphism: menghitung total biaya lewat referensi induk Layanan dan estimasi lewat interface EstimasiWaktu
    public void hitungUlang() throws CucianInvalidException {
        this.totalBiaya = layanan.hitungBiaya(diskonPersen, antarJemput);
        // Pengecekan instanceof & Type Casting ke interface EstimasiWaktu
        if (layanan instanceof EstimasiWaktu) {
            this.estimasiSelesai = ((EstimasiWaktu) layanan).getEstimasiSelesai();
        } else {
            this.estimasiSelesai = "-";
        }
    }

    // Getter / Return instance method (public): mengambil ID transaksi
    public String getIdTransaksi() {
        return idTransaksi;
    }

    // Setter / Void instance method (public) & Custom Exception: memvalidasi dan mengubah ID transaksi
    public void setIdTransaksi(String idTransaksi) throws CucianInvalidException {
        if (idTransaksi == null || idTransaksi.trim().isEmpty()) {
            throw new CucianInvalidException("ID Transaksi tidak boleh kosong!");
        }
        this.idTransaksi = idTransaksi.trim();
    }

    // Getter / Return instance method (public): mengambil objek Pelanggan pemilik transaksi
    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    // Setter / Void instance method (public) & Custom Exception: memvalidasi dan mengubah objek Pelanggan
    public void setPelanggan(Pelanggan pelanggan) throws CucianInvalidException {
        if (pelanggan == null) {
            throw new CucianInvalidException("Pelanggan wajib dipilih!");
        }
        this.pelanggan = pelanggan;
    }

    // Getter / Return instance method (public): mengambil objek Layanan (kiloan atau satuan)
    public Layanan getLayanan() {
        return layanan;
    }

    // Setter / Void instance method (public) & Custom Exception: memvalidasi dan mengubah objek Layanan
    public void setLayanan(Layanan layanan) throws CucianInvalidException {
        if (layanan == null) {
            throw new CucianInvalidException("Layanan tidak boleh kosong!");
        }
        this.layanan = layanan;
    }

    // Getter boolean / Return instance method (public): mengambil status opsi antar-jemput
    public boolean isAntarJemput() {
        return antarJemput;
    }

    // Setter / Void instance method (public): mengubah status opsi antar-jemput
    public void setAntarJemput(boolean antarJemput) {
        this.antarJemput = antarJemput;
    }

    // Getter / Return instance method (public): mengambil persentase diskon
    public double getDiskonPersen() {
        return diskonPersen;
    }

    // Setter / Void instance method (public) & Custom Exception: memvalidasi dan mengubah persentase diskon (0-100%)
    public void setDiskonPersen(double diskonPersen) throws CucianInvalidException {
        if (diskonPersen < 0 || diskonPersen > 100) {
            throw new CucianInvalidException("Diskon harus antara 0% sampai 100%!");
        }
        this.diskonPersen = diskonPersen;
    }

    // Getter / Return instance method (public): mengambil hasil akhir total biaya transaksi
    public double getTotalBiaya() {
        return totalBiaya;
    }

    // Getter / Return instance method (public): mengambil teks estimasi waktu selesai cucian
    public String getEstimasiSelesai() {
        return estimasiSelesai;
    }
}
