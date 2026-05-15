package ui.panels;

import service.HidratacaoService;
import ui.theme.NutrixTheme;
import util.Formatador;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HidratacaoPanel extends JPanel {
    private final HidratacaoService service = new HidratacaoService();
    private JTextField fPeso, fVol;
    private JTextArea resArea;

    public HidratacaoPanel() {
        setLayout(new BorderLayout(48, 0));
        setBackground(Color.WHITE);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(400, 0));

        JLabel title = new JLabel("Cálculo de Hidratação");
        title.setFont(NutrixTheme.H2);
        title.setBorder(new EmptyBorder(0, 0, 32, 0));
        left.add(title);

        fPeso = addInput(left, "Peso (kg)");
        fVol = addInput(left, "Volume Dieta (ml)");

        left.add(Box.createVerticalStrut(24));
        JButton btn = NutrixTheme.createPrimaryButton("CALCULAR");
        btn.addActionListener(e -> {
            try {
                double p = Double.parseDouble(fPeso.getText().replace(",", "."));
                double v = Double.parseDouble(fVol.getText().replace(",", "."));
                double n = service.calcularNecessidadeIdeal(p);
                double a = service.calcularAguaViaTNE(v, 1.2);
                resArea.setText("PLANEJAMENTO HÍDRICO\n==============================\n\n" +
                    "Necessidade Ideal: " + Formatador.ml(n) + "\n" +
                    "Água via Dieta:    " + Formatador.ml(a) + "\n" +
                    "Água Extra Sonda:  " + Formatador.ml(Math.max(0, n - a)));
            } catch (Exception ex) { resArea.setText("Erro."); }
        });
        left.add(btn);

        add(left, BorderLayout.WEST);

        resArea = new JTextArea();
        resArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        resArea.setEditable(false);
        resArea.setBackground(NutrixTheme.BG_SURFACE);
        resArea.setBorder(new EmptyBorder(32, 32, 32, 32));
        add(new JScrollPane(resArea), BorderLayout.CENTER);
    }

    private JTextField addInput(JPanel p, String label) {
        JLabel l = new JLabel(label); l.setFont(NutrixTheme.H3); l.setBorder(new EmptyBorder(0, 0, 8, 0)); p.add(l);
        JTextField f = NutrixTheme.createInput(); p.add(f); p.add(Box.createVerticalStrut(16)); return f;
    }
}
