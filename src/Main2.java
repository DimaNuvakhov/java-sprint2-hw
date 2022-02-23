import inmemorymanagers.FileBackedManager;
import inmemorymanagers.Managers;
import managers.Manager;
import mylibrary.MyLibrary;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;

public class Main2 {
    public static void main(String[] args) {
        // Создаю файл
        File file = new File("Data.csv");
        // Создаю менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Просмотр всех задач
        MyLibrary.showAllItems(fileBackedManager);
        // Просмотр истории
        System.out.println(MyLibrary.printHistory(fileBackedManager));
        // Создаю новую задачу
        Task firstTask = Managers.createTask("Купить мышку", "Купить мышку Apple", TaskStatus.NEW);
        // Добавляю задачу в трекер задач
        fileBackedManager.addTask(firstTask);
        // Просмотр всех задач
        MyLibrary.showAllItems(fileBackedManager);
        // Добавляю задачи в историю
        fileBackedManager.getTaskById(firstTask.getId());
        // Просмотр истории
        System.out.println(MyLibrary.printHistory(fileBackedManager));
    }
}
