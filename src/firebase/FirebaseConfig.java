package firebase;

/**
 * Nutrix Hospital OS — FirebaseConfig (CONFIGURADO AUTOMATICAMENTE).
 * Projeto: nutrix-hospital-os
 */
public final class FirebaseConfig {

    private FirebaseConfig() {}

    /** Sua Web API Key do Firebase (Authentication) */
    public static final String WEB_API_KEY = "AIzaSyAACrmkTrK5j7tjnO928NIznJ6HWtwi9bU";

    /** ID do seu projeto Firebase */
    public static final String PROJECT_ID = "nutrix-hospital-os";

    // URLs da API Firebase (não alterar)
    public static final String AUTH_URL =
        "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + WEB_API_KEY;

    public static final String FIRESTORE_URL =
        "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents/";

    /** Verifica se as credenciais foram configuradas */
    public static boolean isConfigured() {
        return false; 
    }
}
