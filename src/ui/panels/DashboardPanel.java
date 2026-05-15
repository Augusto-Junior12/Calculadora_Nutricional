package ui.panels;

import repository.PacienteRepository;
import repository.FormulaEnteralRepository;
import ui.theme.NutrixIcons;
import ui.theme.NutrixTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Dashboard Senior — Nutrix Hospital OS.
 */
public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout(0, 40));
        setBackground(NutrixTheme.BG_MAIN);
        setBorder(new EmptyBorder(50, 60, 50, 60));

        // --- Header Section ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JPanel titleGroup = new JPanel(new GridLayout(2, 1, 0, 8));
        titleGroup.setOpaque(false);
        
        JLabel welcome = new JLabel("Olá, Nutricionista");
        welcome.setFont(NutrixTheme.FONT_H1);
        welcome.setForeground(NutrixTheme.TEXT_H1);
        
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy"));
        JLabel dateLabel = new JLabel(dateStr.substring(0,1).toUpperCase() + dateStr.substring(1));
        dateLabel.setFont(NutrixTheme.FONT_BODY);
        dateLabel.setForeground(NutrixTheme.TEXT_MUTED);
        
        titleGroup.add(welcome);
        titleGroup.add(dateLabel);
        header.add(titleGroup, BorderLayout.WEST);

        // Stats Row
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 30, 0));
        statsRow.setOpaque(false);
        statsRow.setPreferredSize(new Dimension(0, 160));

        statsRow.add(createSeniorStat("PACIENTES", 
            String.valueOf(PacienteRepository.getInstance().getTotalAtivos()), 
            NutrixIcons.IconType.PATIENT, NutrixTheme.ACCENT));
        statsRow.add(createSeniorStat("CATÁLOGO", 
            String.valueOf(FormulaEnteralRepository.getInstance().getTotal()), 
            NutrixIcons.IconType.BOX, NutrixTheme.SUCCESS));
        statsRow.add(createSeniorStat("ALERTAS", "0", 
            NutrixIcons.IconType.MONITOR, NutrixTheme.DANGER));
        statsRow.add(createSeniorStat("METAS", "100%", 
            NutrixIcons.IconType.TARGET, NutrixTheme.WARNING));

        // Middle Section
        JPanel mainContent = new JPanel(new BorderLayout(30, 0));
        mainContent.setOpaque(false);
        
        // Welcome Card (Glass-ish)
        JPanel welcomeCard = NutrixTheme.createCard();
        welcomeCard.setLayout(new BorderLayout(20, 20));
        
        JLabel cardTitle = new JLabel("Visão Geral do Sistema");
        cardTitle.setFont(NutrixTheme.FONT_H2);
        cardTitle.setForeground(NutrixTheme.TEXT_H1);
        
        JTextArea desc = new JTextArea("O Nutrix Hospital OS v2.0 oferece precisão absoluta nos cálculos de TNE. " +
            "Realize triagens, acompanhe o balanço nitrogenado e gerencie prescrições com o mais alto rigor clínico.");
        desc.setFont(NutrixTheme.FONT_BODY);
        desc.setForeground(NutrixTheme.TEXT_BODY);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setOpaque(false);
        desc.setEditable(false);
        desc.setMargin(new Insets(10, 0, 10, 0));

        welcomeCard.add(cardTitle, BorderLayout.NORTH);
        welcomeCard.add(desc, BorderLayout.CENTER);
        
        JButton actionBtn = NutrixTheme.createButton("ADMITIR PACIENTE", true);
        JPanel bWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT)); bWrap.setOpaque(false); bWrap.add(actionBtn);
        welcomeCard.add(bWrap, BorderLayout.SOUTH);

        mainContent.add(welcomeCard, BorderLayout.CENTER);

        // Assembly
        JPanel assembly = new JPanel();
        assembly.setLayout(new BoxLayout(assembly, BoxLayout.Y_AXIS));
        assembly.setOpaque(false);
        assembly.add(statsRow);
        assembly.add(Box.createVerticalStrut(40));
        assembly.add(mainContent);

        add(header, BorderLayout.NORTH);
        add(assembly, BorderLayout.CENTER);
    }

    private JPanel createSeniorStat(String title, String value, NutrixIcons.IconType type, Color color) {
        JPanel card = NutrixTheme.createCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Top Row: Title + Icon
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(NutrixTheme.FONT_H3);
        t.setForeground(NutrixTheme.TEXT_MUTED);
        
        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                NutrixTheme.aplicarAntiAliasing(g2);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                g2.fillOval(0, 0, getWidth(), getHeight());
                NutrixIcons.drawIcon(g2, type, 18, color);
                g2.dispose();
            }
        };
        iconBadge.setPreferredSize(new Dimension(36, 36));
        
        top.add(t, BorderLayout.WEST);
        top.add(iconBadge, BorderLayout.EAST);

        // Center: Value
        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 38));
        v.setForeground(NutrixTheme.TEXT_H1);

        card.add(top, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        
        return card;
    }
}
