package laundry.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import laundry.controller.LaundryController;

// Inheritance: mewarisi JFrame untuk membentuk jendela login (MVC - View halaman 1)
public class HalamanLogin extends JFrame {
    private static final long serialVersionUID = 1L;

    // Encapsulation & Komposisi MVC: controller dan komponen GUI disembunyikan (private)
    private final LaundryController controller;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnReset;

    // Constructor default (public): membuat objek controller lalu menyiapkan tampilan dan event
    public HalamanLogin() {
        this.controller = new LaundryController();
        inisialisasiGUI();
        pasangEvent();
    }

    // Void instance method (private): menyusun komponen antarmuka dengan BorderLayout, GridBagLayout, dan FlowLayout
    private void inisialisasiGUI() {
        setTitle("Login - CleanWashApp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(430, 270);
        setMinimumSize(new Dimension(400, 250));
        setLocationRelativeTo(null);

        // Panel utama dengan BorderLayout sebagai wadah seluruh area jendela
        JPanel panelUtama = new JPanel(new BorderLayout(10, 10));
        panelUtama.setBorder(new EmptyBorder(16, 20, 16, 20));
        setContentPane(panelUtama);

        // Area judul di bagian atas (NORTH) dengan FlowLayout
        JPanel panelJudul = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblJudul = new JLabel("LOGIN CLEANWASHAPP");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelJudul.add(lblJudul);
        panelUtama.add(panelJudul, BorderLayout.NORTH);

        // Area input username dan password di tengah (CENTER) dengan GridBagLayout
        JPanel panelForm = new JPanel(new GridBagLayout());

        // Label dan input username (baris 0)
        JLabel lblUsername = new JLabel("Username:");
        GridBagConstraints gbcLblUser = new GridBagConstraints();
        gbcLblUser.insets = new Insets(6, 6, 6, 8);
        gbcLblUser.anchor = GridBagConstraints.WEST;
        gbcLblUser.gridx = 0;
        gbcLblUser.gridy = 0;
        panelForm.add(lblUsername, gbcLblUser);

        txtUsername = new JTextField(18);
        GridBagConstraints gbcTxtUser = new GridBagConstraints();
        gbcTxtUser.insets = new Insets(6, 6, 6, 6);
        gbcTxtUser.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtUser.gridx = 1;
        gbcTxtUser.gridy = 0;
        panelForm.add(txtUsername, gbcTxtUser);

        // Label dan input password (baris 1)
        JLabel lblPassword = new JLabel("Password:");
        GridBagConstraints gbcLblPass = new GridBagConstraints();
        gbcLblPass.insets = new Insets(6, 6, 6, 8);
        gbcLblPass.anchor = GridBagConstraints.WEST;
        gbcLblPass.gridx = 0;
        gbcLblPass.gridy = 1;
        panelForm.add(lblPassword, gbcLblPass);

        txtPassword = new JPasswordField(18);
        GridBagConstraints gbcTxtPass = new GridBagConstraints();
        gbcTxtPass.insets = new Insets(6, 6, 6, 6);
        gbcTxtPass.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtPass.gridx = 1;
        gbcTxtPass.gridy = 1;
        panelForm.add(txtPassword, gbcTxtPass);

        // Label petunjuk kredensial default (baris 2)
        JLabel lblInfo = new JLabel("Akun default: admin / 12345");
        lblInfo.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);
        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.insets = new Insets(8, 6, 2, 6);
        gbcInfo.gridwidth = 2;
        gbcInfo.gridx = 0;
        gbcInfo.gridy = 2;
        panelForm.add(lblInfo, gbcInfo);

        panelUtama.add(panelForm, BorderLayout.CENTER);

        // Area tombol aksi di bagian bawah (SOUTH) dengan FlowLayout
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        btnLogin = new JButton("Masuk");
        btnReset = new JButton("Bersihkan");
        panelTombol.add(btnLogin);
        panelTombol.add(btnReset);
        panelUtama.add(panelTombol, BorderLayout.SOUTH);
    }

    // Void instance method (private) & Event Handling: menghubungkan tombol dan kolom password ke ActionListener
    private void pasangEvent() {
        btnLogin.addActionListener(e -> prosesLogin());
        txtPassword.addActionListener(e -> prosesLogin());
        btnReset.addActionListener(e -> bersihkanForm());
    }

    // Void instance method (private): memvalidasi input kosong lalu mencocokkan akun lewat controller
    private void prosesLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Username dan Password tidak boleh kosong!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (controller.validasiLogin(username, password)) {
            JOptionPane.showMessageDialog(
                this,
                "Login berhasil! Selamat datang di CleanWashApp.",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE
            );
            new MenuUtama(controller).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Username atau Password salah!",
                "Login Gagal",
                JOptionPane.ERROR_MESSAGE
            );
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    // Void instance method (private): mengosongkan kolom username dan password
    private void bersihkanForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtUsername.requestFocus();
    }

    // Static void method (public): titik masuk utama untuk menjalankan aplikasi di Event Dispatch Thread Swing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Exception Handling: mengatur tampilan agar mengikuti tema sistem operasi
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new HalamanLogin().setVisible(true);
        });
    }
}