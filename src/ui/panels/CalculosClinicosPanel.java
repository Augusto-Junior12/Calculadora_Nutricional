package ui.panels;

import service.CalculosClinicosService;
import ui.theme.NutrixTheme;
import util.Formatador;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CalculosClinicosPanel extends JPanel {
    private final CalculosClinicosService service = new CalculosClinicosService();
    private JTextField fVol, fPeso;
    private JTextArea resArea;

    public CalculosClinicosPanel() {
        setLayout(new BorderLayout(40, 0));
        setBackground(Color.WHITE);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(380, 0));

        JLabel title = new JLabel("Cálculos Clínicos UTI");
        title.setFont(NutrixTheme.FONT_H2);
        title.setBorder(new EmptyBorder(0, 0, 30, 0));
        left.add(title);

        fVol = addField(left, "Volume Nora (ml/h)");
        fPeso = addField(left, "Peso (kg)");

        left.add(Box.createVerticalStrut(25));
        JButton btn = NutrixTheme.createButton("CALCULAR DOSE", true);
        btn.addActionListener(e -> {
            try {
                double v = Double.parseDouble(fVol.getText().replace(",", "."));
                double p = Double.parseDouble(fPeso.getText().replace(",", "."));
                double d = service.calcularNoraSimples(v, p);
                resArea.setText("💊 MONITORAMENTO UTI\n\n" +
                    "Nora: " + Formatador.decimal2(d) + " mcg/kg/min\n" +
                    (d > 2.0 ? "⚠ ATENÇÃO: Dose Crítica!" : "Status: Sob Controle."));
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
