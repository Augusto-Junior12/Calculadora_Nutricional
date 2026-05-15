package model;

import java.time.LocalDate;

/**
 * Avaliação antropométrica para acompanhamento longitudinal.
 * Armazenada em TreeMap<LocalDate, AvaliacaoAntropometrica> no Paciente.
 */
public class AvaliacaoAntropometrica {

    private final LocalDate data;
    private double cb;    // cm
    private double cp;    // cm
    private double peso;  // kg
    private double dct;   // mm
    private double aj;    // cm

    public AvaliacaoAntropometrica(LocalDate data) {
        this.data = data;
    }

    public LocalDate getData() { return data; }
    public double getCb() { return cb; }
    public void setCb(double cb) { this.cb = cb; }
    public double getCp() { return cp; }
    public void setCp(double cp) { this.cp = cp; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public double getDct() { return dct; }
    public void setDct(double dct) { this.dct = dct; }
    public double getAj() { return aj; }
    public void setAj(double aj) { this.aj = aj; }
}
