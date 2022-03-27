import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import http.KVServer;
import http.LocalDateTimeAdapter;
import managers.HTTPTaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException {
        new KVServer().start();
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        URI urlAddress = URI.create("http://localhost:8079");
        HTTPTaskManager httpTaskManager = new HTTPTaskManager(urlAddress);

        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        httpTaskManager.addTask(firstTask);
        String json = httpTaskManager.kvTaskClient.load("data");
        HashMap<String, Task> allTasks = gson.fromJson(json, new TypeToken<HashMap<String, Task>>() {
        }.getType());
        System.out.println(allTasks.get(firstTask.getId()));

        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        httpTaskManager.addEpic(firstEpic);
        String json1 = httpTaskManager.kvTaskClient.load("data");
        HashMap<String, Task> allTasks1 = gson.fromJson(json1, new TypeToken<HashMap<String, Task>>() {
        }.getType());
        System.out.println(allTasks1);

        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.DONE, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        httpTaskManager.addSubTaskIntoEpic(firstSubTask);
        String json2 = httpTaskManager.kvTaskClient.load("data");
        HashMap<String, Task> allTasks2 = gson.fromJson(json2, new TypeToken<HashMap<String, Task>>() {
        }.getType());
        System.out.println(allTasks2);
        Epic epic = (Epic) allTasks2.get(firstEpic.getId());
        System.out.println(epic);
    }
}
