package GUI;

import Controller.BudgetController;
import Controller.DashboardController;
import Controller.LoginController;
import Controller.TransactionController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class MainGUI {
    //color pallete
    Color kuning = new Color(255,204, 0);
    Color unguMuda = new Color(177,59,255);
    Color unguTua = new Color(71, 19, 150);
    Color biruTua = new Color(9,0, 64);

    //attribute data
    static LoginController authController = new LoginController();
    static DashboardController dashboardController = new DashboardController();
    static TransactionController transactionController = new TransactionController();
    static BudgetController budgetController = new BudgetController();

    public MainGUI (){


        JFrame frame = new JFrame("Personal Finance Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel header = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 10, 2, 10);
        gbc.fill =  GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
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
        gbc.insets = new Insets(0,10,2,10);
        header.add(dashboard, gbc);

        double saldo = dashboardController.getSaldoBulanIni();
        double pemasukan = dashboardController.getTotalPemasukanBulanIni();
        double pengeluaran = dashboardController.getTotalPengeluaranBulanIni();

        JPanel panelKartu = new JPanel();
        panelKartu.setLayout(new BoxLayout(panelKartu, BoxLayout.X_AXIS));
        panelKartu.setBorder(new EmptyBorder(0,30,10,30));

        panelKartu.add(createKartuSaldo("Sisa Saldo",saldo));
        panelKartu.add(Box.createRigidArea(new Dimension(20,0)));
        panelKartu.add(createKartuSaldo("Total Pemasukkan Bulanan",pemasukan));
        panelKartu.add(Box.createRigidArea(new Dimension(20,0)));
        panelKartu.add(createKartuSaldo("Total Pengeluaran Bulanan",pengeluaran));
        panelKartu.add(Box.createRigidArea(new Dimension(20,0)));
        

        frame.add(header, BorderLayout.NORTH);
        frame.add(panelKartu, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel createKartuSaldo(String text,double parameter) {
        RoundedPanel kartuSaldo = new RoundedPanel(25, unguMuda, unguTua);
        kartuSaldo.setLayout(new BorderLayout());
        kartuSaldo.setMaximumSize(new Dimension(340,100));
        kartuSaldo.setBorder(new EmptyBorder(15,15,15,15));
        kartuSaldo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sisaSaldo = new JLabel(text);
        sisaSaldo.setForeground(biruTua);
        sisaSaldo.setFont(new Font("BEBAS NEUE", Font.PLAIN, 16));
        sisaSaldo.setBounds(20,20,100,30);
        kartuSaldo.add(sisaSaldo, BorderLayout.NORTH);

        JLabel saldoAmount = new JLabel();
        saldoAmount.setForeground(biruTua);
        saldoAmount.setFont(new Font("BEBAS NEUE", Font.PLAIN, 24));
        DecimalFormat df = new DecimalFormat("#,###.00");
        saldoAmount.setText(df.format(parameter));


        kartuSaldo.add(saldoAmount, BorderLayout.CENTER);

        return kartuSaldo;
    }


}
