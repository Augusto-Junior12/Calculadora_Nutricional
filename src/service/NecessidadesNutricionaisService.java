package service;

import model.AlertaClinico;
import model.Paciente;
import model.enums.FaseClinica;
import model.enums.NivelAlerta;
import util.ConstantesClinicas;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço de cálculo de necessidades nutricionais.
 * Calcula metas calóricas e proteicas com base na fase clínica e condições.
 */
public class NecessidadesNutricionaisService {

    /**
     * Resultado do cálculo de necessidades.
     */
    public static class ResultadoNecessidades {
        public double kcalMin, kcalMax, ptnMin, ptnMax;
        public double kcalPersonalizado, ptnPersonalizado;
        public List<AlertaClinico> alertas = new ArrayList<>();

        @Override
        public String toString() {
            return String.format("Kcal: %.0f–%.0f | PTN: %.1f–%.1fg", kcalMin, kcalMax, ptnMin, ptnMax);
        }
    }

    /**
     * Calcula necessidades para paciente não-obeso conforme fase clínica.
     * @param peso kg (peso selecionado pelo nutricionista — R01)
     * @param fase AGUDA ou REABILITACAO — seleção manual (R02)
     */
    public ResultadoNecessidades calcularPadrao(double peso, FaseClinica fase) {
        ResultadoNecessidades r = new ResultadoNecessidades();
        r.kcalMin = peso * fase.getKcalKgMin();
        r.kcalMax = peso * fase.getKcalKgMax();
        r.ptnMin = peso * fase.getPtnKgMin();
        r.ptnMax = peso * fase.getPtnKgMax();
        return r;
    }

    /**
     * Calcula necessidades com meta personalizada (kcal/kg e PTN/kg livres).
     */
    public ResultadoNecessidades calcularPersonalizado(double peso, double kcalPorKg, double ptnPorKg) {
        ResultadoNecessidades r = new ResultadoNecessidades();
        r.kcalPersonalizado = peso * kcalPorKg;
        r.ptnPersonalizado = peso * ptnPorKg;
        r.kcalMin = r.kcalPersonalizado;
        r.kcalMax = r.kcalPersonalizado;
        r.ptnMin = r.ptnPersonalizado;
        r.ptnMax = r.ptnPersonalizado;

        // Alertas de faixa
        if (kcalPorKg < ConstantesClinicas.KCAL_KG_MIN_ALERTA || kcalPorKg > ConstantesClinicas.KCAL_KG_MAX_ALERTA) {
            r.alertas.add(new AlertaClinico(NivelAlerta.AVISO, "KCAL",
                String.format("Kcal/kg (%.1f) fora da faixa clínica reconhecida (10–40)", kcalPorKg)));
        }
        if (ptnPorKg > ConstantesClinicas.PTN_KG_MAX_ALERTA) {
            r.alertas.add(new AlertaClinico(NivelAlerta.AVISO, "PTN",
                String.format("PTN/kg (%.1f) acima de 3.0 g/kg — requer justificativa", ptnPorKg)));
        }
        return r;
    }

    /**
     * Calcula necessidades para paciente com hemodiálise.
     */
    public ResultadoNecessidades calcularHemodialise(double peso, boolean continua) {
        ResultadoNecessidades r = calcularPadrao(peso, FaseClinica.AGUDA);
        if (continua) {
            // HD contínua (CRRT): PTN até 2.0 g/kg
            r.ptnMin = peso * 1.5;
            r.ptnMax = peso * 2.0;
        } else {
            // HD intermitente: PTN 1.2-1.5 g/kg com acréscimo
            r.ptnMin = peso * 1.2;
            r.ptnMax = peso * 1.8;
        }
        return r;
    }

    /**
     * Calcula necessidades para paciente obeso.
     * IMC 30-40: kcal 11-14/PA, PTN >2 g/PI
     * IMC >40: kcal 22-25/PI, PTN >2.5 g/PI
     */
    public ResultadoNecessidades calcularObeso(double pesoAtual, double pesoIdeal, double imc) {
        ResultadoNecessidades r = new ResultadoNecessidades();
        if (imc > 40) {
            r.kcalMin = pesoIdeal * 22;
            r.kcalMax = pesoIdeal * 25;
            r.ptnMin = pesoIdeal * 2.0;
            r.ptnMax = pesoIdeal * 2.5;
        } else {
            r.kcalMin = pesoAtual * 11;
            r.kcalMax = pesoAtual * 14;
            r.ptnMin = pesoIdeal * 2.0;
            r.ptnMax = pesoIdeal * 2.5;
        }
        return r;
    }
}
