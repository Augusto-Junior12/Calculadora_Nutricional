package repository;

import model.FormulaEnteral;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Repositório de fórmulas enterais — banco de dados em memória.
 * Carregado com as 51 fórmulas catalogadas da planilha original.
 * Singleton para garantir instância única.
 */
public class FormulaEnteralRepository {

    private static FormulaEnteralRepository instancia;
    private final Map<String, FormulaEnteral> formulas; // HashMap: O(1) por nome

    private FormulaEnteralRepository() {
        formulas = new HashMap<>();
        carregarFormulas();
    }

    public static FormulaEnteralRepository getInstance() {
        if (instancia == null) {
            instancia = new FormulaEnteralRepository();
        }
        return instancia;
    }

    /** Busca fórmula por nome exato. */
    public FormulaEnteral buscarPorNome(String nome) {
        return formulas.get(nome);
    }

    /** Retorna todas as fórmulas como lista ordenada por nome. */
    public List<FormulaEnteral> listarTodas() {
        return new ArrayList<>(formulas.values());
    }

    /** Filtra fórmulas por faixa de densidade. */
    public List<FormulaEnteral> filtrarPorDensidade(double densidadeMin, double densidadeMax) {
        List<FormulaEnteral> resultado = new ArrayList<>();
        for (FormulaEnteral f : formulas.values()) {
            if (f.getDensidade() >= densidadeMin && f.getDensidade() <= densidadeMax) {
                resultado.add(f);
            }
        }
        return resultado;
    }

    /** Filtra fórmulas sem soja (para pacientes alérgicos — R11). */
    public List<FormulaEnteral> filtrarSemSoja() {
        List<FormulaEnteral> resultado = new ArrayList<>();
        for (FormulaEnteral f : formulas.values()) {
            if (!f.isContemSoja()) resultado.add(f);
        }
        return resultado;
    }

    /** Filtra fórmulas sem fibra (para pacientes com íleo/PO — R09). */
    public List<FormulaEnteral> filtrarSemFibra() {
        List<FormulaEnteral> resultado = new ArrayList<>();
        for (FormulaEnteral f : formulas.values()) {
            if (!f.isContemFibra()) resultado.add(f);
        }
        return resultado;
    }

    public int getTotal() { return formulas.size(); }

    // ================ CARGA DE DADOS ================
    private void add(String nome, double dens, double ptn, double cho, double lip,
                     double fib, double k, double osm, String ind, String obs) {
        formulas.put(nome, new FormulaEnteral(nome, dens, ptn, cho, lip, fib, k, osm, ind, obs));
    }

