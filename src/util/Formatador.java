package util;

/**
 * Utilitários de formatação para exibição de resultados.
 */
public final class Formatador {

    private Formatador() {}

    public static String decimal1(double valor) {
        return String.format("%.1f", valor);
    }

    public static String decimal2(double valor) {
        return String.format("%.2f", valor);
    }

    public static String inteiro(double valor) {
        return String.format("%.0f", valor);
    }

    public static String percentual(double valor) {
        return String.format("%.1f%%", valor);
    }

    public static String kcal(double valor) {
        return String.format("%.0f kcal", valor);
    }

    public static String gramas(double valor) {
        return String.format("%.1f g", valor);
    }

    public static String ml(double valor) {
        return String.format("%.0f ml", valor);
    }

    public static String mlH(double valor) {
        return String.format("%.0f ml/h", valor);
    }

    public static String kg(double valor) {
        return String.format("%.1f kg", valor);
    }

    public static String metros(double valor) {
        return String.format("%.2f m", valor);
    }

    public static String cm(double valor) {
        return String.format("%.1f cm", valor);
    }
}
