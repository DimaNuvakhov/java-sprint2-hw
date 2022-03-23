package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import imanagers.Manager;
import managers.Managers;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpRetryException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8003;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Manager fileManager = Managers.getDefaultFileManager();

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/add", new AddTaskHandler());
        httpServer.createContext("/tasks/task", new GetTaskHandler());
        httpServer.start(); // запускаем сервер

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class AddTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            Headers requestHeaders = httpExchange.getRequestHeaders();
            List<String> contentTypeValues = requestHeaders.get("Content-type");
            if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
                System.out.println("Тело запроса передаётся в формате JSON");
            } else {
                throw new IllegalArgumentException("Тело запроса передано не в формате JSON");
            }
            if ("POST".equals(method)) {
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                Gson gson = new Gson();
                Task task = gson.fromJson(body, Task.class);
                fileManager.addTask(task);
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                throw new IllegalArgumentException("Метод запроса не \"POST\"");
            }


            // {"id":"1","name":"go to the gym","description":"go to the favorite gym","status":"DONE","startTime":{"year":2022,"month":12,"dayOfMonth":25,"hour":5,"minute":25},"duration":3}
        }
    }

    static class GetTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            Headers requestHeaders = httpExchange.getRequestHeaders();
            List<String> contentTypeValues = requestHeaders.get("Content-type");
            if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
                System.out.println("Тело запроса передаётся в формате JSON");
            } else {
                throw new IllegalArgumentException("Тело запроса передано не в формате JSON");
            }
            if ("GET".equals(method)) {
                String getTasks = fileManager.getAllTasks().toString();
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream os = httpExchange.getResponseBody();
                os.write(getTasks.getBytes(StandardCharsets.UTF_8));
                os.close();
            }
        }
    }
}
