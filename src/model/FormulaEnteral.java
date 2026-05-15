package model;

/**
 * Fórmula enteral industrializada.
 * Atributos fixos definidos pelo fabricante — imutáveis após criação.
 * O repositório carrega as 51 fórmulas catalogadas.
 */
public class FormulaEnteral {

    private final String nome;
    private final double densidade;    // kcal/ml
    private final double ptnPorLitro;  // g/L
    private final double choPorLitro;  // g/L
    private final double lipPorLitro;  // g/L
    private final double fibrasPorLitro; // g/L (0 se ausente)
    private final double potassio;     // mg/L
    private final double osmolaridade; // mOsm/L
    private final boolean contemFibra;
    private final boolean contemSoja;
    private final String indicacoesEspeciais; // texto livre
    private final String observacoes;

    public FormulaEnteral(String nome, double densidade, double ptnPorLitro,
                          double choPorLitro, double lipPorLitro, double fibrasPorLitro,
                          double potassio, double osmolaridade,
                          String indicacoesEspeciais, String observacoes) {
        this.nome = nome;
        this.densidade = densidade;
        this.ptnPorLitro = ptnPorLitro;
        this.choPorLitro = choPorLitro;
        this.lipPorLitro = lipPorLitro;
        this.fibrasPorLitro = fibrasPorLitro;
        this.potassio = potassio;
        this.osmolaridade = osmolaridade;
        this.contemFibra = fibrasPorLitro > 0;
        this.contemSoja = nome.toLowerCase().contains("soya") || nome.toLowerCase().contains("soja");
        this.indicacoesEspeciais = indicacoesEspeciais;
        this.observacoes = observacoes;
    }

    /**
     * Calcula a quantidade de água por litro de fórmula, conforme a densidade.
     * 1.0 kcal/ml → 850 ml | 1.2 → 800 ml | 1.5 → 750 ml | 2.0 → 700 ml
     */
    public double getAguaPorLitro() {
        if (densidade <= 1.0) return 850;
        if (densidade <= 1.25) return 800;
        if (densidade <= 1.5) return 750;
        return 700;
    }

    /**
     * Faixa de densidade simplificada para agrupamento.
     */
    public String getFaixaDensidade() {
        if (densidade <= 1.0) return "1.0 kcal/ml";
        if (densidade <= 1.25) return "1.2 kcal/ml";
        if (densidade <= 1.5) return "1.5 kcal/ml";
        return "2.0 kcal/ml";
    }

    // --- Getters (imutável) ---

    public String getNome() { return nome; }
    public double getDensidade() { return densidade; }
    public double getPtnPorLitro() { return ptnPorLitro; }
    public double getChoPorLitro() { return choPorLitro; }
    public double getLipPorLitro() { return lipPorLitro; }
    public double getFibrasPorLitro() { return fibrasPorLitro; }
    public double getPotassio() { return potassio; }
    public double getOsmolaridade() { return osmolaridade; }
    public boolean isContemFibra() { return contemFibra; }
    public boolean isContemSoja() { return contemSoja; }
    public String getIndicacoesEspeciais() { return indicacoesEspeciais; }
    public String getObservacoes() { return observacoes; }

    @Override
    public String toString() {
        return String.format("%s (%.2f kcal/ml | PTN %sg/L)", nome, densidade, (int) ptnPorLitro);
    }
}
