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
        setLayout(new BorderLayout(48, 0));
        setBackground(Color.WHITE);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(400, 0));

        JLabel title = new JLabel("Prescrição de Dieta");
        title.setFont(NutrixTheme.H2);
        title.setBorder(new EmptyBorder(0, 0, 32, 0));
        left.add(title);

        fPeso = addInput(left, "Peso de Referência (kg)");
        fVCT = addInput(left, "Meta Calórica (kcal)");
        fPTN = addInput(left, "Meta Proteica (g)");
        
        java.util.List<FormulaEnteral> formulas = FormulaEnteralRepository.getInstance().listarTodas();
        String[] nomes = new String[formulas.size()];
        for(int i=0; i<formulas.size(); i++) nomes[i] = formulas.get(i).getNome();
        fFormula = addCombo(left, "Fórmula Enteral", nomes);

        left.add(Box.createVerticalStrut(24));
        JButton btn = NutrixTheme.createPrimaryButton("GERAR PRESCRIÇÃO");
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
            double vct = Double.parseDouble(fVCT.getText().replace(",", "."));
            double ptn = Double.parseDouble(fPTN.getText().replace(",", "."));
            FormulaEnteral f = FormulaEnteralRepository.getInstance().listarTodas().get(fFormula.getSelectedIndex());
            
            Prescricao res = service.calcularContinuo(f, p, vct, ptn, 22, new ArrayList<>());
            
            StringBuilder sb = new StringBuilder();
            sb.append("DETALHAMENTO DA PRESCRIÇÃO\n");
            sb.append("==============================\n\n");
            sb.append(String.format("%-20s %s\n", "Volume Horário:", Formatador.mlH(res.getVolumeMlH())));
            sb.append(String.format("%-20s %s\n", "Volume Total:", Formatador.ml(res.getVolumeTotal())));
            sb.append(String.format("%-20s %s\n", "Calorias Totais:", Formatador.kcal(res.getKcalTotais())));
            sb.append(String.format("%-20s %s\n", "Proteínas Totais:", Formatador.gramas(res.getPtnTotal())));
            sb.append("\nPROGRESSÃO (R04):\n");
            sb.append("Dia 1: ").append(Formatador.mlH(res.getVolumeProgressao()[0])).append("\n");
            sb.append("Dia 2: ").append(Formatador.mlH(res.getVolumeProgressao()[1])).append("\n");
            sb.append("Dia 4: ").append(Formatador.mlH(res.getVolumeProgressao()[3])).append("\n");
            
            resArea.setText(sb.toString());
        } catch (Exception ex) {
            resArea.setText("Erro no cálculo. Verifique os dados.");
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
