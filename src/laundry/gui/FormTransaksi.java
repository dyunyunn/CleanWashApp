package laundry.gui;

import laundry.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Kelas FormTransaksi menangani antarmuka (GUI) utama untuk pengelolaan data 
 * transaksi laundry (tambah, ubah, hapus, layout, dan penyimpanan file).
 */
public class FormTransaksi extends JFrame {
    // Deklarasi komponen antarmuka (UI)
    private JTextField txtNama, txtKuantitas;
    private JComboBox<String> cbTipe, cbPaket;
    private JButton btnTambah, btnUbah, btnHapus;
    private JTable tabelTransaksi;
    private DefaultTableModel modelTabel;

    public FormTransaksi() {
        // Pengaturan dasar jendela aplikasi (Frame)
        setTitle("Kelola Transaksi Laundry");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // =========================================================
        // BAGIAN 1: PENGATURAN LAYOUT MANAGER (BorderLayout)
        // =========================================================
        // Menggunakan BorderLayout sebagai layout manager utama pada frame
        setLayout(new BorderLayout());

        // Membuat panel khusus form input dengan Absolute Layout (null) 
        // agar koordinat posisi komponen di dalamnya tetap rapi
        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setPreferredSize(new Dimension(700, 220));

        // --- Komponen Input: Nama Pelanggan ---
        JLabel lblNama = new JLabel("Nama Pelanggan:");
        lblNama.setBounds(20, 20, 120, 25);
        panelForm.add(lblNama);

        txtNama = new JTextField();
        txtNama.setBounds(150, 20, 200, 25);
        panelForm.add(txtNama);

        // --- Komponen Input: Tipe Layanan (Kiloan / Satuan) ---
        JLabel lblTipe = new JLabel("Tipe Layanan:");
        lblTipe.setBounds(20, 60, 120, 25);
        panelForm.add(lblTipe);

        String[] tipe = {"Kiloan", "Satuan"};
        cbTipe = new JComboBox<>(tipe);
        cbTipe.setBounds(150, 60, 150, 25);
        panelForm.add(cbTipe);

        // --- Komponen Input: Berat atau Jumlah Cucian ---
        JLabel lblKuantitas = new JLabel("Berat (kg) / Jumlah:");
        lblKuantitas.setBounds(20, 100, 120, 25);
        panelForm.add(lblKuantitas);

        txtKuantitas = new JTextField();
        txtKuantitas.setBounds(150, 100, 100, 25);
        panelForm.add(txtKuantitas);

        // --- Komponen Input: Pilihan Paket (Reguler / Express) ---
        JLabel lblPaket = new JLabel("Pilihan Paket:");
        lblPaket.setBounds(20, 140, 120, 25);
        panelForm.add(lblPaket);

        String[] paket = {"Reguler", "Express"};
        cbPaket = new JComboBox<>(paket);
        cbPaket.setBounds(150, 140, 150, 25);
        panelForm.add(cbPaket);

        // --- Tombol Aksi (CRUD) ---
        btnTambah = new JButton("Tambah");
        btnTambah.setBounds(20, 180, 90, 30);
        panelForm.add(btnTambah);

        btnUbah = new JButton("Ubah");
        btnUbah.setBounds(120, 180, 90, 30);
        panelForm.add(btnUbah);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(220, 180, 90, 30);
        panelForm.add(btnHapus);

        // Menempatkan Panel Form di bagian UTARA (NORTH) dari BorderLayout
        add(panelForm, BorderLayout.NORTH);

        // --- Komponen Tabel Data Transaksi ---
        String[] kolom = {"Nama", "Tipe", "Berat/Jml", "Total Biaya", "Estimasi"};
        modelTabel = new DefaultTableModel(kolom, 0);
        tabelTransaksi = new JTable(modelTabel);
        JScrollPane scrollPane = new JScrollPane(tabelTransaksi);
        
        // Menempatkan Tabel di bagian TENGAH (CENTER) dari BorderLayout
        add(scrollPane, BorderLayout.CENTER);


        // =========================================================
        // BAGIAN 2: EVENT HANDLING & LOGIKA TOMBOL (CRUD & FILE I/O)
        // =========================================================

        // Fitur Interaksi: Saat baris di tabel di-klik, data otomatis naik ke form input atas
        tabelTransaksi.getSelectionModel().addListSelectionListener(e -> {
            int baris = tabelTransaksi.getSelectedRow();
            if (baris >= 0) {
                txtNama.setText(modelTabel.getValueAt(baris, 0).toString());
                cbTipe.setSelectedItem(modelTabel.getValueAt(baris, 1).toString());
                txtKuantitas.setText(modelTabel.getValueAt(baris, 2).toString());
            }
        });

        // 1. Logika Tombol TAMBAH (Create Data & Simpan ke File TXT)
        btnTambah.addActionListener(e -> {
            try {
                // Mengambil input dari form
                String nama = txtNama.getText();
                double kuantitas = Double.parseDouble(txtKuantitas.getText());
                String tipeLayanan = cbTipe.getSelectedItem().toString();
                String tipePaket = cbPaket.getSelectedItem().toString();

                // Validasi input angka tidak boleh negatif atau nol
                if (kuantitas <= 0) {
                    JOptionPane.showMessageDialog(this, "Kuantitas tidak boleh 0 atau negatif!");
                    return;
                }

                // Penerapan Polimorfisme: Memanggil kelas model berdasarkan pilihan tipe
                Layanan layanan;
                if (tipeLayanan.equals("Kiloan")) {
                    layanan = new LayananKiloan(nama, kuantitas, tipePaket);
                } else {
                    layanan = new LayananSatuan(nama, kuantitas, tipePaket); 
                }

                // Menghitung biaya dan mengambil estimasi waktu pengerjaan
                double total = layanan.hitungBiaya();
                String estimasi = ((EstimasiWaktu) layanan).getEstimasiSelesai();

                // Format tampilan harga menjadi format Rupiah Indonesia (contoh: Rp 35.000)
                DecimalFormat formatter = new DecimalFormat("#,###", new DecimalFormatSymbols(new Locale("id", "ID")));
                String totalFormatted = "Rp " + formatter.format(total);

                // Menambahkan data baru ke dalam baris tabel GUI
                modelTabel.addRow(new Object[]{nama, tipeLayanan, kuantitas, totalFormatted, estimasi});

                // Menyimpan data secara permanen ke file teks (.txt)
                String dataSimpan = nama + "," + tipeLayanan + "," + kuantitas + "," + totalFormatted + "," + estimasi;
                FileStorage.simpanData(dataSimpan);

                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan & disimpan ke file!");
                
                // Mengosongkan form input setelah data berhasil dimasukkan
                txtNama.setText("");
                txtKuantitas.setText("");
            } catch (NumberFormatException ex) {
                // Menangani error jika pengguna salah mengetik huruf pada kolom berat/jumlah
                JOptionPane.showMessageDialog(this, "Format berat harus berupa angka!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage());
            }
        });

        // 2. Logika Tombol UBAH / UPDATE (Memperbarui Data pada Baris yang Dipilih)
        btnUbah.addActionListener(e -> {
            int baris = tabelTransaksi.getSelectedRow();
            
            // Mengecek apakah ada baris di tabel yang sedang dipilih
            if (baris >= 0) {
                try {
                    String namaBaru = txtNama.getText();
                    double kuantitasBaru = Double.parseDouble(txtKuantitas.getText());
                    String tipeBaru = cbTipe.getSelectedItem().toString();
                    String paketBaru = cbPaket.getSelectedItem().toString();

                    if (kuantitasBaru <= 0) {
                        JOptionPane.showMessageDialog(this, "Kuantitas tidak boleh 0 atau negatif!");
                        return;
                    }

                    // Menghitung ulang biaya dan estimasi berdasarkan data perubahan terbaru
                    Layanan layanan;
                    if (tipeBaru.equals("Kiloan")) {
                        layanan = new LayananKiloan(namaBaru, kuantitasBaru, paketBaru);
                    } else {
                        layanan = new LayananSatuan(namaBaru, kuantitasBaru, paketBaru);
                    }

                    double totalBaru = layanan.hitungBiaya();
                    String estimasiBaru = ((EstimasiWaktu) layanan).getEstimasiSelesai();

                    // Format ulang hasil perhitungan ke format Rupiah
                    DecimalFormat formatter = new DecimalFormat("#,###", new DecimalFormatSymbols(new Locale("id", "ID")));
                    String totalFormattedBaru = "Rp " + formatter.format(totalBaru);

                    // Memperbarui nilai pada baris tabel yang dipilih
                    modelTabel.setValueAt(namaBaru, baris, 0);
                    modelTabel.setValueAt(tipeBaru, baris, 1);
                    modelTabel.setValueAt(kuantitasBaru, baris, 2);
                    modelTabel.setValueAt(totalFormattedBaru, baris, 3);
                    modelTabel.setValueAt(estimasiBaru, baris, 4);

                    JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
                    
                    // Mengosongkan form input
                    txtNama.setText("");
                    txtKuantitas.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Format berat harus berupa angka!");
                }
            } else {
                // Peringatan jika pengguna menekan tombol Ubah tanpa memilih baris di tabel
                JOptionPane.showMessageDialog(this, "Pilih baris di tabel dulu yang mau diubah!");
            }
        });

        // 3. Logika Tombol HAPUS / DELETE (Menghapus Baris dari Tabel)
        btnHapus.addActionListener(e -> {
            int baris = tabelTransaksi.getSelectedRow();
            
            // Mengecek apakah ada baris yang dipilih untuk dihapus
            if (baris >= 0) {
                int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    // Menghapus baris terpilih dari model tabel
                    modelTabel.removeRow(baris);
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                }
            } else {
                // Peringatan jika belum memilih baris
                JOptionPane.showMessageDialog(this, "Pilih baris di tabel yang mau dihapus!");
            }
        });
    }
}
