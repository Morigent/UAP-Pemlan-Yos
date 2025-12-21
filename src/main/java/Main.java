package Controller;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JLabel totalUang;
        JLabel totalPengeluaran;

        //nanti ambil dari file handiling angkanya
        totalUang = new JLabel("Total Uang: 0");
        totalUang.setFont(new Font("BEBAS NEUE", Font.BOLD, 30));

        totalPengeluaran = new JLabel("Total Pengeluaran: 0");
        totalPengeluaran.setFont(new Font("BEBAS NEUE", Font.BOLD, 30));


        LandingPage landingPage = new LandingPage(totalUang, totalPengeluaran);

        int nilai = 85;

        if (nilai >= 80) {
            System.out.println("Nilai A");
        } else if (nilai >= 70) {
            System.out.println("Nilai B");
        } else {
            System.out.println("Nilai C");
        }
    }
}
