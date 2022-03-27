package tests;

import com.google.gson.*;
import http.HttpTaskServer;
import http.KVServer;
import http.LocalDateTimeAdapter;
import imanagers.Manager;
import managers.FileBackedManager;
import managers.HTTPTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HTTPTaskManagerTest {

    @BeforeAll
    static void beforeAll() throws IOException {
        new KVServer().start();
        new HttpTaskServer().start();
    }

    // Проверка метода addTask, добавление задачи, восстановление менеджера из сервера
    @Test
    public void shouldAddTaskAndLoad() throws IOException {
        URI url = URI.create("http://localhost:8008/tasks/task");
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonTask = gson.toJson(firstTask);
        requestToFindIdGet(url, gsonTask);
        HTTPTaskManager httpTaskManager = new HTTPTaskManager();
        httpTaskManager.loadFromServer();
        Task sameTask = getTaskByName(httpTaskManager, "Помыть посуду");
        assertNotNull(sameTask);
        assertEquals("Помыть тарелки и вилки", sameTask.getDescription());
        assertEquals(TaskStatus.NEW, sameTask.getStatus());
        assertEquals("2022-03-15T10:00", sameTask.getStartTime().toString());
        assertEquals(3, sameTask.getDuration());
    }

    // Проверка метода renewTaskById, обновление задачи, восстановление менеджера из сервера
    @Test
    public void shouldRenewTaskByIdAndLoad() {
        URI url = URI.create("http://localhost:8008/tasks/task");
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonFirstTask = gson.toJson(firstTask);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 20, 11, 0), 3);
        String id = requestToFindIdGet(url, gsonFirstTask);
        URI newUrl = URI.create("http://localhost:8008/tasks/task/?id=" + id);
        String gsonSecondTask = gson.toJson(secondTask);
        requestToFindIdGet(newUrl, gsonSecondTask);
        HTTPTaskManager httpTaskManager = new HTTPTaskManager();
        httpTaskManager.loadFromServer();
        Task oldTask = getTaskByName(httpTaskManager, "Помыть посуду");
        assertNull(oldTask);
        Task renewedTask = getTaskByName(httpTaskManager, "Купить хлеб");
        assertNotNull(renewedTask);
        assertEquals("Нужен хлеб \"Литовский\"", renewedTask.getDescription());
        assertEquals(TaskStatus.DONE, renewedTask.getStatus());
        assertEquals("2022-03-20T11:00", renewedTask.getStartTime().toString());
        assertEquals(3, renewedTask.getDuration());
    }

    // Проверка метода deleteTaskById, удаление задачи, восстановление менеджера из сервера
    @Test
    public void shouldDeleteTaskByIdAndLoad() {
        URI url = URI.create("http://localhost:8008/tasks/task");
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonFirstTask = gson.toJson(firstTask);
        String id = requestToFindIdGet(url, gsonFirstTask);
        URI newUrl = URI.create("http://localhost:8008/tasks/task/?id=" + id);
        assertTrue(requestDelete(newUrl));
    }

    // Проверка метода addEpic, добавлние эпика, восстановление менеджера из сервера
    @Test
    public void shouldAddEpicAndLoad() {
        URI url = URI.create("http://localhost:8008/tasks/epic");
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonTask = gson.toJson(firstEpic);
        requestToFindIdGet(url, gsonTask);
        HTTPTaskManager httpTaskManager = new HTTPTaskManager();
        httpTaskManager.loadFromServer();
        assertEquals(1, httpTaskManager.getAllItems().size());
        Epic sameEpic = (Epic) getTaskByName(httpTaskManager, "Сходить в спортзал");
        assertNotNull(sameEpic);
        assertEquals("Прокачать 3 группы мышц", sameEpic.getDescription());
    }

    // Проверка метода renewTaskById, обновление эпика, восстановление менеджера из файла
    @Test
    public void shouldRenewEpicByIdAndLoad() {
        URI url = URI.create("http://localhost:8008/tasks/epic");
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonFirstTask = gson.toJson(firstEpic);
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        String id = requestToFindIdGet(url, gsonFirstTask);
        URI newUrl = URI.create("http://localhost:8008/tasks/epic/?id=" + id);
        String gsonSecondTask = gson.toJson(secondEpic);
        requestToFindIdGet(newUrl, gsonSecondTask);
        HTTPTaskManager httpTaskManager = new HTTPTaskManager();
        httpTaskManager.loadFromServer();
        Epic oldEpic = (Epic) getTaskByName(httpTaskManager, "Сходить в спортзал");
        assertNull(oldEpic);
        Epic renewedEpic = (Epic) getTaskByName(httpTaskManager, "Изучение Java");
        assertNotNull(renewedEpic);
        assertEquals("Изучить язык программирования Java", renewedEpic.getDescription());
    }

    // Проверка метода deleteTaskById, удаление эпика, восстановление менеджера из сервера
    @Test
    public void shouldDeleteEpicByIdAndLoad() {
        URI url = URI.create("http://localhost:8008/tasks/epic");
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonFirstEpic = gson.toJson(firstEpic);
        String id = requestToFindIdGet(url, gsonFirstEpic);
        URI newUrl = URI.create("http://localhost:8008/tasks/epic/?id=" + id);
        assertTrue(requestDelete(newUrl));
    }

    // Проверка метода addSubTaskIntoEpic, добавление подзадачи, восстановление менеджера из файла
    @Test
    public void shouldAddSubTaskIntoEpicAndLoad() {
        URI url = URI.create("http://localhost:8008/tasks/epic");
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonFirstEpic = gson.toJson(firstEpic);
        String id = requestToFindIdGet(url, gsonFirstEpic);
        URI newUrl = URI.create("http://localhost:8008/tasks/subTask");
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, id,
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        String gsonFirstSubTask = gson.toJson(firstSubTask);
        requestToFindIdGet(newUrl, gsonFirstSubTask);
        HTTPTaskManager httpTaskManager = new HTTPTaskManager();
        httpTaskManager.loadFromServer();
        SubTask sameSubTask = (SubTask) getTaskByName(httpTaskManager, "Изучить Дженерики");
        assertNotNull(sameSubTask);
        assertEquals("Изучить случаи применения дженериков", sameSubTask.getDescription());
        assertEquals(TaskStatus.NEW, sameSubTask.getStatus());
        assertEquals("2022-03-15T10:00", sameSubTask.getStartTime().toString());
        assertEquals(4, sameSubTask.getDuration());
    }

    // Проверка метода renewTaskById, обновление подзадачи, восстановление менеджера из файла
    @Test
    public void shouldRenewSubTaskByIdAndLoad() {
        URI url = URI.create("http://localhost:8008/tasks/epic");
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonFirstEpic = gson.toJson(firstEpic);
        String epicId = requestToFindIdGet(url, gsonFirstEpic);
        URI firstUrl = URI.create("http://localhost:8008/tasks/subTask");
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, epicId,
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        String gsonFirstSubTask = gson.toJson(firstSubTask);
        String subTaskId = requestToFindIdGet(firstUrl, gsonFirstSubTask);
        SubTask secondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, epicId,
                LocalDateTime.of(2022, 3, 16, 10, 0), 3);
        URI secondUrl = URI.create("http://localhost:8008/tasks/subTask/?id=" + subTaskId);
        String gsonSecondSubTask = gson.toJson(secondSubTask);
        requestToFindIdGet(secondUrl, gsonSecondSubTask);
        HTTPTaskManager httpTaskManager = new HTTPTaskManager();
        httpTaskManager.loadFromServer();
        SubTask oldTask = (SubTask) getTaskByName(httpTaskManager, "Изучить Дженерики");
        assertNull(oldTask);
        SubTask renewedSubTask = (SubTask) getTaskByName(httpTaskManager, "Изучить полиморфизм");
        assertNotNull(renewedSubTask);
        assertEquals("Изучить перегрузку методов", renewedSubTask.getDescription());
        assertEquals(TaskStatus.NEW, renewedSubTask.getStatus());
        assertEquals("2022-03-16T10:00", renewedSubTask.getStartTime().toString());
        assertEquals(3, renewedSubTask.getDuration());
    }

    // Проверка метода deleteTaskById, удаление подзадачи, восстановление менеджера из сервера
    @Test
    public void shouldDeleteSubTaskByIdAndLoad() {
        URI url = URI.create("http://localhost:8008/tasks/epic");
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonFirstEpic = gson.toJson(firstEpic);
        String id = requestToFindIdGet(url, gsonFirstEpic);
        URI firstUrl = URI.create("http://localhost:8008/tasks/subTask");
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, id,
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        String gsonFirstSubTask = gson.toJson(firstSubTask);
        String subTaskId = requestToFindIdGet(firstUrl, gsonFirstSubTask);
        URI secondUrl = URI.create("http://localhost:8008/tasks/subTask/?id=" + subTaskId);
        assertTrue(requestDelete(secondUrl));
    }

    private String requestToFindIdGet(URI url, String json) {
        String id = "";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                POST(HttpRequest.BodyPublishers.ofString(json)).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            id = gson.fromJson(response.body(), String.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return id;
    }

    private Boolean requestDelete(URI url) {
        Boolean isDelete = true;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            isDelete = gson.fromJson(response.body(), Boolean.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return isDelete;
    }

    private Task getTaskByName(Manager inMemoryManager, String name) {
        for (Task task : inMemoryManager.getAllItems().values()) {
            if (task.getName().equals(name)) {
                return task;
            }
        }
        return null;
    }
}