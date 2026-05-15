package util;

/**
 * Constantes clínicas para validação fisiológica e alertas.
 * Fonte: diretrizes ASPEN/ESPEN e protocolos de UTI.
 */
public final class ConstantesClinicas {

    private ConstantesClinicas() {} // Utility class

    // --- Faixas fisiológicas normais (para alerta) ---
    public static final double PH_MIN_NORMAL = 7.35;
    public static final double PH_MAX_NORMAL = 7.45;
    public static final double PH_MIN_ACEITAVEL = 7.0;
    public static final double PH_MAX_ACEITAVEL = 7.8;

    public static final double PCO2_MIN_NORMAL = 35.0;
    public static final double PCO2_MAX_NORMAL = 45.0;

    public static final double HCO3_MIN_NORMAL = 22.0;
    public static final double HCO3_MAX_NORMAL = 26.0;

    public static final double K_MIN_CRITICO = 3.0;
    public static final double K_MAX_CRITICO = 6.0;
    public static final double K_MIN_ACEITAVEL = 2.0;
    public static final double K_MAX_ACEITAVEL = 7.0;

    public static final double NA_MIN_CRITICO = 130.0;
    public static final double NA_MAX_CRITICO = 150.0;
    public static final double NA_MIN_ACEITAVEL = 120.0;
    public static final double NA_MAX_ACEITAVEL = 165.0;

    public static final double MG_MIN_ALERTA = 1.5;

    public static final double P_MIN_CRITICO = 1.5;  // R07 — síndrome de realimentação
    public static final double P_MIN_ACEITAVEL = 0.5;
    public static final double P_MAX_ACEITAVEL = 8.0;

    public static final double LACTATO_MAX_NORMAL = 2.0;

    public static final double HGT_MIN_CRITICO = 70.0;
    public static final double HGT_MAX_CRITICO = 180.0;
    public static final double HGT_MIN_ACEITAVEL = 40.0;
    public static final double HGT_MAX_ACEITAVEL = 600.0;

    public static final double DIURESE_OLIGURIA = 400.0; // ml/24h

    // --- Limites de prescrição ---
    public static final double KCAL_KG_MIN_ALERTA = 10.0;
    public static final double KCAL_KG_MAX_ALERTA = 40.0;
    public static final double PTN_KG_MAX_ALERTA = 3.0;

    public static final double OSMOL_MAX_SONDA = 450.0;  // R10
    public static final double VOLUME_MAX_HORARIO = 500.0; // ml
    public static final double VOLUME_MIN_FLUSH = 30.0;
    public static final double VOLUME_MAX_FLUSH = 300.0;
    public static final double VOLUME_MAX_DOSE_ABERTA = 400.0;

    public static final double HIPERALIMENTACAO_LIMITE = 1.30; // R06: 130% do VCT

    // --- Hidratação ---
    public static final double HIDRATACAO_ML_KG_MIN = 25.0;
    public static final double HIDRATACAO_ML_KG_MAX = 30.0;

    // --- Noradrenalina ---
    public static final double NORA_DOSE_MAX_MCG_KG_MIN = 2.0;
    public static final double NORA_CONC_SIMPLES = 0.032;  // mg/ml (2 amp em 250ml)
    public static final double NORA_CONC_CONCENTRADA = 0.064; // mg/ml (4 amp em 250ml)

    // --- Propofol ---
    public static final double PROPOFOL_KCAL_POR_ML = 1.1;
    public static final double PROPOFOL_PRIS_MG_KG_H = 4.0; // R14

    // --- Chumlea ---
    public static final int CHUMLEA_IDADE_MIN = 18;
    public static final int CHUMLEA_IDADE_MAX = 90;

    // --- Balanço Nitrogenado ---
    public static final double BN_FATOR_CONVERSAO_PTN = 6.25;
    public static final double BN_FATOR_UREIA = 0.467;
    public static final double BN_PERDAS_INSENSÍVEIS = 4.0;

    // --- Controle de Ingestão ---
    public static final double INGESTAO_META_ADEQUADA = 75.0; // %
    public static final int INGESTAO_MULTIPLO = 25;

    // --- Prescrito × Infundido ---
    public static final double META_PERCENTUAL_INFUNDIDO = 80.0; // %

    // --- Macronutrientes TNE aberta ---
    public static final double CHO_MIN_PERCENT = 45.0;
    public static final double CHO_MAX_PERCENT = 65.0;
    public static final double PTN_MIN_PERCENT = 15.0;
    public static final double PTN_MAX_PERCENT = 20.0;
    public static final double LIP_MIN_PERCENT = 20.0;
    public static final double LIP_MAX_PERCENT = 35.0;

    // --- Relação kcal não proteicas / g nitrogênio (R15) ---
    public static final double RELACAO_NP_N_MIN = 80.0;
    public static final double RELACAO_NP_N_MAX = 150.0;

    // --- Amputados (Osterkamp, 1995) ---
    public static final double AMPUT_MAO = 0.7;
    public static final double AMPUT_ANTEBRACO = 1.6;
    public static final double AMPUT_ANTEBRACO_MAO = 2.3;
    public static final double AMPUT_BRACO = 2.7;
    public static final double AMPUT_MEMBRO_SUP = 5.0;
    public static final double AMPUT_PE = 1.5;
    public static final double AMPUT_PERNA = 4.4;
    public static final double AMPUT_MEMBRO_INF = 16.0;
}
