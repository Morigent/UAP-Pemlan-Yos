package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends JButton {

    private boolean isOutline;
    private Color colorNormal;
    private Color colorHover;
    private Color colorPressed;
    private Color textColor;

    private boolean isHovering = false;
    private boolean isPressing = false;

    private int radius = 20;

    public Button(String text, Color baseColor, boolean isOutline) {
        super(text);
        this.isOutline = isOutline;

        if (isOutline) {
            this.colorNormal = baseColor;
            this.textColor = baseColor;
            this.colorHover = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 30);
        } else {
            this.colorNormal = baseColor;
            this.textColor = Color.WHITE;
            this.colorHover = baseColor.brighter();
        }
        this.colorPressed = baseColor.darker();

        setForeground(textColor);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("SansSerif", Font.BOLD, 14));

        setMargin(new Insets(10, 20, 10, 20));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovering = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovering = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressing = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressing = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isOutline) {
            if (isHovering) {
                g2.setColor(colorHover);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            } else if (isPressing) {
                g2.setColor(colorHover);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            }

            g2.setColor(colorNormal);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, getHeight(), getHeight());

            setForeground(colorNormal);

        } else {
            if (isPressing) {
                g2.setColor(colorPressed);
            } else if (isHovering) {
                g2.setColor(colorHover);
            } else {
                g2.setColor(colorNormal);
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

            setForeground(Color.WHITE); // Teks putih
        }

        g2.dispose();

        super.paintComponent(g);
    }
}