package model;

import java.time.LocalDate;

/**
 * Registro clínico diário do paciente em UTI.
 * Corresponde a uma linha da ficha semanal.
 */
public class RegistroDiario {

    private LocalDate data;
    private String dietaEmUso;
    private double hgt;         // Glicemia (mg/dL)
    private String suporteVent; // VM / O2 / AA
    private double fosforo;     // P (mg/dL)
    private double magnesio;    // Mg (mg/dL)
    private double potassio;    // K (mEq/L)
    private double sodio;       // Na (mEq/L)
    private double lactato;     // mmol/L
    private double pcr;         // Proteína C-reativa
    private double ph;
    private double pco2;        // mmHg
    private double hco3;        // mEq/L
    private double balancoHidrico; // ml (pode ser negativo)
    private double diurese;     // ml/24h
    private String evacuacao;
    private double percentualNERecebido;

    public RegistroDiario(LocalDate data) {
        this.data = data;
    }

    // --- Getters e Setters ---
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public String getDietaEmUso() { return dietaEmUso; }
    public void setDietaEmUso(String d) { this.dietaEmUso = d; }
    public double getHgt() { return hgt; }
    public void setHgt(double hgt) { this.hgt = hgt; }
    public String getSuporteVent() { return suporteVent; }
    public void setSuporteVent(String s) { this.suporteVent = s; }
    public double getFosforo() { return fosforo; }
    public void setFosforo(double f) { this.fosforo = f; }
    public double getMagnesio() { return magnesio; }
    public void setMagnesio(double m) { this.magnesio = m; }
    public double getPotassio() { return potassio; }
    public void setPotassio(double k) { this.potassio = k; }
    public double getSodio() { return sodio; }
    public void setSodio(double na) { this.sodio = na; }
    public double getLactato() { return lactato; }
    public void setLactato(double l) { this.lactato = l; }
    public double getPcr() { return pcr; }
    public void setPcr(double pcr) { this.pcr = pcr; }
    public double getPh() { return ph; }
    public void setPh(double ph) { this.ph = ph; }
    public double getPco2() { return pco2; }
    public void setPco2(double pco2) { this.pco2 = pco2; }
    public double getHco3() { return hco3; }
    public void setHco3(double hco3) { this.hco3 = hco3; }
    public double getBalancoHidrico() { return balancoHidrico; }
    public void setBalancoHidrico(double bh) { this.balancoHidrico = bh; }
    public double getDiurese() { return diurese; }
    public void setDiurese(double d) { this.diurese = d; }
    public String getEvacuacao() { return evacuacao; }
    public void setEvacuacao(String e) { this.evacuacao = e; }
    public double getPercentualNERecebido() { return percentualNERecebido; }
    public void setPercentualNERecebido(double p) { this.percentualNERecebido = p; }
}
