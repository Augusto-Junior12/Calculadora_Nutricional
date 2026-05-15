package model.enums;

/**
 * Tipo de peso utilizado para cálculos de prescrição.
 * A seleção é SEMPRE explícita pelo nutricionista (R01).
 * O sistema nunca seleciona automaticamente.
 */
public enum TipoPeso {
    ATUAL("Peso Atual"),
    IDEAL("Peso Ideal"),
    AJUSTADO("Peso Ajustado"),
    ESTIMADO("Peso Estimado");

    private final String descricao;

    TipoPeso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() { return descricao; }
}
