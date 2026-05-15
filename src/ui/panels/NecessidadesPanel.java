package ui.panels;

import model.enums.FaseClinica;
import service.NecessidadesNutricionaisService;
import service.NecessidadesNutricionaisService.ResultadoNecessidades;
import ui.components.ClinicalFormPanel;
import ui.theme.NutrixIcons;
import ui.theme.NutrixUI;
import util.Formatador;

import javax.swing.*;
import java.awt.*;

public class NecessidadesPanel extends ClinicalFormPanel {

    private final NecessidadesNutricionaisService service = new NecessidadesNutricionaisService();
    private final JTextField fPeso;
    private final JComboBox<String> cFase;

    public NecessidadesPanel() {
        super("Metas Nutricionais", NutrixIcons.Icon.TARGET, new Color(234, 179, 8), new Color(254, 252, 232));
        fPeso = addField("Peso de Cálculo (kg)");
        cFase = addCombo("Fase Clínica", new String[]{"Aguda", "Reabilitação"});
        addPrimaryButton("CALCULAR METAS").addActionListener(e -> calcular());
    }

    private void calcular() {
        try {
            double p = parseField(fPeso);
            FaseClinica f = cFase.getSelectedIndex() == 0 ? FaseClinica.AGUDA : FaseClinica.REABILITACAO;
            ResultadoNecessidades r = service.calcularPadrao(p, f);
            showResults(new String[][]{
                {"Energia Mín.", Formatador.kcal(r.kcalMin)},
                {"Energia Máx.", Formatador.kcal(r.kcalMax)},
                {"Proteína Mín.", Formatador.gramas(r.ptnMin)},
                {"Proteína Máx.", Formatador.gramas(r.ptnMax)},
            }, NutrixUI.WARNING);
        } catch (Exception ex) { showError("Verifique o peso informado."); }
    }
}
