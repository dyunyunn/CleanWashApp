package laundry.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import laundry.controller.LaundryController;
import laundry.model.CucianInvalidException;
import laundry.model.Pelanggan;

// Inheritance: mewarisi JFrame untuk halaman CRUD Entitas Pelanggan (MVC - View halaman 3)
public class FormPelanggan extends JFrame {
    private static final long serialVersionUID = 1L;

    // Encapsulation & Komposisi MVC: controller dan seluruh komponen GUI berakses private
    private final LaundryController controller;
    private JTextField txtId;
    private JTextField txtNama;
    private JTextField txtNoHp;
    private JTextField txtAlamat;
    private JTextField txtCari;
    private JButton btnTambah;
    private JButton btnUbah;
    private JButton btnHapus;
    private JButton btnBersihkan;
    private JButton btnKembali;
    private JTable tabelPelanggan;
    private DefaultTableModel modelTabel;
    private JMenuItem itemMenuUtama;
    private JMenuItem itemTransaksi;
    private JMenuItem itemLogOut;
    private JMenuItem itemKeluar;
    private JMenuItem itemTentang;

    // Constructor default (Overloading & Chaining): memanggil konstruktor utama dengan null agar kompatibel WindowBuilder
    public FormPelanggan() {
        this(null);
    }

    // Constructor berparameter (Overloading): menyiapkan GUI, memasang event, dan memuat data awal ke tabel
    public FormPelanggan(LaundryController controller) {
        this.controller = controller;
        inisialisasiGUI();
        pasangEvent();
        if (this.controller != null) {
            muatTabel(this.controller.getDaftarPelanggan());
        }
    }

