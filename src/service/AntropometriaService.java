package service;

import model.*;
import model.enums.*;
import util.ConstantesClinicas;
import util.ReferenciaChumlea;
import util.TabelaP50CB;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço de avaliação antropométrica.
 * Calcula estimativas indiretas (Chumlea 1985/1988, Jung 2004, Rabito 2008),
 * peso ideal, peso ajustado, %CB, IMC, % perda de peso, peso corrigido amputados.
 */
public class AntropometriaService {

    /**
     * Calcula altura estimada pela fórmula de Chumlea (1985).
     * R08: válido para 18–90 anos.
     */
    public double calcularAlturaEstimada(Genero genero, double aj, int idade, List<AlertaClinico> alertas) {
        validarIdadeChumlea(idade, alertas);
        double alturaCm = ReferenciaChumlea.estimarAlturaCm(genero, aj, idade);
        return alturaCm / 100.0; // converter para metros
    }

    /**
     * Calcula peso estimado pela fórmula de Chumlea (1988).
     */
    public double calcularPesoEstimado(Genero genero, Etnia etnia, int idade,
                                        double aj, double cb, List<AlertaClinico> alertas) {
        validarIdadeChumlea(idade, alertas);
        return ReferenciaChumlea.estimarPesoKg(genero, etnia, idade, aj, cb);
    }

    /**
     * Calcula peso estimado pela fórmula de Jung (2004).
     */
    public double calcularPesoJung(Genero genero, double aj, double cb, double ca) {
        return ReferenciaChumlea.estimarPesoJung(genero, aj, cb, ca);
    }

    /**
     * Calcula peso estimado pela fórmula de Rabito (2008).
     */
    public double calcularPesoRabito(Genero genero, double aj, double cb, double ca, double cc) {
        return ReferenciaChumlea.estimarPesoRabito(genero, aj, cb, ca, cc);
    }

    /**
     * Calcula peso ideal: altura² × IMC de referência.
     * Homem: IMC 22 | Mulher: IMC 20.8
     */
    public double calcularPesoIdeal(double alturaMetros, Genero genero) {
        return Math.pow(alturaMetros, 2) * genero.getImcReferencia();
    }

    /**
     * Calcula peso ideal com IMC neutro (25).
     */
    public double calcularPesoIdealIMC25(double alturaMetros) {
        return Math.pow(alturaMetros, 2) * 25.0;
    }

    /**
     * Calcula peso ajustado para obesos.
     * Peso ajustado = (peso atual − peso ideal) × 0.25 + peso ideal
     */
    public double calcularPesoAjustado(double pesoAtual, double pesoIdeal) {
        return (pesoAtual - pesoIdeal) * 0.25 + pesoIdeal;
    }

    /** Calcula IMC. */
    public double calcularIMC(double pesoKg, double alturaMetros) {
        if (alturaMetros <= 0) throw new IllegalArgumentException("Altura deve ser positiva");
        return pesoKg / Math.pow(alturaMetros, 2);
    }

    /** Classifica o IMC por OMS ou OPAS conforme a idade. */
    public ClassificacaoIMC classificarIMC(double imc, int idade) {
        return (idade >= 60) ? ClassificacaoIMC.classificarOPAS(imc) : ClassificacaoIMC.classificarOMS(imc);
    }

    /** Calcula % de adequação da CB e classifica. */
    public double calcularAdequacaoCB(double cbMedida, Genero genero, int idade) {
        return TabelaP50CB.calcularAdequacao(cbMedida, genero, idade);
    }

    public ClassificacaoCB classificarCB(double percentualAdequacao) {
        return ClassificacaoCB.classificar(percentualAdequacao);
    }

    /** Calcula % de perda de peso: ((peso usual − peso atual) / peso usual) × 100. */
    public double calcularPerdaPeso(double pesoAtual, double pesoUsual) {
        if (pesoUsual <= 0) return 0;
        return ((pesoUsual - pesoAtual) / pesoUsual) * 100.0;
    }

