package ui.panels;

import ui.theme.NutrixIcons;
import ui.theme.NutrixUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Dashboard v5 — Cards de acesso rápido estilo Linear/Vercel.
 */
public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout(0, 32));
        setOpaque(false);

        // ── Stats Row ──
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 20, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        statsRow.add(statCard("Pacientes", "—", NutrixIcons.Icon.PATIENT, NutrixUI.ACCENT, NutrixUI.ACCENT_LIGHT));
        statsRow.add(statCard("Fórmulas", "51", NutrixIcons.Icon.BOX, NutrixUI.SUCCESS, NutrixUI.SUCCESS_LIGHT));
        statsRow.add(statCard("Alertas", "0", NutrixIcons.Icon.ALERT, NutrixUI.WARNING, NutrixUI.WARNING_LIGHT));
        statsRow.add(statCard("Adequação", "—", NutrixIcons.Icon.TARGET, NutrixUI.DANGER, NutrixUI.DANGER_LIGHT));

        // ── Module Grid ──
        JPanel grid = new JPanel(new GridLayout(2, 3, 20, 20));
        grid.setOpaque(false);

        grid.add(moduleCard("Admissão", "Cadastro e triagem de novos pacientes.", NutrixIcons.Icon.PATIENT, NutrixUI.ACCENT, NutrixUI.ACCENT_LIGHT));
        grid.add(moduleCard("Antropometria", "Estimativas de peso e altura (Chumlea).", NutrixIcons.Icon.SCALE, NutrixUI.SUCCESS, NutrixUI.SUCCESS_LIGHT));
        grid.add(moduleCard("Metas Nutricionais", "Cálculo de VCT e PTN alvo.", NutrixIcons.Icon.TARGET, new Color(234,179,8), new Color(254,252,232)));
        grid.add(moduleCard("Prescrição TNE", "Geração de plano nutricional R04.", NutrixIcons.Icon.PILL, NutrixUI.DANGER, NutrixUI.DANGER_LIGHT));
        grid.add(moduleCard("Hidratação", "Balanço hídrico e flushes.", NutrixIcons.Icon.WATER, new Color(6,182,212), new Color(224,247,250)));
        grid.add(moduleCard("Monitor UTI", "Cálculos de Nora, BN e Propofol.", NutrixIcons.Icon.MONITOR, new Color(168,85,247), new Color(243,232,255)));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.add(statsRow);
        content.add(Box.createVerticalStrut(24));
        content.add(grid);

        add(content, BorderLayout.CENTER);
    }

    private JPanel statCard(String label, String value, NutrixIcons.Icon icon, Color accent, Color accentLight) {
        JPanel card = NutrixUI.card(20);
        card.setLayout(new BorderLayout(12, 0));

        JPanel badge = NutrixIcons.iconBadge(icon, 40, accentLight, accent);

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 22));
        v.setForeground(NutrixUI.TEXT_PRIMARY);
        JLabel l = new JLabel(label);
        l.setFont(NutrixUI.SMALL);
        l.setForeground(NutrixUI.TEXT_SECONDARY);
        info.add(v);
        info.add(l);

        card.add(badge, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        return card;
    }

    private JPanel moduleCard(String title, String desc, NutrixIcons.Icon icon, Color accent, Color accentLight) {
        JPanel card = NutrixUI.card(24);
        card.setLayout(new BorderLayout(0, 14));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Icon badge top
        JPanel badge = NutrixIcons.iconBadge(icon, 46, accentLight, accent);
        JPanel badgeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        badgeRow.setOpaque(false);
        badgeRow.add(badge);

        JLabel t = new JLabel(title);
        t.setFont(NutrixUI.BODY_BOLD);
        t.setForeground(NutrixUI.TEXT_PRIMARY);

        JTextArea d = new JTextArea(desc);
        d.setFont(NutrixUI.SMALL);
        d.setForeground(NutrixUI.TEXT_SECONDARY);
        d.setLineWrap(true); d.setWrapStyleWord(true);
        d.setOpaque(false); d.setEditable(false); d.setFocusable(false);

        card.add(badgeRow, BorderLayout.NORTH);
        card.add(t, BorderLayout.CENTER);
        card.add(d, BorderLayout.SOUTH);
        return card;
    }
}
