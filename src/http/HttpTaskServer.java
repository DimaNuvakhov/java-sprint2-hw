package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import imanagers.Manager;
import managers.HTTPTaskManager;
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

public class HttpTaskServer {
    private static final int PORT = 8008;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private HTTPTaskManager httpManager = new HTTPTaskManager();
    private HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
    }

    public HTTPTaskManager getHttpManager() {
        return httpManager;
    }

    public void stop() {
        httpServer.stop(0);
    }

    public void start() {
        httpServer.start();
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
        httpServer.createContext("/tasks/subTask", new SubTaskHandler());
        httpServer.createContext("/tasks/items", new GetAllItemsHandler());
        httpServer.createContext("/tasks/deleteAll", new DeleteAllTaskHandler());
        httpServer.createContext("/tasks/getPrioritized", new GetPrioritizedTasksHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

     class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String answer = "";
            String method = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            Task task = gson.fromJson(body, Task.class);
            switch (method) {
                case "POST":
                    if (query == null) {
                        httpManager.addTask(task);
                        answer = gson.toJson(task.getId());
                    } else {
                        String id = query.split("=")[1];
                        httpManager.renewTaskById(id, task);
                        answer = gson.toJson(task.getId());
                    }
                    break;
                case "GET":
                    answer = gson.toJson(httpManager.getAllTasks());
                    break;
                case "DELETE":
                    if (query != null) {
                        String id = query.split("=")[1];
                        answer = gson.toJson(httpManager.deleteTaskById(id));
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

     class EpicHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String answer = "";
            String method = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            Epic epic = gson.fromJson(body, Epic.class);
            switch (method) {
                case "POST":
                    if (query == null) {
                        httpManager.addEpic(epic);
                        answer = gson.toJson(epic.getId());
                    } else {
                        String id = query.split("=")[1];
                        httpManager.renewTaskById(id, epic);
                        answer = gson.toJson(epic.getId());
                    }
                    break;
                case "GET":
                    if (query == null) {
                        answer = gson.toJson(httpManager.getAllEpics());
                    } else {
                        String id = query.split("=")[1];
                        answer = gson.toJson(httpManager.getSubTaskListFromEpicById(id));
                    }
                    break;
                case "DELETE":
                    if (query != null) {
                        String id = query.split("=")[1];
                        answer = gson.toJson(httpManager.deleteTaskById(id));
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

     class SubTaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String answer = "";
            String method = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            SubTask subTask = gson.fromJson(body, SubTask.class);
            switch (method) {
                case "POST":
                    if (query == null) {
                        httpManager.addSubTaskIntoEpic(subTask);
                        answer = gson.toJson(subTask.getId());
                    } else {
                        String id = query.split("=")[1];
                        httpManager.renewTaskById(id, subTask);
                        answer = gson.toJson(subTask.getId());
                    }
                    break;
                case "GET":
                    answer = gson.toJson(httpManager.getAllSubtasks());
                    break;
                case "DELETE":
                    if (query != null) {
                        String id = query.split("=")[1];
                        answer = gson.toJson(httpManager.deleteTaskById(id));
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

     class GetAllItemsHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String answer = "";
            String method = httpExchange.getRequestMethod();
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            if (method.equals("GET")) {
                answer = gson.toJson(httpManager.getAllItems());
            } else {
                answer = gson.toJson("Вы использовали неизвестный метод!");
            }
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(answer.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

     class DeleteAllTaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String answer = "";
            String method = httpExchange.getRequestMethod();
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            if (method.equals("DELETE")) {
                httpManager.deleteAllTasks();
                answer = gson.toJson("Все задачи удалены");
            } else {
                answer = gson.toJson("Вы использовали неизвестный метод!");
            }
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(answer.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

     class GetPrioritizedTasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String answer = "";
            String method = httpExchange.getRequestMethod();
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            if (method.equals("GET")) {
                answer = gson.toJson(httpManager.getPrioritizedTasks());
            } else {
                answer = gson.toJson("Вы использовали неизвестный метод!");
            }
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(answer.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

     class HistoryHandler implements HttpHandler {

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
                    httpManager.getTaskById(id);
                    answer = gson.toJson(httpManager.history());
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