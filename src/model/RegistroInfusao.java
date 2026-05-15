package model;

import java.time.LocalDate;

/**
 * Registro de infusão — Prescrito × Infundido.
 * R12: volume recebido ≤ volume prescrito.
 */
public class RegistroInfusao {

    private LocalDate data;
    private double volumePrescrito;  // ml/24h
    private double volumeRecebido;   // ml/24h
    private String causaDeficit;     // jejum, intolerância, etc.
    private boolean dietaSuspensa;

    public RegistroInfusao(LocalDate data, double volumePrescrito, double volumeRecebido) {
        if (volumeRecebido > volumePrescrito && volumePrescrito > 0) {
            throw new IllegalArgumentException("Volume recebido não pode exceder prescrito (R12)");
        }
        this.data = data;
        this.volumePrescrito = volumePrescrito;
        this.volumeRecebido = volumeRecebido;
        this.dietaSuspensa = volumePrescrito == 0;
    }

    /** % recebido = (volume recebido / volume prescrito) × 100 */
    public double getPercentualRecebido() {
        if (dietaSuspensa || volumePrescrito == 0) return 0;
        return (volumeRecebido / volumePrescrito) * 100.0;
    }

    public LocalDate getData() { return data; }
    public double getVolumePrescrito() { return volumePrescrito; }
    public double getVolumeRecebido() { return volumeRecebido; }
    public String getCausaDeficit() { return causaDeficit; }
    public void setCausaDeficit(String c) { this.causaDeficit = c; }
    public boolean isDietaSuspensa() { return dietaSuspensa; }
}
