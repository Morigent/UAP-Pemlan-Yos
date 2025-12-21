package Controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LandingPage {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalUang;
    private JLabel totalPengeluaran;

    public LandingPage (JLabel totalUang, JLabel totalPengeluaran) {
        this.totalUang = totalUang;
        this.totalPengeluaran = totalPengeluaran;

        frame = new JFrame("Moneeeyyyyyy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Welcome to Moneeeyyyyyy");
        title.setFont(new Font("BEBAS NEUE", Font.BOLD, 30));
        title.setBackground(Color.WHITE);

        header.add(title);
        frame.add(header, BorderLayout.NORTH);

        frame.setVisible(true);

    }

}
