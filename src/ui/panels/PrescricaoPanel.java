package ui.panels;

import model.AlertaClinico;
import model.FormulaEnteral;
import model.Prescricao;
import repository.FormulaEnteralRepository;
import service.PrescricaoService;
import ui.theme.HospitalTheme;
import util.Formatador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Painel de prescrição — modo contínuo e intermitente.
 */
public class PrescricaoPanel extends JPanel {

    private final PrescricaoService service = new PrescricaoService();
    private final FormulaEnteralRepository formulaRepo = FormulaEnteralRepository.getInstance();
    private JTextField pesoField, vctField, ptnField, horasField;
    private JComboBox<String> modoBox, formulaBox;
    private JTextArea resultadoArea;
    private JTable formulaTable;

    public PrescricaoPanel() {
        setLayout(new BorderLayout(0, 10));
        setBackground(HospitalTheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        header.add(HospitalTheme.createTitle("Prescrição de Dieta Enteral"));
        header.add(HospitalTheme.createLabel("Cálculo de volume, calorias, proteínas e progressão de 4 dias"));
        add(header, BorderLayout.NORTH);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);

        // Formulário
        JPanel card = HospitalTheme.createCard();
        card.setLayout(new BorderLayout(0, 10));
        card.add(HospitalTheme.createSubtitle("Parâmetros da Prescrição"), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 4, 10, 8));
        grid.setOpaque(false);

        grid.add(HospitalTheme.createLabel("Modo:"));
        modoBox = HospitalTheme.createComboBox(new String[]{"Contínuo (22h)", "Intermitente"});
        grid.add(modoBox);

        pesoField = addCampo(grid, "Peso referência (kg):");
        vctField = addCampo(grid, "Meta calórica (kcal):");
        ptnField = addCampo(grid, "Meta proteica (g):");
        horasField = addCampo(grid, "Horas / Nº horários:");
        horasField.setText("22");

        modoBox.addActionListener(e -> {
            horasField.setText(modoBox.getSelectedIndex() == 0 ? "22" : "6");
        });

        grid.add(HospitalTheme.createLabel("Fórmula:"));
        List<FormulaEnteral> formulas = formulaRepo.listarTodas();
        String[] nomes = new String[formulas.size()];
        for (int i = 0; i < formulas.size(); i++) nomes[i] = formulas.get(i).toString();
        formulaBox = HospitalTheme.createComboBox(nomes);
        formulaBox.setPreferredSize(new Dimension(350, 34));
        grid.add(formulaBox);

        card.add(grid, BorderLayout.CENTER);
        main.add(card);
        main.add(Box.createVerticalStrut(10));

