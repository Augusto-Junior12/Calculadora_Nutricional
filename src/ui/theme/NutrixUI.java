package ui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.*;

/**
 * NutrixUI — v5 "Iconic" Design System.
 *
 * Paleta:
 *   BG_DARK   #0D1117  — sidebar fundo
 *   BG_PAGE   #F0F2F5  — fundo geral
 *   BG_CARD   #FFFFFF  — cards
 *   ACCENT    #7C3AED  — violet 700
 *   ACCENT_HO #6D28D9  — hover
 *   SUCCESS   #059669  — verde esmeralda
 *   DANGER    #DC2626  — vermelho
 */
public final class NutrixUI {

    private NutrixUI() {}

    /* ──────────── COLORS ──────────── */
    public static final Color SIDEBAR_BG   = new Color(13, 17, 23);
    public static final Color SIDEBAR_ITEM = new Color(255, 255, 255, 18);
    public static final Color BG_PAGE      = new Color(240, 242, 245);
    public static final Color BG_CARD      = Color.WHITE;
    public static final Color ACCENT       = new Color(124, 58, 237);   // violet-700
    public static final Color ACCENT_HOVER = new Color(109, 40, 217);   // violet-800
    public static final Color ACCENT_LIGHT = new Color(237, 233, 254);  // violet-100
    public static final Color SUCCESS      = new Color(5, 150, 105);
    public static final Color SUCCESS_LIGHT= new Color(209, 250, 229);
    public static final Color DANGER       = new Color(220, 38, 38);
    public static final Color DANGER_LIGHT = new Color(254, 226, 226);
    public static final Color WARNING      = new Color(217, 119, 6);
    public static final Color WARNING_LIGHT= new Color(254, 243, 199);

    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    public static final Color TEXT_SECONDARY= new Color(71, 85, 105);
    public static final Color TEXT_MUTED   = new Color(148, 163, 184);
    public static final Color BORDER       = new Color(226, 232, 240);
    public static final Color INPUT_BORDER = new Color(203, 213, 225);

    /* ──────────── TYPOGRAPHY ──────────── */
    private static Font load(String family, int style, int size) {
        return new Font(family, style, size);
    }

    public static final Font DISPLAY   = load("Segoe UI", Font.BOLD, 30);
    public static final Font H1        = load("Segoe UI", Font.BOLD, 22);
    public static final Font H2        = load("Segoe UI Semibold", Font.PLAIN, 16);
    public static final Font LABEL     = load("Segoe UI Semibold", Font.PLAIN, 12);
    public static final Font BODY      = load("Segoe UI", Font.PLAIN, 14);
    public static final Font BODY_BOLD = load("Segoe UI Semibold", Font.PLAIN, 14);
    public static final Font MONO      = load("Consolas", Font.PLAIN, 13);
    public static final Font SMALL     = load("Segoe UI", Font.PLAIN, 11);
    public static final Font NAV_LABEL = load("Segoe UI Semibold", Font.PLAIN, 13);

    /* ──────────── UTILITIES ──────────── */
    public static void aa(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    }

    /* ──────────── COMPONENTS ──────────── */

    /** Campo de texto arredondado com foco animado */
    public static JTextField input(String placeholder) {
        return new RoundInput(placeholder);
    }

    /** ComboBox estilizado */
    public static JComboBox<String> combo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(BODY);
        c.setBackground(BG_CARD);
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(INPUT_BORDER, 1, true),
            new EmptyBorder(10, 12, 10, 12)
        ));
        return c;
    }

    /** Botão primário Violet */
    public static JButton btnPrimary(String text) {
        return new RoundButton(text, ACCENT, ACCENT_HOVER, Color.WHITE);
    }

    /** Botão outline */
    public static JButton btnOutline(String text) {
        return new RoundButton(text, BG_CARD, new Color(240, 240, 245), TEXT_PRIMARY) {
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                aa(g2);
                g2.setColor(INPUT_BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.dispose();
            }
        };
    }

    /** Card branco com sombra suave */
    public static JPanel card() {
        return card(24);
    }

    public static JPanel card(int padding) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); aa(g2);
                // shadow
                g2.setColor(new Color(0,0,0,15));
                g2.fillRoundRect(2, 3, getWidth()-4, getHeight()-2, 20, 20);
                // white bg
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth()-2, getHeight()-3, 20, 20);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(padding, padding, padding, padding));
        return p;
    }

    /** Badge colorido */
    public static JLabel badge(String text, Color bg, Color fg) {
        JLabel l = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); aa(g2);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(SMALL);
        l.setForeground(fg);
        l.setOpaque(false);
        l.setBorder(new EmptyBorder(4, 10, 4, 10));
        return l;
    }

    /** Título de seção */
    public static JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(H1);
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    /** Label de campo */
    public static JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(LABEL);
        l.setForeground(TEXT_SECONDARY);
        return l;
    }

    /** Result row */
    public static JPanel resultRow(String label, String value, Color accent) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0, new Color(0,0,0,8)),
            new EmptyBorder(14,0,14,0)
        ));
        JLabel l = new JLabel(label);
        l.setFont(BODY);
        l.setForeground(TEXT_SECONDARY);
        JLabel v = new JLabel(value);
        v.setFont(BODY_BOLD);
        v.setForeground(accent != null ? accent : TEXT_PRIMARY);
        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        return row;
    }

    /* ──────────── INNER COMPONENTS ──────────── */

    private static class RoundInput extends JTextField {
        RoundInput(String placeholder) {
            super();
            setFont(BODY);
            setOpaque(false);
            setBorder(new EmptyBorder(12, 16, 12, 16));
            setCaretColor(ACCENT);
            setPreferredSize(new Dimension(0, 44));
            putClientProperty("placeholder", placeholder);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); aa(g2);
            boolean focused = hasFocus();
            // Background
            g2.setColor(focused ? new Color(237,233,254,80) : BG_CARD);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            // Border
            if (focused) {
                g2.setColor(ACCENT);
                g2.setStroke(new BasicStroke(2f));
            } else {
                g2.setColor(INPUT_BORDER);
                g2.setStroke(new BasicStroke(1.2f));
            }
            g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundButton extends JButton {
        private final Color normalBg, hoverBg, textColor;
        RoundButton(String text, Color normalBg, Color hoverBg, Color textColor) {
            super(text);
            this.normalBg = normalBg; this.hoverBg = hoverBg; this.textColor = textColor;
            setFont(BODY_BOLD);
            setForeground(textColor);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(160, 44));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); aa(g2);
            g2.setColor(getModel().isPressed() ? hoverBg.darker() : getModel().isRollover() ? hoverBg : normalBg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.dispose();
            super.paintChildren(g);
        }
    }
}
