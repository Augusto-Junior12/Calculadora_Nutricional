package ui.panels;

import service.HidratacaoService;
import ui.theme.HospitalTheme;
import util.Formatador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Painel de cálculo de hidratação.
 */
public class HidratacaoPanel extends JPanel {

    private final HidratacaoService service = new HidratacaoService();
    private JTextField pesoField, volumeDietaField;
    private JComboBox<String> densidadeBox, flushesBox;
    private JTextArea resultadoArea;

    public HidratacaoPanel() {
        setLayout(new BorderLayout(0, 10));
        setBackground(HospitalTheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel h = new JPanel(new GridLayout(2, 1)); h.setOpaque(false);
        h.add(HospitalTheme.createTitle("Hidratação"));
        h.add(HospitalTheme.createLabel("Necessidade hídrica, água via TNE e distribuição de flushes"));
        add(h, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        JPanel card = HospitalTheme.createCard();
        card.setLayout(new BorderLayout(0, 10));
        JPanel grid = new JPanel(new GridLayout(0, 4, 10, 8));
        grid.setOpaque(false);
        pesoField = addCampo(grid, "Peso (kg):");
        volumeDietaField = addCampo(grid, "Volume dieta/dia (ml):");
        grid.add(HospitalTheme.createLabel("Densidade fórmula:"));
        densidadeBox = HospitalTheme.createComboBox(new String[]{"1.0 kcal/ml", "1.2 kcal/ml", "1.5 kcal/ml", "2.0 kcal/ml"});
        grid.add(densidadeBox);
        grid.add(HospitalTheme.createLabel("Nº flushes/dia:"));
        flushesBox = HospitalTheme.createComboBox(new String[]{"4", "5", "6", "8"});
        grid.add(flushesBox);
        card.add(grid, BorderLayout.CENTER);
        form.add(card);
        form.add(Box.createVerticalStrut(10));

        JButton btn = HospitalTheme.createPrimaryButton("Calcular Hidratação");
        btn.addActionListener(e -> calcular());
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setOpaque(false);
        bp.add(btn); form.add(bp);
        form.add(Box.createVerticalStrut(10));

        resultadoArea = new JTextArea(15, 50);
        resultadoArea.setFont(HospitalTheme.FONT_MONO);
        resultadoArea.setEditable(false);
        resultadoArea.setBackground(HospitalTheme.SURFACE_ALT);
        resultadoArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        form.add(new JScrollPane(resultadoArea));

        JScrollPane ms = new JScrollPane(form); ms.setBorder(null);
        ms.getVerticalScrollBar().setUnitIncrement(16);
        add(ms, BorderLayout.CENTER);
    }

    private void calcular() {
        try {
            double peso = parseD(pesoField);
            double volDieta = parseD(volumeDietaField);
            double[] densidades = {1.0, 1.2, 1.5, 2.0};
            double dens = densidades[densidadeBox.getSelectedIndex()];
            int[] flushOpts = {4, 5, 6, 8};
            int flushes = flushOpts[flushesBox.getSelectedIndex()];

            double necMin = service.calcularNecessidadeMinima(peso);
            double necIdeal = service.calcularNecessidadeIdeal(peso);
            double aguaTNE = service.calcularAguaViaTNE(volDieta, dens);
            double extraMin = service.calcularAguaExtra(necMin, aguaTNE);
            double extraIdeal = service.calcularAguaExtra(necIdeal, aguaTNE);
            double flushIdeal = service.calcularVolumePorFlush(extraIdeal, flushes);

            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════\n");
            sb.append("  HIDRATAÇÃO\n");
            sb.append("═══════════════════════════════\n\n");
            sb.append("▸ Necessidade Hídrica (25-30 ml/kg/dia)\n");
            sb.append("  Mínimo: ").append(Formatador.ml(necMin)).append("\n");
            sb.append("  Ideal:  ").append(Formatador.ml(necIdeal)).append("\n\n");
            sb.append("▸ Água via TNE (dieta ").append(dens).append(" kcal/ml = ");
            sb.append(Formatador.inteiro(service.getPercentualAguaDieta(dens))).append("% água)\n");
            sb.append("  ").append(Formatador.ml(aguaTNE)).append("\n\n");
            sb.append("▸ Água Extra via Sonda\n");
            sb.append("  Mínimo: ").append(Formatador.ml(extraMin)).append("\n");
            sb.append("  Ideal:  ").append(Formatador.ml(extraIdeal)).append("\n\n");
            sb.append("▸ Volume por Flush (").append(flushes).append("x/dia — via ideal)\n");
            sb.append("  ").append(Formatador.ml(flushIdeal)).append("/vez\n\n");

            // Tabela completa
            sb.append("▸ TABELA COMPLETA\n");
            sb.append(String.format("  %-14s %-10s %-10s %-10s %-10s %-10s\n", "Densidade", "Mín", "Ideal", "4x", "6x", "8x"));
            for (double d : densidades) {
                double a = service.calcularAguaViaTNE(volDieta, d);
                double eMin = service.calcularAguaExtra(necMin, a);
                double eId = service.calcularAguaExtra(necIdeal, a);
                sb.append(String.format("  %-14s %-10s %-10s %-10s %-10s %-10s\n",
                    d + " kcal/ml", Formatador.inteiro(eMin), Formatador.inteiro(eId),
                    Formatador.inteiro(eId / 4), Formatador.inteiro(eId / 6), Formatador.inteiro(eId / 8)));
            }

            resultadoArea.setText(sb.toString());
            resultadoArea.setCaretPosition(0);
        } catch (Exception e) {
            resultadoArea.setText("⚠ Erro: " + e.getMessage());
        }
    }

    private double parseD(JTextField f) {
        String t = f.getText().trim().replace(",", "."); return t.isEmpty() ? 0 : Double.parseDouble(t);
    }
    private JTextField addCampo(JPanel g, String l) {
        g.add(HospitalTheme.createLabel(l)); JTextField f = HospitalTheme.createTextField(); g.add(f); return f;
    }
}
