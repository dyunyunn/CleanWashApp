package laundry.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import laundry.controller.LaundryController;
import laundry.model.CucianInvalidException;
import laundry.model.Pelanggan;
import laundry.model.Transaksi;

// Inheritance: mewarisi JFrame untuk halaman CRUD Entitas Transaksi (MVC - View halaman 4)
public class FormTransaksi extends JFrame {
    private static final long serialVersionUID = 1L;

    // Encapsulation, Komposisi MVC, & Generics: komponen input bervariasi berakses private
    private final LaundryController controller;
    private JTextField txtIdTransaksi;
    private JTextField txtCari;
    private JComboBox<Pelanggan> cbPelanggan;
    private JComboBox<String> cbTipe;
    private JRadioButton rbReguler;
    private JRadioButton rbExpress;
    private ButtonGroup grupPaket;
    private JSpinner spnKuantitas;
    private JSpinner spnDiskon;
    private JCheckBox chkAntarJemput;
    private JLabel lblEstimasiBiaya;

    // Komponen tombol aksi dan tabel transaksi
    private JButton btnTambah;
    private JButton btnUbah;
    private JButton btnHapus;
    private JButton btnBersihkan;
    private JButton btnKembali;
    private JTable tabelTransaksi;
    private DefaultTableModel modelTabel;

    // Komponen menu navigasi bar
    private JMenuItem itemMenuUtama;
    private JMenuItem itemPelanggan;
    private JMenuItem itemLogOut;
    private JMenuItem itemKeluar;
    private JMenuItem itemTentang;

    // Constructor default (Overloading & Chaining): memanggil konstruktor utama dengan null agar kompatibel WindowBuilder
    public FormTransaksi() {
        this(null);
    }

    // Constructor berparameter (Overloading): menyiapkan GUI, event, daftar pelanggan, tabel transaksi, dan estimasi awal
    public FormTransaksi(LaundryController controller) {
        this.controller = controller;
        inisialisasiGUI();
        pasangEvent();
        if (this.controller != null) {
            muatComboPelanggan();
            muatTabel(this.controller.getDaftarTransaksi());
            perbaruiEstimasi();
        }
    }

