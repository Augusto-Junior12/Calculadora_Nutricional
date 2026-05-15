package util;

import model.enums.Genero;

/**
 * Tabela de Percentil 50 (P50) da Circunferência do Braço (CB) por faixa etária e sexo.
 * Usada no cálculo de % de adequação da CB.
 */
public final class TabelaP50CB {

    private TabelaP50CB() {}

    /** Faixas etárias: 18-18.9, 19-19.9, 20-29.9, 30-39.9, 40-49.9, 50-59.9, 60-69.9, 70-79.9, 80-90.9 */
    private static final double[] FAIXAS_MIN = {18, 19, 20, 30, 40, 50, 60, 70, 80};
    private static final double[] FAIXAS_MAX = {18.9, 19.9, 29.9, 39.9, 49.9, 59.9, 69.9, 79.9, 90.9};

    private static final double[] P50_HOMEM = {30.0, 30.5, 31.8, 32.3, 33.0, 32.6, 32.0, 30.6, 28.9};
    private static final double[] P50_MULHER = {26.7, 26.8, 28.1, 30.3, 31.4, 31.9, 31.4, 29.9, 27.8};

    /**
     * Obtém o P50 da CB para o gênero e idade informados.
     * @return valor P50 em cm, ou -1 se faixa etária não encontrada
     */
    public static double getP50(Genero genero, int idade) {
        double[] tabela = (genero == Genero.MASCULINO) ? P50_HOMEM : P50_MULHER;
        for (int i = 0; i < FAIXAS_MIN.length; i++) {
            if (idade >= FAIXAS_MIN[i] && idade <= FAIXAS_MAX[i]) {
                return tabela[i];
            }
        }
        // Extrapola para as bordas
        if (idade < 18) return tabela[0];
        return tabela[tabela.length - 1];
    }

    /**
     * Calcula a % de adequação da CB.
     * % Adequação = (CB medida / P50) × 100
     */
    public static double calcularAdequacao(double cbMedida, Genero genero, int idade) {
        double p50 = getP50(genero, idade);
        if (p50 <= 0) return 0;
        return (cbMedida / p50) * 100.0;
    }
}
