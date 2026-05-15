package auth;

/**
 * Representa o usuário autenticado.
 * Imutável após o login.
 */
public final class AuthUser {
    private final String uid;
    private final String email;
    private final String displayName;
    private final UserRole role;
    private final String idToken;

    public AuthUser(String uid, String email, String displayName, UserRole role, String idToken) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.role = role;
        this.idToken = idToken;
    }

    public String getUid()         { return uid; }
    public String getEmail()       { return email; }
    public String getDisplayName() { return displayName; }
    public UserRole getRole()      { return role; }
    public String getIdToken()     { return idToken; }

    public boolean isAdmin()  { return role == UserRole.ADMIN; }
    public boolean isMedico() { return role == UserRole.MEDICO; }

    /** Primeiras letras do nome para avatar */
    public String getInitials() {
        if (displayName == null || displayName.isBlank()) return "?";
        String[] parts = displayName.trim().split(" ");
        if (parts.length == 1) return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        return (parts[0].charAt(0) + "" + parts[parts.length-1].charAt(0)).toUpperCase();
    }
}
