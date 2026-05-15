package util;

import model.enums.Etnia;
import model.enums.Genero;

/**
 * Tabelas de referência de Chumlea (1985/1988) para estimativa de altura e peso.
 * Fórmulas estratificadas por gênero, etnia e faixa etária.
 *
 * Referências:
 *  - Chumlea WC, Roche AF, Steinbaugh ML. Estimating stature from knee height (1985)
 *  - Chumlea WC, Guo S, Roche AF, Steinbaugh ML. Prediction of body weight (1988)
 */
public final class ReferenciaChumlea {

    private ReferenciaChumlea() {}

    /**
     * Estima a altura (cm) pela fórmula de Chumlea (1985).
     * @param genero MASCULINO ou FEMININO
     * @param aj Altura do joelho em cm
     * @param idade Idade em anos
     * @return Altura estimada em cm
     */
    public static double estimarAlturaCm(Genero genero, double aj, int idade) {
        if (genero == Genero.MASCULINO) {
            return 64.19 - (0.04 * idade) + (2.02 * aj);
        } else {
            return 84.88 - (0.24 * idade) + (1.83 * aj);
        }
    }

    /**
     * Estima o peso (kg) pela fórmula de Chumlea (1988).
     * Estratificado por gênero, etnia e faixa etária.
     */
    public static double estimarPesoKg(Genero genero, Etnia etnia, int idade,
                                        double aj, double cb) {
        if (genero == Genero.MASCULINO) {
            if (etnia == Etnia.BRANCO) {
                if (idade >= 19 && idade <= 59) {
                    return (aj * 1.19) + (cb * 3.21) - 86.82;
                } else {
                    return (aj * 1.10) + (cb * 3.07) - 75.81;
                }
            } else {
                if (idade >= 19 && idade <= 59) {
                    return (aj * 1.09) + (cb * 3.14) - 83.72;
                } else {
                    return (aj * 0.44) + (cb * 2.86) - 39.21;
                }
            }
        } else {
            if (etnia == Etnia.BRANCO) {
                if (idade >= 19 && idade <= 59) {
                    return (aj * 1.01) + (cb * 2.81) - 66.04;
                } else {
                    return (aj * 1.09) + (cb * 2.68) - 65.51;
                }
            } else {
                if (idade >= 19 && idade <= 59) {
                    return (aj * 1.24) + (cb * 2.97) - 82.48;
                } else {
                    return (aj * 1.50) + (cb * 2.58) - 84.22;
                }
            }
        }
    }

    /**
     * Estima o peso (kg) pela fórmula de Jung (2004).
     */
    public static double estimarPesoJung(Genero genero, double aj, double cb, double ca) {
        if (genero == Genero.MASCULINO) {
            return (aj * 0.666) + (cb * 1.901) + (ca * 0.307) - 40.97;
        } else {
            return (aj * 0.249) + (cb * 1.272) + (ca * 0.603) - 25.26;
        }
    }

    /**
     * Estima o peso (kg) pela fórmula de Rabito (2008).
     */
    public static double estimarPesoRabito(Genero genero, double aj, double cb, double ca, double cc) {
        if (genero == Genero.MASCULINO) {
            return 0.5759 * cb + 0.5263 * ca + 1.2452 * aj - 4.8689 * 1 - 32.9241;
        } else {
            return 0.5759 * cb + 0.5263 * ca + 1.2452 * aj - 4.8689 * 0 - 32.9241;
        }
    }
}
