package ui.panels;

import model.enums.FaseClinica;
import service.NecessidadesNutricionaisService;
import service.NecessidadesNutricionaisService.ResultadoNecessidades;
import ui.theme.HospitalTheme;
import util.Formatador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Painel de cálculo de necessidades nutricionais.
 */
public class NecessidadesPanel extends JPanel {

    private final NecessidadesNutricionaisService service = new NecessidadesNutricionaisService();
    private JTextField pesoField, pesoIdealField, imcField;
    private JTextField kcalKgField, ptnKgField;
    private JComboBox<String> faseBox, condicaoBox;
    private JTextArea resultadoArea;

    public NecessidadesPanel() {
        setLayout(new BorderLayout(0, 10));
        setBackground(HospitalTheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel h = new JPanel(new GridLayout(2, 1)); h.setOpaque(false);
        h.add(HospitalTheme.createTitle("Necessidades Nutricionais"));
        h.add(HospitalTheme.createLabel("Metas calóricas e proteicas individualizadas — R01 e R02"));
        add(h, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        JPanel card = HospitalTheme.createCard();
        card.setLayout(new BorderLayout(0, 10));
        card.add(HospitalTheme.createSubtitle("Parâmetros"), BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(0, 4, 10, 8));
        grid.setOpaque(false);
        pesoField = addCampo(grid, "Peso selecionado (kg):");
        pesoIdealField = addCampo(grid, "Peso Ideal (kg):");
        imcField = addCampo(grid, "IMC:");
        grid.add(HospitalTheme.createLabel("Fase Clínica (R02):"));
        faseBox = HospitalTheme.createComboBox(new String[]{"Aguda", "Reabilitação"});
        grid.add(faseBox);
        grid.add(HospitalTheme.createLabel("Condição:"));
        condicaoBox = HospitalTheme.createComboBox(new String[]{"Padrão", "HD Intermitente", "HD Contínua (CRRT)", "Obeso IMC 30-40", "Obeso IMC >40"});
        grid.add(condicaoBox);
        kcalKgField = addCampo(grid, "Kcal/kg personalizado:");
        ptnKgField = addCampo(grid, "PTN/kg personalizado:");
        card.add(grid, BorderLayout.CENTER);
        form.add(card); form.add(Box.createVerticalStrut(10));

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setOpaque(false);
        JButton btn = HospitalTheme.createPrimaryButton("Calcular Necessidades");
        JButton btnP = HospitalTheme.createSecondaryButton("Calcular Personalizado");
        btn.addActionListener(e -> calcularPadrao());
        btnP.addActionListener(e -> calcularPersonalizado());
        bp.add(btn); bp.add(btnP);
        form.add(bp); form.add(Box.createVerticalStrut(10));

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

    private void calcularPadrao() {
        try {
            double peso = parseD(pesoField);
            FaseClinica fase = faseBox.getSelectedIndex() == 0 ? FaseClinica.AGUDA : FaseClinica.REABILITACAO;
            int cond = condicaoBox.getSelectedIndex();
            ResultadoNecessidades r;

            switch (cond) {
                case 1: r = service.calcularHemodialise(peso, false); break;
                case 2: r = service.calcularHemodialise(peso, true); break;
                case 3: r = service.calcularObeso(peso, parseD(pesoIdealField), 35); break;
                case 4: r = service.calcularObeso(peso, parseD(pesoIdealField), 45); break;
                default: r = service.calcularPadrao(peso, fase);
            }

            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════\n");
            sb.append("  NECESSIDADES NUTRICIONAIS\n");
            sb.append("═══════════════════════════════\n\n");
            sb.append("Peso utilizado: ").append(Formatador.kg(peso)).append("\n");
            sb.append("Fase: ").append(fase.getDescricao()).append("\n\n");
            sb.append("▸ CALORIAS\n");
            sb.append("  Mínimo: ").append(Formatador.kcal(r.kcalMin)).append("\n");
            sb.append("  Máximo: ").append(Formatador.kcal(r.kcalMax)).append("\n\n");
            sb.append("▸ PROTEÍNAS\n");
            sb.append("  Mínimo: ").append(Formatador.gramas(r.ptnMin)).append("\n");
            sb.append("  Máximo: ").append(Formatador.gramas(r.ptnMax)).append("\n");

            resultadoArea.setText(sb.toString());
        } catch (Exception e) { resultadoArea.setText("⚠ " + e.getMessage()); }
    }

    private void calcularPersonalizado() {
        try {
            double peso = parseD(pesoField);
            double kcalKg = parseD(kcalKgField), ptnKg = parseD(ptnKgField);
            ResultadoNecessidades r = service.calcularPersonalizado(peso, kcalKg, ptnKg);
            StringBuilder sb = new StringBuilder();
            sb.append("▸ META PERSONALIZADA\n");
            sb.append("  Kcal Total: ").append(Formatador.kcal(r.kcalPersonalizado)).append("\n");
            sb.append("  PTN Total: ").append(Formatador.gramas(r.ptnPersonalizado)).append("\n");
            for (var a : r.alertas) sb.append("  ⚠ ").append(a.getMensagem()).append("\n");
            resultadoArea.setText(sb.toString());
        } catch (Exception e) { resultadoArea.setText("⚠ " + e.getMessage()); }
    }

    private double parseD(JTextField f) {
        String t = f.getText().trim().replace(",", "."); return t.isEmpty() ? 0 : Double.parseDouble(t);
    }
    private JTextField addCampo(JPanel g, String l) {
        g.add(HospitalTheme.createLabel(l)); JTextField f = HospitalTheme.createTextField(); g.add(f); return f;
    }
}
