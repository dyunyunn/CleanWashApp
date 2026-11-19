package laundry.model;

/**
 * [KELAS MODEL / ENTITAS 1]
 * Merepresentasikan data pelanggan laundry dengan penerapan enkapsulasi penuh
 * (atribut private + validasi pada setter).
 */
public class Pelanggan {
    // [ENKAPSULASI] Seluruh atribut bersifat private
    private String idPelanggan;
    private String nama;
    private String noHp;
    private String alamat;

    // [CONSTRUCTOR] Menginisialisasi data sekaligus menjalankan validasi setter
    public Pelanggan(String idPelanggan, String nama, String noHp, String alamat) throws CucianInvalidException {
        setIdPelanggan(idPelanggan);
        setNama(nama);
        setNoHp(noHp);
        setAlamat(alamat);
    }

    // [GETTER & SETTER BERVALIDASI]
    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) throws CucianInvalidException {
        if (idPelanggan == null || idPelanggan.trim().isEmpty()) {
            throw new CucianInvalidException("ID Pelanggan tidak boleh kosong!");
        }
        this.idPelanggan = idPelanggan.trim();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) throws CucianInvalidException {
        if (nama == null || nama.trim().isEmpty()) {
            throw new CucianInvalidException("Nama pelanggan tidak boleh kosong!");
        }
        // Ganti tanda titik koma (;) agar tidak merusak pemisah file .txt
        this.nama = nama.trim().replace(";", ",");
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) throws CucianInvalidException {
        if (noHp == null || !noHp.trim().matches("[0-9+\\-\\s]{8,15}")) {
            throw new CucianInvalidException("No. HP harus berupa angka dengan panjang 8 sampai 15 karakter!");
        }
        this.noHp = noHp.trim();
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) throws CucianInvalidException {
        if (alamat == null || alamat.trim().isEmpty()) {
            throw new CucianInvalidException("Alamat pelanggan tidak boleh kosong!");
        }
        this.alamat = alamat.trim().replace(";", ",");
    }

    /**
     * [METHOD OVERRIDING]
     * Mengatur tampilan teks saat objek Pelanggan dimasukkan ke JComboBox.
     */
    @Override
    public String toString() {
        return idPelanggan + " - " + nama;
    }
}