    /**
     * Calcula peso corrigido para amputados (Osterkamp, 1995).
     * Peso corrigido = peso pré-amputação × (1 − % do membro / 100)
     */
    public double calcularPesoAmputado(double pesoPreAmputacao, double percentualMembro) {
        return pesoPreAmputacao * (1 - percentualMembro / 100.0);
    }

    /** Verifica depleção muscular pela CP (Barbosa-Silva, 2016). */
    public boolean verificarDepleçaoMuscular(double cp, Genero genero) {
        if (genero == Genero.MASCULINO) return cp < 34;
        return cp < 33;
    }

    /**
     * Executa avaliação antropométrica completa do paciente.
     * Preenche os campos calculados nas MedidasAntropometricas.
     */
    public List<AlertaClinico> avaliarCompleto(Paciente paciente) {
        List<AlertaClinico> alertas = new ArrayList<>();
        MedidasAntropometricas m = paciente.getMedidas();
        Genero g = paciente.getGenero();
        int idade = paciente.getIdade();

        // Altura estimada (se AJ disponível)
        if (m.getAj() > 0) {
            double altEst = calcularAlturaEstimada(g, m.getAj(), idade, alertas);
            m.setAlturaEstimada(altEst);
        }

        // Peso estimado (se AJ e CB disponíveis)
        if (m.getAj() > 0 && m.getCb() > 0 && paciente.getEtnia() != null) {
            double pesoEst = calcularPesoEstimado(g, paciente.getEtnia(), idade, m.getAj(), m.getCb(), alertas);
            m.setPesoEstimado(pesoEst);
        }

        // Peso ideal
        double altura = m.getAlturaMelhorEstimativa();
        if (altura > 0) {
            double pi = calcularPesoIdeal(altura, g);
            m.setPesoIdeal(pi);

            // IMC
            double peso = m.getPesoAtual() > 0 ? m.getPesoAtual() : m.getPesoEstimado();
            if (peso > 0) {
                double imc = calcularIMC(peso, altura);
                m.setImc(imc);

                // Peso ajustado (se obeso)
                if (imc > 30 || paciente.getCondicoes().isObeso()) {
                    double pa = calcularPesoAjustado(peso, pi);
                    m.setPesoAjustado(pa);
                    paciente.getCondicoes().setObeso(true);
                }
            }
        }

        // Validações de plausibilidade
        validarPlausibilidade(m, alertas);

        return alertas;
    }

    private void validarIdadeChumlea(int idade, List<AlertaClinico> alertas) {
        if (idade < ConstantesClinicas.CHUMLEA_IDADE_MIN || idade > ConstantesClinicas.CHUMLEA_IDADE_MAX) {
            alertas.add(new AlertaClinico(NivelAlerta.AVISO, "R08",
                "Fórmulas de Chumlea validadas para 18-90 anos. Idade: " + idade,
                "Idade", idade));
        }
    }

    private void validarPlausibilidade(MedidasAntropometricas m, List<AlertaClinico> alertas) {
        if (m.getCb() > 60) alertas.add(new AlertaClinico(NivelAlerta.AVISO, "PLAUS", "CB > 60 cm — valor implausível", "CB", m.getCb()));
        if (m.getCp() > 0 && m.getCp() < 15) alertas.add(new AlertaClinico(NivelAlerta.AVISO, "PLAUS", "CP < 15 cm — valor implausível", "CP", m.getCp()));
        if (m.getAj() > 0 && (m.getAj() < 30 || m.getAj() > 80)) alertas.add(new AlertaClinico(NivelAlerta.AVISO, "PLAUS", "AJ fora de 30-80 cm — valor implausível", "AJ", m.getAj()));
        double peso = m.getPesoAtual() > 0 ? m.getPesoAtual() : m.getPesoEstimado();
        if (peso > 0 && (peso < 20 || peso > 300)) alertas.add(new AlertaClinico(NivelAlerta.AVISO, "PLAUS", "Peso fora de 20-300 kg — valor implausível", "Peso", peso));
    }
}
