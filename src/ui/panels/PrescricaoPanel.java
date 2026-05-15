package ui.panels;

import model.AlertaClinico;
import model.FormulaEnteral;
import model.Prescricao;
import repository.FormulaEnteralRepository;
import service.PrescricaoService;
import ui.theme.NutrixTheme;
import util.Formatador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Prescrição de Dieta — Nutrix Hospital OS.
 */
public class PrescricaoPanel extends JPanel {

    private final PrescricaoService service = new PrescricaoService();
    private final FormulaEnteralRepository formulaRepo = FormulaEnteralRepository.getInstance();
    private JTextField pesoField, vctField, ptnField;
    private JComboBox<String> formulaBox;
    private JTextArea resultadoArea;

    public PrescricaoPanel() {
        setLayout(new BorderLayout(0, 25));
        setBackground(NutrixTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 45, 30, 45));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // --- Config Card ---
        JPanel card = NutrixTheme.createCard();
        card.setLayout(new BorderLayout(0, 20));
        JLabel t = new JLabel("CONFIGURAÇÃO DA PRESCRIÇÃO TNE");
        t.setFont(NutrixTheme.FONT_H3);
        t.setForeground(NutrixTheme.ACCENT);
        card.add(t, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 15));
        grid.setOpaque(false);
        pesoField = addField(grid, "Peso de Referência (kg):");
        vctField = addField(grid, "Meta Calórica (kcal):");
        ptnField = addField(grid, "Meta Proteica (g):");
        
        List<FormulaEnteral> formulas = formulaRepo.listarTodas();
        String[] nomes = new String[formulas.size()];
        for (int i = 0; i < formulas.size(); i++) nomes[i] = formulas.get(i).getNome();
        formulaBox = addCombo(grid, "Fórmula Enteral:", nomes);
        
        card.add(grid, BorderLayout.CENTER);
        content.add(card);
        content.add(Box.createVerticalStrut(25));

        // --- Buttons ---
        JButton calcBtn = NutrixTheme.createButton("GERAR PRESCRIÇÃO", true);
        calcBtn.addActionListener(e -> calcular());
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnWrap.setOpaque(false);
        btnWrap.add(calcBtn);
        content.add(btnWrap);
        content.add(Box.createVerticalStrut(25));

        // --- Result Area ---
        resultadoArea = new JTextArea(15, 50);
        resultadoArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultadoArea.setEditable(false);
        resultadoArea.setBackground(NutrixTheme.BG_INPUT);
        resultadoArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        content.add(new JScrollPane(resultadoArea));

        add(content, BorderLayout.CENTER);
    }

    private void calcular() {
        try {
            double peso = parse(pesoField), vct = parse(vctField), ptn = parse(ptnField);
            FormulaEnteral formula = formulaRepo.listarTodas().get(formulaBox.getSelectedIndex());
            List<AlertaClinico> alertas = new ArrayList<>();
            
            Prescricao p = service.calcularContinuo(formula, peso, vct, ptn, 22, alertas);
            
            StringBuilder sb = new StringBuilder();
            sb.append("📋 PRESCRIÇÃO NUTRICIONAL DETALHADA\n");
            sb.append("------------------------------------------\n");
            sb.append("Volume:          ").append(Formatador.mlH(p.getVolumeMlH())).append("\n");
            sb.append("Volume Total:    ").append(Formatador.ml(p.getVolumeTotal())).append("\n");
            sb.append("Aporte Calórico: ").append(Formatador.kcal(p.getKcalTotais())).append("\n");
            sb.append("Aporte Proteico: ").append(Formatador.gramas(p.getPtnTotal())).append("\n");
            sb.append("------------------------------------------\n");
            sb.append("PROGRESSÃO SUGERIDA (R04):\n");
            sb.append("Dia 1 (25%):  ").append(Formatador.mlH(p.getVolumeProgressao()[0])).append("\n");
            sb.append("Dia 2 (50%):  ").append(Formatador.mlH(p.getVolumeProgressao()[1])).append("\n");
            sb.append("Dia 4 (100%): ").append(Formatador.mlH(p.getVolumeProgressao()[3])).append("\n");
            
            resultadoArea.setText(sb.toString());
        } catch (Exception ex) {
            resultadoArea.setText("⚠ Erro no cálculo: " + ex.getMessage());
        }
    }

    private JTextField addField(JPanel g, String l) {
        JPanel p = new JPanel(new BorderLayout(0, 5)); p.setOpaque(false);
        JLabel lbl = new JLabel(l); lbl.setFont(NutrixTheme.FONT_SMALL); lbl.setForeground(NutrixTheme.TEXT_MUTED);
        JTextField f = NutrixTheme.createTextField(); p.add(lbl, BorderLayout.NORTH); p.add(f, BorderLayout.CENTER);
        g.add(p); return f;
    }

    private JComboBox<String> addCombo(JPanel g, String l, String[] i) {
        JPanel p = new JPanel(new BorderLayout(0, 5)); p.setOpaque(false);
        JLabel lbl = new JLabel(l); lbl.setFont(NutrixTheme.FONT_SMALL); lbl.setForeground(NutrixTheme.TEXT_MUTED);
        JComboBox<String> c = new JComboBox<>(i); p.add(lbl, BorderLayout.NORTH); p.add(c, BorderLayout.CENTER);
        g.add(p); return c;
    }

    private double parse(JTextField f) {
        String t = f.getText().trim().replace(",", ".");
        return t.isEmpty() ? 0 : Double.parseDouble(t);
    }
}
