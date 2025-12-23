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
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class MainGUI {
    //color pallete
    Color kuning = new Color(255,204, 0);
    Color unguMuda = new Color(177,59,255);
    Color unguTua = new Color(71, 19, 150);
    Color biruTua = new Color(9,0, 64);

    //ngambil dari frame
    Color bgColor;

    //attribute data
    static LoginController authController = new LoginController();
    static DashboardController dashboardController = new DashboardController();
    static TransactionController transactionController = new TransactionController();
    static BudgetController budgetController = new BudgetController();


    public MainGUI (){

        var warnings = budgetController.getBudgetWarnings();
        JFrame frame = new JFrame("Personal Finance Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        bgColor = frame.getBackground();

        JPanel header = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JScrollPane mainScroll = new JScrollPane(header);
        mainScroll.setBorder(null);

        gbc.insets = new Insets(2, 10, 2, 10);
        gbc.fill =  GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        header.setBorder(new EmptyBorder(4,20,0,20));

        JLabel helloText = new JLabel("Hello, " + authController.getCurrentUser() + "!");
        helloText.setFont(new Font("BEBAS NEUE", Font.BOLD, 34));
        helloText.setBackground(Color.WHITE);
        helloText.setForeground(biruTua);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(2,10,0,10);
        header.add(helloText, gbc);

        JLabel dashboard = new JLabel("DASHBOARD");
        dashboard.setFont(new Font("BEBAS NEUE", Font.BOLD, 30));
        dashboard.setBackground(Color.white);
        dashboard.setForeground(biruTua);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(0,10,2,10);
        header.add(dashboard, gbc);

        JPanel cardsContainer = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsContainer.setBackground(frame.getBackground());

        double saldo = dashboardController.getSaldoBulanIni();
        double pemasukan = dashboardController.getTotalPemasukanBulanIni();
        double pengeluaran = dashboardController.getTotalPengeluaranBulanIni();

        gbc.insets = new Insets(20,0,0,0);
        cardsContainer.add(createKartuSaldo("Sisa Saldo",saldo));

        cardsContainer.add(createKartuSaldo("Total Pemasukkan Bulanan",pemasukan));

        cardsContainer.add(createKartuSaldo("Total Pengeluaran Bulanan",pengeluaran));


        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.insets = new Insets(10, 10, 20, 10);
        header.add(cardsContainer, gbc);


        if (!warnings.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Cooming soon", "⚠️ PERINGATAN BUDGET", JOptionPane.WARNING_MESSAGE);
            }

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(frame.getBackground());

        //add button
        Button addBtn = new Button("Add", unguTua, true);
        addBtn.setPreferredSize(new Dimension(120, 40));
        addBtn.setFont(new Font("BEBAS NEUE", Font.PLAIN, 16));
        addBtn.addActionListener(e -> {
            // Open Add Transaction Dialog
        });

        actionPanel.add(addBtn, gbc);

        //update Button
        Button updateBtn = new Button("Update", unguTua, true);
        updateBtn.setPreferredSize(new Dimension(120, 40));
        updateBtn.setFont(new Font("BEBAS NEUE", Font.PLAIN, 16));
        updateBtn.addActionListener(e -> {
            // Open Add Transaction Dialog
        });

        actionPanel.add(updateBtn, gbc);

        //delete Button
        Button deleteBtn = new Button("Delete", unguTua, true);
        deleteBtn.setPreferredSize(new Dimension(120, 40));
        deleteBtn.setFont(new Font("BEBAS NEUE", Font.PLAIN, 16));
        deleteBtn.addActionListener(e -> {
            // Open Add Transaction Dialog
        });

        actionPanel.add(deleteBtn, gbc);
        gbc.gridy = 3; gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        header.add(actionPanel, gbc);


        //table
        JPanel tablePanel = createTransactionTable();

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        header.add(tablePanel, gbc);



        frame.add(mainScroll, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    private JPanel createKartuSaldo(String text,double parameter) {
        RoundedPanel kartuSaldo = new RoundedPanel(25, unguMuda, unguTua);
        kartuSaldo.setLayout(new BorderLayout());
        kartuSaldo.setMaximumSize(new Dimension(340,100));
        kartuSaldo.setBorder(new EmptyBorder(15,15,15,15));
        kartuSaldo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sisaSaldo = new JLabel(text);
        sisaSaldo.setForeground(Color.white);
        sisaSaldo.setFont(new Font("BEBAS NEUE", Font.PLAIN, 16));
        sisaSaldo.setBounds(20,20,100,30);
        kartuSaldo.add(sisaSaldo, BorderLayout.NORTH);

        JLabel saldoAmount = new JLabel();
        saldoAmount.setForeground(Color.WHITE);
        saldoAmount.setFont(new Font("BEBAS NEUE", Font.PLAIN, 24));
        DecimalFormat df = new DecimalFormat("#,###.00");
        saldoAmount.setText(df.format(parameter));

        kartuSaldo.add(saldoAmount, BorderLayout.CENTER);

        return kartuSaldo;
    }

    private JPanel createTransactionTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        JLabel title = new JLabel("Riwayat Transaksi Bulan Ini");
        title.setFont(new Font("BEBAS NEUE", Font.BOLD, 30));
        title.setForeground(unguTua);
        title.setBorder(new EmptyBorder(10,10,10,10));
        panel.add(title, BorderLayout.NORTH);

        String[] columnNames = {"Tanggal", "Kategori", "Deskripsi", "Tipe", "Jumlah"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };



        DecimalFormat df = new DecimalFormat("#,###.00");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd MMM yyyy");

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

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);

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