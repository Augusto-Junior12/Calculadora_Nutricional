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
        setLayout(new BorderLayout(48, 0));
        setBackground(Color.WHITE);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(400, 0));

        JLabel title = new JLabel("Monitor UTI");
        title.setFont(NutrixTheme.H2);
        title.setBorder(new EmptyBorder(0, 0, 32, 0));
        left.add(title);

        fVol = addInput(left, "Volume Nora (ml/h)");
        fPeso = addInput(left, "Peso (kg)");

        left.add(Box.createVerticalStrut(24));
        JButton btn = NutrixTheme.createPrimaryButton("CALCULAR");
        btn.addActionListener(e -> {
            try {
                double v = Double.parseDouble(fVol.getText().replace(",", "."));
                double p = Double.parseDouble(fPeso.getText().replace(",", "."));
                double d = service.calcularNoraSimples(v, p);
                resArea.setText("DROGAS VASOATIVAS\n==============================\n\n" +
                    "Noradrenalina: " + Formatador.decimal2(d) + " mcg/kg/min\n" +
                    (d > 2.0 ? "⚠ ATENÇÃO: Dose crítica!" : "Status: Estável."));
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
