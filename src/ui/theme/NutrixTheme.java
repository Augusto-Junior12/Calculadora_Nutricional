package ui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Nutrix v3 — Ultra Minimalist & Modular Design.
 * Focado em clareza, tipografia e fluxo direto.
 */
public final class NutrixTheme {

    private NutrixTheme() {}

    // ==================== PALETA MINIMALISTA ====================
    public static final Color ACCENT = new Color(37, 99, 235);        // Blue 600 (Direto e Profissional)
    public static final Color BG_PAGE = Color.WHITE;                 // Fundo Limpo
    public static final Color BG_SURFACE = new Color(249, 250, 251); // Gray 50
    public static final Color TEXT_PRIMARY = new Color(17, 24, 39);  // Gray 900
    public static final Color TEXT_SECONDARY = new Color(107, 114, 128); // Gray 500
    public static final Color BORDER = new Color(229, 231, 235);     // Gray 200
    
    public static final Color SUCCESS = new Color(22, 163, 74);
    public static final Color DANGER = new Color(220, 38, 38);

    // ==================== FONTES ====================
    public static final Font H1 = new Font("Inter", Font.BOLD, 32);   // Se não tiver Inter, cai no Sans Serif
    public static final Font H2 = new Font("Inter", Font.BOLD, 20);
    public static final Font H3 = new Font("Inter", Font.BOLD, 14);
    public static final Font BODY = new Font("Inter", Font.PLAIN, 14);
    public static final Font SMALL = new Font("Inter", Font.PLAIN, 12);

    static {
        // Fallback para fontes padrão caso Inter não esteja instalada
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        boolean hasInter = false;
        for (String f : fonts) if (f.equals("Inter")) hasInter = true;
        if (!hasInter) {
            // Fallback para Segoe UI ou SansSerif
        }
    }

    // ==================== COMPONENTES MODULARES ====================

    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                NutrixTheme.aplicarAntiAliasing(g2);
                g2.setColor(getModel().isPressed() ? ACCENT.darker() : 
                           (getModel().isRollover() ? ACCENT.brighter() : ACCENT));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintChildren(g);
            }
        };
        btn.setFont(new Font("Inter", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 48));
        return btn;
    }

    public static JPanel createModularCard() {
        JPanel p = new JPanel();
        p.setBackground(BG_SURFACE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(24, 24, 24, 24)
        ));
        p.setLayout(new BorderLayout(0, 16));
        return p;
    }

    public static JTextField createInput() {
        JTextField f = new JTextField();
        f.setFont(BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(12, 16, 12, 16)
        ));
        f.setBackground(Color.WHITE);
        return f;
    }

    public static void aplicarAntiAliasing(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}
