package ui.panels;

import service.AntropometriaService;
import ui.theme.NutrixTheme;
import util.Formatador;
import model.enums.Genero;
import model.enums.Etnia;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Antropometria Direta — Nutrix.
 */
public class AntropometriaPanel extends JPanel {

    private final AntropometriaService service = new AntropometriaService();
    private JTextField fIdade, fPeso, fAltura, fAJ, fCB;
    private JComboBox<String> cGenero;
    private JTextArea resArea;

    public AntropometriaPanel() {
        setLayout(new BorderLayout(48, 0));
        setBackground(Color.WHITE);

        // Left Side: Inputs
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(400, 0));

        JLabel title = new JLabel("Avaliação Antropométrica");
        title.setFont(NutrixTheme.H2);
        title.setBorder(new EmptyBorder(0, 0, 32, 0));
        left.add(title);

        fIdade = addInput(left, "Idade (anos)");
        cGenero = addCombo(left, "Gênero", new String[]{"Masculino", "Feminino"});
        fPeso = addInput(left, "Peso Atual (kg)");
        fAltura = addInput(left, "Altura Atual (m)");
        fAJ = addInput(left, "Altura do Joelho (cm)");
        fCB = addInput(left, "Circunferência do Braço (cm)");

        left.add(Box.createVerticalStrut(24));
        JButton btn = NutrixTheme.createPrimaryButton("CALCULAR");
        btn.addActionListener(e -> calcular());
        left.add(btn);

        add(left, BorderLayout.WEST);

        // Right Side: Results
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(NutrixTheme.BG_SURFACE);
        right.setBorder(new EmptyBorder(32, 32, 32, 32));

        resArea = new JTextArea();
        resArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 14)); // Mono para tabelas
        resArea.setEditable(false);
        resArea.setOpaque(false);
        right.add(new JScrollPane(resArea), BorderLayout.CENTER);

        add(right, BorderLayout.CENTER);
    }

    private void calcular() {
        try {
            int idade = Integer.parseInt(fIdade.getText());
            Genero g = cGenero.getSelectedIndex() == 0 ? Genero.MASCULINO : Genero.FEMININO;
            double aj = Double.parseDouble(fAJ.getText().replace(",", "."));
            double cb = Double.parseDouble(fCB.getText().replace(",", "."));
            
            double altEst = service.calcularAlturaEstimada(g, aj, idade, new ArrayList<>());
            double pesoEst = service.calcularPesoEstimado(g, Etnia.BRANCO, idade, aj, cb, new ArrayList<>());
            
            StringBuilder sb = new StringBuilder();
            sb.append("RESULTADOS ESTIMADOS\n");
            sb.append("==============================\n\n");
            sb.append(String.format("%-20s %s\n", "Altura (Chumlea):", Formatador.metros(altEst)));
            sb.append(String.format("%-20s %s\n", "Peso (Chumlea):", Formatador.kg(pesoEst)));
            sb.append(String.format("%-20s %s\n", "IMC Estimado:", Formatador.decimal1(service.calcularIMC(pesoEst, altEst))));
            
            resArea.setText(sb.toString());
        } catch (Exception ex) {
            resArea.setText("Verifique os campos obrigatórios.");
        }
    }

    private JTextField addInput(JPanel p, String label) {
        JLabel l = new JLabel(label);
        l.setFont(NutrixTheme.H3);
        l.setBorder(new EmptyBorder(0, 0, 8, 0));
        p.add(l);
        JTextField f = NutrixTheme.createInput();
        p.add(f);
        p.add(Box.createVerticalStrut(16));
        return f;
    }

    private JComboBox<String> addCombo(JPanel p, String label, String[] items) {
        JLabel l = new JLabel(label);
        l.setFont(NutrixTheme.H3);
        l.setBorder(new EmptyBorder(0, 0, 8, 0));
        p.add(l);
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(NutrixTheme.BODY);
        p.add(c);
        p.add(Box.createVerticalStrut(16));
        return c;
    }
}
