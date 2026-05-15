package model.enums;

/**
 * Gênero do paciente.
 * Utilizado nas fórmulas de Chumlea e cálculo de peso ideal.
 */
public enum Genero {
    MASCULINO("Masculino", 22.0),
    FEMININO("Feminino", 20.8);

    private final String descricao;
    private final double imcReferencia; // IMC de referência para peso ideal

    Genero(String descricao, double imcReferencia) {
        this.descricao = descricao;
        this.imcReferencia = imcReferencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getImcReferencia() {
        return imcReferencia;
    }

    /**
     * Converte string "M" ou "F" para o enum correspondente.
     */
    public static Genero fromCodigo(String codigo) {
        if (codigo == null) throw new IllegalArgumentException("Código de gênero não pode ser nulo");
        switch (codigo.toUpperCase().trim()) {
            case "M": case "MASCULINO": return MASCULINO;
            case "F": case "FEMININO": return FEMININO;
            default: throw new IllegalArgumentException("Gênero inválido: " + codigo);
        }
    }
}