        // Botões
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);
        JButton calcBtn = HospitalTheme.createPrimaryButton("Calcular Prescrição");
        JButton compBtn = HospitalTheme.createSecondaryButton("Comparar Fórmulas");
        calcBtn.addActionListener(e -> calcular());
        compBtn.addActionListener(e -> mostrarComparativo());
        btnPanel.add(calcBtn);
        btnPanel.add(compBtn);
        main.add(btnPanel);
        main.add(Box.createVerticalStrut(10));

        // Resultado
        resultadoArea = new JTextArea(20, 60);
        resultadoArea.setFont(HospitalTheme.FONT_MONO);
        resultadoArea.setEditable(false);
        resultadoArea.setBackground(HospitalTheme.SURFACE_ALT);
        resultadoArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane sp = new JScrollPane(resultadoArea);
        sp.setBorder(BorderFactory.createTitledBorder("Resultado da Prescrição"));
        main.add(sp);

        JScrollPane mainScroll = new JScrollPane(main);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll, BorderLayout.CENTER);
    }

    private void calcular() {
        try {
            double peso = parseD(pesoField), vct = parseD(vctField), ptn = parseD(ptnField);
            int horas = Integer.parseInt(horasField.getText().trim());
            boolean continuo = modoBox.getSelectedIndex() == 0;

            List<FormulaEnteral> formulas = formulaRepo.listarTodas();
            String selName = formulas.get(formulaBox.getSelectedIndex()).getNome();
            FormulaEnteral formula = formulaRepo.buscarPorNome(selName);

            List<AlertaClinico> alertas = new ArrayList<>();
            Prescricao p;
            if (continuo) {
                p = service.calcularContinuo(formula, peso, vct, ptn, horas, alertas);
            } else {
                p = service.calcularIntermitente(formula, peso, vct, ptn, horas, alertas);
            }

            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════\n");
            sb.append("  PRESCRIÇÃO — ").append(p.getModo().getDescricao().toUpperCase()).append("\n");
            sb.append("═══════════════════════════════════════════\n\n");
            sb.append("Fórmula: ").append(formula.getNome()).append("\n");
            sb.append("Densidade: ").append(Formatador.decimal2(formula.getDensidade())).append(" kcal/ml\n");
            sb.append("PTN/L: ").append(Formatador.decimal1(formula.getPtnPorLitro())).append(" g/L\n\n");

            sb.append("▸ CÁLCULOS\n");
            sb.append("  ").append(continuo ? "Volume ml/h" : "Volume ml/horário").append(": ");
            sb.append(Formatador.decimal1(p.getVolumeMlH())).append("\n");
            sb.append("  Volume Total: ").append(Formatador.ml(p.getVolumeTotal())).append("\n");
            sb.append("  Kcal Totais: ").append(Formatador.kcal(p.getKcalTotais())).append("\n");
            sb.append("  PTN Total: ").append(Formatador.gramas(p.getPtnTotal())).append("\n");
            sb.append("  Kcal/kg: ").append(Formatador.decimal2(p.getKcalPorKg())).append("\n");
            sb.append("  PTN/kg: ").append(Formatador.decimal2(p.getPtnPorKg())).append("\n");
            sb.append("  % VCT: ").append(Formatador.percentual(p.getPercentualVCT())).append("\n");
            sb.append("  % PTN: ").append(Formatador.percentual(p.getPercentualPTN())).append("\n\n");

            sb.append("▸ VOLUME PLENO (100% do VCT)\n");
            sb.append("  Volume: ").append(Formatador.ml(p.getVolumePleno())).append("\n");
            sb.append("  PTN Plena: ").append(Formatador.gramas(p.getPtnPlena())).append("\n");
            sb.append("  PTN Suplementar: ").append(Formatador.gramas(p.getPtnSuplementar())).append("\n\n");

            sb.append("▸ PROGRESSÃO DE 4 DIAS (R04)\n");
            String[] dias = {"1º dia (25%)", "2º dia (50%)", "3º dia (75%)", "4º dia (100%)"};
            for (int i = 0; i < 4; i++) {
                sb.append("  ").append(dias[i]).append(": ");
                sb.append(Formatador.kcal(p.getKcalProgressao()[i])).append(" | ");
                sb.append(continuo ? Formatador.mlH(p.getVolumeProgressao()[i]) :
                    Formatador.ml(p.getVolumeProgressao()[i]) + "/horário").append("\n");
            }

            if (p.getPtnSuplementar() > 0) {
                sb.append("\n▸ SUPLEMENTAÇÃO PROTEICA (a partir do 4º dia — R05)\n");
                double[][] supl = service.calcularSuplementacao(p.getPtnSuplementar());
                sb.append("  Nutren Just Protein: ").append(Formatador.gramas(supl[0][0]));
                sb.append(" (").append(Formatador.kcal(supl[0][1])).append(")\n");
                sb.append("  Fresubin Protein Powder: ").append(Formatador.gramas(supl[1][0]));
                sb.append(" (").append(Formatador.kcal(supl[1][1])).append(")\n");
                sb.append("  Nutridrink Protein: ").append(Formatador.ml(supl[2][0]));
                sb.append(" (").append(Formatador.kcal(supl[2][1])).append(")\n");
            }

            if (!alertas.isEmpty()) {
                sb.append("\n═══ ALERTAS ═══\n");
                for (AlertaClinico a : alertas) sb.append("  ⚠ ").append(a).append("\n");
            }

            resultadoArea.setText(sb.toString());
            resultadoArea.setCaretPosition(0);
        } catch (Exception e) {
            resultadoArea.setText("⚠ Erro: " + e.getMessage());
        }
    }

    private void mostrarComparativo() {
        List<FormulaEnteral> formulas = formulaRepo.listarTodas();
        String[] cols = {"Fórmula", "Dens.", "PTN/L", "CHO/L", "LIP/L", "Fibra", "K", "Osmol."};
        Object[][] data = new Object[formulas.size()][cols.length];
        for (int i = 0; i < formulas.size(); i++) {
            FormulaEnteral f = formulas.get(i);
            data[i] = new Object[]{f.getNome(), f.getDensidade(), f.getPtnPorLitro(),
                f.getChoPorLitro(), f.getLipPorLitro(), f.getFibrasPorLitro(),
                f.getPotassio(), f.getOsmolaridade()};
        }
        JTable table = new JTable(new DefaultTableModel(data, cols));
        table.setFont(HospitalTheme.FONT_SMALL);
        table.setRowHeight(24);
        table.getTableHeader().setFont(HospitalTheme.FONT_BODY_BOLD);
        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(750, 400));
        JOptionPane.showMessageDialog(this, sp, "Comparativo de Fórmulas Enterais", JOptionPane.PLAIN_MESSAGE);
    }

    private double parseD(JTextField f) {
        String t = f.getText().trim().replace(",", ".");
        return t.isEmpty() ? 0 : Double.parseDouble(t);
    }

    private JTextField addCampo(JPanel grid, String label) {
        grid.add(HospitalTheme.createLabel(label));
        JTextField f = HospitalTheme.createTextField();
        grid.add(f);
        return f;
    }
}
