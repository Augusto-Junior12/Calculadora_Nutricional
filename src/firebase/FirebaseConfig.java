package firebase;

/**
 * Configuração do projeto Firebase.
 *
 * INSTRUÇÕES:
 * 1. Acesse https://console.firebase.google.com
 * 2. Crie um projeto e ative Authentication (Email/Password)
 * 3. Ative o Firestore Database
 * 4. Vá em Configurações do Projeto → Web API Key
 * 5. Preencha os campos abaixo com suas credenciais reais
 *
 * Estrutura esperada no Firestore:
 *   Collection: "users"
 *     Document: {uid}
 *       Fields: { displayName: string, role: "admin" | "medico" }
 *
 *   Collection: "patients"
 *     Document: {patientId}
 *       Fields: { nome, rh, idade, genero, etnia, dataInternacao, ... }
 */
public final class FirebaseConfig {

    private FirebaseConfig() {}

    /** Sua Web API Key do Firebase (Authentication) */
    public static final String WEB_API_KEY = "AIzaSyXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    /** ID do seu projeto Firebase */
    public static final String PROJECT_ID = "nutrix-hospital-os";

    // URLs da API Firebase (não alterar)
    public static final String AUTH_URL =
        "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + WEB_API_KEY;

    public static final String FIRESTORE_URL =
        "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents/";

    /** Verifica se as credenciais foram configuradas */
    public static boolean isConfigured() {
        return !WEB_API_KEY.contains("XXXX") && !PROJECT_ID.equals("nutrix-hospital-os");
    }
}
