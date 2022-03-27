package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.KVServer;
import http.KVTaskClient;
import http.LocalDateTimeAdapter;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.net.URI;
import java.time.LocalDateTime;

public class HTTPTaskManager extends FileBackedManager{
    public KVTaskClient kvTaskClient;

    public HTTPTaskManager(URI url) {
        super(null);
        kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonManager = gson.toJson(getAllItems());
        kvTaskClient.save("task",gsonManager);
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonManager = gson.toJson(getAllItems());
        kvTaskClient.save("data",gsonManager);
    }

    @Override
    public void addSubTaskIntoEpic(SubTask subTask) {
        super.addSubTaskIntoEpic(subTask);
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonManager = gson.toJson(getAllItems());
        kvTaskClient.save("data",gsonManager);
    }

    @Override
    public void getTaskById(String id) {
        super.getTaskById(id);
    }

    @Override
    public void renewTaskById(String oldId, Task task) {
        super.renewTaskById(oldId, task);
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
    }

    @Override
    public Boolean deleteTaskById(String id) {
        return super.deleteTaskById(id);
    }

//    public void taskHashMap() {
//        for (Task)
//    }

}
