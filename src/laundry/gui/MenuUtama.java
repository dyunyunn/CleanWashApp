package laundry.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import laundry.controller.LaundryController;

// Inheritance: mewarisi JFrame untuk halaman Dashboard / Menu Utama (MVC - View halaman 2)
public class MenuUtama extends JFrame {
    private static final long serialVersionUID = 1L;

    // Encapsulation & Komposisi MVC: seluruh komponen GUI dan controller berakses private
    private final LaundryController controller;
    private JLabel lblTotalPelanggan;
    private JLabel lblTotalTransaksi;
    private JLabel lblTotalPendapatan;
    private JButton btnPelanggan;
    private JButton btnTransaksi;
    private JButton btnMuatUlang;
    private JButton btnLogOut;
    private JMenuItem itemPelanggan;
    private JMenuItem itemTransaksi;
    private JMenuItem itemLogOut;
    private JMenuItem itemKeluar;
    private JMenuItem itemTentang;

    // Constructor default (Overloading & Chaining): memanggil konstruktor utama dengan null agar kompatibel WindowBuilder
    public MenuUtama() {
        this(null);
    }

    // Constructor berparameter (Overloading): menerima LaundryController agar data antarhalaman tersinkronisasi
    public MenuUtama(LaundryController controller) {
        this.controller = controller;
        inisialisasiGUI();
        pasangEvent();
        perbaruiRingkasan();
    }

