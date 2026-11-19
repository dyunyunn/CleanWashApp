package laundry.model;

/**
 * [KELAS MODEL / ENTITAS 2]
 * Menghubungkan objek Pelanggan dan objek Layanan (Relasi Has-A / Komposisi).
 * Memanfaatkan referensi polimorfis Layanan untuk menghitung total biaya.
 */
public class Transaksi {
    // [ENKAPSULASI] Atribut private
    private String idTransaksi;
    private Pelanggan pelanggan;
    private Layanan layanan;
    private boolean antarJemput;
    private double diskonPersen;
    private double totalBiaya;
    private String estimasiSelesai;

    // [CONSTRUCTOR]
    public Transaksi(String idTransaksi, Pelanggan pelanggan, Layanan layanan,
                     boolean antarJemput, double diskonPersen) throws CucianInvalidException {
        setIdTransaksi(idTransaksi);
        setPelanggan(pelanggan);
        setLayanan(layanan);
        setAntarJemput(antarJemput);
        setDiskonPersen(diskonPersen);
        hitungUlang();
    }

    /**
     * [POLIMORFISME]
     * Memanggil method overloading hitungBiaya(diskon, antarJemput) melalui referensi
     * kelas induk Layanan, serta getEstimasiSelesai() melalui interface EstimasiWaktu.
     */
    public void hitungUlang() throws CucianInvalidException {
        this.totalBiaya = layanan.hitungBiaya(diskonPersen, antarJemput);
        if (layanan instanceof EstimasiWaktu) {
            this.estimasiSelesai = ((EstimasiWaktu) layanan).getEstimasiSelesai();
        } else {
            this.estimasiSelesai = "-";
        }
    }

    // [GETTER & SETTER BERVALIDASI]
    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) throws CucianInvalidException {
        if (idTransaksi == null || idTransaksi.trim().isEmpty()) {
            throw new CucianInvalidException("ID Transaksi tidak boleh kosong!");
        }
        this.idTransaksi = idTransaksi.trim();
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(Pelanggan pelanggan) throws CucianInvalidException {
        if (pelanggan == null) {
            throw new CucianInvalidException("Pelanggan wajib dipilih!");
        }
        this.pelanggan = pelanggan;
    }

    public Layanan getLayanan() {
        return layanan;
    }

    public void setLayanan(Layanan layanan) throws CucianInvalidException {
        if (layanan == null) {
            throw new CucianInvalidException("Layanan tidak boleh kosong!");
        }
        this.layanan = layanan;
    }

    public boolean isAntarJemput() {
        return antarJemput;
    }

    public void setAntarJemput(boolean antarJemput) {
        this.antarJemput = antarJemput;
    }

    public double getDiskonPersen() {
        return diskonPersen;
    }

    public void setDiskonPersen(double diskonPersen) throws CucianInvalidException {
        if (diskonPersen < 0 || diskonPersen > 100) {
            throw new CucianInvalidException("Diskon harus antara 0% sampai 100%!");
        }
        this.diskonPersen = diskonPersen;
    }

    public double getTotalBiaya() {
        return totalBiaya;
    }

    public String getEstimasiSelesai() {
        return estimasiSelesai;
    }
}
