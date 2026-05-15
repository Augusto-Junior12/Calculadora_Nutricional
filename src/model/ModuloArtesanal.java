package model;

/**
 * Módulo artesanal para TNE em sistema aberto.
 * Cada módulo tem uma medida padrão com composição fixa.
 */
public class ModuloArtesanal {

    private final String nome;
    private final double medidaPadrao;   // g ou ml
    private final String unidadeMedida;  // "g" ou "ml"
    private final double kcalPorMedida;
    private final double choPorMedida;
    private final double ptnPorMedida;
    private final double lipPorMedida;
    private final double tamanhoLata;    // g (apresentação comercial)

    public ModuloArtesanal(String nome, double medidaPadrao, String unidadeMedida,
                           double kcal, double cho, double ptn, double lip, double tamanhoLata) {
        this.nome = nome;
        this.medidaPadrao = medidaPadrao;
        this.unidadeMedida = unidadeMedida;
        this.kcalPorMedida = kcal;
        this.choPorMedida = cho;
        this.ptnPorMedida = ptn;
        this.lipPorMedida = lip;
        this.tamanhoLata = tamanhoLata;
    }

    public String getNome() { return nome; }
    public double getMedidaPadrao() { return medidaPadrao; }
    public String getUnidadeMedida() { return unidadeMedida; }
    public double getKcalPorMedida() { return kcalPorMedida; }
    public double getChoPorMedida() { return choPorMedida; }
    public double getPtnPorMedida() { return ptnPorMedida; }
    public double getLipPorMedida() { return lipPorMedida; }
    public double getTamanhoLata() { return tamanhoLata; }
}
