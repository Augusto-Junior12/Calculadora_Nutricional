package ui.panels;

import ui.theme.NutrixTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Minimalist Dashboard — Hub de Ferramentas Nutrix.
 */
public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout(0, 48));
        setBackground(Color.WHITE);

        // Header Section
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 8));
        header.setOpaque(false);
        
        JLabel title = new JLabel("Central de Terapia Nutricional");
        title.setFont(NutrixTheme.H1);
        title.setForeground(NutrixTheme.TEXT_PRIMARY);
        
        JLabel sub = new JLabel("Selecione um módulo para iniciar os cálculos clínicos.");
        sub.setFont(NutrixTheme.BODY);
        sub.setForeground(NutrixTheme.TEXT_SECONDARY);
        
        header.add(title);
        header.add(sub);
        add(header, BorderLayout.NORTH);

        // Modular Hub Grid
        JPanel grid = new JPanel(new GridLayout(2, 3, 24, 24));
        grid.setOpaque(false);

        grid.add(createModuleCard("Admissão", "Cadastro e triagem de novos pacientes."));
        grid.add(createModuleCard("Antropometria", "Cálculos de peso, altura e composição."));
        grid.add(createModuleCard("Prescrição", "Planejamento de dieta enteral (R04)."));
        grid.add(createModuleCard("Hidratação", "Balanço hídrico e distribuição de flushes."));
        grid.add(createModuleCard("Monitor UTI", "Noradrenalina, Propofol e BN."));
        grid.add(createModuleCard("Fórmulas", "Consulta ao catálogo de 51 fórmulas."));

        add(grid, BorderLayout.CENTER);
    }

    private JPanel createModuleCard(String title, String desc) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(NutrixTheme.BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NutrixTheme.BORDER, 1),
            new EmptyBorder(32, 32, 32, 32)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel t = new JLabel(title);
        t.setFont(NutrixTheme.H2);
        t.setForeground(NutrixTheme.TEXT_PRIMARY);

        JTextArea d = new JTextArea(desc);
        d.setFont(NutrixTheme.BODY);
        d.setForeground(NutrixTheme.TEXT_SECONDARY);
        d.setLineWrap(true);
        d.setWrapStyleWord(true);
        d.setOpaque(false);
        d.setEditable(false);
        d.setFocusable(false);

        card.add(t, BorderLayout.NORTH);
        card.add(d, BorderLayout.CENTER);
        
        // Hover Effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(NutrixTheme.ACCENT, 1),
                    new EmptyBorder(32, 32, 32, 32)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(NutrixTheme.BORDER, 1),
                    new EmptyBorder(32, 32, 32, 32)
                ));
            }
        });

        return card;
    }
}
