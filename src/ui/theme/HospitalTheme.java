package ui.theme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Design System hospitalar para Swing.
 * Paleta azul-petróleo — confiança, tranquilidade, profissionalismo.
 */
public final class HospitalTheme {

    private HospitalTheme() {}

    // ==================== CORES ====================
    public static final Color PRIMARY = new Color(13, 115, 119);       // #0D7377
    public static final Color PRIMARY_DARK = new Color(9, 79, 82);     // #094F52
    public static final Color PRIMARY_LIGHT = new Color(20, 163, 168); // #14A3A8
    public static final Color SECONDARY = new Color(50, 62, 79);       // #323E4F
    public static final Color BACKGROUND = new Color(244, 246, 249);   // #F4F6F9
    public static final Color SURFACE = Color.WHITE;
    public static final Color SURFACE_ALT = new Color(237, 242, 247);  // #EDF2F7
    public static final Color TEXT_PRIMARY = new Color(26, 32, 44);    // #1A202C
    public static final Color TEXT_SECONDARY = new Color(113, 128, 150);// #718096
    public static final Color SUCCESS = new Color(56, 161, 105);       // #38A169
    public static final Color WARNING = new Color(214, 158, 46);       // #D69E2E
    public static final Color DANGER = new Color(229, 62, 62);         // #E53E3E
    public static final Color INFO = new Color(49, 130, 206);          // #3182CE
    public static final Color BORDER = new Color(226, 232, 240);       // #E2E8F0
    public static final Color SIDEBAR_BG = new Color(30, 41, 59);      // Sidebar escura
    public static final Color SIDEBAR_TEXT = new Color(203, 213, 225);
    public static final Color SIDEBAR_HOVER = new Color(51, 65, 85);
    public static final Color SIDEBAR_ACTIVE = PRIMARY;

    // ==================== FONTES ====================
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);
    public static final Font FONT_SIDEBAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SIDEBAR_TITLE = new Font("Segoe UI", Font.BOLD, 15);

    // ==================== ESPAÇAMENTOS ====================
    public static final int PADDING_SM = 6;
    public static final int PADDING_MD = 12;
    public static final int PADDING_LG = 20;
    public static final int PADDING_XL = 30;
    public static final int BORDER_RADIUS = 8;
    public static final int SIDEBAR_WIDTH = 240;

    // ==================== BORDAS ====================
    public static Border cardBorder() {
        return new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(PADDING_LG, PADDING_LG, PADDING_LG, PADDING_LG)
        );
    }

    public static Border inputBorder() {
        return new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(PADDING_SM, PADDING_MD, PADDING_SM, PADDING_MD)
        );
    }

    public static Border sectionBorder() {
        return new EmptyBorder(PADDING_MD, 0, PADDING_MD, 0);
    }

    // ==================== COMPONENTES ESTILIZADOS ====================

    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setBackground(PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 40, 38));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(PRIMARY_DARK); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(PRIMARY); }
        });
        return btn;
    }

    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setForeground(PRIMARY);
        btn.setBackground(SURFACE);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(new LineBorder(PRIMARY, 1, true), new EmptyBorder(6, 16, 6, 16)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton createDangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setBackground(DANGER);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(FONT_INPUT);
        field.setBorder(inputBorder());
        field.setPreferredSize(new Dimension(200, 34));
        return field;
    }

    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(TEXT_SECONDARY);
        return label;
    }

    public static JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JLabel createSubtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SUBTITLE);
        label.setForeground(SECONDARY);
        return label;
    }

    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(SURFACE);
        card.setBorder(cardBorder());
        return card;
    }

    public static JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(FONT_INPUT);
        cb.setBackground(SURFACE);
        cb.setPreferredSize(new Dimension(200, 34));
        return cb;
    }

    /** Aplica anti-aliasing global para texto suavizado. */
    public static void aplicarAntiAliasing(Graphics g) {
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        }
    }

    /** Configura o Look and Feel do sistema para o tema hospitalar. */
    public static void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Panel.background", BACKGROUND);
            UIManager.put("OptionPane.background", SURFACE);
            UIManager.put("OptionPane.messageFont", FONT_BODY);
            UIManager.put("Button.font", FONT_BUTTON);
        } catch (Exception e) {
            // Fallback para look padrão
        }
    }
}
