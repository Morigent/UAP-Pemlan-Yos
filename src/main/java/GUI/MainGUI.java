package GUI;

import Controller.BudgetController;
import Controller.DashboardController;
import Controller.LoginController;
import Controller.TransactionController;
import model.Transaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class MainGUI {
    // --- 1. WARNA & ATTRIBUTE ---
    Color kuning = new Color(255, 204, 0);
    Color unguMuda = new Color(177, 59, 255);
    Color unguTua = new Color(71, 19, 150);
    Color biruTua = new Color(9, 0, 64);
    Color bgColor;

    // Controllers
    static LoginController authController = new LoginController();
    static DashboardController dashboardController = new DashboardController();
    static TransactionController transactionController = new TransactionController();
    static BudgetController budgetController = new BudgetController();

    private JFrame frame;
    private JPanel mainBodyPanel;
    private CardLayout cardLayout;

    private JLabel navDashboard, navHistory, navProfile;

    public MainGUI() {
        frame = new JFrame("Personal Finance Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        bgColor = frame.getBackground();


        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        headerPanel.setBackground(unguTua);
        headerPanel.setBorder(new EmptyBorder(1,10,1,10));

        // Membuat Text Menu Header
        navDashboard = createNavLabel("Dashboard", true); // Aktif (Kuning) saat pertama buka
        navHistory = createNavLabel("History Transaction", false);
        navProfile = createNavLabel("Profile", false);

        headerPanel.add(navDashboard);
        headerPanel.add(navHistory);
        headerPanel.add(navProfile);

        // --- 3. MAIN CONTENT (CARD LAYOUT) ---
        cardLayout = new CardLayout();
        mainBodyPanel = new JPanel(cardLayout);


        JScrollPane dashboardScroll = new JScrollPane(createDashboardContent());
        dashboardScroll.setBorder(null);
        dashboardScroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel historyPanel = new JPanel();
        historyPanel.setBackground(Color.WHITE);
        historyPanel.add(new JLabel("Halaman History Transaction"));

        JPanel profilePanel = new JPanel();
        profilePanel.setBackground(Color.WHITE);
        profilePanel.add(new JLabel("Halaman Profile"));

        mainBodyPanel.add(dashboardScroll, "DASHBOARD");
        mainBodyPanel.add(historyPanel, "HISTORY");
        mainBodyPanel.add(profilePanel, "PROFILE");

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(mainBodyPanel, BorderLayout.CENTER);

        setupNavigationListeners();

        var warnings = budgetController.getBudgetWarnings();
        if (!warnings.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Coming soon", "⚠️ PERINGATAN BUDGET", JOptionPane.WARNING_MESSAGE);
        }

        frame.setVisible(true);
    }

    private JPanel createDashboardContent() {
        JPanel dashboardPanel = new JPanel(new GridBagLayout());
        dashboardPanel.setBackground(bgColor);
        dashboardPanel.setBorder(new EmptyBorder(4,20,0,20)); // Border luar
        GridBagConstraints gbc = new GridBagConstraints();

        // A. HELLO TEXT
        JLabel helloText = new JLabel("Hello, " + authController.getCurrentUser() + "!");
        helloText.setFont(new Font("BEBAS NEUE", Font.BOLD, 34));
        helloText.setForeground(biruTua);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2,10,0,10);
        dashboardPanel.add(helloText, gbc);

        JLabel subTitle = new JLabel("DASHBOARD");
        subTitle.setFont(new Font("BEBAS NEUE", Font.BOLD, 30));
        subTitle.setForeground(biruTua);

        gbc.gridy = 1;
        gbc.insets = new Insets(0,10,2,10);
        dashboardPanel.add(subTitle, gbc);

        // B. SUMMARY CARDS (KARTU SALDO)
        JPanel cardsContainer = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsContainer.setBackground(bgColor);

        double saldo = dashboardController.getSaldoBulanIni();
        double pemasukan = dashboardController.getTotalPemasukanBulanIni();
        double pengeluaran = dashboardController.getTotalPengeluaranBulanIni();

        cardsContainer.add(createKartuSaldo("Sisa Saldo", saldo));
        cardsContainer.add(createKartuSaldo("Total Pemasukan", pemasukan));
        cardsContainer.add(createKartuSaldo("Total Pengeluaran", pengeluaran));

        gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 20, 10);
        dashboardPanel.add(cardsContainer, gbc);

        JPanel budgetingSection = createBudgetingSection();

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 20, 10);
        dashboardPanel.add(budgetingSection, gbc);

        // D. TOMBOL AKSI (ADD/UPDATE/DELETE) - MENGEMBALIKAN KODE ASLI ANDA
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(bgColor);

        Button addBtn = new Button("Add Transaction", unguTua, false);

        addBtn.addActionListener(e -> {
            // Open Add Transaction Dialog
        });
        actionPanel.add(addBtn);

        Button updateBtn = new Button("Update", unguTua, false);
        updateBtn.addActionListener(e -> {
            // Open Update Dialog
        });
        actionPanel.add(updateBtn);

        Button deleteBtn = new Button("Delete", unguTua, false);
        deleteBtn.addActionListener(e -> {
            // Open Delete Dialog
        });
        actionPanel.add(deleteBtn);

        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 10, 10);
        dashboardPanel.add(actionPanel, gbc);

        // E. TABLE TRANSAKSI (MENGEMBALIKAN POSISI ASLI)
        JPanel tablePanel = createTransactionTable();

        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 20, 0);
        dashboardPanel.add(tablePanel, gbc);

        return dashboardPanel;
    }

    // ==================================================================================
    // BAGIAN 2: LOGIKA BUDGETING (SAWERIA STYLE)
    // ==================================================================================

    private JPanel createBudgetingSection() {
        // Container utama budgeting
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(bgColor);

        // 1. Header: Judul + Tombol Add
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bgColor);
        header.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel title = new JLabel("SAVING TARGETS");
        title.setFont(new Font("BEBAS NEUE", Font.BOLD, 24));
        title.setForeground(unguTua);

        Button addTargetBtn = new Button("+ Add Target", unguTua, true);
        addTargetBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Form Tambah Target Muncul Disini"));

        // Membungkus tombol agar di kanan
        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnWrapper.setBackground(bgColor);
        btnWrapper.add(addTargetBtn);

        header.add(title, BorderLayout.WEST);
        header.add(btnWrapper, BorderLayout.EAST);

        container.add(header, BorderLayout.NORTH);

        // 2. List Progress Bar
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(bgColor);

        // Dummy Data
        listPanel.add(createSingleTargetItem("Macbook Air M2", 18000000, 12500000));
        listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        listPanel.add(createSingleTargetItem("Liburan Akhir Tahun", 10000000, 2500000));

        container.add(listPanel, BorderLayout.CENTER);

        return container;
    }

    // Membuat Item Progress Bar
    private JPanel createSingleTargetItem(String itemName, double targetPrice, double currentAmount) {
        RoundedPanel panel = new RoundedPanel(15, Color.WHITE, Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setMaximumSize(new Dimension(2000, 85));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.WHITE);

        JLabel lblName = new JLabel(itemName);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblName.setForeground(biruTua);

        int percent = (int) ((currentAmount / targetPrice) * 100);
        JLabel lblStatus = new JLabel(percent + "% Terkumpul");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblStatus.setForeground(Color.GRAY);

        infoPanel.add(lblName);
        infoPanel.add(lblStatus);

        // Progress Bar (Tengah/Kanan)
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(Color.WHITE);
        progressPanel.setBorder(new EmptyBorder(5, 20, 5, 20));

        JProgressBar progressBar = new JProgressBar(0, (int)targetPrice);
        progressBar.setValue((int)currentAmount);
        progressBar.setStringPainted(false);
        progressBar.setForeground(kuning); // Warna Bar
        progressBar.setBackground(new Color(230, 230, 230));
        progressBar.setPreferredSize(new Dimension(100, 12));

        DecimalFormat df = new DecimalFormat("#,###");
        JLabel lblNominal = new JLabel("Rp " + df.format(currentAmount) + " / Rp " + df.format(targetPrice));
        lblNominal.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblNominal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblNominal.setBorder(new EmptyBorder(0, 0, 5, 0));

        progressPanel.add(lblNominal, BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);

        panel.add(infoPanel, BorderLayout.WEST);
        panel.add(progressPanel, BorderLayout.CENTER);

        return panel;
    }


    private JLabel createNavLabel(String text, boolean isActive) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("BEBAS NEUE", Font.PLAIN, 24));
        label.setForeground(isActive ? kuning : Color.WHITE);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return label;
    }

    private void setupNavigationListeners() {
        navDashboard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { switchPanel("DASHBOARD", navDashboard); }
        });
        navHistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { switchPanel("HISTORY", navHistory); }
        });
        navProfile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { switchPanel("PROFILE", navProfile); }
        });
    }

    private void switchPanel(String cardName, JLabel activeLabel) {
        cardLayout.show(mainBodyPanel, cardName);
        navDashboard.setForeground(Color.WHITE);
        navHistory.setForeground(Color.WHITE);
        navProfile.setForeground(Color.WHITE);
        activeLabel.setForeground(kuning);
    }


    private JPanel createKartuSaldo(String text, double parameter) {
        RoundedPanel kartuSaldo = new RoundedPanel(25, unguMuda, unguTua);
        kartuSaldo.setLayout(new BorderLayout());
        kartuSaldo.setMaximumSize(new Dimension(340,100));
        kartuSaldo.setBorder(new EmptyBorder(15,15,15,15));
        kartuSaldo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel(text);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("BEBAS NEUE", Font.PLAIN, 16));
        kartuSaldo.add(title, BorderLayout.NORTH);

        JLabel amount = new JLabel();
        amount.setForeground(Color.WHITE);
        amount.setFont(new Font("BEBAS NEUE", Font.PLAIN, 24));
        DecimalFormat df = new DecimalFormat("Rp #,###.00");
        amount.setText(df.format(parameter));
        kartuSaldo.add(amount, BorderLayout.CENTER);

        return kartuSaldo;
    }

    private JPanel createTransactionTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        JLabel title = new JLabel("Riwayat Transaksi Bulan Ini");
        title.setFont(new Font("BEBAS NEUE", Font.BOLD, 30));
        title.setForeground(unguTua);
        title.setBorder(new EmptyBorder(10,0,10,0)); // Sesuaikan border
        panel.add(title, BorderLayout.NORTH);

        String[] columnNames = {"Tanggal", "Kategori", "Deskripsi", "Tipe", "Jumlah"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        DecimalFormat df = new DecimalFormat("#,###.00");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd MMM yyyy");

        if(transactionController != null) {
            for (Transaction t : transactionController.getTransactionsThisMonth()) {
                Object[] row = {
                        t.getDate().format(dateFmt),
                        t.getCategory(),
                        t.getDescription(),
                        t.getType(),
                        "Rp " + df.format(t.getAmount())
                };
                model.addRow(row);
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(unguTua);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}