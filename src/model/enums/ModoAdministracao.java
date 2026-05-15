package model.enums;

/**
 * Modo de administração da nutrição enteral.
 * Contínuo e intermitente são mutuamente exclusivos (R03).
 */
public enum ModoAdministracao {
    CONTINUO("Contínuo", 22),       // 22 horas/dia padrão UTI
    INTERMITENTE("Intermitente", 6); // 6 vezes/dia padrão (ajustável)

    private final String descricao;
    private final int valorPadrao; // horas (contínuo) ou nº horários (intermitente)

    ModoAdministracao(String descricao, int valorPadrao) {
        this.descricao = descricao;
        this.valorPadrao = valorPadrao;
    }

    public String getDescricao() { return descricao; }
    public int getValorPadrao() { return valorPadrao; }
}
