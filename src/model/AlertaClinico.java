package model;

import model.enums.NivelAlerta;

/**
 * Alerta clínico gerado pelo sistema.
 * Centraliza alertas de regras de negócio (R01–R18) e validações fisiológicas.
 */
public class AlertaClinico {

    private final NivelAlerta nivel;
    private final String codigoRegra;  // ex: "R07", "R11"
    private final String mensagem;
    private final String parametro;    // campo que gerou o alerta
    private final double valor;        // valor que disparou o alerta

    public AlertaClinico(NivelAlerta nivel, String codigoRegra, String mensagem) {
        this(nivel, codigoRegra, mensagem, "", 0);
    }

    public AlertaClinico(NivelAlerta nivel, String codigoRegra, String mensagem,
                         String parametro, double valor) {
        this.nivel = nivel;
        this.codigoRegra = codigoRegra;
        this.mensagem = mensagem;
        this.parametro = parametro;
        this.valor = valor;
    }

    public NivelAlerta getNivel() { return nivel; }
    public String getCodigoRegra() { return codigoRegra; }
    public String getMensagem() { return mensagem; }
    public String getParametro() { return parametro; }
    public double getValor() { return valor; }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", nivel, codigoRegra, mensagem);
    }
}
