package model;

import model.enums.ModoAdministracao;
import java.time.LocalDate;

/**
 * Prescrição de nutrição enteral — resultado do cálculo de prescrição.
 * R03: Contínuo e Intermitente são mutuamente exclusivos.
 * R04: Toda prescrição inicia com progressão de 4 dias.
 * R18: Apenas uma prescrição ativa por paciente.
 */
public class Prescricao {

    private final LocalDate dataPrescricao;
    private final ModoAdministracao modo;
    private final FormulaEnteral formula;
    private boolean ativa;

    // Parâmetros de entrada
    private double pesoReferencia;  // kg
    private double metaCalorica;    // VCT alvo (kcal)
    private double metaProteica;    // PTN alvo (g)
    private int numHorasOuHorarios; // 22h (contínuo) ou nº horários (intermitente)

    // Resultados calculados
    private double volumeMlH;         // ml/h (contínuo) ou ml/horário (intermitente)
    private double volumeTotal;       // ml/24h
    private double kcalTotais;
    private double ptnTotal;          // g
    private double kcalPorKg;
    private double ptnPorKg;
    private double percentualVCT;     // % do VCT atingido
    private double percentualPTN;     // % da PTN atingida
    private double volumePleno;       // ml para 100% do VCT
    private double ptnPlena;          // g de PTN no volume pleno
    private double ptnSuplementar;    // g faltantes de PTN

    // Progressão de 4 dias
    private double[] volumeProgressao = new double[4];  // 25%, 50%, 75%, 100%
    private double[] kcalProgressao = new double[4];

    // Dia atual da progressão (1 a 4)
    private int diaProgressaoAtual = 1;
    private String justificativaPularProgressao; // R04

    public Prescricao(LocalDate data, ModoAdministracao modo, FormulaEnteral formula) {
        this.dataPrescricao = data;
        this.modo = modo;
        this.formula = formula;
        this.ativa = true;
    }

    // --- Getters e Setters ---

    public LocalDate getDataPrescricao() { return dataPrescricao; }
    public ModoAdministracao getModo() { return modo; }
    public FormulaEnteral getFormula() { return formula; }
    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }

    public double getPesoReferencia() { return pesoReferencia; }
    public void setPesoReferencia(double pesoReferencia) { this.pesoReferencia = pesoReferencia; }

    public double getMetaCalorica() { return metaCalorica; }
    public void setMetaCalorica(double metaCalorica) { this.metaCalorica = metaCalorica; }

    public double getMetaProteica() { return metaProteica; }
    public void setMetaProteica(double metaProteica) { this.metaProteica = metaProteica; }

    public int getNumHorasOuHorarios() { return numHorasOuHorarios; }
    public void setNumHorasOuHorarios(int n) { this.numHorasOuHorarios = n; }

    public double getVolumeMlH() { return volumeMlH; }
    public void setVolumeMlH(double v) { this.volumeMlH = v; }

    public double getVolumeTotal() { return volumeTotal; }
    public void setVolumeTotal(double v) { this.volumeTotal = v; }

    public double getKcalTotais() { return kcalTotais; }
    public void setKcalTotais(double v) { this.kcalTotais = v; }

    public double getPtnTotal() { return ptnTotal; }
    public void setPtnTotal(double v) { this.ptnTotal = v; }

    public double getKcalPorKg() { return kcalPorKg; }
    public void setKcalPorKg(double v) { this.kcalPorKg = v; }

    public double getPtnPorKg() { return ptnPorKg; }
    public void setPtnPorKg(double v) { this.ptnPorKg = v; }

    public double getPercentualVCT() { return percentualVCT; }
    public void setPercentualVCT(double v) { this.percentualVCT = v; }

    public double getPercentualPTN() { return percentualPTN; }
    public void setPercentualPTN(double v) { this.percentualPTN = v; }

    public double getVolumePleno() { return volumePleno; }
    public void setVolumePleno(double v) { this.volumePleno = v; }

    public double getPtnPlena() { return ptnPlena; }
    public void setPtnPlena(double v) { this.ptnPlena = v; }

    public double getPtnSuplementar() { return ptnSuplementar; }
    public void setPtnSuplementar(double v) { this.ptnSuplementar = v; }

    public double[] getVolumeProgressao() { return volumeProgressao; }
    public void setVolumeProgressao(double[] v) { this.volumeProgressao = v; }

    public double[] getKcalProgressao() { return kcalProgressao; }
    public void setKcalProgressao(double[] v) { this.kcalProgressao = v; }

    public int getDiaProgressaoAtual() { return diaProgressaoAtual; }
    public void setDiaProgressaoAtual(int dia) { this.diaProgressaoAtual = dia; }

    public String getJustificativaPularProgressao() { return justificativaPularProgressao; }
    public void setJustificativaPularProgressao(String j) { this.justificativaPularProgressao = j; }
}
