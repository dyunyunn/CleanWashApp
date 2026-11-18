package laundry.gui;

import laundry.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * [KELAS UTAMA / CLASS GUI]
 * Mengelola antarmuka transaksi, JMenuBar, tabel read-only,
 * operasi CRUD tersinkronisasi file txt, dan penerapan relasi Pelanggan.
 */
public class FormTransaksi extends JFrame {
    
    // [ATRIBUT / VARIABEL GLOBAL]
    private JTextField txtNama, txtKuantitas;
    private JComboBox<String> cbTipe, cbPaket;
    private JButton btnTambah, btnUbah, btnHapus, btnLogOut;
    private JTable tabelTransaksi;
    private DefaultTableModel modelTabel;
    
    // [KONSTANTA]
    private static final String FILE_NAME = "data_laundry.txt";

    /**
     * [CONSTRUCTOR]
     */
    public FormTransaksi() {
        setTitle("Kelola Transaksi Laundry - CleanWashApp");
        setSize(700, 580); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // [LAYOUT MANAGER UTAMA]
        setLayout(new BorderLayout());

        // =========================================================
        // [FITUR MENU BAR / JMenuBar]
        // =========================================================
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuFile = new JMenu("File");
        JMenuItem itemLogOut = new JMenuItem("Log Out");
        JMenuItem itemKeluar = new JMenuItem("Keluar dari Aplikasi");
        menuFile.add(itemLogOut);
        menuFile.addSeparator(); 
        menuFile.add(itemKeluar);

        JMenu menuBantuan = new JMenu("Bantuan");
        JMenuItem itemTentang = new JMenuItem("Tentang CleanWashApp");
        menuBantuan.add(itemTentang);

        menuBar.add(menuFile);
        menuBar.add(menuBantuan);
        setJMenuBar(menuBar);

        // =========================================================
        // --- PANEL FORM INPUT (Posisi NORTH) - Absolute Layout ---
        // =========================================================
        JPanel panelForm = new JPanel();
        panelForm.setLayout(null); // Menggunakan layout manual sesuai desain awal
        panelForm.setPreferredSize(new Dimension(700, 230));

        JLabel lblNama = new JLabel("Nama Pelanggan:");
        lblNama.setBounds(20, 20, 120, 25);
        panelForm.add(lblNama);

        txtNama = new JTextField();
        txtNama.setBounds(150, 20, 200, 25);
        panelForm.add(txtNama);

        JLabel lblTipe = new JLabel("Tipe Layanan:");
        lblTipe.setBounds(20, 60, 120, 25);
        panelForm.add(lblTipe);

        String[] tipe = {"Kiloan", "Satuan"};
        cbTipe = new JComboBox<>(tipe);
        cbTipe.setBounds(150, 60, 150, 25);
        panelForm.add(cbTipe);

        JLabel lblKuantitas = new JLabel("Berat (kg) / Jumlah:");
        lblKuantitas.setBounds(20, 100, 120, 25);
        panelForm.add(lblKuantitas);

        txtKuantitas = new JTextField();
        txtKuantitas.setBounds(150, 100, 100, 25);
        panelForm.add(txtKuantitas);

        JLabel lblPaket = new JLabel("Pilihan Paket:");
        lblPaket.setBounds(20, 140, 120, 25);
        panelForm.add(lblPaket);

        String[] paket = {"Reguler", "Express"};
        cbPaket = new JComboBox<>(paket);
        cbPaket.setBounds(150, 140, 150, 25);
        panelForm.add(cbPaket);

        btnTambah = new JButton("Tambah");
        btnTambah.setBounds(20, 180, 90, 30);
        panelForm.add(btnTambah);

        btnUbah = new JButton("Ubah");
        btnUbah.setBounds(120, 180, 90, 30);
        panelForm.add(btnUbah);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(220, 180, 90, 30);
        panelForm.add(btnHapus);

        // Posisi tombol Log Out dikembalikan ke pojok kanan atas
        btnLogOut = new JButton("Log Out");
        btnLogOut.setBounds(570, 20, 90, 30);
        panelForm.add(btnLogOut);

        add(panelForm, BorderLayout.NORTH);

        // =========================================================
        // --- TABEL TRANSAKSI (Posisi CENTER) ---
        // =========================================================
        String[] kolom = {"Nama", "Tipe", "Berat/Jml", "Total Biaya", "Estimasi"};
        
        // [OVERRIDING LOGIKA TABEL] Mencegah sel diedit secara langsung (Read-Only)
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        tabelTransaksi = new JTable(modelTabel);
        JScrollPane scrollPane = new JScrollPane(tabelTransaksi);
        add(scrollPane, BorderLayout.CENTER);

        // [PEMANGGILAN METHOD] Menarik data lama dari file txt
        muatDataDariFile();

        // =========================================================
        // EVENT LISTENER & LOGIKA AKSI TOMBOL
        // =========================================================

        // [EVENT LISTENER] Seleksi Baris Tabel
        tabelTransaksi.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelTransaksi.getSelectedRow() >= 0) {
                int baris = tabelTransaksi.getSelectedRow();
                txtNama.setText(modelTabel.getValueAt(baris, 0).toString());
                cbTipe.setSelectedItem(modelTabel.getValueAt(baris, 1).toString());
                txtKuantitas.setText(modelTabel.getValueAt(baris, 2).toString());
            }
        });

        // 1. [METHOD CRUD - CREATE] 
        btnTambah.addActionListener(e -> {
            try {
                String nama = txtNama.getText();
                double kuantitas = Double.parseDouble(txtKuantitas.getText());
                String tipeLayanan = cbTipe.getSelectedItem().toString();
                String tipePaket = cbPaket.getSelectedItem().toString();

                if (kuantitas <= 0) {
                    JOptionPane.showMessageDialog(this, "Kuantitas tidak boleh 0 atau negatif!");
                    return;
                }

                // [PEMBUATAN OBJEK ENTITAS KEDUA]
                Pelanggan pelangganBaru = new Pelanggan(nama);

                // [POLIMORFISME & RELASI] Memasukkan Entitas Pelanggan ke Layanan
                Layanan layanan;
                if (tipeLayanan.equals("Kiloan")) {
                    layanan = new LayananKiloan(pelangganBaru, kuantitas, tipePaket);
                } else {
                    layanan = new LayananSatuan(pelangganBaru, kuantitas, tipePaket); 
                }

                double total = layanan.hitungBiaya();
                String estimasi = ((EstimasiWaktu) layanan).getEstimasiSelesai();

                // Format angka menggunakan Locale Java modern
                DecimalFormat formatter = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.forLanguageTag("id-ID")));
                String totalFormatted = "Rp " + formatter.format(total);
                Object tampilKuantitas = (kuantitas == (int) kuantitas) ? (int) kuantitas : kuantitas;

                // [PEMBUKTIAN RELASI] Memanggil nama melalui getter Entitas Pelanggan
                String namaTampil = layanan.getPelanggan().getNama();

                modelTabel.addRow(new Object[]{namaTampil, tipeLayanan, tampilKuantitas, totalFormatted, estimasi});

                // [FILE I/O STORAGE] Simpan permanen
                String dataSimpan = namaTampil + "," + tipeLayanan + "," + tampilKuantitas + "," + totalFormatted + "," + estimasi;
                FileStorage.simpanData(dataSimpan);

                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan & disimpan permanen!");
                txtNama.setText("");
                txtKuantitas.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Format berat harus berupa angka!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage());
            }
        });

        // 2. [METHOD CRUD - UPDATE] 
        btnUbah.addActionListener(e -> {
            int baris = tabelTransaksi.getSelectedRow();
            if (baris >= 0) {
                try {
                    String namaBaru = txtNama.getText();
                    if (namaBaru.trim().isEmpty() || txtKuantitas.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Nama dan Berat/Jumlah tidak boleh kosong!");
                        return;
                    }

                    double kuantitasBaru = Double.parseDouble(txtKuantitas.getText());
                    String tipeBaru = cbTipe.getSelectedItem().toString();
                    String paketBaru = cbPaket.getSelectedItem().toString();

                    if (kuantitasBaru <= 0) {
                        JOptionPane.showMessageDialog(this, "Kuantitas tidak boleh 0 atau negatif!");
                        return;
                    }

                    // [PEMBUATAN OBJEK ENTITAS KEDUA]
                    Pelanggan pelangganUpdate = new Pelanggan(namaBaru);
                    
                    // [POLIMORFISME]
                    Layanan layanan;
                    if (tipeBaru.equals("Kiloan")) {
                        layanan = new LayananKiloan(pelangganUpdate, kuantitasBaru, paketBaru);
                    } else {
                        layanan = new LayananSatuan(pelangganUpdate, kuantitasBaru, paketBaru);
                    }

                    double totalBaru = layanan.hitungBiaya();
                    String estimasiBaru = ((EstimasiWaktu) layanan).getEstimasiSelesai();
                    
                    // Format angka menggunakan Locale Java modern
                    DecimalFormat formatter = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.forLanguageTag("id-ID")));
                    String totalFormattedBaru = "Rp " + formatter.format(totalBaru);
                    Object tampilKuantitasBaru = (kuantitasBaru == (int) kuantitasBaru) ? (int) kuantitasBaru : kuantitasBaru;
                    
                    // [PEMBUKTIAN RELASI] Memanggil nama melalui getter Entitas Pelanggan
                    String namaTampilUpdate = layanan.getPelanggan().getNama();

                    modelTabel.setValueAt(namaTampilUpdate, baris, 0);
                    modelTabel.setValueAt(tipeBaru, baris, 1);
                    modelTabel.setValueAt(tampilKuantitasBaru, baris, 2);
                    modelTabel.setValueAt(totalFormattedBaru, baris, 3);
                    modelTabel.setValueAt(estimasiBaru, baris, 4);

                    // [SINKRONISASI FILE I/O] Memperbarui file txt
                    simpanSemuaDataKeFile();

                    JOptionPane.showMessageDialog(this, "Data berhasil diubah & diperbarui!");
                    tabelTransaksi.clearSelection();
                    txtNama.setText("");
                    txtKuantitas.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Format berat harus berupa angka!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris di tabel dulu yang mau diubah!");
            }
        });

        // 3. [METHOD CRUD - DELETE]
        btnHapus.addActionListener(e -> {
            int baris = tabelTransaksi.getSelectedRow();
            if (baris >= 0) {
                int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    modelTabel.removeRow(baris);
                    
                    // [SINKRONISASI FILE I/O]
                    simpanSemuaDataKeFile();
                    
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus dari sistem!");
                    txtNama.setText("");
                    txtKuantitas.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris di tabel yang mau dihapus!");
            }
        });

        // =========================================================
        // [EVENT LISTENER NAVIGASI MENU]
        // =========================================================
        btnLogOut.addActionListener(e -> prosesLogOut());
        itemLogOut.addActionListener(e -> prosesLogOut());
        
        itemKeluar.addActionListener(e -> {
            int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin ingin menutup aplikasi?", "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                System.exit(0); 
            }
        });

        itemTentang.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "CleanWashApp v1.0\nDikembangkan oleh:\n- Yuni\n- Grey\n- Rosi\n\nTugas Proyek PBO 100% Selesai!", 
                "Tentang Aplikasi", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    /**
     * [METHOD BANTUAN]
     */
    private void prosesLogOut() {
        int konfirmasi = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin Log Out?", "Konfirmasi Log Out", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            dispose(); 
            new HalamanLogin().setVisible(true); 
        }
    }

    /**
     * [METHOD FILE I/O - READ/LOAD]
     */
    private void muatDataDariFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                String[] data = baris.split(",");
                if (data.length == 5) {
                    modelTabel.addRow(new Object[]{data[0], data[1], data[2], data[3], data[4]});
                }
            }
        } catch (IOException e) {}
    }

    /**
     * [METHOD FILE I/O - WRITE/SAVE ALL]
     */
    private void simpanSemuaDataKeFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, false))) { 
            for (int i = 0; i < modelTabel.getRowCount(); i++) {
                String nama = modelTabel.getValueAt(i, 0).toString();
                String tipe = modelTabel.getValueAt(i, 1).toString();
                String berat = modelTabel.getValueAt(i, 2).toString();
                String total = modelTabel.getValueAt(i, 3).toString();
                String estimasi = modelTabel.getValueAt(i, 4).toString();
                bw.write(nama + "," + tipe + "," + berat + "," + total + "," + estimasi);
                bw.newLine();
            }
        } catch (IOException e) {}
    }
}
