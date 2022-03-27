import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

public class Main {

    public static void main(String[] args) throws IOException {
        new KVServer().start();
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        URI urlAddress = URI.create("http://localhost:8079");
        HTTPTaskManager httpTaskManager = new HTTPTaskManager();

        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);

        httpTaskManager.addTask(firstTask);

        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);

        httpTaskManager.renewTaskById(firstTask.getId(), secondTask);
        System.out.println(httpTaskManager.getAllItems());


//        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
//        httpTaskManager.addEpic(firstEpic);
//
//
//        SubTask firstSubTask = new SubTask("Изучить Дженерики",
//                "Изучить случаи применения дженериков", TaskStatus.DONE, firstEpic.getId(),
//                LocalDateTime.of(2022, 3, 18, 10, 0), 4);
//
//        SubTask secondSubTask = new SubTask("Изучить полиморфизм",
//                "Изучить перегрузку методов", TaskStatus.NEW, firstEpic.getId(),
//                LocalDateTime.of(2022, 3, 16, 10, 0), 3);
//        httpTaskManager.addSubTaskIntoEpic(firstSubTask);
//        httpTaskManager.addSubTaskIntoEpic(secondSubTask);
//
//        httpTaskManager.getTaskById(firstTask.getId());
//        httpTaskManager.getTaskById(firstEpic.getId());
//        System.out.println("ИСТОРИЯ ДО");
//        System.out.println(httpTaskManager.history());
//        httpTaskManager = null;
//        HTTPTaskManager newHttpTaskManager = new HTTPTaskManager();
//        newHttpTaskManager.loadFromServer();
//        System.out.println("ПОСЛЕ ЛОАДА");
//        System.out.println(newHttpTaskManager.getAllItems());
//        System.out.println(newHttpTaskManager.history());
//        Task secondTask = new Task("Помыть машину", "Помыть кузов и салон", TaskStatus.NEW,
//                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
//        newHttpTaskManager.addTask(firstTask);
//        newHttpTaskManager.addTask(secondTask);
//        newHttpTaskManager = null;
//        HTTPTaskManager secondHttpTaskManager = new HTTPTaskManager();
//        secondHttpTaskManager.loadFromServer();
//        secondHttpTaskManager.getTaskById(secondTask.getId());
//        System.out.println(secondHttpTaskManager.getAllItems());
//        System.out.println(secondHttpTaskManager.history());
//        secondHttpTaskManager.deleteTaskById(secondTask.getId());
//        System.out.println("УДАЛИЛИ ЗАДАЧУ");
//        System.out.println(secondHttpTaskManager.getAllItems());
//        System.out.println(secondHttpTaskManager.history());
//        secondHttpTaskManager.deleteAllTasks();
//        System.out.println(secondHttpTaskManager.getAllItems());
//        System.out.println(secondHttpTaskManager.history());
    }
}
