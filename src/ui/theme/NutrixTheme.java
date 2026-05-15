package ui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Nutrix v4 — Premium Aesthetic Design System.
 * Focado em beleza visual, bordas arredondadas e suavidade.
 */
public final class NutrixTheme {

    private NutrixTheme() {}

    // ==================== PALETA DE CORES VIBRANTE & SUAVE ====================
    public static final Color ACCENT = new Color(99, 102, 241);       // Indigo 500
    public static final Color ACCENT_SOFT = new Color(238, 242, 255); // Indigo 50
    public static final Color BG_PAGE = new Color(243, 244, 246);     // Gray 100
    public static final Color BG_SIDEBAR = new Color(31, 41, 55);     // Gray 800
    public static final Color TEXT_H1 = new Color(17, 24, 39);        // Gray 900
    public static final Color TEXT_BODY = new Color(55, 65, 81);      // Gray 700
    public static final Color TEXT_MUTED = new Color(156, 163, 175);  // Gray 400
    
    public static final Color SUCCESS = new Color(34, 197, 94);
    public static final Color DANGER = new Color(239, 68, 68);
    public static final Color BORDER = new Color(229, 231, 235);

    // ==================== FONTES PREMIUM ====================
    public static final Font FONT_H1 = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_H2 = new Font("Segoe UI Semibold", Font.PLAIN, 18);
    public static final Font FONT_H3 = new Font("Segoe UI Semibold", Font.PLAIN, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI Semibold", Font.PLAIN, 12);
    
    // Alíases para compatibilidade
    public static final Font H2 = FONT_H2;
    public static final Font BODY = FONT_BODY;

    // ==================== COMPONENTES ARREDONDADOS ====================

    public static JButton createButton(String text, boolean primary) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                aplicarAntiAliasing(g2);
                if (primary) {
                    g2.setColor(getModel().isPressed() ? ACCENT.darker() : 
                               (getModel().isRollover() ? ACCENT.brighter() : ACCENT));
                } else {
                    g2.setColor(getModel().isRollover() ? BORDER : Color.WHITE);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                if (!primary) {
                    g2.setColor(BORDER);
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                }
                g2.dispose();
                super.paintChildren(g);
            }
        };
        btn.setFont(FONT_SMALL);
        btn.setForeground(primary ? Color.WHITE : TEXT_BODY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 45));
        return btn;
    }

    public static JTextField createTextField() {
        JTextField f = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                aplicarAntiAliasing(g2);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(hasFocus() ? ACCENT : BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        f.setFont(FONT_BODY);
        f.setOpaque(false);
        f.setBorder(new EmptyBorder(10, 15, 10, 15));
        f.setCaretColor(ACCENT);
        return f;
    }

    public static JPanel createRoundedPanel(int radius, Color bg) {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                aplicarAntiAliasing(g2);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    public static void aplicarAntiAliasing(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}
