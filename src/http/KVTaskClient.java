package http;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    static String keyNumber;

    public KVTaskClient() {
        keyNumber = getKey();
    }

    private String getKey() {
        String answer = "";
        HttpClient client = HttpClient.newHttpClient();
        URI registerUrl = URI.create("http://localhost:8075/" + "register");
        HttpRequest request = HttpRequest.newBuilder().uri(registerUrl).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            answer = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + registerUrl + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return answer;
    }

    public void save(String key, String json) {
        URI saverUrl = URI.create("http://localhost:8075/" + "save" + "/" + key + "/" + "?API_KEY=" + keyNumber);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(saverUrl).
                POST(HttpRequest.BodyPublishers.ofString(json)).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + saverUrl + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) {
        String answer = "";
        HttpClient client = HttpClient.newHttpClient();
        URI loadUrl = URI.create("http://localhost:8075/" + "load" + "/" + key + "/" + "?API_KEY=" + keyNumber);
        HttpRequest request = HttpRequest.newBuilder().uri(loadUrl).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            answer = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + loadUrl + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return answer;
    }
}
