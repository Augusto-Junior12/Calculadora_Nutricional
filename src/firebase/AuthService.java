package firebase;

import auth.AuthUser;
import auth.UserRole;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * AuthService — Firebase Authentication via REST API.
 * Não requer JARs externos. Usa java.net.http nativo (JDK 11+).
 *
 * Fluxo:
 *   1. signIn(email, password) → Firebase REST → idToken + uid
 *   2. getUserProfile(uid, idToken) → Firestore → role + displayName
 *   3. Retorna AuthUser completo
 */
public class AuthService {

    private static final HttpClient HTTP = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    /** Result class para encapsular resultado ou erro */
    public record AuthResult(AuthUser user, String errorMessage) {
        public boolean isSuccess() { return user != null; }
    }

    /**
     * Autentica email/senha e busca perfil no Firestore.
     * Chame em background thread — nunca na EDT.
     */
    public AuthResult signIn(String email, String password) {
        try {
            // ── Passo 1: Autenticar com Firebase Auth ──
            String authBody = """
                {"email":"%s","password":"%s","returnSecureToken":true}
                """.formatted(email.trim(), password);

            HttpRequest authReq = HttpRequest.newBuilder()
                .uri(URI.create(FirebaseConfig.AUTH_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(authBody))
                .timeout(Duration.ofSeconds(15))
                .build();

            HttpResponse<String> authResp = HTTP.send(authReq, HttpResponse.BodyHandlers.ofString());

            if (authResp.statusCode() != 200) {
                String msg = parseFirebaseError(authResp.body());
                return new AuthResult(null, msg);
            }

            String uid = extractJsonField(authResp.body(), "localId");
            String idToken = extractJsonField(authResp.body(), "idToken");

            // ── Passo 2: Buscar perfil no Firestore ──
            String profileUrl = FirebaseConfig.FIRESTORE_URL + "users/" + uid;

            HttpRequest profileReq = HttpRequest.newBuilder()
                .uri(URI.create(profileUrl))
                .header("Authorization", "Bearer " + idToken)
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

            HttpResponse<String> profileResp = HTTP.send(profileReq, HttpResponse.BodyHandlers.ofString());

            String displayName = email.split("@")[0]; // fallback
            UserRole role = UserRole.MEDICO;           // fallback

            if (profileResp.statusCode() == 200) {
                String body = profileResp.body();
                String nameVal = extractFirestoreString(body, "displayName");
                String roleVal = extractFirestoreString(body, "role");
                if (nameVal != null) displayName = nameVal;
                if (roleVal != null) role = UserRole.from(roleVal);
            }

            return new AuthResult(new AuthUser(uid, email, displayName, role, idToken), null);

        } catch (IOException | InterruptedException e) {
            return new AuthResult(null, "Sem conexão. Verifique sua internet.");
        } catch (Exception e) {
            return new AuthResult(null, "Erro inesperado: " + e.getMessage());
        }
    }

    // ── Parsing utilities (sem biblioteca externa) ──

    private String extractJsonField(String json, String field) {
        String key = "\"" + field + "\"";
        int idx = json.indexOf(key);
        if (idx == -1) return null;
        int colon = json.indexOf(':', idx + key.length());
        int start = json.indexOf('"', colon + 1);
        int end = json.indexOf('"', start + 1);
        if (start == -1 || end == -1) return null;
        return json.substring(start + 1, end);
    }

    /** Extrai valor de campo Firestore: {"stringValue":"valor"} */
    private String extractFirestoreString(String json, String field) {
        String key = "\"" + field + "\"";
        int idx = json.indexOf(key);
        if (idx == -1) return null;
        String sub = json.substring(idx);
        int sv = sub.indexOf("\"stringValue\"");
        if (sv == -1) return null;
        int q1 = sub.indexOf('"', sv + 14);
        int q2 = sub.indexOf('"', q1 + 1);
        return sub.substring(q1 + 1, q2);
    }

    private String parseFirebaseError(String body) {
        if (body.contains("EMAIL_NOT_FOUND") || body.contains("INVALID_EMAIL"))
            return "E-mail não encontrado.";
        if (body.contains("INVALID_PASSWORD") || body.contains("INVALID_LOGIN_CREDENTIALS"))
            return "Senha incorreta.";
        if (body.contains("USER_DISABLED"))
            return "Conta desativada. Contate o administrador.";
        if (body.contains("TOO_MANY_ATTEMPTS"))
            return "Muitas tentativas. Aguarde alguns minutos.";
        return "Credenciais inválidas.";
    }
}
