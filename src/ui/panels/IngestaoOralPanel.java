package ui.panels;

import ui.theme.HospitalTheme;
import util.Formatador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Painel de controle de ingestão oral.
 * Registro de % de consumo por refeição, 5 dias, média geral.
 * Média < 75% indica necessidade de suplementação ou manutenção da TNE.
 */
public class IngestaoOralPanel extends JPanel {

    private static final String[] REFEICOES = {
        "Café da manhã", "Lanche da manhã", "Almoço", "Lanche da tarde", "Jantar", "Ceia"
    };
    private static final String[] OPCOES_PERCENT = {"—", "0%", "25%", "50%", "75%", "100%"};
    private static final int NUM_DIAS = 5;

    private JComboBox<String>[][] combos;
    private JTextArea resultadoArea;

    @SuppressWarnings("unchecked")
    public IngestaoOralPanel() {
        setLayout(new BorderLayout(0, 10));
        setBackground(HospitalTheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel h = new JPanel(new GridLayout(2, 1)); h.setOpaque(false);
        h.add(HospitalTheme.createTitle("Controle de Ingestão Oral"));
        h.add(HospitalTheme.createLabel("Transição TNE → via oral — média ≥ 75% indica possibilidade de desmame"));
        add(h, BorderLayout.NORTH);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);

        // Tabela de ingestão
        JPanel card = HospitalTheme.createCard();
        card.setLayout(new BorderLayout(0, 10));
        card.add(HospitalTheme.createSubtitle("Registro de % Consumo (5 dias)"), BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new GridLayout(REFEICOES.length + 1, NUM_DIAS + 1, 5, 5));
        tablePanel.setOpaque(false);
        combos = new JComboBox[REFEICOES.length][NUM_DIAS];

        // Header
        tablePanel.add(HospitalTheme.createLabel("Refeição"));
        for (int d = 0; d < NUM_DIAS; d++) {
            JLabel dl = new JLabel("Dia " + (d + 1), JLabel.CENTER);
            dl.setFont(HospitalTheme.FONT_BODY_BOLD);
            dl.setForeground(HospitalTheme.PRIMARY);
            tablePanel.add(dl);
        }

        // Rows
        for (int r = 0; r < REFEICOES.length; r++) {
            JLabel rl = new JLabel(REFEICOES[r]);
            rl.setFont(HospitalTheme.FONT_BODY);
            tablePanel.add(rl);
            for (int d = 0; d < NUM_DIAS; d++) {
                combos[r][d] = new JComboBox<>(OPCOES_PERCENT);
                combos[r][d].setFont(HospitalTheme.FONT_SMALL);
                tablePanel.add(combos[r][d]);
            }
        }

        card.add(tablePanel, BorderLayout.CENTER);
        main.add(card);
        main.add(Box.createVerticalStrut(10));

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setOpaque(false);
        JButton btn = HospitalTheme.createPrimaryButton("Calcular Média");
        btn.addActionListener(e -> calcular());
        bp.add(btn);
        main.add(bp);
        main.add(Box.createVerticalStrut(10));

        resultadoArea = new JTextArea(10, 50);
        resultadoArea.setFont(HospitalTheme.FONT_MONO);
        resultadoArea.setEditable(false);
        resultadoArea.setBackground(HospitalTheme.SURFACE_ALT);
        resultadoArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        main.add(new JScrollPane(resultadoArea));

        JScrollPane ms = new JScrollPane(main); ms.setBorder(null);
        ms.getVerticalScrollBar().setUnitIncrement(16);
        add(ms, BorderLayout.CENTER);
    }

    private void calcular() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════\n");
        sb.append("  CONTROLE DE INGESTÃO ORAL\n");
        sb.append("═══════════════════════════════\n\n");

        double[] mediaDia = new double[NUM_DIAS];
        int[] diasPreenchidos = new int[NUM_DIAS];

        for (int d = 0; d < NUM_DIAS; d++) {
            double soma = 0;
            int count = 0;
            sb.append("DIA ").append(d + 1).append(":\n");
            for (int r = 0; r < REFEICOES.length; r++) {
                int sel = combos[r][d].getSelectedIndex();
                if (sel > 0) { // não é "—"
                    double val = (sel - 1) * 25.0;
                    soma += val;
                    count++;
                    sb.append("  ").append(String.format("%-18s", REFEICOES[r])).append(Formatador.percentual(val)).append("\n");
                }
            }
            if (count > 0) {
                mediaDia[d] = soma / count;
                diasPreenchidos[d] = 1;
                sb.append("  Média dia: ").append(Formatador.percentual(mediaDia[d])).append("\n\n");
            } else {
                sb.append("  (sem registro)\n\n");
            }
        }

        // Média geral (apenas dias preenchidos)
        double somaGeral = 0;
        int totalDias = 0;
        for (int d = 0; d < NUM_DIAS; d++) {
            if (diasPreenchidos[d] == 1) {
                somaGeral += mediaDia[d];
                totalDias++;
            }
        }

        if (totalDias > 0) {
            double mediaGeral = somaGeral / totalDias;
            sb.append("▸ MÉDIA GERAL (").append(totalDias).append(" dias): ");
            sb.append(Formatador.percentual(mediaGeral)).append("\n\n");
            if (mediaGeral >= 75) {
                sb.append("✅ Média ≥ 75% — possibilidade de desmame da TNE\n");
            } else {
                sb.append("⚠ Média < 75% — manter suplementação oral ou TNE\n");
            }
        } else {
            sb.append("⚠ Nenhum dia preenchido.\n");
        }

        resultadoArea.setText(sb.toString());
        resultadoArea.setCaretPosition(0);
    }
}
