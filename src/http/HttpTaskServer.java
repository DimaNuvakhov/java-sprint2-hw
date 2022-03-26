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
    private static final int PORT = 8008;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Manager fileManager = Managers.getDefaultFileManager();

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/epic", new TaskHandler());
        httpServer.createContext("/tasks/subTask", new TaskHandler());
        httpServer.createContext("/tasks/items", new GetAllItemsHandler());
        httpServer.createContext("/tasks/deleteAll", new DeleteAllTaskHandler());
        httpServer.createContext("/tasks/getPrioritized", new GetPrioritizedTasksHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String answer = "";
            String method = httpExchange.getRequestMethod();
            Headers requestHeaders = httpExchange.getRequestHeaders();
            List<String> contentTypeValues = requestHeaders.get("Content-type");
            String query = httpExchange.getRequestURI().getQuery();
            if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
                System.out.println("Тело запроса передаётся в формате JSON");
            } else {
                throw new IllegalArgumentException("Тело запроса передано не в формате JSON");
            }
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            Task task = gson.fromJson(body, Task.class);
            switch (method) {
                case "POST":
                    if (query == null) {
                        fileManager.addTask(task);
                        answer = gson.toJson(task.getId());
                    } else {
                         String id = query.split("=")[1];
                        fileManager.renewTaskById(id, task);
                        answer = gson.toJson(fileManager.getAllTasks());
                    }
                    break;
                case "GET":
                    answer = gson.toJson(fileManager.getAllTasks());
                    break;
                case "DELETE":
                    if (query != null) {
                        String id = query.split("=")[1];
                        answer = gson.toJson(fileManager.deleteTaskById(id));
                    }
                    break;
                default:
                    answer = "Вы использовали неизвестный метод!";
            }
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(answer.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

    static class EpicHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String answer = "";
            String method = httpExchange.getRequestMethod();
            Headers requestHeaders = httpExchange.getRequestHeaders();
            List<String> contentTypeValues = requestHeaders.get("Content-type");
            String query = httpExchange.getRequestURI().getQuery();
            System.out.println(query);
            if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
                System.out.println("Тело запроса передаётся в формате JSON");
            } else {
                throw new IllegalArgumentException("Тело запроса передано не в формате JSON");
            }
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            Epic epic = gson.fromJson(body, Epic.class);
            switch (method) {
                case "POST":
                    if (query == null) {
                        fileManager.addEpic(epic);
                        answer = gson.toJson(epic.getId());
                    } else {
                        String id = query.split("=")[1];
                        fileManager.renewTaskById(id, epic);
                        answer = gson.toJson(fileManager.getAllEpics());
                    }
                    break;
                case "GET":
                    answer = gson.toJson(fileManager.getAllEpics());
                    break;
                case "DELETE":
                    if (query != null) {
                        String id = query.split("=")[1];
                        answer = gson.toJson(fileManager.deleteTaskById(id));
                    }
                    break;
                default:
                    answer = gson.toJson("Вы использовали неизвестный метод!");
            }
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(answer.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

    static class SubTaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String answer = "";
            String method = httpExchange.getRequestMethod();
            Headers requestHeaders = httpExchange.getRequestHeaders();
            List<String> contentTypeValues = requestHeaders.get("Content-type");
            String query = httpExchange.getRequestURI().getQuery();
            System.out.println(query);
            if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
                System.out.println("Тело запроса передаётся в формате JSON");
            } else {
                throw new IllegalArgumentException("Тело запроса передано не в формате JSON");
            }
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            SubTask subTask = gson.fromJson(body, SubTask.class);
            switch (method) {
                case "POST":
                    if (query == null) {
                        fileManager.addSubTaskIntoEpic(subTask);
                        answer = gson.toJson(subTask.getId());
                    } else {
                        String id = query.split("=")[1];
                        fileManager.renewTaskById(id, subTask);
                        answer = gson.toJson(fileManager.getAllEpics());
                    }
                    break;
                case "GET":
                    if (query == null) {
                        answer = gson.toJson(fileManager.getAllSubtasks());
                    } else {
                        String id = query.split("=")[1];
                        answer = gson.toJson(fileManager.getSubTaskListFromEpicById(id));
                    }
                    break;
                case "DELETE":
                    if (query != null) {
                        String id = query.split("=")[1];
                        answer = gson.toJson(fileManager.deleteTaskById(id));
                    }
                    break;
                default:
                    answer = gson.toJson("Вы использовали неизвестный метод!");
            }
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(answer.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

    static class GetAllItemsHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String answer = "";
            String method = httpExchange.getRequestMethod();
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            if (method.equals("GET")) {
                answer = gson.toJson(fileManager.getAllItems());
            } else {
                answer = gson.toJson("Вы использовали неизвестный метод!");
            }
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(answer.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

    static class DeleteAllTaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String answer = "";
            String method = httpExchange.getRequestMethod();
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            if (method.equals("DELETE")) {
                fileManager.deleteAllTasks();
                answer =  gson.toJson("Все задачи удалены");
            } else {
                answer = gson.toJson("Вы использовали неизвестный метод!");
            }
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(answer.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

    static class GetPrioritizedTasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String answer = "";
            String method = httpExchange.getRequestMethod();
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            if (method.equals("GET")) {
                answer = gson.toJson(fileManager.getPrioritizedTasks());
            } else {
                answer = gson.toJson("Вы использовали неизвестный метод!");
            }
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(answer.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

    static class HistoryHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String answer = "";
            String method = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            if (method.equals("GET")) {
                if (query != null) {
                    String id = query.split("=")[1];
                    fileManager.getTaskById(id);
                    answer = gson.toJson(fileManager.history());
                }
            } else {
                answer = gson.toJson("Вы использовали неизвестный метод!");
            }
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(answer.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }
}