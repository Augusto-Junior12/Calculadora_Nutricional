package ui.panels;

import repository.PacienteRepository;
import repository.FormulaEnteralRepository;
import ui.theme.HospitalTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Dashboard — visão geral do sistema com métricas e acesso rápido.
 */
public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(HospitalTheme.BACKGROUND);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel titulo = HospitalTheme.createTitle("Dashboard");
        JLabel sub = HospitalTheme.createLabel("Visão geral — Terapia Nutricional em UTI");
        JPanel hText = new JPanel(new GridLayout(2, 1));
        hText.setOpaque(false);
        hText.add(titulo);
        hText.add(sub);
        header.add(hText, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Cards de métricas
        JPanel metricas = new JPanel(new GridLayout(1, 4, 15, 0));
        metricas.setOpaque(false);
        metricas.add(criarCardMetrica("👥 Pacientes Ativos",
            String.valueOf(PacienteRepository.getInstance().getTotalAtivos()), HospitalTheme.PRIMARY));
        metricas.add(criarCardMetrica("💊 Fórmulas Cadastradas",
            String.valueOf(FormulaEnteralRepository.getInstance().getTotal()), HospitalTheme.INFO));
        metricas.add(criarCardMetrica("⚠ Alertas Pendentes", "0", HospitalTheme.WARNING));
        metricas.add(criarCardMetrica("📈 Taxa de Adequação", "—", HospitalTheme.SUCCESS));

        // Painel central
        JPanel centro = new JPanel(new BorderLayout(0, 15));
        centro.setOpaque(false);
        centro.add(metricas, BorderLayout.NORTH);

        // Informações do sistema
        JPanel infoCard = HospitalTheme.createCard();
        infoCard.setLayout(new BorderLayout(0, 10));
        infoCard.add(HospitalTheme.createSubtitle("Bem-vindo ao Facilita Nutri UTI"), BorderLayout.NORTH);

        JTextArea info = new JTextArea();
        info.setFont(HospitalTheme.FONT_BODY);
        info.setEditable(false);
        info.setOpaque(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setText("Sistema de apoio à decisão em terapia nutricional enteral para UTI.\n\n" +
            "Módulos disponíveis:\n" +
            "  • Cadastro de Pacientes — admissão com dados completos\n" +
            "  • Avaliação Antropométrica — Chumlea, Jung, Rabito, IMC, CB, CP\n" +
            "  • Necessidades Nutricionais — calorias e proteínas por fase clínica\n" +
            "  • Prescrição Contínua — cálculo de ml/h com progressão de 4 dias\n" +
            "  • Prescrição Intermitente — cálculo por horários fixos\n" +
            "  • Hidratação — necessidade hídrica e distribuição de flushes\n" +
            "  • Cálculos Clínicos — noradrenalina, balanço nitrogenado, propofol\n" +
            "  • Monitor Diário — ficha clínica semanal com alertas fisiológicos\n" +
            "  • Controle de Ingestão — transição TNE para via oral\n" +
            "  • Prescrito × Infundido — indicador de qualidade assistencial\n" +
            "  • Consulta de Fórmulas — tabela comparativa das 51 fórmulas\n" +
            "  • TNE Sistema Aberto — nutrição modular artesanal\n\n" +
            "⚕ Este sistema não substitui o julgamento clínico.\n" +
            "Toda prescrição final é responsabilidade do nutricionista habilitado.\n\n" +
            "Referências: Chumlea WC (1985/1988) | ASPEN/ESPEN Guidelines\n" +
            "Desenvolvido por Augusto Jr — Instituto Federal de Sergipe");
        infoCard.add(info, BorderLayout.CENTER);
        centro.add(infoCard, BorderLayout.CENTER);

        add(centro, BorderLayout.CENTER);
    }

    private JPanel criarCardMetrica(String titulo, String valor, Color cor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                HospitalTheme.aplicarAntiAliasing(g);
                g.setColor(HospitalTheme.SURFACE);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        card.setLayout(new BorderLayout(0, 5));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel tLbl = new JLabel(titulo);
        tLbl.setFont(HospitalTheme.FONT_SMALL);
        tLbl.setForeground(HospitalTheme.TEXT_SECONDARY);

        JLabel vLbl = new JLabel(valor);
        vLbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        vLbl.setForeground(cor);

        card.add(tLbl, BorderLayout.NORTH);
        card.add(vLbl, BorderLayout.CENTER);
        return card;
    }
}
