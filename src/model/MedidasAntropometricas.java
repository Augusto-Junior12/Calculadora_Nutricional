package model;

/**
 * Medidas antropométricas do paciente.
 * Separado de Paciente por SRP — responsabilidade única de armazenar medidas físicas.
 */
public class MedidasAntropometricas {

    private double pesoAtual;       // kg
    private double pesoIdeal;       // kg (calculado)
    private double pesoAjustado;    // kg (calculado para obesos)
    private double pesoEstimado;    // kg (Chumlea 1988)
    private double altura;          // metros
    private double alturaEstimada;  // metros (Chumlea 1985)
    private double imc;             // kg/m²
    private double cb;              // Circunferência do braço (cm)
    private double cp;              // Circunferência da panturrilha (cm)
    private double ca;              // Circunferência abdominal (cm)
    private double cc;              // Circunferência da coxa (cm)
    private double aj;              // Altura do joelho (cm)
    private double dct;             // Dobra cutânea triciptal (mm)

    public MedidasAntropometricas() {}

    // --- Getters e Setters com validação básica ---

    public double getPesoAtual() { return pesoAtual; }
    public void setPesoAtual(double pesoAtual) {
        if (pesoAtual < 0) throw new IllegalArgumentException("Peso atual não pode ser negativo");
        this.pesoAtual = pesoAtual;
    }

    public double getPesoIdeal() { return pesoIdeal; }
    public void setPesoIdeal(double pesoIdeal) { this.pesoIdeal = pesoIdeal; }

    public double getPesoAjustado() { return pesoAjustado; }
    public void setPesoAjustado(double pesoAjustado) { this.pesoAjustado = pesoAjustado; }

    public double getPesoEstimado() { return pesoEstimado; }
    public void setPesoEstimado(double pesoEstimado) { this.pesoEstimado = pesoEstimado; }

    public double getAltura() { return altura; }
    public void setAltura(double altura) {
        if (altura < 0) throw new IllegalArgumentException("Altura não pode ser negativa");
        this.altura = altura;
    }

    public double getAlturaEstimada() { return alturaEstimada; }
    public void setAlturaEstimada(double alturaEstimada) { this.alturaEstimada = alturaEstimada; }

    public double getImc() { return imc; }
    public void setImc(double imc) { this.imc = imc; }

    public double getCb() { return cb; }
    public void setCb(double cb) {
        if (cb < 0) throw new IllegalArgumentException("CB não pode ser negativa");
        this.cb = cb;
    }

    public double getCp() { return cp; }
    public void setCp(double cp) {
        if (cp < 0) throw new IllegalArgumentException("CP não pode ser negativa");
        this.cp = cp;
    }

    public double getCa() { return ca; }
    public void setCa(double ca) { this.ca = ca; }

    public double getCc() { return cc; }
    public void setCc(double cc) { this.cc = cc; }

    public double getAj() { return aj; }
    public void setAj(double aj) {
        if (aj < 0) throw new IllegalArgumentException("AJ não pode ser negativa");
        this.aj = aj;
    }

    public double getDct() { return dct; }
    public void setDct(double dct) { this.dct = dct; }

    /**
     * Retorna o melhor valor de altura disponível (direta ou estimada).
     */
    public double getAlturaMelhorEstimativa() {
        return altura > 0 ? altura : alturaEstimada;
    }

    /**
     * Retorna se todas as medidas mínimas para avaliação estão preenchidas.
     */
    public boolean possuiMedidasMinimas() {
        return (pesoAtual > 0 || pesoEstimado > 0) && (altura > 0 || alturaEstimada > 0);
    }
}
