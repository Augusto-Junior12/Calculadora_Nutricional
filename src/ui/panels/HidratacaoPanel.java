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
        setLayout(new BorderLayout(40, 0));
        setBackground(Color.WHITE);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(380, 0));

        JLabel title = new JLabel("Hidratação Extra");
        title.setFont(NutrixTheme.FONT_H2);
        title.setBorder(new EmptyBorder(0, 0, 30, 0));
        left.add(title);

        fPeso = addField(left, "Peso do Paciente (kg)");
        fVol = addField(left, "Volume de Dieta (ml)");

        left.add(Box.createVerticalStrut(25));
        JButton btn = NutrixTheme.createButton("CALCULAR FLUSHES", true);
        btn.addActionListener(e -> {
            try {
                double p = Double.parseDouble(fPeso.getText().replace(",", "."));
                double v = Double.parseDouble(fVol.getText().replace(",", "."));
                double n = service.calcularNecessidadeIdeal(p);
                double a = service.calcularAguaViaTNE(v, 1.2);
                resArea.setText("💧 PLANEJAMENTO HÍDRICO\n\n" +
                    "Necessidade Total: " + Formatador.ml(n) + "\n" +
                    "Água via Dieta:    " + Formatador.ml(a) + "\n" +
                    "Água Extra Sonda:  " + Formatador.ml(Math.max(0, n - a)));
            } catch (Exception ex) { resArea.setText("Erro."); }
        });
        left.add(btn);

        add(left, BorderLayout.WEST);

        JPanel right = NutrixTheme.createRoundedPanel(25, NutrixTheme.ACCENT_SOFT);
        right.setLayout(new BorderLayout());
        right.setBorder(new EmptyBorder(35, 35, 35, 35));

        resArea = new JTextArea();
        resArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        resArea.setEditable(false);
        resArea.setOpaque(false);
        right.add(new JScrollPane(resArea), BorderLayout.CENTER);

        add(right, BorderLayout.CENTER);
    }

    private JTextField addField(JPanel p, String label) {
        JLabel l = new JLabel(label); l.setFont(NutrixTheme.FONT_SMALL); l.setForeground(NutrixTheme.TEXT_MUTED);
        l.setBorder(new EmptyBorder(0, 5, 5, 0)); p.add(l);
        JTextField f = NutrixTheme.createTextField(); f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42)); p.add(f);
        p.add(Box.createVerticalStrut(15)); return f;
    }
}
