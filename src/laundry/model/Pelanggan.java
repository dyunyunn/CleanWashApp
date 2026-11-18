package laundry.model;

/**
 * Kelas Pelanggan merepresentasikan data identitas pelanggan laundry.
 */
public class Pelanggan {
    private String nama;
    private String noTelp;

    public Pelanggan(String nama, String noTelp) {
        this.nama = nama;
        this.noTelp = noTelp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }
}
