package service;

import util.ConstantesClinicas;

/**
 * Serviço de cálculo de hidratação.
 * 25-30 ml/kg/dia, água via TNE, e distribuição de flushes.
 */
public class HidratacaoService {

    public double calcularNecessidadeMinima(double pesoKg) {
        return pesoKg * ConstantesClinicas.HIDRATACAO_ML_KG_MIN;
    }

    public double calcularNecessidadeIdeal(double pesoKg) {
        return pesoKg * ConstantesClinicas.HIDRATACAO_ML_KG_MAX;
    }

    /**
     * Calcula água fornecida pela dieta enteral.
     * @param volumeDietaMl volume total de dieta administrada (ml/dia)
     * @param densidadeFórmula kcal/ml da fórmula
     */
    public double calcularAguaViaTNE(double volumeDietaMl, double densidadeFormula) {
        double aguaPorLitro;
        if (densidadeFormula <= 1.0) aguaPorLitro = 850;
        else if (densidadeFormula <= 1.25) aguaPorLitro = 800;
        else if (densidadeFormula <= 1.5) aguaPorLitro = 750;
        else aguaPorLitro = 700;
        return (volumeDietaMl / 1000.0) * aguaPorLitro;
    }

    /**
     * Calcula volume de água extra a administrar via sonda.
     */
    public double calcularAguaExtra(double necessidadeTotal, double aguaViaTNE) {
        return Math.max(0, necessidadeTotal - aguaViaTNE);
    }

    /**
     * Calcula volume por flush.
     * @param aguaExtra volume total extra (ml)
     * @param numFlushes nº de vezes ao dia (4, 5, 6 ou 8)
     */
    public double calcularVolumePorFlush(double aguaExtra, int numFlushes) {
        if (numFlushes <= 0) return 0;
        return aguaExtra / numFlushes;
    }

    /**
     * % de água na dieta conforme densidade.
     */
    public double getPercentualAguaDieta(double densidade) {
        if (densidade <= 1.0) return 85;
        if (densidade <= 1.25) return 80;
        if (densidade <= 1.5) return 75;
        return 70;
    }
}
