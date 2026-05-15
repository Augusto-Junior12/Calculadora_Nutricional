package ui.panels;

import model.FormulaEnteral;
import model.Prescricao;
import repository.FormulaEnteralRepository;
import service.PrescricaoService;
import ui.theme.NutrixTheme;
import util.Formatador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class PrescricaoPanel extends JPanel {

    private final PrescricaoService service = new PrescricaoService();
    private JTextField fPeso, fVCT, fPTN;
    private JComboBox<String> fFormula;
    private JTextArea resArea;

    public PrescricaoPanel() {
        setLayout(new BorderLayout(40, 0));
        setBackground(Color.WHITE);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(380, 0));

        JLabel title = new JLabel("Configuração TNE");
        title.setFont(NutrixTheme.FONT_H2);
        title.setBorder(new EmptyBorder(0, 0, 30, 0));
        left.add(title);

        fPeso = addField(left, "Peso de Cálculo (kg)");
        fVCT = addField(left, "Meta Calórica (kcal)");
        fPTN = addField(left, "Meta Proteica (g)");
        
        java.util.List<FormulaEnteral> formulas = FormulaEnteralRepository.getInstance().listarTodas();
        String[] nomes = new String[formulas.size()];
        for(int i=0; i<formulas.size(); i++) nomes[i] = formulas.get(i).getNome();
        fFormula = addCombo(left, "Fórmula Enteral", nomes);

        left.add(Box.createVerticalStrut(25));
        JButton btn = NutrixTheme.createButton("GERAR PRESCRIÇÃO", true);
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
        
        JLabel resTitle = new JLabel("Plano de Dieta Nutrix");
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
            double vct = Double.parseDouble(fVCT.getText().replace(",", "."));
            double ptn = Double.parseDouble(fPTN.getText().replace(",", "."));
            FormulaEnteral f = FormulaEnteralRepository.getInstance().listarTodas().get(fFormula.getSelectedIndex());
            
            Prescricao res = service.calcularContinuo(f, p, vct, ptn, 22, new ArrayList<>());
            
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("• Volume:        %s\n", Formatador.mlH(res.getVolumeMlH())));
            sb.append(String.format("• Vol. Total:    %s\n", Formatador.ml(res.getVolumeTotal())));
            sb.append(String.format("• Calorias:      %s\n", Formatador.kcal(res.getKcalTotais())));
            sb.append(String.format("• Proteínas:     %s\n", Formatador.gramas(res.getPtnTotal())));
            sb.append("\nPROGRESSÃO (R04):\n");
            sb.append("Dia 1: ").append(Formatador.mlH(res.getVolumeProgressao()[0])).append("\n");
            sb.append("Dia 4: ").append(Formatador.mlH(res.getVolumeProgressao()[3])).append("\n");
            
            resArea.setText(sb.toString());
        } catch (Exception ex) {
            resArea.setText("Erro no cálculo. Verifique os dados.");
        }
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
