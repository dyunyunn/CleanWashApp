package laundry.model;

// MVC Model (Entitas 1): merepresentasikan data pelanggan dengan enkapsulasi penuh
public class Pelanggan {

    // Encapsulation (private): seluruh atribut hanya bisa diakses dan diubah lewat getter/setter
    private String idPelanggan;
    private String nama;
    private String noHp;
    private String alamat;

    // Constructor berparameter (public) & Exception Handling (throws): menginisialisasi data sekaligus memvalidasi lewat setter
    public Pelanggan(String idPelanggan, String nama, String noHp, String alamat) throws CucianInvalidException {
        setIdPelanggan(idPelanggan);
        setNama(nama);
        setNoHp(noHp);
        setAlamat(alamat);
    }

    // Getter / Return instance method (public): mengambil ID pelanggan
    public String getIdPelanggan() {
        return idPelanggan;
    }

    // Setter / Void instance method (public) & Custom Exception (throw/throws): memvalidasi dan mengubah ID pelanggan
    public void setIdPelanggan(String idPelanggan) throws CucianInvalidException {
        if (idPelanggan == null || idPelanggan.trim().isEmpty()) {
            throw new CucianInvalidException("ID Pelanggan tidak boleh kosong!");
        }
        this.idPelanggan = idPelanggan.trim();
    }

    // Getter / Return instance method (public): mengambil nama pelanggan
    public String getNama() {
        return nama;
    }

    // Setter / Void instance method (public) & Custom Exception (throw/throws): memvalidasi dan membersihkan nama pelanggan
    public void setNama(String nama) throws CucianInvalidException {
        if (nama == null || nama.trim().isEmpty()) {
            throw new CucianInvalidException("Nama pelanggan tidak boleh kosong!");
        }
        // Mengganti tanda titik koma (;) agar tidak merusak pemisah kolom di file .txt
        this.nama = nama.trim().replace(";", ",");
    }

    // Getter / Return instance method (public): mengambil nomor HP pelanggan
    public String getNoHp() {
        return noHp;
    }

    // Setter / Void instance method (public) & Custom Exception (throw/throws): memvalidasi format nomor HP memakai regex
    public void setNoHp(String noHp) throws CucianInvalidException {
        if (noHp == null || !noHp.trim().matches("[0-9+\\-\\s]{8,15}")) {
            throw new CucianInvalidException("No. HP harus berupa angka dengan panjang 8 sampai 15 karakter!");
        }
        this.noHp = noHp.trim();
    }

    // Getter / Return instance method (public): mengambil alamat pelanggan
    public String getAlamat() {
        return alamat;
    }

    // Setter / Void instance method (public) & Custom Exception (throw/throws): memvalidasi dan mengubah alamat pelanggan
    public void setAlamat(String alamat) throws CucianInvalidException {
        if (alamat == null || alamat.trim().isEmpty()) {
            throw new CucianInvalidException("Alamat pelanggan tidak boleh kosong!");
        }
        this.alamat = alamat.trim().replace(";", ",");
    }

    // Polymorphism (Method Overriding dari kelas Object) - Return instance method (public): mengatur format teks saat tampil di JComboBox
    @Override
    public String toString() {
        return idPelanggan + " - " + nama;
    }
}
