package ui.panels;

import model.AlertaClinico;
import service.CalculosClinicosService;
import ui.theme.HospitalTheme;
import util.Formatador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Painel de cálculos clínicos auxiliares.
 * Noradrenalina, Balanço Nitrogenado, Propofol.
 */
public class CalculosClinicosPanel extends JPanel {

    private final CalculosClinicosService service = new CalculosClinicosService();
    private JTextArea resultadoArea;

    // Noradrenalina
    private JTextField noraVolField, noraPesoField;
    // BN
    private JTextField bnPtnField, bnUreiaField;
    // Propofol
    private JTextField propVolField, propPesoField;

    public CalculosClinicosPanel() {
        setLayout(new BorderLayout(0, 10));
        setBackground(HospitalTheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel h = new JPanel(new GridLayout(2, 1)); h.setOpaque(false);
        h.add(HospitalTheme.createTitle("Cálculos Clínicos Auxiliares"));
        h.add(HospitalTheme.createLabel("Noradrenalina, Balanço Nitrogenado, Propofol"));
        add(h, BorderLayout.NORTH);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);

        // --- Noradrenalina ---
        JPanel noraCard = criarSecao("Noradrenalina (mcg/kg/min)");
        JPanel noraGrid = new JPanel(new GridLayout(0, 4, 10, 8));
        noraGrid.setOpaque(false);
        noraVolField = addCampo(noraGrid, "Volume ml/h:");
        noraPesoField = addCampo(noraGrid, "Peso (kg):");
        noraCard.add(noraGrid, BorderLayout.CENTER);
        JButton noraBtn = HospitalTheme.createPrimaryButton("Calcular Nora");
        noraBtn.addActionListener(e -> calcularNora());
        JPanel nbp = new JPanel(new FlowLayout(FlowLayout.LEFT)); nbp.setOpaque(false);
        nbp.add(noraBtn);
        noraCard.add(nbp, BorderLayout.SOUTH);
        main.add(noraCard); main.add(Box.createVerticalStrut(10));

        // --- Balanço Nitrogenado ---
        JPanel bnCard = criarSecao("Balanço Nitrogenado");
        JPanel bnGrid = new JPanel(new GridLayout(0, 4, 10, 8));
        bnGrid.setOpaque(false);
        bnPtnField = addCampo(bnGrid, "PTN ingerida 24h (g):");
        bnUreiaField = addCampo(bnGrid, "Ureia urinária 24h:");
        bnCard.add(bnGrid, BorderLayout.CENTER);
        JButton bnBtn = HospitalTheme.createPrimaryButton("Calcular BN");
        bnBtn.addActionListener(e -> calcularBN());
        JPanel bbp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bbp.setOpaque(false);
        bbp.add(bnBtn);
        bnCard.add(bbp, BorderLayout.SOUTH);
        main.add(bnCard); main.add(Box.createVerticalStrut(10));

        // --- Propofol ---
        JPanel propCard = criarSecao("Calorias do Propofol");
        JPanel propGrid = new JPanel(new GridLayout(0, 4, 10, 8));
        propGrid.setOpaque(false);
        propVolField = addCampo(propGrid, "Volume ml/h:");
        propPesoField = addCampo(propGrid, "Peso (kg):");
        propCard.add(propGrid, BorderLayout.CENTER);
        JButton propBtn = HospitalTheme.createPrimaryButton("Calcular Propofol");
        propBtn.addActionListener(e -> calcularPropofol());
        JPanel pbp = new JPanel(new FlowLayout(FlowLayout.LEFT)); pbp.setOpaque(false);
        pbp.add(propBtn);
        propCard.add(pbp, BorderLayout.SOUTH);
        main.add(propCard); main.add(Box.createVerticalStrut(10));

        // Resultado
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

    private void calcularNora() {
        try {
            double vol = parseD(noraVolField), peso = parseD(noraPesoField);
            double simples = service.calcularNoraSimples(vol, peso);
            double concentrada = service.calcularNoraConcentrada(vol, peso);
            List<AlertaClinico> alertas = service.alertasNora(Math.max(simples, concentrada));
            StringBuilder sb = new StringBuilder();
            sb.append("▸ NORADRENALINA\n");
            sb.append("  Simples (2 amp/250ml): ").append(Formatador.decimal2(simples)).append(" mcg/kg/min\n");
            sb.append("  Concentrada (4 amp/250ml): ").append(Formatador.decimal2(concentrada)).append(" mcg/kg/min\n");
            for (AlertaClinico a : alertas) sb.append("  ⚠ ").append(a.getMensagem()).append("\n");
            resultadoArea.setText(sb.toString());
        } catch (Exception e) { resultadoArea.setText("⚠ " + e.getMessage()); }
    }

    private void calcularBN() {
        try {
            double ptn = parseD(bnPtnField), ureia = parseD(bnUreiaField);
            double bn = service.calcularBalancoNitrogenado(ptn, ureia);
            StringBuilder sb = new StringBuilder();
            sb.append("▸ BALANÇO NITROGENADO\n");
            sb.append("  N ingerido: ").append(Formatador.decimal2(ptn / 6.25)).append(" g\n");
            sb.append("  N excretado: ").append(Formatador.decimal2(ureia * 0.467 + 4)).append(" g\n");
            sb.append("  BN: ").append(Formatador.decimal2(bn)).append("\n");
            sb.append("  Interpretação: ").append(service.interpretarBN(bn)).append("\n");
            resultadoArea.setText(sb.toString());
        } catch (Exception e) { resultadoArea.setText("⚠ " + e.getMessage()); }
    }

    private void calcularPropofol() {
        try {
            double vol = parseD(propVolField), peso = parseD(propPesoField);
            double kcal = service.calcularCaloriasPropofol(vol);
            double mgKgH = peso > 0 ? service.calcularDosePropofolMgKgH(vol, peso) : 0;
            List<AlertaClinico> alertas = peso > 0 ? service.alertasPropofol(mgKgH) : java.util.Collections.emptyList();
            StringBuilder sb = new StringBuilder();
            sb.append("▸ PROPOFOL\n");
            sb.append("  Calorias: ").append(Formatador.kcal(kcal)).append("/dia\n");
            sb.append("  ⚡ Descontar da meta calórica diária!\n");
            if (peso > 0) sb.append("  Dose: ").append(Formatador.decimal1(mgKgH)).append(" mg/kg/h\n");
            for (AlertaClinico a : alertas) sb.append("  ⚠ ").append(a.getMensagem()).append("\n");
            resultadoArea.setText(sb.toString());
        } catch (Exception e) { resultadoArea.setText("⚠ " + e.getMessage()); }
    }

    private JPanel criarSecao(String t) {
        JPanel c = HospitalTheme.createCard(); c.setLayout(new BorderLayout(0, 10));
        c.add(HospitalTheme.createSubtitle(t), BorderLayout.NORTH); return c;
    }
    private double parseD(JTextField f) {
        String t = f.getText().trim().replace(",", "."); return t.isEmpty() ? 0 : Double.parseDouble(t);
    }
    private JTextField addCampo(JPanel g, String l) {
        g.add(HospitalTheme.createLabel(l)); JTextField f = HospitalTheme.createTextField(); g.add(f); return f;
    }
}