    private void carregarFormulas() {
        // --- Densidade 1.0 kcal/ml ---
        add("Fresubin Original", 1.0, 38, 138, 34, 0, 1250, 220, "", "");
        add("Fresubin Original Fibre", 1.0, 38, 130, 34, 13, 1550, 285, "", "");
        add("Fresubin Soya Fibre", 1.0, 38, 121, 36, 20, 1330, 410, "", "Soya — sem lactose");
        add("Nutrison 1.0", 1.0, 40, 120, 39, 0, 1500, 255, "", "");
        add("Nutrison Multifiber", 1.0, 40, 120, 39, 15, 1500, 255, "", "");
        add("Diben", 1.0, 46.5, 92.5, 46, 15, 1430, 345, "diabetes", "Controle glicêmico");
        add("Diben HP", 1.0, 75, 131, 70, 23, 1800, 450, "diabetes", "Hiperproteico + glicêmico");
        add("Diamax", 1.0, 43, 110, 43, 15, 1590, 275, "diabetes", "Controle glicêmico");
        add("Novasource Proline", 1.0, 58, 130, 30, 15, 1650, 351, "", "Arginina, w3, w6, prolina, colina");
        add("Reconvan", 1.0, 55, 120, 33, 0, 2070, 270, "", "Imunomodulação");
        add("Impact", 1.0, 65, 140, 28, 0, 2100, 350, "", "Arginina, w3, w6, nucleotídeos");
        add("Peptamen pó", 1.0, 41, 130, 40, 0, 1100, 260, "", "Semi-elementar");
        add("Peptamen Intense", 1.0, 92, 72, 38, 5, 1500, 266, "", "Semi-elementar hiperproteico");
        add("Survimed OPD", 1.0, 45, 90, 28, 0, 2000, 300, "", "Semi-elementar");
        add("Peptimax pó", 1.0, 36, 126, 100, 0, 8360, 326, "", "Semi-elementar");
        add("Nutrison Advanced Peptisorb", 1.0, 40, 180, 170, 0, 1500, 455, "", "Peptídica");

        // --- Densidade 1.12–1.24 kcal/ml ---
        add("Novasource GC", 1.12, 49, 95, 60, 15, 2800, 337, "diabetes", "Controle glicêmico");
        add("Novasource GC HP", 1.14, 64, 120, 45, 12, 2250, 271, "diabetes", "Hiperproteico + glicêmico");
        add("Fresubin 1.2 HP Fibre", 1.2, 60, 140, 41, 20, 2230, 345, "", "Hiperproteico");
        add("Nutri enteral Soya", 1.2, 48, 170, 37, 0, 1700, 305, "", "Soya");
        add("Nutri enteral Soya Fiber", 1.2, 44, 170, 40, 18, 1330, 305, "", "Soya + fibra");
        add("Trophic Basic", 1.2, 46, 160, 41, 0, 2040, 328, "", "");
        add("Trophic Fiber", 1.2, 45, 170, 39, 20, 2340, 334, "", "");
        add("Trophic Soya", 1.2, 46, 170, 39, 0, 2320, 354, "", "Soya");
        add("Isosource Mix", 1.23, 43, 160, 46, 15, 1700, 305, "", "");
        add("Isosource Soya", 1.21, 44, 170, 39, 0, 1900, 276, "", "Soya");
        add("Isosource Soya Fiber", 1.24, 44, 170, 43, 17, 1950, 321, "", "Soya + fibra");
        add("Novasource Senior", 1.24, 65, 140, 47, 0, 1950, 333, "", "Idosos");

        // --- Densidade 1.3 kcal/ml ---
        add("Fresubin Hepa", 1.3, 40, 174, 47, 20, 1200, 330, "hepatopatia", "Hepática");
        add("Peptamen HN", 1.31, 66, 150, 49, 0, 1500, 350, "", "Semi-elementar hiperproteico");
        add("Survimed OPD HN", 1.33, 67, 183, 37, 0, 2600, 370, "", "Semi-elementar hiperproteico");

        // --- Densidade 1.5 kcal/ml ---
        add("Fresubin Energy Fibre", 1.5, 56, 180, 58, 15, 2070, 325, "", "");
        add("Fresubin HP Energy", 1.5, 75, 170, 58, 0, 2340, 300, "", "Hiperproteico");
        add("Fresubin Lipid", 1.5, 100, 118, 67, 24, 1280, 340, "", "Hiperproteico + lipídico");
        add("Isosource 1.5", 1.5, 63, 210, 45, 8, 2400, 450, "", "");
        add("Novasource Hi Protein", 1.5, 83, 120, 80, 0, 2100, 283, "", "Hiperproteico");
        add("Novasource GC 1.5", 1.5, 75, 130, 74, 15, 2500, 352, "diabetes", "Glicêmico 1.5");
        add("Novasource GI control", 1.5, 60, 180, 60, 20, 1630, 436, "", "Fibra + controle GI");
        add("Nutrison Energy", 1.5, 60, 180, 58, 0, 2010, 360, "", "");
        add("Nutrison Energy Multifiber", 1.5, 60, 180, 58, 15, 2010, 390, "", "");
        add("Nutri Enteral 1.5", 1.5, 64, 210, 42, 0, 1700, 330, "", "");
        add("Trophic 1.5", 1.5, 58, 200, 50, 0, 2250, 476, "", "");
        add("Trophic EP", 1.5, 69, 190, 50, 0, 2570, 426, "", "Especializada");
        add("Trophic 1.5 Soya", 1.5, 57, 210, 49, 0, 3000, 411, "", "Soya");
        add("Impact 1.5", 1.5, 94, 140, 64, 0, 1900, 393, "", "Arginina, w3, w6, nucleotídeos");
        add("Peptamen 1.5", 1.5, 68, 180, 56, 0, 2150, 436, "", "Semi-elementar");

        // --- Densidade 2.0 kcal/ml ---
        add("Fresubin 2kcal HP (500ml)", 2.0, 50, 87.5, 50, 0, 1700, 395, "", "Restrição hídrica");
        add("Fresubin 2kcal HP Fibre(500ml)", 2.0, 50, 83.5, 50, 15, 1700, 395, "", "Restrição hídrica + fibra");
        add("Novasource Ren", 2.0, 74, 200, 100, 0, 1500, 739, "renal", "Renal — carnitina, arginina, w3, w6, taurina");
    }
}
