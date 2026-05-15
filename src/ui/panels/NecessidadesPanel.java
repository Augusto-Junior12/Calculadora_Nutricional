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
        setLayout(new BorderLayout(48, 0));
        setBackground(Color.WHITE);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(400, 0));

        JLabel title = new JLabel("Metas Nutricionais");
        title.setFont(NutrixTheme.H2);
        title.setBorder(new EmptyBorder(0, 0, 32, 0));
        left.add(title);

        fPeso = addInput(left, "Peso de Cálculo (kg)");
        fFase = addCombo(left, "Fase Clínica", new String[]{"Aguda", "Reabilitação"});

        left.add(Box.createVerticalStrut(24));
        JButton btn = NutrixTheme.createPrimaryButton("CALCULAR METAS");
        btn.addActionListener(e -> calcular());
        left.add(btn);

        add(left, BorderLayout.WEST);

        resArea = new JTextArea();
        resArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        resArea.setEditable(false);
        resArea.setBackground(NutrixTheme.BG_SURFACE);
        resArea.setBorder(new EmptyBorder(32, 32, 32, 32));
        add(new JScrollPane(resArea), BorderLayout.CENTER);
    }

    private void calcular() {
        try {
            double p = Double.parseDouble(fPeso.getText().replace(",", "."));
            FaseClinica f = fFase.getSelectedIndex() == 0 ? FaseClinica.AGUDA : FaseClinica.REABILITACAO;
            ResultadoNecessidades r = service.calcularPadrao(p, f);
            resArea.setText("METAS CALCULADAS\n==============================\n\n" +
                "Kcal: " + Formatador.kcal(r.kcalMin) + " - " + Formatador.kcal(r.kcalMax) + "\n" +
                "PTN:  " + Formatador.gramas(r.ptnMin) + " - " + Formatador.gramas(r.ptnMax));
        } catch (Exception ex) { resArea.setText("Erro."); }
    }

    private JTextField addInput(JPanel p, String label) {
        JLabel l = new JLabel(label); l.setFont(NutrixTheme.H3); l.setBorder(new EmptyBorder(0, 0, 8, 0)); p.add(l);
        JTextField f = NutrixTheme.createInput(); p.add(f); p.add(Box.createVerticalStrut(16)); return f;
    }
    private JComboBox<String> addCombo(JPanel p, String label, String[] items) {
        JLabel l = new JLabel(label); l.setFont(NutrixTheme.H3); l.setBorder(new EmptyBorder(0, 0, 8, 0)); p.add(l);
        JComboBox<String> c = new JComboBox<>(items); c.setFont(NutrixTheme.BODY); p.add(c); p.add(Box.createVerticalStrut(16)); return c;
    }
}
