package firebase;

import auth.UserSession;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * FirestoreService — CRUD via REST API.
 * Usa o idToken do usuário logado para autenticar.
 */
public class FirestoreService {

    private static final HttpClient HTTP = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    /** Salva um documento no Firestore */
    public boolean save(String collection, String docId, String jsonFields) {
        try {
            String token = UserSession.get().getUser().getIdToken();
            String url = FirebaseConfig.FIRESTORE_URL + collection + "/" + docId;

            // Firestore PATCH para criar ou atualizar
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(
                    "{\"fields\":{" + jsonFields + "}}"
                ))
                .timeout(Duration.ofSeconds(10))
                .build();

            HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
            return resp.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    /** Lê um documento do Firestore */
    public String get(String collection, String docId) {
        try {
            String token = UserSession.get().getUser().getIdToken();
            String url = FirebaseConfig.FIRESTORE_URL + collection + "/" + docId;

            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

            HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
            return resp.statusCode() == 200 ? resp.body() : null;
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    /** Lista uma coleção (limitado a 50 documentos) */
    public String list(String collection) {
        try {
            String token = UserSession.get().getUser().getIdToken();
            String url = FirebaseConfig.FIRESTORE_URL + collection + "?pageSize=50";

            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

            HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
            return resp.statusCode() == 200 ? resp.body() : null;
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    // ── Helpers para construir JSON de campo Firestore ──

    public static String field(String name, String value) {
        return "\"" + name + "\":{\"stringValue\":\"" + escape(value) + "\"}";
    }

    public static String fieldInt(String name, int value) {
        return "\"" + name + "\":{\"integerValue\":" + value + "}";
    }

    public static String fieldDouble(String name, double value) {
        return "\"" + name + "\":{\"doubleValue\":" + value + "}";
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
