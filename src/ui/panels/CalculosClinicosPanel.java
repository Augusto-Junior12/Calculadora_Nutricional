package ui.panels;

import service.CalculosClinicosService;
import ui.components.ClinicalFormPanel;
import ui.theme.NutrixIcons;
import ui.theme.NutrixUI;
import util.Formatador;

import javax.swing.*;
import java.awt.*;

public class CalculosClinicosPanel extends ClinicalFormPanel {

    private final CalculosClinicosService service = new CalculosClinicosService();
    private final JTextField fVol, fPeso;

    public CalculosClinicosPanel() {
        super("Monitoramento UTI", NutrixIcons.Icon.MONITOR, new Color(168, 85, 247), new Color(243, 232, 255));
        fVol  = addField("Volume Noradrenalina (ml/h)");
        fPeso = addField("Peso do Paciente (kg)");
        addPrimaryButton("CALCULAR DOSE").addActionListener(e -> calcular());
    }

    private void calcular() {
        try {
            double v = parseField(fVol), p = parseField(fPeso);
            double d = service.calcularNoraSimples(v, p);
            boolean critico = d > 2.0;

            showResults(new String[][]{
                {"Dose Noradrenalina", Formatador.decimal2(d) + " mcg/kg/min"},
                {"Nível de Risco", critico ? "⚠ ALTO" : "✓ Controlado"},
            }, critico ? NutrixUI.DANGER : NutrixUI.SUCCESS);
        } catch (Exception ex) { showError("Verifique os dados informados."); }
    }
}
