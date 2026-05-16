package ui.panels;

import model.enums.Etnia;
import model.enums.Genero;
import service.AntropometriaService;
import ui.components.ClinicalFormPanel;
import ui.theme.NutrixIcons;
import ui.theme.NutrixUI;
import util.Formatador;

import javax.swing.*;
import java.util.ArrayList;

public class AntropometriaPanel extends ClinicalFormPanel {

    private final AntropometriaService service = new AntropometriaService();
    private final JTextField fIdade, fPeso, fAltura, fAJ, fCB;
    private final JComboBox<String> cGenero, cEtnia;

    public AntropometriaPanel() {
        super("Antropometria", NutrixIcons.Icon.SCALE, NutrixUI.SUCCESS, NutrixUI.SUCCESS_LIGHT);

        fIdade  = addField("Idade (anos)");
        cGenero = addCombo("Gênero", new String[]{"Masculino", "Feminino"});
        cEtnia  = addCombo("Etnia", new String[]{"Branco", "Negro"});
        fPeso   = addField("Peso Atual (kg)");
        fAltura = addField("Altura Atual (m) — opcional");
        fAJ     = addField("Altura do Joelho (cm)");
        fCB     = addField("Circunferência do Braço (cm)");

        JButton btn = addPrimaryButton("CALCULAR");
        btn.addActionListener(e -> calcular());
    }

    private void calcular() {
        try {
            int idade = Integer.parseInt(fIdade.getText().trim());
            Genero g = cGenero.getSelectedIndex() == 0 ? Genero.MASCULINO : Genero.FEMININO;
            Etnia e = cEtnia.getSelectedIndex() == 0 ? Etnia.BRANCO : Etnia.NEGRO;
            double aj = parseField(fAJ), cb = parseField(fCB);
            
            var alertas = new ArrayList<model.AlertaClinico>();
            double altEst = service.calcularAlturaEstimada(g, aj, idade, alertas);
            double pesoEst = service.calcularPesoEstimado(g, e, idade, aj, cb, alertas);
            
            // Se altura manual foi informada, use-a para o IMC
            double altFinal = altEst;
            String altLabel = Formatador.metros(altEst) + " (est.)";
            
            String manualAltTxt = fAltura.getText().trim();
            if (!manualAltTxt.isEmpty()) {
                altFinal = Double.parseDouble(manualAltTxt.replace(",", "."));
                altLabel = Formatador.metros(altFinal) + " (real)";
            }

            double imc = service.calcularIMC(pesoEst, altFinal);
            String classIMC = service.classificarIMC(imc, idade).getDescricao();
 
            showResults(new String[][]{
                {"Altura Utilizada", altLabel},
                {"Peso Estimado (Chumlea)", Formatador.kg(pesoEst)},
                {"IMC Calculado", Formatador.decimal1(imc)},
                {"Classificação IMC", classIMC},
            }, NutrixUI.SUCCESS);
        } catch (Exception ex) {
            showError("Preencha Idade, AJ e CB corretamente.");
        }
    }
}
