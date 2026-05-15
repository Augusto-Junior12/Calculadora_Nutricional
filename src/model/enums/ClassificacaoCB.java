package model.enums;

/**
 * Classificação da % de adequação da CB (Circunferência do Braço).
 */
public enum ClassificacaoCB {
    DESNUTRICAO_GRAVE("Desnutrição Grave", 0, 70),
    DESNUTRICAO_MODERADA("Desnutrição Moderada", 70, 80),
    DESNUTRICAO_LEVE("Desnutrição Leve", 80, 90),
    EUTROFIA("Eutrofia", 90, 110),
    SOBREPESO("Sobrepeso", 110, 120),
    OBESIDADE("Obesidade", 120, Double.MAX_VALUE);

    private final String descricao;
    private final double limiteInferior;
    private final double limiteSuperior;

    ClassificacaoCB(String descricao, double limiteInferior, double limiteSuperior) {
        this.descricao = descricao;
        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
    }

    public String getDescricao() { return descricao; }

    /**
     * Classifica a % de adequação da CB.
     */
    public static ClassificacaoCB classificar(double percentualAdequacao) {
        for (ClassificacaoCB c : values()) {
            if (percentualAdequacao >= c.limiteInferior && percentualAdequacao < c.limiteSuperior) {
                return c;
            }
        }
        return OBESIDADE;
    }
}
