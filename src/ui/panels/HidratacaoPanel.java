package ui.panels;

import service.HidratacaoService;
import ui.theme.NutrixTheme;
import util.Formatador;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HidratacaoPanel extends JPanel {
    private final HidratacaoService service = new HidratacaoService();
    private JTextField pesoField, volField;
    private JComboBox<String> densBox;
    private JTextArea resArea;

    public HidratacaoPanel() {
        setLayout(new BorderLayout(0, 25));
        setBackground(NutrixTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 45, 30, 45));

        JPanel card = NutrixTheme.createCard();
        card.setLayout(new BorderLayout(0, 20));
        JLabel t = new JLabel("PLANEJAMENTO HÍDRICO");
        t.setFont(NutrixTheme.FONT_H3);
        t.setForeground(NutrixTheme.ACCENT);
        card.add(t, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 15));
        grid.setOpaque(false);
        pesoField = addF(grid, "Peso (kg):");
        volField = addF(grid, "Vol. Dieta (ml):");
        densBox = addC(grid, "Densidade:", new String[]{"1.0", "1.2", "1.5"});
        card.add(grid, BorderLayout.CENTER);

        JButton b = NutrixTheme.createButton("CALCULAR HIDRATAÇÃO", true);
        b.addActionListener(e -> {
            try {
                double p = Double.parseDouble(pesoField.getText().replace(",", "."));
                double v = Double.parseDouble(volField.getText().replace(",", "."));
                double d = Double.parseDouble((String)densBox.getSelectedItem());
                double a = service.calcularAguaViaTNE(v, d);
                double n = service.calcularNecessidadeIdeal(p);
                resArea.setText("💧 BALANÇO HÍDRICO\n--------------------------\n" +
                    "Necessidade Total: " + Formatador.ml(n) + "\n" +
                    "Água via Dieta:    " + Formatador.ml(a) + "\n" +
                    "Água Extra Sonda:  " + Formatador.ml(Math.max(0, n - a)));
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
    private JComboBox<String> addC(JPanel g, String l, String[] i) {
        JPanel p = new JPanel(new BorderLayout(0, 5)); p.setOpaque(false);
        JLabel lbl = new JLabel(l); lbl.setFont(NutrixTheme.FONT_SMALL); lbl.setForeground(NutrixTheme.TEXT_MUTED);
        JComboBox<String> c = new JComboBox<>(i); p.add(lbl, BorderLayout.NORTH); p.add(c, BorderLayout.CENTER);
        g.add(p); return c;
    }
}
