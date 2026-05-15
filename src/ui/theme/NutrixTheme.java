package ui.theme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Nutrix Hospital OS — Novo Design System.
 * Estética moderna, "Dark-ish" Sidebar e "Clean Modern" Content.
 * Focado em robustez, legibilidade e visual premium.
 */
public final class NutrixTheme {

    private NutrixTheme() {}

    // ==================== PALETA DE CORES (MODERNA) ====================
    // Primárias (Deep Teal / Modern Blue)
    public static final Color ACCENT = new Color(0, 184, 169);        // Teal vibrante para ações
    public static final Color PRIMARY = new Color(45, 55, 72);        // Slate Blue escuro para texto/branding
    public static final Color SECONDARY = new Color(74, 85, 104);     // Slate médio
    
    // Backgrounds
    public static final Color BG_MAIN = new Color(248, 250, 252);     // Quase branco (Sky Blue Tint)
    public static final Color BG_SIDEBAR = new Color(15, 23, 42);     // Navy escuro (Modern OS style)
    public static final Color BG_CARD = Color.WHITE;
    public static final Color BG_INPUT = new Color(241, 245, 249);
    
    // Status
    public static final Color SUCCESS = new Color(34, 197, 94);       // Emerald Green
    public static final Color WARNING = new Color(245, 158, 11);      // Amber
    public static final Color DANGER = new Color(239, 68, 68);        // Rose Red
    public static final Color INFO = new Color(59, 130, 246);         // Bright Blue
    
    // Textos
    public static final Color TEXT_H1 = new Color(15, 23, 42);
    public static final Color TEXT_BODY = new Color(51, 65, 85);
    public static final Color TEXT_MUTED = new Color(100, 116, 139);
    public static final Color TEXT_ON_DARK = new Color(241, 245, 249);

    // Bordas
    public static final Color BORDER = new Color(226, 232, 240);
    public static final Color BORDER_FOCUS = ACCENT;

    // ==================== FONTES ====================
    public static final Font FONT_H1 = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_H2 = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_H3 = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    // ==================== COMPONENTES PREMIUM ====================

    /** Botão com estilo "Modern OS" */
    public static JButton createButton(String text, boolean primary) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                aplicarAntiAliasing(g2);
                if (getModel().isPressed()) {
                    g2.setColor(primary ? ACCENT.darker() : BORDER);
                } else if (getModel().isRollover()) {
                    g2.setColor(primary ? ACCENT.brighter() : BG_INPUT);
                } else {
                    g2.setColor(primary ? ACCENT : Color.WHITE);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                if (!primary) {
                    g2.setColor(BORDER);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                }
                g2.dispose();
                super.paintChildren(g);
            }
        };
        btn.setFont(FONT_BODY_BOLD);
        btn.setForeground(primary ? Color.WHITE : TEXT_BODY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 40));
        return btn;
    }

    /** Card com sombra suave e bordas arredondadas */
    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                aplicarAntiAliasing(g2);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 15, 15);
                g2.setColor(new Color(0, 0, 0, 15)); // Sombra suave
                g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 15, 15);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(25, 25, 25, 25));
        return card;
    }

    /** Campo de texto moderno com background sutil */
    public static JTextField createTextField() {
        JTextField f = new JTextField();
        f.setFont(FONT_BODY);
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_BODY);
        f.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        f.setCaretColor(ACCENT);
        return f;
    }

    public static void aplicarAntiAliasing(Graphics g) {
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
    }
}
