package model.enums;

/**
 * Indicação principal do suplemento.
 */
public enum IndicacaoSuplemento {
    HIPERPROTEICO("Hiperproteico"),
    CONTROLE_GLICEMICO("Controle Glicêmico"),
    IMUNOMODULACAO("Imunomodulação"),
    ALTA_DENSIDADE("Alta Densidade"),
    RENAL("Renal"),
    MODULO_PROTEICO("Módulo Proteico"),
    MODULO_CALORICO("Módulo Calórico"),
    PADRAO("Padrão");

    private final String descricao;

    IndicacaoSuplemento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() { return descricao; }
}
