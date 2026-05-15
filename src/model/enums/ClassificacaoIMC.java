package model.enums;

/**
 * Classificação do IMC conforme OMS (1997) e OPAS (2002 — idosos).
 */
public enum ClassificacaoIMC {
    DESNUTRICAO("Desnutrição", Double.MIN_VALUE, 18.5, Double.MIN_VALUE, 23.0),
    EUTROFIA("Eutrofia", 18.5, 25.0, 23.0, 28.0),
    SOBREPESO("Sobrepeso", 25.0, 30.0, 28.0, 30.0),
    OBESIDADE_I("Obesidade Grau I", 30.0, 35.0, 30.0, Double.MAX_VALUE),
    OBESIDADE_II("Obesidade Grau II", 35.0, 40.0, Double.MAX_VALUE, Double.MAX_VALUE),
    OBESIDADE_III("Obesidade Grau III", 40.0, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

    private final String descricao;
    private final double omsMin, omsMax;
    private final double opasMin, opasMax;

    ClassificacaoIMC(String descricao, double omsMin, double omsMax, double opasMin, double opasMax) {
        this.descricao = descricao;
        this.omsMin = omsMin;
        this.omsMax = omsMax;
        this.opasMin = opasMin;
        this.opasMax = opasMax;
    }

    public String getDescricao() { return descricao; }

    /**
     * Classifica o IMC pela escala OMS (adultos < 60 anos).
     */
    public static ClassificacaoIMC classificarOMS(double imc) {
        for (ClassificacaoIMC c : values()) {
            if (imc >= c.omsMin && imc < c.omsMax) return c;
        }
        return OBESIDADE_III;
    }

    /**
     * Classifica o IMC pela escala OPAS (idosos ≥ 60 anos).
     */
    public static ClassificacaoIMC classificarOPAS(double imc) {
        for (ClassificacaoIMC c : values()) {
            if (imc >= c.opasMin && imc < c.opasMax) return c;
        }
        return OBESIDADE_I;
    }
}