    // Void instance method (private): menyusun antarmuka form 2 kolom (GridBagLayout), tombol CRUD, pencarian, dan JTable
    private void inisialisasiGUI() {
        setTitle("Kelola Transaksi Laundry - CleanWashApp");
        setSize(920, 620);
        setMinimumSize(new Dimension(820, 540));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu bar navigasi antarhalaman
        JMenuBar menuBar = new JMenuBar();
        JMenu menuNavigasi = new JMenu("Menu");
        itemMenuUtama = new JMenuItem("Menu Utama");
        itemPelanggan = new JMenuItem("Kelola Data Pelanggan");
        itemLogOut = new JMenuItem("Log Out");
        itemKeluar = new JMenuItem("Keluar Aplikasi");

        menuNavigasi.add(itemMenuUtama);
        menuNavigasi.add(itemPelanggan);
        menuNavigasi.addSeparator();
        menuNavigasi.add(itemLogOut);
        menuNavigasi.add(itemKeluar);

        JMenu menuBantuan = new JMenu("Bantuan");
        itemTentang = new JMenuItem("Tentang CleanWashApp");
        menuBantuan.add(itemTentang);

        menuBar.add(menuNavigasi);
        menuBar.add(menuBantuan);
        setJMenuBar(menuBar);

        // Panel utama dengan BorderLayout
        JPanel panelUtama = new JPanel(new BorderLayout(10, 10));
        setContentPane(panelUtama);

        // Panel atas menampung form input 2 kolom dan tombol aksi
        JPanel panelUtara = new JPanel(new BorderLayout());

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Form Input Transaksi Laundry"));

        // Kolom kiri baris 0: ID transaksi otomatis (read-only)
        JLabel lblIdTransaksi = new JLabel("ID Transaksi:");
        GridBagConstraints gbcLblId = new GridBagConstraints();
        gbcLblId.insets = new Insets(5, 10, 5, 10);
        gbcLblId.anchor = GridBagConstraints.WEST;
        gbcLblId.gridx = 0;
        gbcLblId.gridy = 0;
        panelForm.add(lblIdTransaksi, gbcLblId);

        txtIdTransaksi = new JTextField("(Otomatis)", 15);
        txtIdTransaksi.setEditable(false);
        GridBagConstraints gbcTxtId = new GridBagConstraints();
        gbcTxtId.insets = new Insets(5, 10, 5, 10);
        gbcTxtId.anchor = GridBagConstraints.WEST;
        gbcTxtId.gridx = 1;
        gbcTxtId.gridy = 0;
        panelForm.add(txtIdTransaksi, gbcTxtId);

        // Kolom kiri baris 1: pilihan objek Pelanggan menggunakan JComboBox
        JLabel lblPelanggan = new JLabel("Pelanggan:");
        GridBagConstraints gbcLblPelanggan = new GridBagConstraints();
        gbcLblPelanggan.insets = new Insets(5, 10, 5, 10);
        gbcLblPelanggan.anchor = GridBagConstraints.WEST;
        gbcLblPelanggan.gridx = 0;
        gbcLblPelanggan.gridy = 1;
        panelForm.add(lblPelanggan, gbcLblPelanggan);

        cbPelanggan = new JComboBox<>();
        cbPelanggan.setPreferredSize(new Dimension(210, 25));
        GridBagConstraints gbcCbPelanggan = new GridBagConstraints();
        gbcCbPelanggan.insets = new Insets(5, 10, 5, 10);
        gbcCbPelanggan.anchor = GridBagConstraints.WEST;
        gbcCbPelanggan.gridx = 1;
        gbcCbPelanggan.gridy = 1;
        panelForm.add(cbPelanggan, gbcCbPelanggan);

        // Kolom kiri baris 2: pilihan tipe layanan Kiloan atau Satuan
        JLabel lblTipe = new JLabel("Tipe Layanan:");
        GridBagConstraints gbcLblTipe = new GridBagConstraints();
        gbcLblTipe.insets = new Insets(5, 10, 5, 10);
        gbcLblTipe.anchor = GridBagConstraints.WEST;
        gbcLblTipe.gridx = 0;
        gbcLblTipe.gridy = 2;
        panelForm.add(lblTipe, gbcLblTipe);

        cbTipe = new JComboBox<>(new String[]{"Kiloan", "Satuan"});
        cbTipe.setPreferredSize(new Dimension(150, 25));
        GridBagConstraints gbcCbTipe = new GridBagConstraints();
        gbcCbTipe.insets = new Insets(5, 10, 5, 10);
        gbcCbTipe.anchor = GridBagConstraints.WEST;
        gbcCbTipe.gridx = 1;
        gbcCbTipe.gridy = 2;
        panelForm.add(cbTipe, gbcCbTipe);

        // Kolom kiri baris 3: pilihan paket Reguler/Express dengan JRadioButton dan ButtonGroup
        JLabel lblPaket = new JLabel("Pilihan Paket:");
        GridBagConstraints gbcLblPaket = new GridBagConstraints();
        gbcLblPaket.insets = new Insets(5, 10, 5, 10);
        gbcLblPaket.anchor = GridBagConstraints.WEST;
        gbcLblPaket.gridx = 0;
        gbcLblPaket.gridy = 3;
        panelForm.add(lblPaket, gbcLblPaket);

        JPanel panelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        rbReguler = new JRadioButton("Reguler", true);
        rbExpress = new JRadioButton("Express");
        grupPaket = new ButtonGroup();
        grupPaket.add(rbReguler);
        grupPaket.add(rbExpress);
        panelRadio.add(rbReguler);
        panelRadio.add(rbExpress);
        GridBagConstraints gbcPanelRadio = new GridBagConstraints();
        gbcPanelRadio.insets = new Insets(5, 10, 5, 10);
        gbcPanelRadio.anchor = GridBagConstraints.WEST;
        gbcPanelRadio.gridx = 1;
        gbcPanelRadio.gridy = 3;
        panelForm.add(panelRadio, gbcPanelRadio);

        // Kolom kanan baris 0: input berat atau jumlah item menggunakan JSpinner
        JLabel lblKuantitas = new JLabel("Berat (kg) / Jumlah:");
        GridBagConstraints gbcLblKuantitas = new GridBagConstraints();
        gbcLblKuantitas.insets = new Insets(5, 10, 5, 10);
        gbcLblKuantitas.anchor = GridBagConstraints.WEST;
        gbcLblKuantitas.gridx = 2;
        gbcLblKuantitas.gridy = 0;
        panelForm.add(lblKuantitas, gbcLblKuantitas);

        spnKuantitas = new JSpinner(new SpinnerNumberModel(1.0, 0.5, 100.0, 0.5));
        spnKuantitas.setPreferredSize(new Dimension(100, 25));
        GridBagConstraints gbcSpnKuantitas = new GridBagConstraints();
        gbcSpnKuantitas.insets = new Insets(5, 10, 5, 10);
        gbcSpnKuantitas.anchor = GridBagConstraints.WEST;
        gbcSpnKuantitas.gridx = 3;
        gbcSpnKuantitas.gridy = 0;
        panelForm.add(spnKuantitas, gbcSpnKuantitas);

        // Spacer kanan agar form tidak melar saat jendela diperbesar
        Component spacerKanan = Box.createHorizontalGlue();
        GridBagConstraints gbcSpacer = new GridBagConstraints();
        gbcSpacer.insets = new Insets(5, 10, 5, 10);
        gbcSpacer.anchor = GridBagConstraints.WEST;
        gbcSpacer.gridx = 4;
        gbcSpacer.gridy = 0;
        gbcSpacer.weightx = 1.0;
        panelForm.add(spacerKanan, gbcSpacer);

        // Kolom kanan baris 1: input diskon persen menggunakan JSpinner
        JLabel lblDiskon = new JLabel("Diskon (%):");
        GridBagConstraints gbcLblDiskon = new GridBagConstraints();
        gbcLblDiskon.insets = new Insets(5, 10, 5, 10);
        gbcLblDiskon.anchor = GridBagConstraints.WEST;
        gbcLblDiskon.gridx = 2;
        gbcLblDiskon.gridy = 1;
        panelForm.add(lblDiskon, gbcLblDiskon);

        spnDiskon = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100.0, 5.0));
        spnDiskon.setPreferredSize(new Dimension(100, 25));
        GridBagConstraints gbcSpnDiskon = new GridBagConstraints();
        gbcSpnDiskon.insets = new Insets(5, 10, 5, 10);
        gbcSpnDiskon.anchor = GridBagConstraints.WEST;
        gbcSpnDiskon.gridx = 3;
        gbcSpnDiskon.gridy = 1;
        panelForm.add(spnDiskon, gbcSpnDiskon);

        // Kolom kanan baris 2: opsi layanan tambahan antar-jemput menggunakan JCheckBox
        JLabel lblTambahan = new JLabel("Layanan Tambahan:");
        GridBagConstraints gbcLblTambahan = new GridBagConstraints();
        gbcLblTambahan.insets = new Insets(5, 10, 5, 10);
        gbcLblTambahan.anchor = GridBagConstraints.WEST;
        gbcLblTambahan.gridx = 2;
        gbcLblTambahan.gridy = 2;
        panelForm.add(lblTambahan, gbcLblTambahan);

        chkAntarJemput = new JCheckBox("Antar-Jemput (+Rp 5.000)");
        GridBagConstraints gbcChkAntarJemput = new GridBagConstraints();
        gbcChkAntarJemput.insets = new Insets(5, 10, 5, 10);
        gbcChkAntarJemput.anchor = GridBagConstraints.WEST;
        gbcChkAntarJemput.gridx = 3;
        gbcChkAntarJemput.gridy = 2;
        panelForm.add(chkAntarJemput, gbcChkAntarJemput);

        // Kolom kanan baris 3: label estimasi biaya otomatis secara real-time
        JLabel lblEstimasi = new JLabel("Estimasi Biaya:");
        GridBagConstraints gbcLblEstimasi = new GridBagConstraints();
        gbcLblEstimasi.insets = new Insets(5, 10, 5, 10);
        gbcLblEstimasi.anchor = GridBagConstraints.WEST;
        gbcLblEstimasi.gridx = 2;
        gbcLblEstimasi.gridy = 3;
        panelForm.add(lblEstimasi, gbcLblEstimasi);

        lblEstimasiBiaya = new JLabel("Rp 6.000");
        lblEstimasiBiaya.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblEstimasiBiaya.setForeground(new Color(0, 102, 153));
        GridBagConstraints gbcLblEstimasiBiaya = new GridBagConstraints();
        gbcLblEstimasiBiaya.insets = new Insets(5, 10, 5, 10);
        gbcLblEstimasiBiaya.anchor = GridBagConstraints.WEST;
        gbcLblEstimasiBiaya.gridx = 3;
        gbcLblEstimasiBiaya.gridy = 3;
        panelForm.add(lblEstimasiBiaya, gbcLblEstimasiBiaya);

        // Panel tombol aksi CRUD dengan FlowLayout
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        btnTambah = new JButton("Tambah");
        btnUbah = new JButton("Ubah");
        btnHapus = new JButton("Hapus");
        btnBersihkan = new JButton("Bersihkan");
        btnKembali = new JButton("Menu Utama");

        panelTombol.add(btnTambah);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        panelTombol.add(btnBersihkan);
        panelTombol.add(btnKembali);

        panelUtara.add(panelForm, BorderLayout.CENTER);
        panelUtara.add(panelTombol, BorderLayout.SOUTH);
        panelUtama.add(panelUtara, BorderLayout.NORTH);

        // Panel tengah menampung pencarian dan tabel transaksi
        JPanel panelTengah = new JPanel(new BorderLayout(5, 5));
        panelTengah.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JLabel lblCari = new JLabel("Cari Transaksi (ID/Nama/Tipe/Paket):");
        panelCari.add(lblCari);
        txtCari = new JTextField(24);
        panelCari.add(txtCari);
        panelTengah.add(panelCari, BorderLayout.NORTH);

        // Anonymous subclass DefaultTableModel dengan Overridden return method agar tabel read-only
        String[] kolom = {
            "ID", "Pelanggan", "Tipe", "Paket", "Berat/Jml",
            "Antar-Jemput", "Diskon", "Total Biaya", "Estimasi"
        };
        modelTabel = new DefaultTableModel(kolom, 0) {
            private static final long serialVersionUID = 1L;

            // Overridden return method (public): mengembalikan false agar sel tabel tidak bisa diedit langsung
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelTransaksi = new JTable(modelTabel);
        tabelTransaksi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabel = new JScrollPane(tabelTransaksi);
        panelTengah.add(scrollTabel, BorderLayout.CENTER);

        panelUtama.add(panelTengah, BorderLayout.CENTER);
    }

    // Void instance method (private) & Event Handling: mendaftarkan listener estimasi, CRUD, tabel, pencarian, dan menu bar
    private void pasangEvent() {
        // Listener perubahan input untuk memperbarui estimasi biaya otomatis
        cbTipe.addActionListener(e -> perbaruiEstimasi());
        rbReguler.addActionListener(e -> perbaruiEstimasi());
        rbExpress.addActionListener(e -> perbaruiEstimasi());
        spnKuantitas.addChangeListener(e -> perbaruiEstimasi());
        spnDiskon.addChangeListener(e -> perbaruiEstimasi());
        chkAntarJemput.addActionListener(e -> perbaruiEstimasi());

        // Listener tombol CRUD dan navigasi
        btnTambah.addActionListener(e -> tambahTransaksi());
        btnUbah.addActionListener(e -> ubahTransaksi());
        btnHapus.addActionListener(e -> hapusTransaksi());
        btnBersihkan.addActionListener(e -> bersihkanForm());
        btnKembali.addActionListener(e -> bukaMenuUtama());

        // Listener seleksi baris tabel untuk mengisi ulang form input
        tabelTransaksi.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                isiFormDariTabel();
            }
        });

        // Interface Implementation (DocumentListener) dengan 3 Overridden void method untuk pencarian real-time
        txtCari.getDocument().addDocumentListener(new DocumentListener() {
            // Overridden void method (public): dipanggil saat karakter ditambahkan ke kolom cari
            @Override
            public void insertUpdate(DocumentEvent e) { cariData(); }

            // Overridden void method (public): dipanggil saat karakter dihapus dari kolom cari
            @Override
            public void removeUpdate(DocumentEvent e) { cariData(); }

            // Overridden void method (public): dipanggil saat atribut teks kolom cari berubah
            @Override
            public void changedUpdate(DocumentEvent e) { cariData(); }
        });

        itemMenuUtama.addActionListener(e -> bukaMenuUtama());
        itemPelanggan.addActionListener(e -> bukaFormPelanggan());
        itemLogOut.addActionListener(e -> prosesLogOut());
        itemKeluar.addActionListener(e -> prosesKeluar());
        itemTentang.addActionListener(e -> tampilkanTentang());
    }

    // Void instance method (private): mengisi pilihan JComboBox dari daftar pelanggan di controller
    private void muatComboPelanggan() {
        if (controller == null) {
            return;
        }
        cbPelanggan.removeAllItems();
        for (Pelanggan p : controller.getDaftarPelanggan()) {
            cbPelanggan.addItem(p);
        }
    }

    // Return instance method (private): mengembalikan teks paket ("Express" atau "Reguler") sesuai radio button terpilih
    private String getPaketTerpilih() {
        return rbExpress.isSelected() ? "Express" : "Reguler";
    }

    // Return instance method (private): mengubah angka nominal menjadi teks format mata uang Rupiah
    private String formatRupiah(double nominal) {
        DecimalFormat formatter = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.forLanguageTag("id-ID")));
        return "Rp " + formatter.format(nominal);
    }

    // Void instance method (private) & Exception Handling: menghitung dan menampilkan estimasi biaya real-time
    private void perbaruiEstimasi() {
        if (controller == null) {
            return;
        }
        try {
            String tipe = cbTipe.getSelectedItem().toString();
            double kuantitas = ((Number) spnKuantitas.getValue()).doubleValue();
            double diskon = ((Number) spnDiskon.getValue()).doubleValue();
            boolean antarJemput = chkAntarJemput.isSelected();

            double estimasi = controller.hitungEstimasiBiaya(
                tipe, kuantitas, getPaketTerpilih(), antarJemput, diskon
            );
            lblEstimasiBiaya.setText(formatRupiah(estimasi));
        } catch (CucianInvalidException ex) {
            lblEstimasiBiaya.setText("Rp 0");
        }
    }

    // Void instance method (private) & Generics: menampilkan List<Transaksi> ke JTable (CRUD Read)
    private void muatTabel(List<Transaksi> daftar) {
        modelTabel.setRowCount(0);
        for (Transaksi t : daftar) {
            double qty = t.getLayanan().getKuantitas();
            Object tampilQty = (qty == (int) qty) ? (int) qty : qty;

            double disc = t.getDiskonPersen();
            String tampilDisc = (disc == (int) disc) ? ((int) disc + "%") : (disc + "%");

            modelTabel.addRow(new Object[]{
                t.getIdTransaksi(),
                t.getPelanggan().getNama(),
                t.getLayanan().getTipeLayanan(),
                t.getLayanan().getJenisPaket(),
                tampilQty,
                t.isAntarJemput() ? "Ya" : "Tidak",
                tampilDisc,
                formatRupiah(t.getTotalBiaya()),
                t.getEstimasiSelesai()
            });
        }
    }

    // Void instance method (private): menyalin detail transaksi dari baris tabel yang diklik ke form input
    private void isiFormDariTabel() {
        if (controller == null) {
            return;
        }
        int baris = tabelTransaksi.getSelectedRow();
        if (baris >= 0) {
            String idTrx = modelTabel.getValueAt(baris, 0).toString();
            Transaksi trx = controller.cariTransaksiById(idTrx);
            if (trx != null) {
                txtIdTransaksi.setText(trx.getIdTransaksi());
                pilihPelangganDiCombo(trx.getPelanggan().getIdPelanggan());
                cbTipe.setSelectedItem(trx.getLayanan().getTipeLayanan());
                if (trx.getLayanan().getJenisPaket().equals("Express")) {
                    rbExpress.setSelected(true);
                } else {
                    rbReguler.setSelected(true);
                }
                spnKuantitas.setValue(trx.getLayanan().getKuantitas());
                spnDiskon.setValue(trx.getDiskonPersen());
                chkAntarJemput.setSelected(trx.isAntarJemput());
                perbaruiEstimasi();
            }
        }
    }

    // Void instance method (private): mencocokkan dan memilih item pelanggan di JComboBox berdasarkan ID
    private void pilihPelangganDiCombo(String idPelanggan) {
        for (int i = 0; i < cbPelanggan.getItemCount(); i++) {
            Pelanggan p = cbPelanggan.getItemAt(i);
            if (p.getIdPelanggan().equals(idPelanggan)) {
                cbPelanggan.setSelectedIndex(i);
                return;
            }
        }
    }

    // Void instance method (private) & Exception Handling: menambahkan transaksi baru lalu menyimpannya ke file (CRUD Create)
    private void tambahTransaksi() {
        if (controller == null) {
            return;
        }
        Pelanggan pelanggan = (Pelanggan) cbPelanggan.getSelectedItem();
        if (pelanggan == null) {
            JOptionPane.showMessageDialog(this,
                "Belum ada data pelanggan! Tambahkan pelanggan dulu di menu Kelola Data Pelanggan.",
                "Pelanggan Kosong", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String tipe = cbTipe.getSelectedItem().toString();
            double kuantitas = ((Number) spnKuantitas.getValue()).doubleValue();
            double diskon = ((Number) spnDiskon.getValue()).doubleValue();
            boolean antarJemput = chkAntarJemput.isSelected();

            controller.tambahTransaksi(pelanggan, tipe, kuantitas, getPaketTerpilih(), antarJemput, diskon);
            muatTabel(controller.getDaftarTransaksi());
            bersihkanForm();
            JOptionPane.showMessageDialog(this, "Transaksi berhasil ditambahkan & disimpan permanen!");
        } catch (CucianInvalidException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan file: " + ex.getMessage(), "Error File", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Void instance method (private) & Exception Handling: memperbarui transaksi yang dipilih di tabel (CRUD Update)
    private void ubahTransaksi() {
        if (controller == null) {
            return;
        }
        int baris = tabelTransaksi.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris transaksi pada tabel yang ingin diubah!");
            return;
        }
        Pelanggan pelanggan = (Pelanggan) cbPelanggan.getSelectedItem();
        if (pelanggan == null) {
            JOptionPane.showMessageDialog(this, "Data pelanggan tidak tersedia!");
            return;
        }

        try {
            String id = txtIdTransaksi.getText();
            String tipe = cbTipe.getSelectedItem().toString();
            double kuantitas = ((Number) spnKuantitas.getValue()).doubleValue();
            double diskon = ((Number) spnDiskon.getValue()).doubleValue();
            boolean antarJemput = chkAntarJemput.isSelected();

            controller.ubahTransaksi(id, pelanggan, tipe, kuantitas, getPaketTerpilih(), antarJemput, diskon);
            muatTabel(controller.getDaftarTransaksi());
            bersihkanForm();
            JOptionPane.showMessageDialog(this, "Transaksi berhasil diperbarui!");
        } catch (CucianInvalidException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui file: " + ex.getMessage(), "Error File", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Void instance method (private) & Exception Handling: menghapus transaksi terpilih setelah konfirmasi (CRUD Delete)
    private void hapusTransaksi() {
        if (controller == null) {
            return;
        }
        int baris = tabelTransaksi.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris transaksi yang ingin dihapus!");
            return;
        }
        String id = modelTabel.getValueAt(baris, 0).toString();
        String nama = modelTabel.getValueAt(baris, 1).toString();
        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus transaksi " + id + " milik " + nama + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                controller.hapusTransaksi(id);
                muatTabel(controller.getDaftarTransaksi());
                bersihkanForm();
                JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!");
            } catch (CucianInvalidException | IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal Hapus", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Void instance method (private): menyaring isi tabel transaksi sesuai kata kunci pencarian
    private void cariData() {
        if (controller == null) {
            return;
        }
        muatTabel(controller.cariTransaksi(txtCari.getText()));
    }

    // Void instance method (private): mengembalikan seluruh input form transaksi ke nilai awal
    private void bersihkanForm() {
        txtIdTransaksi.setText("(Otomatis)");
        if (cbPelanggan.getItemCount() > 0) {
            cbPelanggan.setSelectedIndex(0);
        }
        cbTipe.setSelectedIndex(0);
        rbReguler.setSelected(true);
        spnKuantitas.setValue(1.0);
        spnDiskon.setValue(0.0);
        chkAntarJemput.setSelected(false);
        tabelTransaksi.clearSelection();
        perbaruiEstimasi();
    }

    // Void instance method (private): membuka halaman Menu Utama dan menutup jendela ini
    private void bukaMenuUtama() {
        new MenuUtama(controller).setVisible(true);
        dispose();
    }

    // Void instance method (private): berpindah ke halaman Kelola Data Pelanggan dan menutup jendela ini
    private void bukaFormPelanggan() {
        new FormPelanggan(controller).setVisible(true);
        dispose();
    }

    // Void instance method (private): kembali ke halaman login setelah konfirmasi
    private void prosesLogOut() {
        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin Log Out?", "Konfirmasi Log Out", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            new HalamanLogin().setVisible(true);
            dispose();
        }
    }

    // Void instance method (private): menutup aplikasi setelah konfirmasi
    private void prosesKeluar() {
        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menutup aplikasi?", "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // Void instance method (private): menampilkan dialog informasi aplikasi dan anggota tim
    private void tampilkanTentang() {
        JOptionPane.showMessageDialog(this,
            "CleanWashApp v2.0 (Arsitektur MVC)\nDikembangkan oleh:\n- Yunita Dwi Ardilasari\n- Greycilfa\n- Rosita Salsabila",
            "Tentang CleanWashApp", JOptionPane.INFORMATION_MESSAGE);
    }
}
