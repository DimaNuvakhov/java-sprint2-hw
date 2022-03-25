package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import imanagers.Manager;
import managers.Managers;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8007;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Manager fileManager = Managers.getDefaultFileManager();

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/add", new AddTaskHandler());
        httpServer.createContext("/tasks/task", new GetTaskHandler());
        httpServer.createContext("/tasks/epic", new GetEpicHandler());
        httpServer.createContext("/tasks/subTask", new GetSubTaskHandler());
        httpServer.createContext("/tasks/items", new GetAllItemsHandler());
        httpServer.createContext("/tasks/task1", new GetTaskByIdHandler());

        httpServer.start(); // запускаем сервер
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class AddTaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String answer = "";
            String method = httpExchange.getRequestMethod();
            Headers requestHeaders = httpExchange.getRequestHeaders();
            List<String> contentTypeValues = requestHeaders.get("Content-type");
            if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
                System.out.println("Тело запроса передаётся в формате JSON");
            } else {
                throw new IllegalArgumentException("Тело запроса передано не в формате JSON");
            }
            if (method.equals("POST")) {
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                Gson gson = new GsonBuilder().
                        registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).
                        create();
                if (body.contains("\"type\":\"Task\"")) {
                    Task task = gson.fromJson(body, Task.class);
                    fileManager.addTask(task);
                    answer = task.getId();
                } else if (body.contains("\"type\":\"Epic\"")) {
                    Epic epic = gson.fromJson(body, Epic.class);
                    fileManager.addEpic(epic);
                    answer = epic.getId();
                } else if (body.contains("\"type\":\"Sub\"")) {
                    SubTask subTask = gson.fromJson(body, SubTask.class);
                    fileManager.addSubTaskIntoEpic(subTask);
                    answer = subTask.getId();
                }
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream os = httpExchange.getResponseBody();
                os.write(answer.getBytes(StandardCharsets.UTF_8));
                os.close();
            } else {
                throw new IllegalArgumentException("Метод запроса не \"POST\"");
            }
        }
    }

    static class GetTaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            if (method.equals("GET")) {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                        .create();
                String postSerialized = gson.toJson(fileManager.getAllTasks());
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream os = httpExchange.getResponseBody();
                os.write(postSerialized.getBytes(StandardCharsets.UTF_8));
                os.close();
            } else {
                throw new IllegalArgumentException("Метод запроса не \"GET\"");
            }
        }
    }

    static class GetEpicHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            if (method.equals("GET")) {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                        .create();
                String postSerialized = gson.toJson(fileManager.getAllEpics());
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream os = httpExchange.getResponseBody();
                os.write(postSerialized.getBytes(StandardCharsets.UTF_8));
                os.close();
            } else {
                throw new IllegalArgumentException("Метод запроса не \"GET\"");
            }
        }
    }

    static class GetSubTaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            if (method.equals("GET")) {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                        .create();
                String postSerialized = gson.toJson(fileManager.getAllSubtasks());
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream os = httpExchange.getResponseBody();
                os.write(postSerialized.getBytes(StandardCharsets.UTF_8));
                os.close();
            } else {
                throw new IllegalArgumentException("Метод запроса не \"GET\"");
            }
        }
    }

    static class GetAllItemsHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            if (method.equals("GET")) {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                        .create();
                String postSerialized = gson.toJson(fileManager.getAllItems());
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream os = httpExchange.getResponseBody();
                os.write(postSerialized.getBytes(StandardCharsets.UTF_8));
                os.close();
            } else {
                throw new IllegalArgumentException("Метод запроса не \"GET\"");
            }
        }
    }

    static class GetTaskByIdHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            if (method.equals("GET")) {
                String path = httpExchange.getRequestURI().getPath();
//                String id = path.split("/")[2];
                System.out.println(path);
            }
        }
    }
}