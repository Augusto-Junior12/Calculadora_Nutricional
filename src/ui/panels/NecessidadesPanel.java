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
    private JTextField fPeso;
    private JComboBox<String> fFase;
    private JTextArea resArea;

    public NecessidadesPanel() {
        setLayout(new BorderLayout(40, 0));
        setBackground(Color.WHITE);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(380, 0));

        JLabel title = new JLabel("Metas Nutricionais");
        title.setFont(NutrixTheme.FONT_H2);
        title.setBorder(new EmptyBorder(0, 0, 30, 0));
        left.add(title);

        fPeso = addField(left, "Peso de Cálculo (kg)");
        fFase = addCombo(left, "Fase Clínica", new String[]{"Aguda", "Reabilitação"});

        left.add(Box.createVerticalStrut(25));
        JButton btn = NutrixTheme.createButton("CALCULAR METAS", true);
        btn.addActionListener(e -> calcular());
        left.add(btn);

        add(left, BorderLayout.WEST);

        JPanel right = NutrixTheme.createRoundedPanel(25, NutrixTheme.ACCENT_SOFT);
        right.setLayout(new BorderLayout());
        right.setBorder(new EmptyBorder(35, 35, 35, 35));

        resArea = new JTextArea();
        resArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        resArea.setEditable(false);
        resArea.setOpaque(false);
        
        JLabel resTitle = new JLabel("Alvo Nutricional");
        resTitle.setFont(NutrixTheme.FONT_H3);
        resTitle.setForeground(NutrixTheme.ACCENT);
        resTitle.setBorder(new EmptyBorder(0, 0, 20, 0));

        right.add(resTitle, BorderLayout.NORTH);
        right.add(new JScrollPane(resArea), BorderLayout.CENTER);

        add(right, BorderLayout.CENTER);
    }

    private void calcular() {
        try {
            double p = Double.parseDouble(fPeso.getText().replace(",", "."));
            FaseClinica f = fFase.getSelectedIndex() == 0 ? FaseClinica.AGUDA : FaseClinica.REABILITACAO;
            ResultadoNecessidades r = service.calcularPadrao(p, f);
            resArea.setText("🎯 METAS CALCULADAS\n\n" +
                "Kcal: " + Formatador.kcal(r.kcalMin) + " - " + Formatador.kcal(r.kcalMax) + "\n" +
                "PTN:  " + Formatador.gramas(r.ptnMin) + " - " + Formatador.gramas(r.ptnMax));
        } catch (Exception ex) { resArea.setText("Erro."); }
    }

    private JTextField addField(JPanel p, String label) {
        JLabel l = new JLabel(label); l.setFont(NutrixTheme.FONT_SMALL); l.setForeground(NutrixTheme.TEXT_MUTED);
        l.setBorder(new EmptyBorder(0, 5, 5, 0)); p.add(l);
        JTextField f = NutrixTheme.createTextField(); f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42)); p.add(f);
        p.add(Box.createVerticalStrut(15)); return f;
    }

    private JComboBox<String> addCombo(JPanel p, String label, String[] items) {
        JLabel l = new JLabel(label); l.setFont(NutrixTheme.FONT_SMALL); l.setForeground(NutrixTheme.TEXT_MUTED);
        l.setBorder(new EmptyBorder(0, 5, 5, 0)); p.add(l);
        JComboBox<String> c = new JComboBox<>(items); c.setFont(NutrixTheme.FONT_BODY);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42)); p.add(c);
        p.add(Box.createVerticalStrut(15)); return c;
    }
}
