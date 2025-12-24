// java
package GUI;

import Controller.BudgetController;
import Controller.DashboardController;
import Controller.LoginController;
import Controller.TransactionController;
import model.Budget;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainGUI {

    private Runnable refreshTransaction;

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
        navDashboard = createNavLabel("Dashboard", true);
        navHistory = createNavLabel("History Transaction", false);
        navProfile = createNavLabel("Profile", false);

        headerPanel.add(navDashboard);
        headerPanel.add(navHistory);
        headerPanel.add(navProfile);

        cardLayout = new CardLayout();
        mainBodyPanel = new JPanel(cardLayout);


        JScrollPane dashboardScroll = new JScrollPane(createDashboardContent());
        dashboardScroll.setBorder(null);
        dashboardScroll.getVerticalScrollBar().setUnitIncrement(16);

        // HISTORY: use transaction data from controller
        JScrollPane historyScroll = new JScrollPane(createTransactionTable());
        historyScroll.setBorder(null);
        historyScroll.getVerticalScrollBar().setUnitIncrement(16);

        // PROFILE: show current user + dummy data + Logout button
        JPanel profilePanel = createProfilePanel();

        mainBodyPanel.add(dashboardScroll, "DASHBOARD");
        mainBodyPanel.add(historyScroll, "HISTORY");
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

        JPanel cardsContainer = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsContainer.setBackground(bgColor);

        double saldo = dashboardController.getSaldoBulanIni();
        double pemasukan = dashboardController.getTotalPemasukanBulanIni();
        double pengeluaran = dashboardController.getTotalPemasukanBulanIni(); // keep or change as needed

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

        // D. TOMBOL AKSI (ADD/UPDATE/DELETE)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(bgColor);

        Button addBtn = new Button("Add Transaction", unguTua, false);

        addBtn.addActionListener(e -> {
            JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
            JTextField descField = new JTextField();
            JTextField categoryField = new JTextField();
            String[] types = {"Income", "Expense"};
            JComboBox<String> typeBox = new JComboBox<>(types);
            JTextField amountField = new JTextField();

            form.add(new JLabel("Description:"));
            form.add(descField);
            form.add(new JLabel("Category:"));
            form.add(categoryField);
            form.add(new JLabel("Type:"));
            form.add(typeBox);
            form.add(new JLabel("Amount:"));
            form.add(amountField);

            int result = JOptionPane.showConfirmDialog(frame, form, "Add Transaction", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    double amount = Double.parseDouble(amountField.getText().trim());
                    String description = descField.getText().trim();
                    String category = categoryField.getText().trim();
                    String type = (String) typeBox.getSelectedItem();
                    LocalDate date = LocalDate.now();

                    // Run persistence off the EDT
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            transactionController.addTransaction(type, category, amount, date, description);
                            return null;
                        }
                        @Override
                        protected void done() {
                            try {
                                get(); // rethrow exceptions if any
                                if (refreshTransaction != null) refreshTransaction.run();
                                JOptionPane.showMessageDialog(frame, "Transaction added.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(frame, "Failed to add transaction: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }.execute();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        actionPanel.add(addBtn);

        Button updateBtn = new Button("Update", unguTua, false);
        updateBtn.addActionListener(e -> {
            String idInput = JOptionPane.showInputDialog(frame, "Enter transaction ID to update:", "Update Transaction", JOptionPane.QUESTION_MESSAGE);
            if (idInput == null || idInput.trim().isEmpty()) return;
            String txId = idInput.trim();

            JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
            JTextField descField = new JTextField();
            JTextField categoryField = new JTextField();
            String[] types = {"Income", "Expense"};
            JComboBox<String> typeBox = new JComboBox<>(types);
            JTextField amountField = new JTextField();

            form.add(new JLabel("New Description:"));
            form.add(descField);
            form.add(new JLabel("New Category:"));
            form.add(categoryField);
            form.add(new JLabel("New Type:"));
            form.add(typeBox);
            form.add(new JLabel("New Amount:"));
            form.add(amountField);

            int result = JOptionPane.showConfirmDialog(frame, form, "Update Transaction ID: " + txId, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    double amount = Double.parseDouble(amountField.getText().trim());
                    String description = descField.getText().trim();
                    String category = categoryField.getText().trim();
                    String type = (String) typeBox.getSelectedItem();

                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            transactionController.updateTransaction(txId, type, category, amount, LocalDate.now(), description);
                            return null;
                        }
                        @Override
                        protected void done() {
                            try {
                                get();
                                if (refreshTransaction != null) refreshTransaction.run();
                                JOptionPane.showMessageDialog(frame, "Transaction updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(frame, "Failed to update transaction: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }.execute();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        actionPanel.add(updateBtn);

        Button deleteBtn = new Button("Delete", unguTua, false);
        deleteBtn.addActionListener(e -> {
            String idInput = JOptionPane.showInputDialog(frame, "Enter transaction ID to delete:", "Delete Transaction", JOptionPane.QUESTION_MESSAGE);
            if (idInput == null || idInput.trim().isEmpty()) return;

            String txId = idInput.trim();
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete transaction ID: " + txId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        transactionController.deleteTransaction(txId);
                        return null;
                    }
                    @Override
                    protected void done() {
                        try {
                            get();
                            if (refreshTransaction != null) refreshTransaction.run();
                            JOptionPane.showMessageDialog(frame, "Transaction deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, "Failed to delete transaction: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.execute();
            }
        });
        actionPanel.add(deleteBtn);

        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 10, 10);
        dashboardPanel.add(actionPanel, gbc);

        JPanel tablePanel = createTransactionTable();

        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 20, 0);
        dashboardPanel.add(tablePanel, gbc);

        return dashboardPanel;
    }


    private JPanel createBudgetingSection() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(bgColor);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bgColor);
        header.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel title = new JLabel("SAVING TARGETS");
        title.setFont(new Font("BEBAS NEUE", Font.BOLD, 24));
        title.setForeground(unguTua);

        Button addTargetBtn = new Button("+ Add Target", unguTua, true);
        addTargetBtn.addActionListener(e -> {
                JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
                JTextField categoryField = new JTextField();
                JSpinner monthSpinner = new JSpinner(new SpinnerNumberModel(LocalDate.now().getMonthValue(), 1, 12, 1));
                JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2000, LocalDate.now().getYear() + 5, 1));
                JTextField amountField = new JTextField();

                form.add(new JLabel("Category:"));
                form.add(categoryField);
                form.add(new JLabel("Month (1-12):"));
                form.add(monthSpinner);
                form.add(new JLabel("Year:"));
                form.add(yearSpinner);
                form.add(new JLabel("Budget Amount:"));
                form.add(amountField);

                int result = JOptionPane.showConfirmDialog(frame, form, "Set Budget", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result != JOptionPane.OK_OPTION) return;

                String category = categoryField.getText().trim();
                int month = (int) monthSpinner.getValue();
                int year = (int) yearSpinner.getValue();
                double amount;
                try {
                    amount = Double.parseDouble(amountField.getText().trim());
                    if (category.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Category is required.", "Input Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (amount < 0) {
                        JOptionPane.showMessageDialog(frame, "Amount must be non-negative.", "Input Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Persist on background thread and refresh UI when done
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // call BudgetController properly
                        budgetController.setBudget(category, month, year, amount);
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            get();
                            if (refreshTransaction != null) refreshTransaction.run();
                            JOptionPane.showMessageDialog(frame, "Budget set for " + category + " (" + month + "/" + year + ").", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, "Failed to set budget: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.execute();
        });


        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnWrapper.setBackground(bgColor);
        btnWrapper.add(addTargetBtn);

        header.add(title, BorderLayout.WEST);
        header.add(btnWrapper, BorderLayout.EAST);

        container.add(header, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(bgColor);

        listPanel.add(createSingleTargetItem("Macbook Air M2", 18000000, 12500000));
        listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        listPanel.add(createSingleTargetItem("Liburan Akhir Tahun", 10000000, 2500000));

        List<Budget> budgets = budgetController.getBudgetsThisMonth();
        for (Budget b : budgets) {
            double currentAmount = 0;
            try {
               // currentAmount = budgetController.getTotalExpenseForCategoryMonth(
                //   b.getCategory(), b.getMonth(), b.getYear());
            } catch (Exception ignored) {}
            listPanel.add(createSingleTargetItem(b.getCategory(), b.getLimit(), currentAmount));
            listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        container.add(listPanel, BorderLayout.CENTER);

        return container;
    }

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

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(Color.WHITE);
        progressPanel.setBorder(new EmptyBorder(5, 20, 5, 20));

        JProgressBar progressBar = new JProgressBar(0, (int)targetPrice);
        progressBar.setValue((int)currentAmount);
        progressBar.setStringPainted(false);
        progressBar.setForeground(kuning);
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
        title.setBorder(new EmptyBorder(10,0,10,0));

        // Top area: title + filter bar
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBackground(bgColor);
        top.add(title);

        // Filter controls
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        filterBar.setBackground(bgColor);

        String[] filters = {"This Month", "By Month", "By Date Range", "By Category"};
        JComboBox<String> filterCombo = new JComboBox<>(filters);
        filterBar.add(new JLabel("Filter:"));
        filterBar.add(filterCombo);

        // Card panel for inputs
        JPanel inputCards = new JPanel(new CardLayout());
        inputCards.setBackground(bgColor);

        // 1) This Month: no inputs
        JPanel cardThis = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cardThis.setBackground(bgColor);
        inputCards.add(cardThis, "THIS");

        // 2) By Month: month + year
        JPanel cardMonth = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        cardMonth.setBackground(bgColor);
        String[] months = {
                "January","February","March","April","May","June",
                "July","August","September","October","November","December"
        };
        JComboBox<String> monthCombo = new JComboBox<>(months);
        JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(),
                2000, LocalDate.now().getYear() + 1, 1));
        cardMonth.add(new JLabel("Month:"));
        cardMonth.add(monthCombo);
        cardMonth.add(new JLabel("Year:"));
        cardMonth.add(yearSpinner);
        inputCards.add(cardMonth, "MONTH");

        // 3) By Date Range: start + end (yyyy-MM-dd)
        JPanel cardDate = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        cardDate.setBackground(bgColor);
        JTextField startField = new JTextField(10);
        JTextField endField = new JTextField(10);
        startField.setToolTipText("yyyy-MM-dd");
        endField.setToolTipText("yyyy-MM-dd");
        cardDate.add(new JLabel("From:"));
        cardDate.add(startField);
        cardDate.add(new JLabel("To:"));
        cardDate.add(endField);
        inputCards.add(cardDate, "DATE");

        // 4) By Category: category dropdown (load from controller)
        JPanel cardCategory = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        cardCategory.setBackground(bgColor);
        List<String> categories = List.of();
        try {
            categories = transactionController.getAllCategories();
        } catch (Exception ignored) {}
        JComboBox<String> categoryCombo;
        if (categories.isEmpty()) {
            categoryCombo = new JComboBox<>(new String[]{"No categories"});
            categoryCombo.setEnabled(false);
        } else {
            categoryCombo = new JComboBox<>(categories.toArray(new String[0]));
        }
        cardCategory.add(new JLabel("Category:"));
        cardCategory.add(categoryCombo);
        inputCards.add(cardCategory, "CATEGORY");

        filterBar.add(inputCards);

        // Apply button
        JButton applyBtn = new JButton("Apply");
        filterBar.add(applyBtn);

        top.add(filterBar);
        panel.add(top, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Tanggal", "Kategori", "Deskripsi", "Tipe", "Jumlah"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

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

        // Helper to update table rows
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DecimalFormat df = new DecimalFormat("#,###.00");
        Runnable loadThisMonth = () -> {
            try {
                List<Transaction> list = transactionController.getTransactionsThisMonth();
                model.setRowCount(0);
                for (Transaction t : list) {
                    model.addRow(new Object[]{
                            t.getDate().format(dateFmt),
                            t.getCategory(),
                            t.getDescription(),
                            t.getType(),
                            "Rp " + df.format(t.getAmount())
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Failed to load transactions: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        };

        // Card switching on filter selection
        filterCombo.addActionListener(e -> {
            String sel = (String) filterCombo.getSelectedItem();
            CardLayout cl = (CardLayout) inputCards.getLayout();
            if ("By Month".equals(sel)) cl.show(inputCards, "MONTH");
            else if ("By Date Range".equals(sel)) cl.show(inputCards, "DATE");
            else if ("By Category".equals(sel)) cl.show(inputCards, "CATEGORY");
            else cl.show(inputCards, "THIS");
        });

        // Apply button action
        applyBtn.addActionListener(e -> {
            String sel = (String) filterCombo.getSelectedItem();
            try {
                List<Transaction> results;
                switch (sel) {
                    case "By Month":
                        int month = monthCombo.getSelectedIndex() + 1;
                        int year = (int) yearSpinner.getValue();
                        results = transactionController.getTransactionsByMonth(month, year);
                        break;
                    case "By Date Range":
                        LocalDate start = LocalDate.parse(startField.getText().trim());
                        LocalDate end = LocalDate.parse(endField.getText().trim());
                        results = transactionController.getTransactionsByDateRange(start, end);
                        break;
                    case "By Category":
                        if (categoryCombo.isEnabled()) {
                            String cat = (String) categoryCombo.getSelectedItem();
                            results = transactionController.getTransactionsByCategory(cat);
                        } else {
                            results = List.of();
                        }
                        break;
                    default: // This Month
                        results = transactionController.getTransactionsThisMonth();
                        break;
                }
                // update table
                model.setRowCount(0);
                for (Transaction t : results) {
                    model.addRow(new Object[]{
                            t.getDate().format(dateFmt),
                            t.getCategory(),
                            t.getDescription(),
                            t.getType(),
                            "Rp " + df.format(t.getAmount())
                    });
                }
            } catch (DateTimeParseException dtpe) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Use yyyy-MM-dd",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Failed to fetch transactions: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // initial load (this month)
        loadThisMonth.run();

        return panel;
    }

    // Profile panel: shows current user, dummy details and a Logout button
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20,20,20,20));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Name: " + (authController.getCurrentUser() != null ? authController.getCurrentUser() : "Guest"));
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setForeground(unguTua);
        info.add(nameLabel);
        info.add(Box.createRigidArea(new Dimension(0,10)));

        JLabel emailLabel = new JLabel("Email: user@example.com"); // dummy
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        info.add(emailLabel);
        info.add(Box.createRigidArea(new Dimension(0,8)));

        JLabel memberLabel = new JLabel("Member since: 2024"); // dummy
        memberLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        info.add(memberLabel);

        panel.add(info, BorderLayout.CENTER);

        // Logout button using Button class
        Button logoutBtn = new Button("Logout", unguTua, false);
        logoutBtn.addActionListener(e -> {
            // If you have a authController.logout() method, call it here.
            frame.dispose();
            System.exit(0);
        });

        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnWrap.setBackground(Color.WHITE);
        btnWrap.add(logoutBtn);

        panel.add(btnWrap, BorderLayout.SOUTH);
        return panel;
    }
}