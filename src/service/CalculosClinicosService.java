package service;

import model.AlertaClinico;
import model.enums.NivelAlerta;
import util.ConstantesClinicas;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço de cálculos clínicos auxiliares:
 * noradrenalina, balanço nitrogenado, propofol.
 */
public class CalculosClinicosService {

    // --- Noradrenalina ---

    /** Dose em mcg/kg/min para diluição simples (2 ampolas em 250ml). */
    public double calcularNoraSimples(double volumeMlH, double pesoKg) {
        return (volumeMlH * ConstantesClinicas.NORA_CONC_SIMPLES * 1000) / (pesoKg * 60);
    }

    /** Dose em mcg/kg/min para diluição concentrada (4 ampolas em 250ml). */
    public double calcularNoraConcentrada(double volumeMlH, double pesoKg) {
        return (volumeMlH * ConstantesClinicas.NORA_CONC_CONCENTRADA * 1000) / (pesoKg * 60);
    }

    /** Dose livre: ml de nora, nº ampolas, soro. */
    public double calcularNoraLivre(double volumeMlH, double mlNora, int numAmpolas,
                                     double qtdSoro, double pesoKg) {
        double concentracao = (numAmpolas * 4.0) / (qtdSoro + mlNora); // mg/ml
        return (volumeMlH * concentracao * 1000) / (pesoKg * 60);
    }

    public List<AlertaClinico> alertasNora(double doseMcgKgMin) {
        List<AlertaClinico> alertas = new ArrayList<>();
        if (doseMcgKgMin > ConstantesClinicas.NORA_DOSE_MAX_MCG_KG_MIN) {
            alertas.add(new AlertaClinico(NivelAlerta.CRITICO, "NORA",
                String.format("Dose de noradrenalina (%.2f mcg/kg/min) acima do máximo convencional (2.0)", doseMcgKgMin)));
        }
        return alertas;
    }

    // --- Balanço Nitrogenado ---

    /**
     * BN = (PTN ingerida / 6.25) − (ureia urinária 24h × 0.467 + 4)
     * R13: inválido em pacientes em diálise.
     */
    public double calcularBalancoNitrogenado(double ptnIngerida24h, double ureiaUrinaria24h) {
        double nitrogenioIngerido = ptnIngerida24h / ConstantesClinicas.BN_FATOR_CONVERSAO_PTN;
        double nitrogenioExcretado = ureiaUrinaria24h * ConstantesClinicas.BN_FATOR_UREIA
                                     + ConstantesClinicas.BN_PERDAS_INSENSÍVEIS;
        return nitrogenioIngerido - nitrogenioExcretado;
    }

    public String interpretarBN(double bn) {
        if (bn > 0) return "Anabolismo (BN positivo)";
        if (bn == 0) return "Equilíbrio nitrogenado";
        return "Catabolismo proteico ativo (BN negativo)";
    }

    // --- Propofol ---

    /** Calorias do propofol = volume ml/h × 24 × 1.1 kcal/ml. */
    public double calcularCaloriasPropofol(double volumeMlH) {
        return volumeMlH * 24 * ConstantesClinicas.PROPOFOL_KCAL_POR_ML;
    }

    /** Dose de propofol em mg/kg/h (concentração 10mg/ml). */
    public double calcularDosePropofolMgKgH(double volumeMlH, double pesoKg) {
        return (volumeMlH * 10) / pesoKg;
    }

    public List<AlertaClinico> alertasPropofol(double mgKgH) {
        List<AlertaClinico> alertas = new ArrayList<>();
        if (mgKgH > ConstantesClinicas.PROPOFOL_PRIS_MG_KG_H) {
            alertas.add(new AlertaClinico(NivelAlerta.CRITICO, "R14",
                String.format("Propofol %.1f mg/kg/h > 4.0 — RISCO DE PRIS", mgKgH)));
        }
        return alertas;
    }

    // --- Relação kcal não proteicas / g nitrogênio (R15) ---

    public double calcularRelacaoNPN(double kcalTotais, double ptnGramas) {
        double kcalPtn = ptnGramas * 4;
        double kcalNP = kcalTotais - kcalPtn;
        double nGramas = ptnGramas / ConstantesClinicas.BN_FATOR_CONVERSAO_PTN;
        if (nGramas <= 0) return 0;
        return kcalNP / nGramas;
    }
}
