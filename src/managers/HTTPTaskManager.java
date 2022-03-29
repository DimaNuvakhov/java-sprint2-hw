package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import http.KVTaskClient;
import http.LocalDateTimeAdapter;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HTTPTaskManager extends InMemoryManager {
    public KVTaskClient kvTaskClient;

    public HTTPTaskManager() {
        kvTaskClient = new KVTaskClient();
    }

    @Override
    public void addTask(Task task) { // Проверен
        super.addTask(task);
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonTaskList = gson.toJson(getAllTasks());
        kvTaskClient.save("task", gsonTaskList);
    }

    @Override
    public void addEpic(Epic epic) { // Проверен
        super.addEpic(epic);
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonEpicList = gson.toJson(getAllEpics());
        kvTaskClient.save("epic", gsonEpicList);
    }

    @Override
    public void addSubTaskIntoEpic(SubTask subTask) { // Проверен
        super.addSubTaskIntoEpic(subTask);
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String gsonSubTaskList = gson.toJson(getAllSubtasks());
        kvTaskClient.save("subTask", gsonSubTaskList);
    }

    @Override
    public void getTaskById(String id) { // Проверен
        super.getTaskById(id);
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        ArrayList<String> ids = new ArrayList<>();
        for (Task task : history()) {
            ids.add(task.getId());
        }
        String gsonHistory = gson.toJson(ids);
        kvTaskClient.save("history", gsonHistory);
    }

    @Override
    public void renewTaskById(String oldId, Task task) {
        super.renewTaskById(oldId, task);
        saveManagerStatus();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        saveManagerStatus();
        clearHistory();
    }

    public void clearHistory() {
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String jsonHistory = kvTaskClient.load("history");
        if (!jsonHistory.isEmpty()) {
            ArrayList<String> history = new ArrayList<>();
            String newGsonHistory = gson.toJson(history);
            System.out.println("УДАЛЯЮ!!!!!!!!!!!!!!");
            kvTaskClient.save("history", newGsonHistory);
        }
    }

    @Override
    public Boolean deleteTaskById(String id) {
        boolean isDelete = super.deleteTaskById(id);
        saveManagerStatus();
        return isDelete;
    }

   private void saveManagerStatus() {
       Gson gson = new GsonBuilder().
               registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
       String gsonTaskList = gson.toJson(getAllTasks());
       kvTaskClient.save("task", gsonTaskList);
       String gsonEpicList = gson.toJson(getAllEpics());
       kvTaskClient.save("epic", gsonEpicList);
       String gsonSubTaskList = gson.toJson(getAllSubtasks());
       kvTaskClient.save("subTask", gsonSubTaskList);
   }

    public void loadFromServer() {
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String jsonTasks = kvTaskClient.load("task");
        ArrayList<Task> tasks = gson.fromJson(jsonTasks, new TypeToken<ArrayList<Task>>() {
        }.getType());
        if (!jsonTasks.isEmpty()) {
            for (Task task : tasks) {
                addTask(task);
            }
        }
        String jsonEpics = kvTaskClient.load("epic");
        ArrayList<Epic> epics = gson.fromJson(jsonEpics, new TypeToken<ArrayList<Epic>>() {
        }.getType());
        if (!jsonEpics.isEmpty()) {
            for (Epic epic : epics) {
                addEpic(epic);
            }
        }
        String jsonSubTasks = kvTaskClient.load("subTask");
        ArrayList<SubTask> subTasks = gson.fromJson(jsonSubTasks, new TypeToken<ArrayList<SubTask>>() {
        }.getType());
        if (!jsonSubTasks.isEmpty()) {
            for (SubTask subTask : subTasks) {
                addSubTaskIntoEpic(subTask);
            }
        }
        String jsonHistory= kvTaskClient.load("history");
        if (!jsonHistory.isEmpty()) {
            ArrayList<String> history = gson.fromJson(jsonHistory, new TypeToken<ArrayList<String>>() {
            }.getType());
            for (int i = history.size() - 1; i >= 0; i--) {
                getTaskById(history.get(i));
            }
        }
    }
}