    // Void instance method (private): menyusun antarmuka Dashboard dengan JMenuBar, BorderLayout, GridLayout, dan FlowLayout
    private void inisialisasiGUI() {
        setTitle("Menu Utama - CleanWashApp");
        setSize(700, 400);
        setMinimumSize(new Dimension(600, 350));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu bar navigasi yang konsisten antarhalaman
        JMenuBar menuBar = new JMenuBar();

        JMenu menuNavigasi = new JMenu("Menu");
        itemPelanggan = new JMenuItem("Kelola Data Pelanggan");
        itemTransaksi = new JMenuItem("Kelola Transaksi Laundry");
        itemLogOut = new JMenuItem("Log Out");
        itemKeluar = new JMenuItem("Keluar Aplikasi");

        menuNavigasi.add(itemPelanggan);
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

        // Area header judul di bagian atas (NORTH)
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));

        JLabel lblJudul = new JLabel("Dashboard CleanWashApp");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 20));
        JLabel lblSub = new JLabel("Sistem Manajemen Pelanggan dan Transaksi Laundry");
        lblSub.setForeground(Color.DARK_GRAY);

        panelHeader.add(lblJudul, BorderLayout.NORTH);
        panelHeader.add(lblSub, BorderLayout.SOUTH);
        panelUtama.add(panelHeader, BorderLayout.NORTH);

        // Area 3 kartu ringkasan statistik di tengah (CENTER) dengan GridLayout 1x3
        JPanel panelStatistik = new JPanel(new GridLayout(1, 3, 15, 15));
        panelStatistik.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Kartu 1: menampilkan jumlah total pelanggan
        JPanel kartuPelanggan = new JPanel(new BorderLayout(5, 10));
        kartuPelanggan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        JLabel lblJudulPelanggan = new JLabel("Total Pelanggan", SwingConstants.CENTER);
        lblJudulPelanggan.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblTotalPelanggan = new JLabel("0", SwingConstants.CENTER);
        lblTotalPelanggan.setFont(new Font("SansSerif", Font.BOLD, 20));
        kartuPelanggan.add(lblJudulPelanggan, BorderLayout.NORTH);
        kartuPelanggan.add(lblTotalPelanggan, BorderLayout.CENTER);
        panelStatistik.add(kartuPelanggan);

        // Kartu 2: menampilkan jumlah total transaksi
        JPanel kartuTransaksi = new JPanel(new BorderLayout(5, 10));
        kartuTransaksi.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        JLabel lblJudulTransaksi = new JLabel("Total Transaksi", SwingConstants.CENTER);
        lblJudulTransaksi.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblTotalTransaksi = new JLabel("0", SwingConstants.CENTER);
        lblTotalTransaksi.setFont(new Font("SansSerif", Font.BOLD, 20));
        kartuTransaksi.add(lblJudulTransaksi, BorderLayout.NORTH);
        kartuTransaksi.add(lblTotalTransaksi, BorderLayout.CENTER);
        panelStatistik.add(kartuTransaksi);

        // Kartu 3: menampilkan akumulasi total pendapatan
        JPanel kartuPendapatan = new JPanel(new BorderLayout(5, 10));
        kartuPendapatan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        JLabel lblJudulPendapatan = new JLabel("Total Pendapatan", SwingConstants.CENTER);
        lblJudulPendapatan.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblTotalPendapatan = new JLabel("Rp 0", SwingConstants.CENTER);
        lblTotalPendapatan.setFont(new Font("SansSerif", Font.BOLD, 20));
        kartuPendapatan.add(lblJudulPendapatan, BorderLayout.NORTH);
        kartuPendapatan.add(lblTotalPendapatan, BorderLayout.CENTER);
        panelStatistik.add(kartuPendapatan);

        panelUtama.add(panelStatistik, BorderLayout.CENTER);

        // Area tombol navigasi di bagian bawah (SOUTH) dengan FlowLayout
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 20));
        btnPelanggan = new JButton("Kelola Pelanggan");
        btnTransaksi = new JButton("Kelola Transaksi");
        btnMuatUlang = new JButton("Muat Ulang");
        btnLogOut = new JButton("Log Out");

        panelTombol.add(btnPelanggan);
        panelTombol.add(btnTransaksi);
        panelTombol.add(btnMuatUlang);
        panelTombol.add(btnLogOut);
        panelUtama.add(panelTombol, BorderLayout.SOUTH);
    }

    // Void instance method (private) & Event Handling: menghubungkan tombol dan item menu ke ActionListener
    private void pasangEvent() {
        btnPelanggan.addActionListener(e -> bukaFormPelanggan());
        itemPelanggan.addActionListener(e -> bukaFormPelanggan());

        btnTransaksi.addActionListener(e -> bukaFormTransaksi());
        itemTransaksi.addActionListener(e -> bukaFormTransaksi());

        btnMuatUlang.addActionListener(e -> muatUlangData());

        btnLogOut.addActionListener(e -> prosesLogOut());
        itemLogOut.addActionListener(e -> prosesLogOut());

        itemKeluar.addActionListener(e -> prosesKeluar());
        itemTentang.addActionListener(e -> tampilkanTentang());
    }

    // Void instance method (private): mengambil dan memformat angka ringkasan statistik dari controller
    private void perbaruiRingkasan() {
        if (controller == null) {
            return;
        }
        DecimalFormat formatter = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.forLanguageTag("id-ID")));
        lblTotalPelanggan.setText(String.valueOf(controller.getTotalPelanggan()));
        lblTotalTransaksi.setText(String.valueOf(controller.getTotalTransaksi()));
        lblTotalPendapatan.setText("Rp " + formatter.format(controller.getTotalPendapatan()));
    }

    // Void instance method (private): memuat ulang data dari file .txt lalu menyegarkan kartu statistik
    private void muatUlangData() {
        if (controller == null) {
            return;
        }
        controller.muatSemuaData();
        perbaruiRingkasan();
        JOptionPane.showMessageDialog(this, "Ringkasan data berhasil diperbarui!");
    }

    // Void instance method (private): membuka halaman Kelola Data Pelanggan dan menutup jendela ini
    private void bukaFormPelanggan() {
        new FormPelanggan(controller).setVisible(true);
        dispose();
    }

    // Void instance method (private): membuka halaman Kelola Transaksi Laundry dan menutup jendela ini
    private void bukaFormTransaksi() {
        new FormTransaksi(controller).setVisible(true);
        dispose();
    }

    // Void instance method (private): menampilkan konfirmasi lalu kembali ke halaman login
    private void prosesLogOut() {
        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin Log Out?", "Konfirmasi Log Out", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            new HalamanLogin().setVisible(true);
            dispose();
        }
    }

    // Void instance method (private): menampilkan konfirmasi lalu menghentikan aplikasi
    private void prosesKeluar() {
        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menutup aplikasi?", "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // Void instance method (private): menampilkan dialog informasi versi dan pengembang aplikasi
    private void tampilkanTentang() {
        JOptionPane.showMessageDialog(this,
            "CleanWashApp v2.0 (Arsitektur MVC)\n" +
            "Dikembangkan oleh:\n" +
            "- Yunita Dwi Ardilasari\n" +
            "- Greycilfa\n" +
            "- Rosita Salsabila",
            "Tentang CleanWashApp", JOptionPane.INFORMATION_MESSAGE);
    }
}
