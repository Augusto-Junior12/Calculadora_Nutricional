package model.enums;

/**
 * Nível de severidade dos alertas clínicos.
 */
public enum NivelAlerta {
    INFO("Informação"),
    AVISO("Aviso"),
    ALERTA("Alerta"),
    CRITICO("Crítico"),
    BLOQUEIO("Bloqueio");

    private final String descricao;

    NivelAlerta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() { return descricao; }
}
