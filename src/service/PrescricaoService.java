package service;

import model.AlertaClinico;
import model.FormulaEnteral;
import model.Prescricao;
import model.enums.ModoAdministracao;
import model.enums.NivelAlerta;
import util.ConstantesClinicas;
import java.time.LocalDate;
import java.util.List;

/**
 * Serviço de prescrição de nutrição enteral.
 * Modos contínuo e intermitente. Progressão R04, suplementação R05, alertas R10.
 */
public class PrescricaoService {

    public Prescricao calcularContinuo(FormulaEnteral formula, double peso, double vctAlvo,
                                        double ptnAlvo, int numHoras, List<AlertaClinico> alertas) {
        Prescricao p = new Prescricao(LocalDate.now(), ModoAdministracao.CONTINUO, formula);
        p.setPesoReferencia(peso); p.setMetaCalorica(vctAlvo);
        p.setMetaProteica(ptnAlvo); p.setNumHorasOuHorarios(numHoras);
        double mlH = vctAlvo / (formula.getDensidade() * numHoras);
        p.setVolumeMlH(mlH);
        double vt = mlH * numHoras; p.setVolumeTotal(vt);
        double kcal = vt * formula.getDensidade(); p.setKcalTotais(kcal);
        double ptn = (vt / 1000.0) * formula.getPtnPorLitro(); p.setPtnTotal(ptn);
        p.setKcalPorKg(kcal / peso); p.setPtnPorKg(ptn / peso);
        p.setPercentualVCT((kcal / vctAlvo) * 100.0);
        p.setPercentualPTN((ptn / ptnAlvo) * 100.0);
        double volPleno = vctAlvo / formula.getDensidade(); p.setVolumePleno(volPleno);
        double ptnPlena = (volPleno / 1000.0) * formula.getPtnPorLitro(); p.setPtnPlena(ptnPlena);
        p.setPtnSuplementar(Math.max(0, ptnAlvo - ptnPlena));
        calcularProgressao(p, vctAlvo, formula.getDensidade(), numHoras);
        if (formula.getOsmolaridade() > ConstantesClinicas.OSMOL_MAX_SONDA)
            alertas.add(new AlertaClinico(NivelAlerta.AVISO, "R10", "Osmolaridade > 450 mOsm/L"));
        return p;
    }

    public Prescricao calcularIntermitente(FormulaEnteral formula, double peso, double vctAlvo,
                                            double ptnAlvo, int numHorarios, List<AlertaClinico> alertas) {
        Prescricao p = new Prescricao(LocalDate.now(), ModoAdministracao.INTERMITENTE, formula);
        p.setPesoReferencia(peso); p.setMetaCalorica(vctAlvo);
        p.setMetaProteica(ptnAlvo); p.setNumHorasOuHorarios(numHorarios);
        double vol = vctAlvo / (formula.getDensidade() * numHorarios);
        p.setVolumeMlH(vol);
        double vt = vol * numHorarios; p.setVolumeTotal(vt);
        double kcal = vt * formula.getDensidade(); p.setKcalTotais(kcal);
        double ptn = (vt / 1000.0) * formula.getPtnPorLitro(); p.setPtnTotal(ptn);
        p.setKcalPorKg(kcal / peso); p.setPtnPorKg(ptn / peso);
        p.setPercentualVCT((kcal / vctAlvo) * 100.0);
        p.setPercentualPTN((ptn / ptnAlvo) * 100.0);
        double volPleno = vctAlvo / formula.getDensidade(); p.setVolumePleno(volPleno);
        double ptnPlena = (volPleno / 1000.0) * formula.getPtnPorLitro(); p.setPtnPlena(ptnPlena);
        p.setPtnSuplementar(Math.max(0, ptnAlvo - ptnPlena));
        calcularProgressao(p, vctAlvo, formula.getDensidade(), numHorarios);
        if (vol > ConstantesClinicas.VOLUME_MAX_HORARIO)
            alertas.add(new AlertaClinico(NivelAlerta.ALERTA, "VOL", "Volume/horário > 500ml"));
        if (numHorarios < 4)
            alertas.add(new AlertaClinico(NivelAlerta.AVISO, "HOR", "< 4 horários/dia"));
        return p;
    }

    private void calcularProgressao(Prescricao p, double vct, double dens, int unidades) {
        double[] pcts = {0.25, 0.50, 0.75, 1.00};
        double[] vols = new double[4]; double[] kcals = new double[4];
        for (int i = 0; i < 4; i++) { kcals[i] = vct * pcts[i]; vols[i] = kcals[i] / (dens * unidades); }
        p.setVolumeProgressao(vols); p.setKcalProgressao(kcals);
    }

    public boolean podeSuplementar(int dia, List<AlertaClinico> alertas) {
        if (dia < 4) { alertas.add(new AlertaClinico(NivelAlerta.BLOQUEIO, "R05", "Suplementação só a partir do 4º dia")); return false; }
        return true;
    }

    public double[][] calcularSuplementacao(double ptnFaltante) {
        return new double[][] {
            {(ptnFaltante / 13.0) * 15.0, (ptnFaltante / 13.0) * 52.0},
            {(ptnFaltante / 10.4) * 12.0, (ptnFaltante / 10.4) * 41.6},
            {(ptnFaltante / 6.0) * 20.0, (ptnFaltante / 6.0) * 62.64}
        };
    }
}
