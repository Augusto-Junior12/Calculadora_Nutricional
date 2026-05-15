package auth;

/**
 * Sessão do usuário atual — Singleton thread-safe.
 * Use UserSession.get() para acessar o usuário logado.
 */
public final class UserSession {

    private static volatile UserSession instance;
    private AuthUser currentUser;

    private UserSession() {}

    public static UserSession get() {
        if (instance == null) {
            synchronized (UserSession.class) {
                if (instance == null) instance = new UserSession();
            }
        }
        return instance;
    }

    public void login(AuthUser user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public AuthUser getUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return isLoggedIn() && currentUser.isAdmin();
    }
}
