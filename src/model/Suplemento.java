package model;

import model.enums.IndicacaoSuplemento;

/**
 * Suplemento oral ou módulo nutricional.
 * Valores por porção (conforme apresentação comercial).
 */
public class Suplemento {

    private final String nome;
    private final double quantidadeGramas;
    private final double kcal, ptn, cho, acucar, lip;
    private final double sodio, potassio, fosforo, ferro, fibras;
    private final double osmolaridade;
    private final String observacoes;
    private final IndicacaoSuplemento indicacao;
    private final boolean contemArginina, contemOmega3, contemNucleotideos;
    private final boolean contemCarnitina, contemProlina, contemColina;

    public Suplemento(String nome, double qtd, double kcal, double ptn, double cho,
                      double acucar, double lip, double sodio, double potassio, double fosforo,
                      double ferro, double fibras, double osmolaridade, String obs,
                      IndicacaoSuplemento indicacao, boolean arginina, boolean omega3,
                      boolean nucleotideos, boolean carnitina, boolean prolina, boolean colina) {
        this.nome = nome; this.quantidadeGramas = qtd; this.kcal = kcal;
        this.ptn = ptn; this.cho = cho; this.acucar = acucar; this.lip = lip;
        this.sodio = sodio; this.potassio = potassio; this.fosforo = fosforo;
        this.ferro = ferro; this.fibras = fibras; this.osmolaridade = osmolaridade;
        this.observacoes = obs; this.indicacao = indicacao;
        this.contemArginina = arginina; this.contemOmega3 = omega3;
        this.contemNucleotideos = nucleotideos; this.contemCarnitina = carnitina;
        this.contemProlina = prolina; this.contemColina = colina;
    }

    public String getNome() { return nome; }
    public double getQuantidadeGramas() { return quantidadeGramas; }
    public double getKcal() { return kcal; }
    public double getPtn() { return ptn; }
    public double getCho() { return cho; }
    public double getAcucar() { return acucar; }
    public double getLip() { return lip; }
    public double getSodio() { return sodio; }
    public double getPotassio() { return potassio; }
    public double getFosforo() { return fosforo; }
    public double getFerro() { return ferro; }
    public double getFibras() { return fibras; }
    public double getOsmolaridade() { return osmolaridade; }
    public String getObservacoes() { return observacoes; }
    public IndicacaoSuplemento getIndicacao() { return indicacao; }
    public boolean isContemArginina() { return contemArginina; }
    public boolean isContemOmega3() { return contemOmega3; }
    public boolean isContemNucleotideos() { return contemNucleotideos; }
    public boolean isContemCarnitina() { return contemCarnitina; }
    public boolean isContemProlina() { return contemProlina; }
    public boolean isContemColina() { return contemColina; }

    @Override
    public String toString() {
        return String.format("%s (%.0f kcal | %.1fg PTN)", nome, kcal, ptn);
    }
}
