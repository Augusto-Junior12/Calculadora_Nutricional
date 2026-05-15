package ui.panels;

import service.CalculosClinicosService;
import ui.theme.NutrixTheme;
import util.Formatador;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CalculosClinicosPanel extends JPanel {
    private final CalculosClinicosService service = new CalculosClinicosService();
    private JTextField noraVol, noraPeso;
    private JTextArea resArea;

    public CalculosClinicosPanel() {
        setLayout(new BorderLayout(0, 25));
        setBackground(NutrixTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 45, 30, 45));

        JPanel card = NutrixTheme.createCard();
        card.setLayout(new BorderLayout(0, 20));
        JLabel t = new JLabel("MONITORAMENTO CLÍNICO UTI");
        t.setFont(NutrixTheme.FONT_H3);
        t.setForeground(NutrixTheme.ACCENT);
        card.add(t, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 2, 20, 15));
        grid.setOpaque(false);
        noraVol = addF(grid, "Volume Nora (ml/h):");
        noraPeso = addF(grid, "Peso Paciente (kg):");
        card.add(grid, BorderLayout.CENTER);

        JButton b = NutrixTheme.createButton("CALCULAR DOSE NORA", true);
        b.addActionListener(e -> {
            try {
                double v = Double.parseDouble(noraVol.getText().replace(",", "."));
                double p = Double.parseDouble(noraPeso.getText().replace(",", "."));
                double d = service.calcularNoraSimples(v, p);
                resArea.setText("💊 MONITORAMENTO DROGAS VASOATIVAS\n--------------------------\n" +
                    "Dose Noradrenalina: " + Formatador.decimal2(d) + " mcg/kg/min\n" +
                    (d > 2.0 ? "⚠ ALERTA: Dose acima do padrão!" : "✅ Dose dentro do esperado."));
            } catch (Exception ex) { resArea.setText("⚠ Erro"); }
        });

        resArea = new JTextArea(8, 50);
        resArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resArea.setBackground(NutrixTheme.BG_INPUT);
        resArea.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel main = new JPanel(); main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS)); main.setOpaque(false);
        main.add(card); main.add(Box.createVerticalStrut(20)); main.add(b); main.add(Box.createVerticalStrut(20)); main.add(new JScrollPane(resArea));
        add(main, BorderLayout.CENTER);
    }

    private JTextField addF(JPanel g, String l) {
        JPanel p = new JPanel(new BorderLayout(0, 5)); p.setOpaque(false);
        JLabel lbl = new JLabel(l); lbl.setFont(NutrixTheme.FONT_SMALL); lbl.setForeground(NutrixTheme.TEXT_MUTED);
        JTextField f = NutrixTheme.createTextField(); p.add(lbl, BorderLayout.NORTH); p.add(f, BorderLayout.CENTER);
        g.add(p); return f;
    }
}
