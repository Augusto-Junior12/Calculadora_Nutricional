package model.enums;

/**
 * Etnia do paciente — usada na estratificação das fórmulas de Chumlea (1985/1988).
 */
public enum Etnia {
    BRANCO("Branco"),
    NEGRO("Negro");

    private final String descricao;

    Etnia(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
