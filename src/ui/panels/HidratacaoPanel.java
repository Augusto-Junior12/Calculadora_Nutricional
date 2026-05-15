package ui.panels;

import service.HidratacaoService;
import ui.components.ClinicalFormPanel;
import ui.theme.NutrixIcons;
import ui.theme.NutrixUI;
import util.Formatador;

import javax.swing.*;
import java.awt.*;

public class HidratacaoPanel extends ClinicalFormPanel {

    private final HidratacaoService service = new HidratacaoService();
    private final JTextField fPeso, fVol;
    private final JComboBox<String> cDens;

    public HidratacaoPanel() {
        super("Hidratação", NutrixIcons.Icon.WATER, new Color(6, 182, 212), new Color(224, 247, 250));
        fPeso = addField("Peso (kg)");
        fVol  = addField("Volume Dieta (ml)");
        cDens = addCombo("Densidade Calórica", new String[]{"1.0 kcal/ml", "1.2 kcal/ml", "1.5 kcal/ml"});
        addPrimaryButton("CALCULAR FLUSHES").addActionListener(e -> calcular());
    }

    private void calcular() {
        try {
            double p = parseField(fPeso), v = parseField(fVol);
            double[] densidades = {1.0, 1.2, 1.5};
            double d = densidades[cDens.getSelectedIndex()];
            double agua = service.calcularAguaViaTNE(v, d);
            double nec  = service.calcularNecessidadeIdeal(p);
            double extra = Math.max(0, nec - agua);

            showResults(new String[][]{
                {"Necessidade Hídrica Ideal", Formatador.ml(nec)},
                {"Água via Dieta Enteral",    Formatador.ml(agua)},
                {"Água Extra pela Sonda",     Formatador.ml(extra)},
            }, new Color(6, 182, 212));
        } catch (Exception ex) { showError("Verifique os dados."); }
    }
}
