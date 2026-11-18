package laundry.gui;

import laundry.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormTransaksi extends JFrame {
    private JTextField txtNama, txtKuantitas;
    private JComboBox<String> cbTipe, cbPaket;
    private JButton btnTambah, btnUbah, btnHapus;
    private JTable tabelTransaksi;
    private DefaultTableModel modelTabel;

    public FormTransaksi() {
        setTitle("Kelola Transaksi Laundry");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // =========================================================
        // BAGIAN 1: Mengatur Layout Manager (Syarat: Layout Manager)
        // =========================================================
        // Mengubah layout frame utama menjadi BorderLayout
        setLayout(new BorderLayout());

        // Membuat Panel khusus (Panel Form) dengan Absolute Layout (null)
        // agar posisi komponen tetap aman dan tidak berantakan
        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setPreferredSize(new Dimension(700, 220));

        // --- Komponen Input ---
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

        // --- Komponen Tombol ---
        btnTambah = new JButton("Tambah");
        btnTambah.setBounds(20, 180, 90, 30);
        panelForm.add(btnTambah);

        btnUbah = new JButton("Ubah");
        btnUbah.setBounds(120, 180, 90, 30);
        panelForm.add(btnUbah);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(220, 180, 90, 30);
        panelForm.add(btnHapus);

        // Menempatkan Panel Form di bagian UTARA (Atas) frame
        add(panelForm, BorderLayout.NORTH);

        // --- Komponen Tabel (Bawah/Tengah) ---
        String[] kolom = {"Nama", "Tipe", "Berat/Jml", "Total Biaya (Rp)", "Estimasi"};
        modelTabel = new DefaultTableModel(kolom, 0);
        tabelTransaksi = new JTable(modelTabel);
        JScrollPane scrollPane = new JScrollPane(tabelTransaksi);
        
        // Menempatkan Tabel di TENGAH agar menyesuaikan sisa layar
        add(scrollPane, BorderLayout.CENTER);

        // =========================================================
        // BAGIAN 2: Event Handling (Exception, CRUD, & Simpan TXT)
        // =========================================================
        
        // 1. Logika Tombol TAMBAH (Create)
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

                // Polimorfisme: Menggunakan kelas model berdasarkan pilihan tipe
                Layanan layanan;
                if (tipeLayanan.equals("Kiloan")) {
                    layanan = new LayananKiloan(nama, kuantitas, tipePaket);
                } else {
                    layanan = new LayananSatuan(nama, kuantitas, tipePaket); 
                }

                // Menghitung biaya dan mengambil estimasi waktu
                double total = layanan.hitungBiaya();
                String estimasi = ((EstimasiWaktu) layanan).getEstimasiSelesai();

                // Menambahkan data baru ke dalam Tabel GUI
                modelTabel.addRow(new Object[]{nama, tipeLayanan, kuantitas, total, estimasi});

                // Syarat: Simpan data permanen (File I/O)
                String dataSimpan = nama + "," + tipeLayanan + "," + kuantitas + "," + total + "," + estimasi;
                FileStorage.simpanData(dataSimpan);

                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan & disimpan ke TXT!");
                
                // Mengosongkan form setelah input
                txtNama.setText("");
                txtKuantitas.setText("");
            } catch (NumberFormatException ex) {
                // Menangani error jika user mengetik huruf pada kolom angka
                JOptionPane.showMessageDialog(this, "Format berat harus berupa angka!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage());
            }
        });

        // 2. Logika Tombol UBAH (Update)
        btnUbah.addActionListener(e -> {
            // Mengecek baris ke berapa yang dipilih pada tabel
            int baris = tabelTransaksi.getSelectedRow();
            
            if (baris >= 0) {
                try {
                    String namaBaru = txtNama.getText();
                    double kuantitasBaru = Double.parseDouble(txtKuantitas.getText());
                    
                    // Memperbarui data pada tabel layar (Kolom 0: Nama, Kolom 2: Berat)
                    modelTabel.setValueAt(namaBaru, baris, 0); 
                    modelTabel.setValueAt(kuantitasBaru, baris, 2); 
                    
                    JOptionPane.showMessageDialog(this, "Data di tabel berhasil diubah!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Format berat harus berupa angka!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris di tabel dulu sebelum mengubah data!");
            }
        });

        // 3. Logika Tombol HAPUS (Delete)
        btnHapus.addActionListener(e -> {
            // Mengecek baris ke berapa yang dipilih pada tabel
            int baris = tabelTransaksi.getSelectedRow();
            
            if (baris >= 0) {
                int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    // Menghapus data dari tabel
                    modelTabel.removeRow(baris);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris di tabel yang mau dihapus!");
            }
        });
    }
}
