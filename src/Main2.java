import managers.FileBackedManager;
import imanagers.Manager;
import mylibrary.MyLibrary;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;

public class Main2 {
    public static void main(String[] args) {
        // Создание файла
        File file = new File("Data.csv");
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Просмотр всех задач
        MyLibrary.showAllItems(fileBackedManager);
        // Просмотр истории
        System.out.println(MyLibrary.printHistory(fileBackedManager));
        // Создание новой задачи
        Task firstTask = new Task("Купить мышку", "Купить мышку Apple", TaskStatus.NEW);
        // Добавление задачи в трекер задач
        fileBackedManager.addTask(firstTask);
        // Просмотр всех задач
        MyLibrary.showAllItems(fileBackedManager);
        // Добавление задачи в историю
        fileBackedManager.getTaskById(firstTask.getId());
        // Просмотр истории
        System.out.println(MyLibrary.printHistory(fileBackedManager));
    }
}
