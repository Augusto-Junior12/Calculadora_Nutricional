package ui.panels;

import model.FormulaEnteral;
import model.Prescricao;
import repository.FormulaEnteralRepository;
import service.PrescricaoService;
import ui.components.ClinicalFormPanel;
import ui.theme.NutrixIcons;
import ui.theme.NutrixUI;
import util.Formatador;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PrescricaoPanel extends ClinicalFormPanel {

    private final PrescricaoService service = new PrescricaoService();
    private final JTextField fPeso, fVCT, fPTN;
    private final JComboBox<String> fFormula;

    public PrescricaoPanel() {
        super("Prescrição TNE", NutrixIcons.Icon.PILL, NutrixUI.DANGER, NutrixUI.DANGER_LIGHT);

        fPeso    = addField("Peso de Cálculo (kg)");
        fVCT     = addField("Meta Calórica (kcal)");
        fPTN     = addField("Meta Proteica (g)");

        List<FormulaEnteral> formulas = FormulaEnteralRepository.getInstance().listarTodas();
        String[] nomes = new String[formulas.size()];
        for (int i = 0; i < formulas.size(); i++) nomes[i] = formulas.get(i).getNome();
        fFormula = addCombo("Fórmula Enteral", nomes);

        addPrimaryButton("GERAR PRESCRIÇÃO").addActionListener(e -> calcular());
    }

    private void calcular() {
        try {
            double p = parseField(fPeso), vct = parseField(fVCT), ptn = parseField(fPTN);
            FormulaEnteral f = FormulaEnteralRepository.getInstance().listarTodas().get(fFormula.getSelectedIndex());
            Prescricao r = service.calcularContinuo(f, p, vct, ptn, 22, new ArrayList<>());

            double[] prog = r.getVolumeProgressao();
            showResults(new String[][]{
                {"Volume Horário", Formatador.mlH(r.getVolumeMlH())},
                {"Volume Total/dia", Formatador.ml(r.getVolumeTotal())},
                {"Calorias Totais", Formatador.kcal(r.getKcalTotais())},
                {"Proteínas Totais", Formatador.gramas(r.getPtnTotal())},
                {"Progressão D1 (25%)", Formatador.mlH(prog[0])},
                {"Progressão D2 (50%)", Formatador.mlH(prog[1])},
                {"Progressão D4 (100%)", Formatador.mlH(prog[3])},
            }, NutrixUI.DANGER);
        } catch (Exception ex) { showError("Preencha todos os campos."); }
    }
}
