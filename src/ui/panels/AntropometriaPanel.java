package ui.panels;

import model.AlertaClinico;
import model.enums.ClassificacaoCB;
import model.enums.ClassificacaoIMC;
import model.enums.Etnia;
import model.enums.Genero;
import service.AntropometriaService;
import ui.theme.NutrixTheme;
import util.Formatador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Avaliação Antropométrica — Nutrix Hospital OS.
 */
public class AntropometriaPanel extends JPanel {

    private final AntropometriaService service = new AntropometriaService();
    private JTextField ajField, cbField, cpField, idadeField, pesoAtualField, alturaField;
    private JComboBox<String> generoBox, etniaBox;
    private JTextArea resultadoArea;

    public AntropometriaPanel() {
        setLayout(new BorderLayout(0, 25));
        setBackground(NutrixTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 45, 30, 45));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // --- Input Card ---
        JPanel inputCard = NutrixTheme.createCard();
        inputCard.setLayout(new BorderLayout(0, 20));
        JLabel t = new JLabel("PARÂMETROS ANTROPOMÉTRICOS");
        t.setFont(NutrixTheme.FONT_H3);
        t.setForeground(NutrixTheme.ACCENT);
        inputCard.add(t, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 4, 15, 15));
        grid.setOpaque(false);
        idadeField = addField(grid, "Idade:");
        generoBox = addCombo(grid, "Gênero:", new String[]{"Masculino", "Feminino"});
        etniaBox = addCombo(grid, "Etnia:", new String[]{"Branco", "Negro"});
        pesoAtualField = addField(grid, "Peso Atual (kg):");
        alturaField = addField(grid, "Altura (m):");
        ajField = addField(grid, "Alt. Joelho (cm):");
        cbField = addField(grid, "Circ. Braço (cm):");
        cpField = addField(grid, "Circ. Pant. (cm):");
        inputCard.add(grid, BorderLayout.CENTER);
        content.add(inputCard);
        content.add(Box.createVerticalStrut(25));

        // --- Buttons ---
        JButton calcBtn = NutrixTheme.createButton("CALCULAR ESTIMATIVAS", true);
        calcBtn.addActionListener(e -> calcular());
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnWrap.setOpaque(false);
        btnWrap.add(calcBtn);
        content.add(btnWrap);
        content.add(Box.createVerticalStrut(25));

        // --- Result Area ---
        resultadoArea = new JTextArea(12, 50);
        resultadoArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultadoArea.setEditable(false);
        resultadoArea.setBackground(NutrixTheme.BG_INPUT);
        resultadoArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        content.add(new JScrollPane(resultadoArea));

        add(content, BorderLayout.CENTER);
    }

    private void calcular() {
        try {
            int idade = Integer.parseInt(idadeField.getText());
            Genero g = generoBox.getSelectedIndex() == 0 ? Genero.MASCULINO : Genero.FEMININO;
            Etnia e = etniaBox.getSelectedIndex() == 0 ? Etnia.BRANCO : Etnia.NEGRO;
            double aj = parse(ajField), cb = parse(cbField), cp = parse(cpField);
            
            List<AlertaClinico> alertas = new ArrayList<>();
            double altEst = service.calcularAlturaEstimada(g, aj, idade, alertas);
            double pesoEst = service.calcularPesoEstimado(g, e, idade, aj, cb, alertas);
            double imc = service.calcularIMC(pesoEst, altEst);
            
            StringBuilder sb = new StringBuilder();
            sb.append("📊 RELATÓRIO ANTROPOMÉTRICO\n");
            sb.append("------------------------------------------\n");
            sb.append(String.format("Altura Estimada (Chumlea): %s\n", Formatador.metros(altEst)));
            sb.append(String.format("Peso Estimado (Chumlea):   %s\n", Formatador.kg(pesoEst)));
            sb.append(String.format("IMC Estimado:              %s (%s)\n", 
                Formatador.decimal2(imc), service.classificarIMC(imc, idade).getDescricao()));
            
            if (cp > 0) {
                sb.append(String.format("Depleção Muscular:        %s\n", 
                    service.verificarDepleçaoMuscular(cp, g) ? "RISCO DETECTADO" : "Normal"));
            }
            
            resultadoArea.setText(sb.toString());
            resultadoArea.setForeground(NutrixTheme.TEXT_BODY);
        } catch (Exception ex) {
            resultadoArea.setText("⚠ Erro nos dados: " + ex.getMessage());
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
