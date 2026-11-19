package laundry.model;

/**
 * [ABSTRAKSI & INDUK PEWARISAN / ABSTRACT CLASS]
 * Menjadi blueprint dasar untuk semua jenis layanan laundry.
 * Menerapkan enkapsulasi (protected + setter bervalidasi) dan polimorfisme overloading.
 */
public abstract class Layanan {
    // [KONSTANTA] Biaya tambahan layanan antar-jemput
    public static final double BIAYA_ANTAR_JEMPUT = 5000.0;

    // [ENKAPSULASI] Atribut protected agar bisa diakses oleh subclass
    protected double kuantitas;
    protected String jenisPaket;

    // [CONSTRUCTOR]
    public Layanan(double kuantitas, String jenisPaket) throws CucianInvalidException {
        setKuantitas(kuantitas);
        setJenisPaket(jenisPaket);
    }

    // [METHOD ABSTRAK] Wajib di-override oleh setiap kelas turunan
    public abstract double hitungBiaya();
    public abstract String getTipeLayanan();

    /**
     * [POLIMORFISME - METHOD OVERLOADING 1]
     * Menghitung biaya dasar dengan potongan diskon persentase.
     */
    public double hitungBiaya(double diskonPersen) throws CucianInvalidException {
        if (diskonPersen < 0 || diskonPersen > 100) {
            throw new CucianInvalidException("Diskon harus berada di antara 0% sampai 100%!");
        }
        double subtotal = hitungBiaya();
        double potongan = subtotal * (diskonPersen / 100.0);
        return subtotal - potongan;
    }

    /**
     * [POLIMORFISME - METHOD OVERLOADING 2]
     * Menghitung biaya dengan diskon persentase serta opsi tambahan antar-jemput.
     */
    public double hitungBiaya(double diskonPersen, boolean antarJemput) throws CucianInvalidException {
        double biayaSetelahDiskon = hitungBiaya(diskonPersen);
        if (antarJemput) {
            biayaSetelahDiskon += BIAYA_ANTAR_JEMPUT;
        }
        return biayaSetelahDiskon;
    }

    // [GETTER & SETTER BERVALIDASI]
    public double getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(double kuantitas) throws CucianInvalidException {
        if (kuantitas <= 0) {
            throw new CucianInvalidException("Berat (kg) atau jumlah potong harus lebih dari 0!");
        }
        this.kuantitas = kuantitas;
    }

    public String getJenisPaket() {
        return jenisPaket;
    }

    public void setJenisPaket(String jenisPaket) throws CucianInvalidException {
        if (jenisPaket == null || (!jenisPaket.equals("Reguler") && !jenisPaket.equals("Express"))) {
            throw new CucianInvalidException("Jenis paket harus Reguler atau Express!");
        }
        this.jenisPaket = jenisPaket;
    }
}
