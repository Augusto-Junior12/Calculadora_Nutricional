package model.enums;

/**
 * Fase clínica do paciente em UTI.
 * Determina as faixas de necessidades calóricas e proteicas.
 * Seleção é SEMPRE manual pelo nutricionista (R02).
 */
public enum FaseClinica {
    AGUDA("Fase Aguda", 15.0, 20.0, 1.2, 1.5),
    REABILITACAO("Reabilitação", 25.0, 30.0, 1.5, 2.0);

    private final String descricao;
    private final double kcalKgMin;
    private final double kcalKgMax;
    private final double ptnKgMin;
    private final double ptnKgMax;

    FaseClinica(String descricao, double kcalKgMin, double kcalKgMax, double ptnKgMin, double ptnKgMax) {
        this.descricao = descricao;
        this.kcalKgMin = kcalKgMin;
        this.kcalKgMax = kcalKgMax;
        this.ptnKgMin = ptnKgMin;
        this.ptnKgMax = ptnKgMax;
    }

    public String getDescricao() { return descricao; }
    public double getKcalKgMin() { return kcalKgMin; }
    public double getKcalKgMax() { return kcalKgMax; }
    public double getPtnKgMin() { return ptnKgMin; }
    public double getPtnKgMax() { return ptnKgMax; }
}
