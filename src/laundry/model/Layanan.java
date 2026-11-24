package laundry.model;

// Abstraction (Abstract Class) & Superclass: kerangka dasar untuk seluruh jenis layanan laundry
public abstract class Layanan {

    // Konstanta (public static final): tarif tetap tambahan untuk layanan antar-jemput
    public static final double BIAYA_ANTAR_JEMPUT = 5000.0;

    // Encapsulation (protected): atribut hanya dapat diakses di dalam kelas ini, paket yang sama, dan subclass
    protected double kuantitas;
    protected String jenisPaket;

    // Constructor berparameter (public) & Exception Handling (throws): menginisialisasi atribut lewat setter bervalidasi
    public Layanan(double kuantitas, String jenisPaket) throws CucianInvalidException {
        setKuantitas(kuantitas);
        setJenisPaket(jenisPaket);
    }

    // Abstract return method (public): wajib di-override oleh subclass untuk menghitung tarif dasar layanan
    public abstract double hitungBiaya();

    // Abstract return method (public): wajib di-override oleh subclass untuk mengembalikan nama tipe layanan
    public abstract String getTipeLayanan();

    // Polymorphism (Method Overloading 1) - Return instance method (public): menghitung biaya setelah potongan diskon
    public double hitungBiaya(double diskonPersen) throws CucianInvalidException {
        if (diskonPersen < 0 || diskonPersen > 100) {
            // Custom Exception (throw): melempar error jika persentase diskon di luar 0-100%
            throw new CucianInvalidException("Diskon harus berada di antara 0% sampai 100%!");
        }
        double subtotal = hitungBiaya();
        double potongan = subtotal * (diskonPersen / 100.0);
        return subtotal - potongan;
    }

    // Polymorphism (Method Overloading 2) - Return instance method (public): menghitung biaya dengan diskon dan opsi antar-jemput
    public double hitungBiaya(double diskonPersen, boolean antarJemput) throws CucianInvalidException {
        double biayaSetelahDiskon = hitungBiaya(diskonPersen);
        if (antarJemput) {
            biayaSetelahDiskon += BIAYA_ANTAR_JEMPUT;
        }
        return biayaSetelahDiskon;
    }

    // Getter / Return instance method (public): mengambil nilai kuantitas (berat atau jumlah item)
    public double getKuantitas() {
        return kuantitas;
    }

    // Setter / Void instance method (public) & Exception Handling: memvalidasi dan mengubah nilai kuantitas
    public void setKuantitas(double kuantitas) throws CucianInvalidException {
        if (kuantitas <= 0) {
            throw new CucianInvalidException("Berat (kg) atau jumlah potong harus lebih dari 0!");
        }
        this.kuantitas = kuantitas;
    }

    // Getter / Return instance method (public): mengambil jenis paket layanan
    public String getJenisPaket() {
        return jenisPaket;
    }

    // Setter / Void instance method (public) & Exception Handling: memvalidasi dan mengubah jenis paket
    public void setJenisPaket(String jenisPaket) throws CucianInvalidException {
        if (jenisPaket == null || (!jenisPaket.equals("Reguler") && !jenisPaket.equals("Express"))) {
            throw new CucianInvalidException("Jenis paket harus Reguler atau Express!");
        }
        this.jenisPaket = jenisPaket;
    }
}
