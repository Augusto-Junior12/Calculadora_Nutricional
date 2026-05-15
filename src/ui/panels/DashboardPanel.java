package ui.panels;

import repository.PacienteRepository;
import repository.FormulaEnteralRepository;
import ui.theme.NutrixTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Dashboard Nutrix Hospital OS.
 */
public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout(0, 30));
        setBackground(NutrixTheme.BG_MAIN);
        setBorder(new EmptyBorder(40, 45, 40, 45));

        // --- Top Bar ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        
        JLabel title = new JLabel("Painel de Controle");
        title.setFont(NutrixTheme.FONT_H1);
        title.setForeground(NutrixTheme.TEXT_H1);
        
        String dataHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM"));
        JLabel dateLabel = new JLabel(dataHoje.substring(0, 1).toUpperCase() + dataHoje.substring(1));
        dateLabel.setFont(NutrixTheme.FONT_BODY);
        dateLabel.setForeground(NutrixTheme.TEXT_MUTED);

        JPanel titleGrp = new JPanel(new GridLayout(2, 1, 0, 5));
        titleGrp.setOpaque(false);
        titleGrp.add(title);
        titleGrp.add(dateLabel);
        
        topBar.add(titleGrp, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        // --- Main Content ---
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // Stats Row
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 25, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        statsRow.add(createStatCard("PACIENTES", 
            String.valueOf(PacienteRepository.getInstance().getTotalAtivos()), "Ativos agora", NutrixTheme.INFO));
        statsRow.add(createStatCard("FÓRMULAS", 
            String.valueOf(FormulaEnteralRepository.getInstance().getTotal()), "Catálogo TNE", NutrixTheme.ACCENT));
        statsRow.add(createStatCard("ALERTAS", "0", "Críticos hoje", NutrixTheme.DANGER));
        statsRow.add(createStatCard("METAS", "—", "Adequação média", NutrixTheme.SUCCESS));

        content.add(statsRow);
        content.add(Box.createVerticalStrut(30));

        // Welcome Card
        JPanel welcomeCard = NutrixTheme.createCard();
        welcomeCard.setLayout(new BorderLayout(25, 0));
        
        JPanel welcomeText = new JPanel(new GridLayout(0, 1, 0, 10));
        welcomeText.setOpaque(false);
        
        JLabel wTitle = new JLabel("Bem-vindo ao Nutrix Hospital OS");
        wTitle.setFont(NutrixTheme.FONT_H2);
        wTitle.setForeground(NutrixTheme.TEXT_H1);
        
        JTextArea wDesc = new JTextArea("Este sistema foi projetado para gestão clínica avançada de Terapia Nutricional Enteral (TNE) em ambientes de alta complexidade. " +
            "Utilize os módulos laterais para realizar avaliações antropométricas, prescrições precisas e monitoramento clínico diário.");
        wDesc.setFont(NutrixTheme.FONT_BODY);
        wDesc.setForeground(NutrixTheme.TEXT_BODY);
        wDesc.setLineWrap(true);
        wDesc.setWrapStyleWord(true);
        wDesc.setOpaque(false);
        wDesc.setEditable(false);
        
        welcomeText.add(wTitle);
        welcomeText.add(wDesc);
        
        welcomeCard.add(welcomeText, BorderLayout.CENTER);
        
        // Quick Action Button
        JButton startBtn = NutrixTheme.createButton("Nova Admissão", true);
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnWrap.setOpaque(false);
        btnWrap.add(startBtn);
        welcomeCard.add(btnWrap, BorderLayout.SOUTH);

        content.add(welcomeCard);
        add(content, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, String sub, Color color) {
        JPanel card = NutrixTheme.createCard();
        card.setLayout(new BorderLayout(0, 5));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel t = new JLabel(title);
        t.setFont(NutrixTheme.FONT_H3);
        t.setForeground(NutrixTheme.TEXT_MUTED);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 36));
        v.setForeground(color);

        JLabel s = new JLabel(sub);
        s.setFont(NutrixTheme.FONT_SMALL);
        s.setForeground(NutrixTheme.TEXT_MUTED);

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        card.add(s, BorderLayout.SOUTH);
        
        return card;
    }
}
