package ui.panels;

import service.NecessidadesNutricionaisService;
import service.NecessidadesNutricionaisService.ResultadoNecessidades;
import ui.theme.NutrixTheme;
import util.Formatador;
import model.enums.FaseClinica;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NecessidadesPanel extends JPanel {
    private final NecessidadesNutricionaisService service = new NecessidadesNutricionaisService();
    private JTextField pesoField, idealField;
    private JComboBox<String> faseBox, condBox;
    private JTextArea resultadoArea;

    public NecessidadesPanel() {
        setLayout(new BorderLayout(0, 25));
        setBackground(NutrixTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 45, 30, 45));

        JPanel card = NutrixTheme.createCard();
        card.setLayout(new BorderLayout(0, 20));
        JLabel t = new JLabel("METAS NUTRICIONAIS ALVO");
        t.setFont(NutrixTheme.FONT_H3);
        t.setForeground(NutrixTheme.ACCENT);
        card.add(t, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 2, 20, 15));
        grid.setOpaque(false);
        pesoField = addField(grid, "Peso de Cálculo (kg):");
        idealField = addField(grid, "Peso Ideal (kg):");
        faseBox = addCombo(grid, "Fase Clínica:", new String[]{"Aguda", "Reabilitação"});
        condBox = addCombo(grid, "Condição Especial:", new String[]{"Padrão", "HD", "Obeso"});
        card.add(grid, BorderLayout.CENTER);

        JButton btn = NutrixTheme.createButton("CALCULAR METAS", true);
        btn.addActionListener(e -> calcular());
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.LEFT)); btnWrap.setOpaque(false); btnWrap.add(btn);
        
        JPanel resWrap = new JPanel(new BorderLayout(0, 15));
        resWrap.setOpaque(false);
        resultadoArea = new JTextArea(8, 50);
        resultadoArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultadoArea.setBackground(NutrixTheme.BG_INPUT);
        resultadoArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        resWrap.add(new JScrollPane(resultadoArea), BorderLayout.CENTER);

        JPanel main = new JPanel(); main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS)); main.setOpaque(false);
        main.add(card); main.add(Box.createVerticalStrut(20)); main.add(btnWrap); main.add(Box.createVerticalStrut(20)); main.add(resWrap);
        add(main, BorderLayout.CENTER);
    }

    private void calcular() {
        try {
            double p = Double.parseDouble(pesoField.getText().replace(",", "."));
            FaseClinica f = faseBox.getSelectedIndex() == 0 ? FaseClinica.AGUDA : FaseClinica.REABILITACAO;
            ResultadoNecessidades r = service.calcularPadrao(p, f);
            resultadoArea.setText("🎯 METAS CALCULADAS\n--------------------------\n" +
                "Kcal: " + Formatador.kcal(r.kcalMin) + " - " + Formatador.kcal(r.kcalMax) + "\n" +
                "PTN:  " + Formatador.gramas(r.ptnMin) + " - " + Formatador.gramas(r.ptnMax));
        } catch (Exception ex) { resultadoArea.setText("⚠ Erro"); }
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
}
