package laundry.model;

/**
 * [ENTITAS KEDUA / RELASI KELAS]
 * Kelas ini membuktikan penerapan relasi antar-objek (Has-A relationship).
 */
public class Pelanggan {
    // [ATRIBUT]
    private String nama;

    // [CONSTRUCTOR] Ini yang dicari oleh FormTransaksi (Menerima parameter String)
    public Pelanggan(String nama) {
        this.nama = nama;
    }

    // [GETTER]
    public String getNama() {
        return nama;
    }
}
