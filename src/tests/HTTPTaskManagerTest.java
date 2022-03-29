package tests;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
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
import java.util.ArrayList;
import java.util.List;

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
    public void shouldAddTaskAndLoad() {
        // Очистка хранилища данных  для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
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
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
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
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
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
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
        URI url = URI.create("http://localhost:8008/tasks/epic");
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonEpic = gson.toJson(firstEpic);
        requestToFindIdGet(url, gsonEpic);
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
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
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
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
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
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
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
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
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
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
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

    // Проверка метода getAllTasks, восстановление менеджера из сервера
    @Test
    public void shouldReturnTaskListAndLoad() {
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 16, 10, 0), 3);
        URI url = URI.create("http://localhost:8008/tasks/task");
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonFirstTask = gson.toJson(firstTask);
        String gsonSecondTask = gson.toJson(secondTask);
        String firstTaskId = requestToFindIdGet(url, gsonFirstTask);
        String secondTaskId = requestToFindIdGet(url, gsonSecondTask);
        // Проверяю эндпойнт Get возвращаю все задачи менеджера
        ArrayList<Task> tasks = returnTaskList(url);
        assertEquals(2, tasks.size());
        for (Task task : tasks) {
            if (task.getId().equals(firstTaskId)) {
                assertEquals("Помыть посуду", task.getName());
                assertEquals("Помыть тарелки и вилки", task.getDescription());
                assertEquals(TaskStatus.NEW, task.getStatus());
                assertEquals("2022-03-15T10:00", task.getStartTime().toString());
                assertEquals(3, task.getDuration());
            } else if (task.getId().equals(secondTaskId)) {
                assertEquals("Купить хлеб", task.getName());
                assertEquals("Нужен хлеб \"Литовский\"", task.getDescription());
                assertEquals(TaskStatus.DONE, task.getStatus());
                assertEquals("2022-03-16T10:00", task.getStartTime().toString());
                assertEquals(3, task.getDuration());
            }
        }
    }

    // Проверка метода getAllEpics, восстановление менеджера из сервера
    @Test
    public void shouldReturnEpicListAndLoad() {
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        URI url = URI.create("http://localhost:8008/tasks/epic");
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonFirstEpic = gson.toJson(firstEpic);
        String gsonSecondEpic = gson.toJson(secondEpic);
        String firstEpicId = requestToFindIdGet(url, gsonFirstEpic);
        String secondEpicId = requestToFindIdGet(url, gsonSecondEpic);
        // Проверяю эндпойнт Get возвращаю все эпики менеджера
        ArrayList<Epic> epics = returnEpicList(url);
        assertEquals(2, epics.size());
        for (Epic epic : epics) {
            if (epic.getId().equals(firstEpicId)) {
                assertEquals("Переезд", epic.getName());
                assertEquals("Собрать все вещи", epic.getDescription());
            } else if (epic.getId().equals(secondEpicId)) {
                assertEquals("Изучение Java", epic.getName());
                assertEquals("Изучить язык программирования Java", epic.getDescription());
            }
        }
    }

    // Проверка метода getSubTaskListFromEpicById, восстановление менеджера из сервера
    @Test
    public void shouldReturnSubTaskListByEpicIdAndLoad() {
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        URI firstUrl = URI.create("http://localhost:8008/tasks/epic");
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonFirstEpic = gson.toJson(firstEpic);
        String gsonSecondEpic = gson.toJson(secondEpic);
        String firstEpicId = requestToFindIdGet(firstUrl, gsonFirstEpic);
        String secondEpicId = requestToFindIdGet(firstUrl, gsonSecondEpic);
        URI secondUrl = URI.create("http://localhost:8008/tasks/subTask");
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpicId,
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpicId,
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpicId,
                LocalDateTime.of(2022, 4, 22, 10, 0), 4);
        String gsonFirstEpicFirstSubTask = gson.toJson(firstEpicFirstSubTask);
        String gsonSecondEpicFirstSubTask = gson.toJson(secondEpicFirstSubTask);
        String gsonSecondEpicSecondSubTask = gson.toJson(secondEpicSecondSubTask);
        String firstEpicFirstSubTaskId = requestToFindIdGet(secondUrl, gsonFirstEpicFirstSubTask);
        String secondEpicFirstSubTaskId = requestToFindIdGet(secondUrl, gsonSecondEpicFirstSubTask);
        String secondEpicSecondSubTaskId = requestToFindIdGet(secondUrl, gsonSecondEpicSecondSubTask);
        URI thirdUrl = URI.create("http://localhost:8008/tasks/epic/?id=" + secondEpicId);
        // Проверяю эндпойнт Get возвращаю все подзадачи определенного эпика по id
        ArrayList<SubTask> subTasks = returnSubTaskList(thirdUrl);
        assertEquals(2, subTasks.size());
        for (SubTask subTask : subTasks) {
            if (subTask.getId().equals(secondEpicFirstSubTaskId)) {
                assertEquals("Изучить Дженерики", subTask.getName());
                assertEquals("Изучить случаи применения дженериков", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(secondEpicId, subTask.getEpicId());
                assertEquals("2022-04-21T10:00", subTask.getStartTime().toString());
                assertEquals(4, subTask.getDuration());
            } else {
                assertEquals("Изучить полиморфизм", subTask.getName());
                assertEquals("Изучить перегрузку методов", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(secondEpicId, subTask.getEpicId());
                assertEquals("2022-04-22T10:00", subTask.getStartTime().toString());
                assertEquals(4, subTask.getDuration());
            }
        }
    }

    // Проверка метода getAllSubTasks, восстановление менеджера из сервера
    @Test
    public void shouldReturnSubTaskListAndLoad() {
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        URI firstUrl = URI.create("http://localhost:8008/tasks/epic");
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonFirstEpic = gson.toJson(firstEpic);
        String gsonSecondEpic = gson.toJson(secondEpic);
        String firstEpicId = requestToFindIdGet(firstUrl, gsonFirstEpic);
        String secondEpicId = requestToFindIdGet(firstUrl, gsonSecondEpic);
        URI secondUrl = URI.create("http://localhost:8008/tasks/subTask");
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpicId,
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpicId,
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpicId,
                LocalDateTime.of(2022, 4, 22, 10, 0), 4);
        String gsonFirstEpicFirstSubTask = gson.toJson(firstEpicFirstSubTask);
        String gsonSecondEpicFirstSubTask = gson.toJson(secondEpicFirstSubTask);
        String gsonSecondEpicSecondSubTask = gson.toJson(secondEpicSecondSubTask);
        String firstEpicFirstSubTaskId = requestToFindIdGet(secondUrl, gsonFirstEpicFirstSubTask);
        String secondEpicFirstSubTaskId = requestToFindIdGet(secondUrl, gsonSecondEpicFirstSubTask);
        String secondEpicSecondSubTaskId = requestToFindIdGet(secondUrl, gsonSecondEpicSecondSubTask);
        URI thirdUrl = URI.create("http://localhost:8008/tasks/subTask");
        ArrayList<SubTask> subTasks = returnSubTaskList(thirdUrl);
        assertEquals(3, subTasks.size());
        for (SubTask subTask : subTasks) {
            if (subTask.getId().equals(firstEpicFirstSubTaskId)) {
                assertEquals("Собрать чемодан", subTask.getName());
                assertEquals("Положить в чемодан все необходимое", subTask.getDescription());
                assertEquals(TaskStatus.DONE, subTask.getStatus());
                assertEquals(firstEpicId, subTask.getEpicId());
                assertEquals("2022-04-20T10:00", subTask.getStartTime().toString());
                assertEquals(4, subTask.getDuration());
            } else if (subTask.getId().equals(secondEpicFirstSubTaskId)) {
                assertEquals("Изучить Дженерики", subTask.getName());
                assertEquals("Изучить случаи применения дженериков", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(secondEpicId, subTask.getEpicId());
                assertEquals("2022-04-21T10:00", subTask.getStartTime().toString());
                assertEquals(4, subTask.getDuration());
            } else {
                assertEquals("Изучить полиморфизм", subTask.getName());
                assertEquals("Изучить перегрузку методов", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(secondEpicId, subTask.getEpicId());
                assertEquals("2022-04-22T10:00", subTask.getStartTime().toString());
                assertEquals(4, subTask.getDuration());
            }
        }
    }

    // Проверка методов history и getTaskById. Проверка истории, 2 задачи в истории, восстановление менеджера из сервера
    @Test
    public void shouldReturnHistoryAndLoad() {
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        URI url = URI.create("http://localhost:8008/tasks/task");
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonFirstTask = gson.toJson(firstTask);
        String gsonSecondTask = gson.toJson(secondTask);
        String firstTaskId = requestToFindIdGet(url, gsonFirstTask);
        String secondTaskId = requestToFindIdGet(url, gsonSecondTask);
        URI getFirstTaskByIdUrl = URI.create("http://localhost:8008/tasks/history/?id=" + firstTaskId);
        URI getSecondTaskByIdUrl = URI.create("http://localhost:8008/tasks/history/?id=" + secondTaskId);
        getHistoryRequest(getFirstTaskByIdUrl);
        getHistoryRequest(getSecondTaskByIdUrl);
        HTTPTaskManager httpTaskManager = new HTTPTaskManager();
        httpTaskManager.loadFromServer();
        List<Task> history = httpTaskManager.history();
        for (int i = history.size(); i >= 0; i--) {
            if (i == 0) {
                assertEquals("Купить хлеб", history.get(i).getName());
                assertEquals("Нужен хлеб \"Литовский\"", history.get(i).getDescription());
                assertEquals(TaskStatus.DONE, history.get(i).getStatus());
                assertEquals("2022-04-20T10:00", history.get(i).getStartTime().toString());
                assertEquals(4, history.get(i).getDuration());
            } else if (i == 1) {
                assertEquals("Помыть посуду", history.get(i).getName());
                assertEquals("Помыть тарелки и вилки", history.get(i).getDescription());
                assertEquals(TaskStatus.NEW, history.get(i).getStatus());
                assertEquals("2022-04-21T10:00", history.get(i).getStartTime().toString());
                assertEquals(4, history.get(i).getDuration());
            }
        }
    }

    // Проверка метода history и getTaskById. Проверка истории, дублирование, восстановление менеджера из сервера
    @Test
    public void shouldReturnHistoryWhenDuplicationAndLoad() {
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        URI url = URI.create("http://localhost:8008/tasks/task");
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonFirstTask = gson.toJson(firstTask);
        String gsonSecondTask = gson.toJson(secondTask);
        String firstTaskId = requestToFindIdGet(url, gsonFirstTask);
        String secondTaskId = requestToFindIdGet(url, gsonSecondTask);
        URI getFirstTaskByIdUrl = URI.create("http://localhost:8008/tasks/history/?id=" + firstTaskId);
        URI getSecondTaskByIdUrl = URI.create("http://localhost:8008/tasks/history/?id=" + secondTaskId);
        getHistoryRequest(getFirstTaskByIdUrl);
        getHistoryRequest(getSecondTaskByIdUrl);
        getHistoryRequest(getFirstTaskByIdUrl);
        HTTPTaskManager httpTaskManager = new HTTPTaskManager();
        httpTaskManager.loadFromServer();
        List<Task> history = httpTaskManager.history();
        for (int i = history.size(); i >= 0; i--) {
            if (i == 0) {
                assertEquals("Помыть посуду", history.get(i).getName());
                assertEquals("Помыть тарелки и вилки", history.get(i).getDescription());
                assertEquals(TaskStatus.NEW, history.get(i).getStatus());
                assertEquals("2022-04-21T10:00", history.get(i).getStartTime().toString());
                assertEquals(4, history.get(i).getDuration());
            } else if (i == 1) {
                assertEquals("Купить хлеб", history.get(i).getName());
                assertEquals("Нужен хлеб \"Литовский\"", history.get(i).getDescription());
                assertEquals(TaskStatus.DONE, history.get(i).getStatus());
                assertEquals("2022-04-20T10:00", history.get(i).getStartTime().toString());
                assertEquals(4, history.get(i).getDuration());
            }
        }
    }

    // Проверка правильности работы
    @Test
    public void shouldReloadManager() {
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
        URI taskUrl = URI.create("http://localhost:8008/tasks/task");
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonTask = gson.toJson(firstTask);
        requestToFindIdGet(taskUrl, gsonTask);
        URI epicUrl = URI.create("http://localhost:8008/tasks/epic");
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        String gsonEpic = gson.toJson(firstEpic);
        String epicId = requestToFindIdGet(epicUrl, gsonEpic);
        URI subTaskUrl = URI.create("http://localhost:8008/tasks/subTask");
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, epicId,
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        String gsonFirstSubTask = gson.toJson(firstSubTask);
        requestToFindIdGet(subTaskUrl, gsonFirstSubTask);
        HTTPTaskManager httpTaskManager = new HTTPTaskManager();
        httpTaskManager.loadFromServer();
        for (Task task : httpTaskManager.getAllItems().values()) {
            if (task.getClass().getName().equals("tasks.Task")) {
                assertEquals("Помыть посуду", task.getName());
                assertEquals("Помыть тарелки и вилки", task.getDescription());
                assertEquals(TaskStatus.NEW, task.getStatus());
                assertEquals("2022-03-15T10:00", task.getStartTime().toString());
                assertEquals(3, task.getDuration());
            } else if (task.getClass().getName().equals("tasks.Epic")) {
                assertEquals("Изучение Java", task.getName());
                assertEquals("Изучить язык программирования Java", task.getDescription());
            } else {
                SubTask subTask = (SubTask) task;
                assertEquals("Изучить Дженерики", subTask.getName());
                assertEquals("Изучить случаи применения дженериков", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(epicId, subTask.getEpicId());
                assertEquals("2022-03-15T10:00", subTask.getStartTime().toString());
                assertEquals(4, subTask.getDuration());
            }
        }
    }

    // Проверка метода getAllTasks, пустой список задач
    @Test
    public void shouldReturnZeroWhenTaskListIsEmpty() {
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
        URI url = URI.create("http://localhost:8008/tasks/task");
        ArrayList<Task> tasks = returnTaskList(url);
        assertEquals(0, tasks.size());
    }

    // Проверка метода getAllSubTasks, пустой список задач
    @Test
    public void shouldReturnZeroWhenSubTaskListIsEmpty() {
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
        URI url = URI.create("http://localhost:8008/tasks/epic");
        ArrayList<Epic> epics = returnEpicList(url);
        assertEquals(0, epics.size());
    }

    // Проверка метода history. Проверка истории просмотра задач, пустая история
    @Test
    public void shouldReturnZeroIfZeroTasksInHistory() {
        // Очистка хранилища данных для тестов
        URI deleteAllUrl = URI.create("http://localhost:8008/tasks/deleteAll");
        requestDelete(deleteAllUrl);
        URI url = URI.create("http://localhost:8008/tasks/subTask");
        ArrayList<SubTask> subTasks  = returnSubTaskList(url);
        assertEquals(0, subTasks.size());
    }

    private void getHistoryRequest(URI url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    private ArrayList<Task> returnTaskList(URI url) {
        ArrayList<Task> tasks = null;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
            }.getType());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return tasks;
    }

    private ArrayList<Epic> returnEpicList(URI url) {
        ArrayList<Epic> epics = null;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            epics = gson.fromJson(response.body(), new TypeToken<ArrayList<Epic>>() {
            }.getType());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return epics;
    }

    private ArrayList<SubTask> returnSubTaskList(URI url) {
        ArrayList<SubTask> subTasks = null;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
            subTasks = gson.fromJson(response.body(), new TypeToken<ArrayList<SubTask>>() {
            }.getType());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return subTasks;
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