    // Void instance method (private): menyusun antarmuka form pelanggan, tombol CRUD, pencarian, dan JTable
    private void inisialisasiGUI() {
        setTitle("Kelola Data Pelanggan - CleanWashApp");
        setSize(780, 560);
        setMinimumSize(new Dimension(680, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu bar navigasi antarhalaman
        JMenuBar menuBar = new JMenuBar();
        JMenu menuNavigasi = new JMenu("Menu");
        itemMenuUtama = new JMenuItem("Menu Utama");
        itemTransaksi = new JMenuItem("Kelola Transaksi Laundry");
        itemLogOut = new JMenuItem("Log Out");
        itemKeluar = new JMenuItem("Keluar Aplikasi");

        menuNavigasi.add(itemMenuUtama);
        menuNavigasi.add(itemTransaksi);
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

        // Panel atas menampung form input (GridBagLayout) dan tombol aksi (FlowLayout)
        JPanel panelUtara = new JPanel(new BorderLayout());

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Form Data Pelanggan"));

        // Baris 0: input ID pelanggan (otomatis dan read-only)
        JLabel lblId = new JLabel("ID Pelanggan:");
        GridBagConstraints gbcLblId = new GridBagConstraints();
        gbcLblId.insets = new Insets(5, 10, 5, 10);
        gbcLblId.anchor = GridBagConstraints.WEST;
        gbcLblId.gridx = 0;
        gbcLblId.gridy = 0;
        panelForm.add(lblId, gbcLblId);

        txtId = new JTextField("(Otomatis)", 14);
        txtId.setEditable(false);
        GridBagConstraints gbcTxtId = new GridBagConstraints();
        gbcTxtId.insets = new Insets(5, 10, 5, 10);
        gbcTxtId.anchor = GridBagConstraints.WEST;
        gbcTxtId.gridx = 1;
        gbcTxtId.gridy = 0;
        panelForm.add(txtId, gbcTxtId);

        // Spacer kanan agar input tetap rapi rata kiri saat jendela diperbesar
        Component spacerKanan = Box.createHorizontalGlue();
        GridBagConstraints gbcSpacer = new GridBagConstraints();
        gbcSpacer.insets = new Insets(5, 10, 5, 10);
        gbcSpacer.anchor = GridBagConstraints.WEST;
        gbcSpacer.gridx = 2;
        gbcSpacer.gridy = 0;
        gbcSpacer.weightx = 1.0;
        panelForm.add(spacerKanan, gbcSpacer);

        // Baris 1: input nama pelanggan
        JLabel lblNama = new JLabel("Nama Pelanggan:");
        GridBagConstraints gbcLblNama = new GridBagConstraints();
        gbcLblNama.insets = new Insets(5, 10, 5, 10);
        gbcLblNama.anchor = GridBagConstraints.WEST;
        gbcLblNama.gridx = 0;
        gbcLblNama.gridy = 1;
        panelForm.add(lblNama, gbcLblNama);

        txtNama = new JTextField(22);
        GridBagConstraints gbcTxtNama = new GridBagConstraints();
        gbcTxtNama.insets = new Insets(5, 10, 5, 10);
        gbcTxtNama.anchor = GridBagConstraints.WEST;
        gbcTxtNama.gridx = 1;
        gbcTxtNama.gridy = 1;
        panelForm.add(txtNama, gbcTxtNama);

        // Baris 2: input nomor HP
        JLabel lblNoHp = new JLabel("No. HP:");
        GridBagConstraints gbcLblNoHp = new GridBagConstraints();
        gbcLblNoHp.insets = new Insets(5, 10, 5, 10);
        gbcLblNoHp.anchor = GridBagConstraints.WEST;
        gbcLblNoHp.gridx = 0;
        gbcLblNoHp.gridy = 2;
        panelForm.add(lblNoHp, gbcLblNoHp);

        txtNoHp = new JTextField(16);
        GridBagConstraints gbcTxtNoHp = new GridBagConstraints();
        gbcTxtNoHp.insets = new Insets(5, 10, 5, 10);
        gbcTxtNoHp.anchor = GridBagConstraints.WEST;
        gbcTxtNoHp.gridx = 1;
        gbcTxtNoHp.gridy = 2;
        panelForm.add(txtNoHp, gbcTxtNoHp);

        // Baris 3: input alamat
        JLabel lblAlamat = new JLabel("Alamat:");
        GridBagConstraints gbcLblAlamat = new GridBagConstraints();
        gbcLblAlamat.insets = new Insets(5, 10, 5, 10);
        gbcLblAlamat.anchor = GridBagConstraints.WEST;
        gbcLblAlamat.gridx = 0;
        gbcLblAlamat.gridy = 3;
        panelForm.add(lblAlamat, gbcLblAlamat);

        txtAlamat = new JTextField(28);
        GridBagConstraints gbcTxtAlamat = new GridBagConstraints();
        gbcTxtAlamat.insets = new Insets(5, 10, 5, 10);
        gbcTxtAlamat.anchor = GridBagConstraints.WEST;
        gbcTxtAlamat.gridx = 1;
        gbcTxtAlamat.gridy = 3;
        panelForm.add(txtAlamat, gbcTxtAlamat);

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

        // Panel tengah menampung kolom pencarian dan JTable
        JPanel panelTengah = new JPanel(new BorderLayout(5, 5));
        panelTengah.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JLabel lblCari = new JLabel("Cari Pelanggan (ID/Nama/HP/Alamat):");
        panelCari.add(lblCari);
        txtCari = new JTextField(22);
        panelCari.add(txtCari);
        panelTengah.add(panelCari, BorderLayout.NORTH);

        // Anonymous subclass DefaultTableModel dengan Overridden return method agar tabel read-only
        String[] kolom = {"ID Pelanggan", "Nama Pelanggan", "No. HP", "Alamat"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            private static final long serialVersionUID = 1L;

            // Overridden return method (public): mengembalikan false agar sel tabel tidak bisa diedit langsung
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelPelanggan = new JTable(modelTabel);
        tabelPelanggan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabel = new JScrollPane(tabelPelanggan);
        panelTengah.add(scrollTabel, BorderLayout.CENTER);

        panelUtama.add(panelTengah, BorderLayout.CENTER);
    }

    // Void instance method (private) & Event Handling: mendaftarkan listener tombol, tabel, pencarian, dan menu bar
    private void pasangEvent() {
        btnTambah.addActionListener(e -> tambahPelanggan());
        btnUbah.addActionListener(e -> ubahPelanggan());
        btnHapus.addActionListener(e -> hapusPelanggan());
        btnBersihkan.addActionListener(e -> bersihkanForm());
        btnKembali.addActionListener(e -> bukaMenuUtama());

        // Listener seleksi baris tabel untuk menyalin data terpilih ke form input
        tabelPelanggan.getSelectionModel().addListSelectionListener(e -> {
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
        itemTransaksi.addActionListener(e -> bukaFormTransaksi());
        itemLogOut.addActionListener(e -> prosesLogOut());
        itemKeluar.addActionListener(e -> prosesKeluar());
        itemTentang.addActionListener(e -> tampilkanTentang());
    }

    // Void instance method (private) & Generics: menampilkan List<Pelanggan> ke JTable (CRUD Read)
    private void muatTabel(List<Pelanggan> daftar) {
        modelTabel.setRowCount(0);
        for (Pelanggan p : daftar) {
            modelTabel.addRow(new Object[]{
                p.getIdPelanggan(),
                p.getNama(),
                p.getNoHp(),
                p.getAlamat()
            });
        }
    }

    // Void instance method (private): menyalin nilai baris tabel yang diklik ke kolom input form
    private void isiFormDariTabel() {
        int baris = tabelPelanggan.getSelectedRow();
        if (baris >= 0) {
            txtId.setText(modelTabel.getValueAt(baris, 0).toString());
            txtNama.setText(modelTabel.getValueAt(baris, 1).toString());
            txtNoHp.setText(modelTabel.getValueAt(baris, 2).toString());
            txtAlamat.setText(modelTabel.getValueAt(baris, 3).toString());
        }
    }

    // Void instance method (private) & Exception Handling: menambah pelanggan baru lewat controller (CRUD Create)
    private void tambahPelanggan() {
        if (controller == null) {
            return;
        }
        try {
            controller.tambahPelanggan(txtNama.getText(), txtNoHp.getText(), txtAlamat.getText());
            muatTabel(controller.getDaftarPelanggan());
            bersihkanForm();
            JOptionPane.showMessageDialog(this, "Data pelanggan berhasil ditambahkan!");
        } catch (CucianInvalidException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan file: " + ex.getMessage(), "Error File", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Void instance method (private) & Exception Handling: memperbarui data pelanggan terpilih (CRUD Update)
    private void ubahPelanggan() {
        if (controller == null) {
            return;
        }
        int baris = tabelPelanggan.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris pelanggan pada tabel terlebih dahulu!");
            return;
        }
        try {
            String id = txtId.getText();
            controller.ubahPelanggan(id, txtNama.getText(), txtNoHp.getText(), txtAlamat.getText());
            muatTabel(controller.getDaftarPelanggan());
            bersihkanForm();
            JOptionPane.showMessageDialog(this, "Data pelanggan berhasil diperbarui!");
        } catch (CucianInvalidException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui file: " + ex.getMessage(), "Error File", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Void instance method (private) & Exception Handling: menghapus pelanggan terpilih setelah konfirmasi (CRUD Delete)
    private void hapusPelanggan() {
        if (controller == null) {
            return;
        }
        int baris = tabelPelanggan.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris pelanggan yang ingin dihapus!");
            return;
        }
        String id = modelTabel.getValueAt(baris, 0).toString();
        String nama = modelTabel.getValueAt(baris, 1).toString();
        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus pelanggan " + nama + " (" + id + ")?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                controller.hapusPelanggan(id);
                muatTabel(controller.getDaftarPelanggan());
                bersihkanForm();
                JOptionPane.showMessageDialog(this, "Data pelanggan berhasil dihapus!");
            } catch (CucianInvalidException | IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal Hapus", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Void instance method (private): menyaring isi tabel pelanggan sesuai kata kunci pencarian
    private void cariData() {
        if (controller == null) {
            return;
        }
        muatTabel(controller.cariPelanggan(txtCari.getText()));
    }

    // Void instance method (private): mengosongkan seluruh kolom input dan melepas seleksi tabel
    private void bersihkanForm() {
        txtId.setText("(Otomatis)");
        txtNama.setText("");
        txtNoHp.setText("");
        txtAlamat.setText("");
        tabelPelanggan.clearSelection();
    }

    // Void instance method (private): membuka halaman Menu Utama dan menutup jendela ini
    private void bukaMenuUtama() {
        new MenuUtama(controller).setVisible(true);
        dispose();
    }

    // Void instance method (private): berpindah ke halaman Kelola Transaksi dan menutup jendela ini
    private void bukaFormTransaksi() {
        new FormTransaksi(controller).setVisible(true);
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

    // Void instance method (private): menutup seluruh aplikasi setelah konfirmasi
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
