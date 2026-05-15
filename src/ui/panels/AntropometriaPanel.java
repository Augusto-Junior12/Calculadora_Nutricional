package ui.panels;

import service.AntropometriaService;
import ui.theme.NutrixIcons;
import ui.theme.NutrixTheme;
import util.Formatador;
import model.enums.Genero;
import model.enums.Etnia;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Antropometria Premium — Nutrix v4.
 * Estético, Arredondado e Direto.
 */
public class AntropometriaPanel extends JPanel {

    private final AntropometriaService service = new AntropometriaService();
    private JTextField fIdade, fPeso, fAltura, fAJ, fCB;
    private JComboBox<String> cGenero;
    private JTextArea resArea;

    public AntropometriaPanel() {
        setLayout(new BorderLayout(40, 0));
        setBackground(Color.WHITE);

        // --- Form Section ---
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(380, 0));

        JLabel title = new JLabel("Parâmetros do Paciente");
        title.setFont(NutrixTheme.FONT_H2);
        title.setBorder(new EmptyBorder(0, 0, 30, 0));
        left.add(title);

        fIdade = addField(left, "Idade (anos)");
        cGenero = addCombo(left, "Gênero", new String[]{"Masculino", "Feminino"});
        fPeso = addField(left, "Peso Atual (kg)");
        fAltura = addField(left, "Altura Atual (m)");
        fAJ = addField(left, "Altura do Joelho (cm)");
        fCB = addField(left, "Circunferência do Braço (cm)");

        left.add(Box.createVerticalStrut(25));
        JButton btn = NutrixTheme.createButton("CALCULAR RESULTADOS", true);
        btn.addActionListener(e -> calcular());
        left.add(btn);

        add(left, BorderLayout.WEST);

        // --- Result Section (Rounded Card) ---
        JPanel right = NutrixTheme.createRoundedPanel(25, NutrixTheme.ACCENT_SOFT);
        right.setLayout(new BorderLayout());
        right.setBorder(new EmptyBorder(35, 35, 35, 35));

        resArea = new JTextArea();
        resArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        resArea.setEditable(false);
        resArea.setOpaque(false);
        resArea.setForeground(NutrixTheme.TEXT_H1);
        
        JLabel resTitle = new JLabel("Resumo Antropométrico");
        resTitle.setFont(NutrixTheme.FONT_H3);
        resTitle.setForeground(NutrixTheme.ACCENT);
        resTitle.setBorder(new EmptyBorder(0, 0, 20, 0));

        right.add(resTitle, BorderLayout.NORTH);
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
            double imc = service.calcularIMC(pesoEst, altEst);
            
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("• Altura Estimada: %s\n", Formatador.metros(altEst)));
            sb.append(String.format("• Peso Estimado:   %s\n", Formatador.kg(pesoEst)));
            sb.append(String.format("• IMC Estimado:    %s\n", Formatador.decimal1(imc)));
            sb.append(String.format("• Classificação:   %s\n", service.classificarIMC(imc, idade).getDescricao()));
            
            resArea.setText(sb.toString());
        } catch (Exception ex) {
            resArea.setText("Preencha todos os campos corretamente.");
        }
    }

    private JTextField addField(JPanel p, String label) {
        JLabel l = new JLabel(label);
        l.setFont(NutrixTheme.FONT_SMALL);
        l.setForeground(NutrixTheme.TEXT_MUTED);
        l.setBorder(new EmptyBorder(0, 5, 5, 0));
        p.add(l);
        JTextField f = NutrixTheme.createTextField();
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        p.add(f);
        p.add(Box.createVerticalStrut(15));
        return f;
    }

    private JComboBox<String> addCombo(JPanel p, String label, String[] items) {
        JLabel l = new JLabel(label);
        l.setFont(NutrixTheme.FONT_SMALL);
        l.setForeground(NutrixTheme.TEXT_MUTED);
        l.setBorder(new EmptyBorder(0, 5, 5, 0));
        p.add(l);
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(NutrixTheme.FONT_BODY);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        p.add(c);
        p.add(Box.createVerticalStrut(15));
        return c;
    }
}
