package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

class RoundedPanel extends JPanel {
    private int radius;
    private Color colorStart;
    private Color colorEnd;

    public RoundedPanel(int radius, Color start, Color end) {
        this.radius = radius;
        this.colorStart = start;
        this.colorEnd = end;
        setOpaque(false); // Transparan agar sudut lengkung tidak kotak putih
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Mengaktifkan Anti-Aliasing (Agar lengkungan halus, tidak pecah)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Membuat Gradasi Warna
        GradientPaint gp = new GradientPaint(0, 0, colorStart, getWidth(), getHeight(), colorEnd);
        g2.setPaint(gp);

        // Menggambar Persegi Panjang Melengkung (Rounded Rectangle)
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
    }
}
