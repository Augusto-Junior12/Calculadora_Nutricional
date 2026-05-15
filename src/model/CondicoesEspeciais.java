package model;

/**
 * Condições especiais do paciente que influenciam cálculos e regras de negócio.
 * Composição de Paciente — não herança.
 */
public class CondicoesEspeciais {

    private boolean obeso;             // IMC > 30
    private boolean hemodialiseIntermitente;
    private boolean hemodialisesContinua; // CRRT
    private boolean alergiaSoja;       // R11 — bloqueio de fórmulas Soya
    private boolean ileoParalitico;    // R09 — alerta para fórmulas com fibra
    private boolean posOperatorioAbdominal; // R09
    private boolean restricaoHidrica;  // Altera cálculo de hidratação
    private boolean insuficienciaCardiaca;
    private boolean insuficienciaRenalAnurica; // R13 — invalida BN por ureia urinária

    public CondicoesEspeciais() {}

    // --- Getters e Setters ---

    public boolean isObeso() { return obeso; }
    public void setObeso(boolean obeso) { this.obeso = obeso; }

    public boolean isHemodialiseIntermitente() { return hemodialiseIntermitente; }
    public void setHemodialiseIntermitente(boolean hemodialiseIntermitente) {
        this.hemodialiseIntermitente = hemodialiseIntermitente;
    }

    public boolean isHemodialisesContinua() { return hemodialisesContinua; }
    public void setHemodialisesContinua(boolean hemodialisesContinua) {
        this.hemodialisesContinua = hemodialisesContinua;
    }

    public boolean isAlergiaSoja() { return alergiaSoja; }
    public void setAlergiaSoja(boolean alergiaSoja) { this.alergiaSoja = alergiaSoja; }

    public boolean isIleoParalitico() { return ileoParalitico; }
    public void setIleoParalitico(boolean ileoParalitico) { this.ileoParalitico = ileoParalitico; }

    public boolean isPosOperatorioAbdominal() { return posOperatorioAbdominal; }
    public void setPosOperatorioAbdominal(boolean posOperatorioAbdominal) {
        this.posOperatorioAbdominal = posOperatorioAbdominal;
    }

    public boolean isRestricaoHidrica() { return restricaoHidrica; }
    public void setRestricaoHidrica(boolean restricaoHidrica) { this.restricaoHidrica = restricaoHidrica; }

    public boolean isInsuficienciaCardiaca() { return insuficienciaCardiaca; }
    public void setInsuficienciaCardiaca(boolean insuficienciaCardiaca) {
        this.insuficienciaCardiaca = insuficienciaCardiaca;
    }

    public boolean isInsuficienciaRenalAnurica() { return insuficienciaRenalAnurica; }
    public void setInsuficienciaRenalAnurica(boolean insuficienciaRenalAnurica) {
        this.insuficienciaRenalAnurica = insuficienciaRenalAnurica;
    }

    /**
     * Verifica se o paciente possui alguma condição que afete a diálise.
     */
    public boolean emDialise() {
        return hemodialiseIntermitente || hemodialisesContinua;
    }

    /**
     * Verifica se o paciente possui contra-indicação para fibras enterais (R09).
     */
    public boolean contraindicacaoFibra() {
        return ileoParalitico || posOperatorioAbdominal;
    }
}
