package GUI;

import controller.LoginController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LandingPage {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalUang;
    private JLabel totalPengeluaran;

    public LandingPage () {
        frame = new JFrame("Moneeeyyyyyy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(new Color(252, 252, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.CENTER;
        header.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Welcome to Moneeeyyyyyy");
        title.setFont(new Font("BEBAS NEUE", Font.BOLD, 30));
        title.setBackground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0;
        header.add(title, gbc);

        //textfield username
        JTextField username = new JTextField("Username");
        username.setForeground(Color.GRAY);
        username.setFont(new Font("Arial", Font.PLAIN, 14));
        username.setPreferredSize(new Dimension(200, 30));

        username.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (username.getForeground() == Color.GRAY || username.getText().equals("Username")) {
                    username.setText("");
                    username.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (username.getText().trim().isEmpty()) {
                    username.setText("Username");
                    username.setForeground(new Color(0x8F8F8F));
                }
            }
        });

        gbc.gridx = 0; gbc.gridy = 1;
        header.add(username, gbc);

        //textfield password
        JPasswordField password = new JPasswordField("Password");
        password.setForeground(Color.GRAY);
        password.setFont(new Font("Arial", Font.PLAIN, 14));
        password.setPreferredSize(new Dimension(200, 30));
        password.setEchoChar((char) 0);

        password.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(password.getForeground() == Color.GRAY || String.valueOf(password.getPassword()).equals("Password")) {
                    password.setText("");
                    password.setForeground(Color.BLACK);
                    password.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (password.getPassword().length == 0) {
                    password.setText("Password");
                    password.setForeground(new Color(0x8F8F8F));
                    password.setEchoChar((char) 0);
                }
            }
        });

        gbc.gridx = 0; gbc.gridy = 2;
        header.add(password, gbc);

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        loginBtn.setPreferredSize(new Dimension(200, 30));

        gbc.gridx = 0; gbc.gridy = 4;
        header.add(loginBtn, gbc);

        JLabel registerBtn = new JLabel("Don't have an account? Register");
        registerBtn.setFont(new Font("Arial", Font.PLAIN, 9));
        registerBtn.setForeground(new Color(0x111111));
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.setFocusable(true);
        registerBtn.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                registerBtn.setForeground(new Color(0x00F539));
                registerBtn.setBorder(BorderFactory.createDashedBorder(Color.RED));
            }

            @Override
            public void focusLost(FocusEvent e) {
                registerBtn.setForeground(new Color(0x111111));
            }
        });

        gbc.gridx = 0; gbc.gridy = 3;
        header.add(registerBtn, gbc);

        header.add(title);
        frame.add(header, BorderLayout.CENTER);

        loginBtn.addActionListener( e -> {
            LoginController loginController = new LoginController();
            String userText = username.getText();
            String pwdText = String.valueOf(password.getPassword());

            //pengecekan login
            if (loginController.login(userText,pwdText)){
                MainGUI mainGUI = new MainGUI();
                frame.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(frame, "Username atau Password salah", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                registerPage();
                frame.setVisible(false);
            }
        });

        frame.setVisible(true);

    }

    public void registerPage() {
        JFrame frame = new JFrame("Register Page");
        JPanel panel = new JPanel(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel registerLabel = new JLabel("Register Page");
        registerLabel.setFont(new Font("BEBAS NEUE", Font.BOLD, 30));
        registerLabel.setBackground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(registerLabel, gbc);

        //textfield username
        JTextField username = new JTextField("Username");
        username.setForeground(Color.GRAY);
        username.setFont(new Font("Arial", Font.PLAIN, 14));
        username.setPreferredSize(new Dimension(200, 30));

        username.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(username.getForeground() == Color.GRAY || username.getText().equals("Username")) {
                    username.setText("");
                    username.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if( username.getText().trim().isEmpty()) {
                    username.setText("Username");
                    username.setForeground(new Color(0x8F8F8F));
                }
            }
        });

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(username, gbc);

        //textfield password
        JPasswordField password = new JPasswordField("Password");
        password.setForeground(Color.GRAY);
        password.setFont(new Font("Arial", Font.PLAIN, 14));
        password.setPreferredSize(new Dimension(200, 30));
        password.setEchoChar((char) 0);

        password.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (password.getForeground() == Color.GRAY || String.valueOf(password.getPassword()).equals("Password")) {
                    password.setText("");
                    password.setForeground(Color.BLACK);
                    password.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if( password.getPassword().length == 0) {
                    password.setText("Password");
                    password.setForeground(new Color(0x8F8F8F));
                    password.setEchoChar((char) 0);
                }
            }
        });

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(password, gbc);

        //textfield confirm password
        JPasswordField confirmPassword = new JPasswordField("Confirm Password");
        confirmPassword.setForeground(Color.GRAY);
        confirmPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPassword.setPreferredSize(new Dimension(200, 30));
        confirmPassword.setEchoChar((char) 0);

        confirmPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(confirmPassword.getForeground() == Color.GRAY || String.valueOf(confirmPassword.getPassword()).equals("Confirm Password")) {
                    confirmPassword.setText("");
                    confirmPassword.setForeground(Color.BLACK);
                    confirmPassword.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if( confirmPassword.getPassword().length == 0) {
                    confirmPassword.setText("Confirm Password");
                    confirmPassword.setForeground(new Color(0x8F8F8F));
                    confirmPassword.setEchoChar((char) 0);
                }
            }
        });

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(confirmPassword, gbc);

        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        registerBtn.setPreferredSize(new Dimension(200, 30));

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(registerBtn, gbc);

        registerBtn.addActionListener(e -> {
            LoginController loginController = new LoginController();
            String userText = username.getText();
            String pwdText = String.valueOf(password.getPassword());
            String confirmPwdText = String.valueOf(confirmPassword.getPassword());

            //pengecekan register
            if (loginController.register(userText, pwdText, confirmPwdText)){
                JOptionPane.showMessageDialog(frame, "Register berhasil! Silahkan login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                LandingPage landingPage = new LandingPage();
            } else {
                JOptionPane.showMessageDialog(frame, "Register gagal! Coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        frame.add(panel, BorderLayout.NORTH);

        frame.setVisible(true);


    }

}
