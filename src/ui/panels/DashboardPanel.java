package ui.panels;

import ui.theme.NutrixIcons;
import ui.theme.NutrixTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Premium Dashboard — Nutrix v4.
 * Visualmente agradável, arredondado e direto.
 */
public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout(0, 40));
        setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JLabel title = new JLabel("Bem-vindo ao Nutrix");
        title.setFont(NutrixTheme.FONT_H1);
        title.setForeground(NutrixTheme.TEXT_H1);
        
        JLabel subtitle = new JLabel("Sistema Operacional de Nutrição Clínica Hospitalar");
        subtitle.setFont(NutrixTheme.FONT_BODY);
        subtitle.setForeground(NutrixTheme.TEXT_MUTED);
        
        JPanel titleGrp = new JPanel(new GridLayout(2, 1, 0, 5));
        titleGrp.setOpaque(false);
        titleGrp.add(title);
        titleGrp.add(subtitle);
        header.add(titleGrp, BorderLayout.WEST);
        
        add(header, BorderLayout.NORTH);

        // Grid of Stats/Quick Actions
        JPanel grid = new JPanel(new GridLayout(2, 2, 30, 30));
        grid.setOpaque(false);

        grid.add(createPremiumCard("Identificação", "Admissão e triagem inicial de pacientes.", NutrixIcons.IconType.PATIENT, NutrixTheme.ACCENT));
        grid.add(createPremiumCard("Cálculos", "Antropometria e estimativas corporais.", NutrixIcons.IconType.SCALE, NutrixTheme.SUCCESS));
        grid.add(createPremiumCard("Prescrição", "Planejamento nutricional e dieta enteral.", NutrixIcons.IconType.PILL, NutrixTheme.DANGER));
        grid.add(createPremiumCard("Monitoramento", "Controle de infusão e balanço hídrico.", NutrixIcons.IconType.WATER, Color.ORANGE));

        add(grid, BorderLayout.CENTER);
    }

    private JPanel createPremiumCard(String title, String desc, NutrixIcons.IconType type, Color accent) {
        JPanel card = NutrixTheme.createRoundedPanel(25, NutrixTheme.ACCENT_SOFT);
        card.setLayout(new BorderLayout(20, 15));
        card.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Icon Badge
        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                NutrixTheme.aplicarAntiAliasing(g2);
                g2.setColor(accent);
                g2.fillOval(0, 0, getWidth(), getHeight());
                NutrixIcons.drawIcon(g2, type, 24, Color.WHITE);
                g2.dispose();
            }
        };
        iconBadge.setPreferredSize(new Dimension(50, 50));
        
        JPanel top = new JPanel(new BorderLayout(15, 0));
        top.setOpaque(false);
        top.add(iconBadge, BorderLayout.WEST);
        
        JLabel t = new JLabel(title);
        t.setFont(NutrixTheme.FONT_H2);
        t.setForeground(NutrixTheme.TEXT_H1);
        top.add(t, BorderLayout.CENTER);

        JTextArea d = new JTextArea(desc);
        d.setFont(NutrixTheme.FONT_BODY);
        d.setForeground(NutrixTheme.TEXT_BODY);
        d.setLineWrap(true);
        d.setWrapStyleWord(true);
        d.setOpaque(false);
        d.setEditable(false);

        card.add(top, BorderLayout.NORTH);
        card.add(d, BorderLayout.CENTER);

        return card;
    }
}
