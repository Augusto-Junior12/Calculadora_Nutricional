package auth;

/**
 * Roles de acesso do sistema Nutrix.
 * ADMIN — acesso total (gestão + clínico)
 * MEDICO — acesso clínico apenas
 */
public enum UserRole {
    ADMIN("Administrador"),
    MEDICO("Nutricionista");

    private final String label;
    UserRole(String label) { this.label = label; }
    public String getLabel() { return label; }

    public static UserRole from(String str) {
        if (str == null) return MEDICO;
        return switch (str.toLowerCase()) {
            case "admin" -> ADMIN;
            default -> MEDICO;
        };
    }
}
