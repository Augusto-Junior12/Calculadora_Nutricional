package ui.theme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Nutrix Hospital OS — Senior Design System.
 * Glassmorphism, Smooth Shadows, Custom Layouts.
 */
public final class NutrixTheme {

    private NutrixTheme() {}

    // ==================== PALETA DE CORES PREMIUM ====================
    public static final Color ACCENT = new Color(56, 189, 248);       // Sky Blue 400
    public static final Color ACCENT_DARK = new Color(14, 165, 233);   // Sky Blue 600
    public static final Color PRIMARY = new Color(15, 23, 42);        // Slate 900
    public static final Color BG_MAIN = new Color(241, 245, 249);     // Slate 100
    public static final Color BG_SIDEBAR = new Color(2, 6, 23);       // Darker Navy
    public static final Color BG_INPUT = new Color(241, 245, 249);    // Slate 100
    public static final Color GLASS_BG = new Color(255, 255, 255, 180);
    
    public static final Color SUCCESS = new Color(16, 185, 129);      // Emerald 500
    public static final Color WARNING = new Color(245, 158, 11);      // Amber 500
    public static final Color DANGER = new Color(244, 63, 94);        // Rose 500
    
    public static final Color TEXT_H1 = new Color(15, 23, 42);
    public static final Color TEXT_BODY = new Color(71, 85, 105);     // Slate 600
    public static final Color TEXT_MUTED = new Color(148, 163, 184);  // Slate 400
    public static final Color TEXT_ON_DARK = new Color(248, 250, 252);

    public static final Color BORDER = new Color(226, 232, 240);      // Slate 200

    // ==================== FONTES ====================
    public static final Font FONT_H1 = new Font("Segoe UI Semibold", Font.PLAIN, 28);
    public static final Font FONT_H2 = new Font("Segoe UI Semibold", Font.PLAIN, 20);
    public static final Font FONT_H3 = new Font("Segoe UI Semibold", Font.PLAIN, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI Semibold", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    // ==================== UTILITIES ====================
    public static void aplicarAntiAliasing(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }

    // ==================== COMPONENTES SENIOR ====================

    public static JButton createButton(String text, boolean primary) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                aplicarAntiAliasing(g2);
                
                // Shadow / Glow effect
                if (primary && getModel().isRollover()) {
                    g2.setColor(new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), 60));
                    g2.fillRoundRect(2, 4, getWidth()-4, getHeight()-4, 12, 12);
                }

                if (getModel().isPressed()) {
                    g2.setColor(primary ? ACCENT_DARK : BORDER.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(primary ? ACCENT : BORDER);
                } else {
                    g2.setColor(primary ? ACCENT : Color.WHITE);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                if (!primary) {
                    g2.setColor(BORDER);
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                }
                g2.dispose();
                super.paintChildren(g);
            }
        };
        btn.setFont(FONT_BODY_BOLD);
        btn.setForeground(primary ? Color.WHITE : TEXT_H1);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 45));
        return btn;
    }

    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                aplicarAntiAliasing(g2);
                
                // Subtle gradient background
                GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(252, 253, 255));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Soft Border
                g2.setColor(new Color(0, 0, 0, 10));
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(30, 30, 30, 30));
        return card;
    }

    public static JTextField createTextField() {
        JTextField f = new JTextField();
        f.setFont(FONT_BODY);
        f.setBackground(Color.WHITE);
        f.setForeground(TEXT_H1);
        f.setCaretColor(ACCENT);
        f.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        return f;
    }

    public static JLabel createLabel(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }
}
