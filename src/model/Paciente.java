package model;

import model.enums.Etnia;
import model.enums.FaseClinica;
import model.enums.Genero;
import model.enums.TipoPeso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Entidade central do sistema — representa um paciente internado em UTI.
 *
 * Usa composição (MedidasAntropometricas, CondicoesEspeciais) em vez de herança,
 * respeitando o princípio de composição sobre herança e SRP.
 */
public class Paciente {

    // --- Identificação ---
    private String nome;
    private String codigo;
    private int idade;
    private Genero genero;
    private Etnia etnia;
    private LocalDate dataInternacao;
    private String setorUTI;
    private boolean ativo;

    // --- Composição ---
    private MedidasAntropometricas medidas;
    private CondicoesEspeciais condicoes;

    // --- Estado clínico ---
    private FaseClinica faseClinica;
    private TipoPeso tipoPesoSelecionado; // R01 — seleção explícita
    private String suporteVentilatorio;   // VM, O2, AA

    // --- Necessidades calculadas ---
    private double necessidadeCalorica;   // NC
    private double necessidadeProteica;   // NP

    // --- Históricos (estruturas de dados) ---
    private final List<RegistroDiario> registrosDiarios;
    private final List<RegistroInfusao> registrosInfusao;
    private final TreeMap<LocalDate, AvaliacaoAntropometrica> acompanhamentoAntropometrico;
    private Prescricao prescricaoAtiva; // R18 — apenas uma ativa

    /**
     * Construtor completo para admissão de paciente.
     */
    public Paciente(String nome, String codigo, int idade, Genero genero, Etnia etnia,
                    LocalDate dataInternacao, String setorUTI) {
        this.nome = nome;
        this.codigo = codigo;
        this.idade = idade;
        this.genero = genero;
        this.etnia = etnia;
        this.dataInternacao = dataInternacao;
        this.setorUTI = setorUTI;
        this.ativo = true;

        this.medidas = new MedidasAntropometricas();
        this.condicoes = new CondicoesEspeciais();
        this.registrosDiarios = new ArrayList<>();
        this.registrosInfusao = new ArrayList<>();
        this.acompanhamentoAntropometrico = new TreeMap<>();
    }

    /**
     * Retorna o peso a ser utilizado nos cálculos, conforme o tipo selecionado (R01).
     *
     * @throws IllegalStateException se nenhum tipo de peso foi selecionado
     */
    public double getPesoParaCalculo() {
        if (tipoPesoSelecionado == null) {
            throw new IllegalStateException("Tipo de peso para cálculo não selecionado (R01)");
        }
        switch (tipoPesoSelecionado) {
            case ATUAL: return medidas.getPesoAtual();
            case IDEAL: return medidas.getPesoIdeal();
            case AJUSTADO: return medidas.getPesoAjustado();
            case ESTIMADO: return medidas.getPesoEstimado();
            default: throw new IllegalStateException("Tipo de peso desconhecido");
        }
    }

    /**
     * Define nova prescrição, inativando a anterior (R18).
     */
    public void setPrescricaoAtiva(Prescricao novaPrescricao) {
        if (this.prescricaoAtiva != null) {
            this.prescricaoAtiva.setAtiva(false);
        }
        this.prescricaoAtiva = novaPrescricao;
    }

    /**
     * Adiciona registro diário ao histórico do paciente.
     */
    public void adicionarRegistroDiario(RegistroDiario registro) {
        this.registrosDiarios.add(registro);
    }

    /**
     * Adiciona registro de infusão ao histórico.
     */
    public void adicionarRegistroInfusao(RegistroInfusao registro) {
        this.registrosInfusao.add(registro);
    }

    /**
     * Adiciona avaliação antropométrica ao acompanhamento longitudinal.
     */
    public void adicionarAvaliacaoAntropometrica(LocalDate data, AvaliacaoAntropometrica avaliacao) {
        this.acompanhamentoAntropometrico.put(data, avaliacao);
    }

    /**
     * Inativa o paciente (alta/transferência). Histórico preservado.
     */
    public void inativar() {
        this.ativo = false;
    }

    // --- Getters e Setters ---

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }

    public Etnia getEtnia() { return etnia; }
    public void setEtnia(Etnia etnia) { this.etnia = etnia; }

    public LocalDate getDataInternacao() { return dataInternacao; }
    public void setDataInternacao(LocalDate dataInternacao) { this.dataInternacao = dataInternacao; }

    public String getSetorUTI() { return setorUTI; }
    public void setSetorUTI(String setorUTI) { this.setorUTI = setorUTI; }

    public boolean isAtivo() { return ativo; }

    public MedidasAntropometricas getMedidas() { return medidas; }
    public void setMedidas(MedidasAntropometricas medidas) { this.medidas = medidas; }

    public CondicoesEspeciais getCondicoes() { return condicoes; }
    public void setCondicoes(CondicoesEspeciais condicoes) { this.condicoes = condicoes; }

    public FaseClinica getFaseClinica() { return faseClinica; }
    public void setFaseClinica(FaseClinica faseClinica) { this.faseClinica = faseClinica; }

    public TipoPeso getTipoPesoSelecionado() { return tipoPesoSelecionado; }
    public void setTipoPesoSelecionado(TipoPeso tipoPesoSelecionado) {
        this.tipoPesoSelecionado = tipoPesoSelecionado;
    }

    public String getSuporteVentilatorio() { return suporteVentilatorio; }
    public void setSuporteVentilatorio(String suporteVentilatorio) {
        this.suporteVentilatorio = suporteVentilatorio;
    }

    public double getNecessidadeCalorica() { return necessidadeCalorica; }
    public void setNecessidadeCalorica(double necessidadeCalorica) {
        this.necessidadeCalorica = necessidadeCalorica;
    }

    public double getNecessidadeProteica() { return necessidadeProteica; }
    public void setNecessidadeProteica(double necessidadeProteica) {
        this.necessidadeProteica = necessidadeProteica;
    }

    public List<RegistroDiario> getRegistrosDiarios() { return registrosDiarios; }
    public List<RegistroInfusao> getRegistrosInfusao() { return registrosInfusao; }
    public TreeMap<LocalDate, AvaliacaoAntropometrica> getAcompanhamentoAntropometrico() {
        return acompanhamentoAntropometrico;
    }
    public Prescricao getPrescricaoAtiva() { return prescricaoAtiva; }

    @Override
    public String toString() {
        return codigo + " — " + nome + " (" + (ativo ? "Ativo" : "Inativo") + ")";
    }
}
