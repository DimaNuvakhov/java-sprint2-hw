import managers.FileBackedManager;
import mylibrary.MyLibrary;
import tasks.Epic;
import imanagers.Manager;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла и проверка удаления
        boolean isDelete = file.delete();
        System.out.println(isDelete);
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        // Добавление задачи в трекер задач
        fileBackedManager.addTask(firstTask);
        // Создание эпика
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        // Добавление эпика в трекер задач
        fileBackedManager.addEpic(firstEpic);
        // Создание субтасок к эпику
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId());
        SubTask firstEpicSecondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.NEW, firstEpic.getId());
        SubTask firstEpicThirdSubTask = new SubTask("Убрать в доме",
                "Убрать в доме перед отъездом", TaskStatus.DONE, firstEpic.getId());
        // Добавление субтасок в трекер задач
        fileBackedManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        fileBackedManager.addSubTaskIntoEpic(firstEpicSecondSubTask);
        fileBackedManager.addSubTaskIntoEpic(firstEpicThirdSubTask);
        // Добавление задач в историю
        fileBackedManager.getTaskById(firstTask.getId());
        fileBackedManager.getTaskById(firstEpicFirstSubTask.getId());
        fileBackedManager.getTaskById(firstEpic.getId());
        // Просмотр всех задач
        MyLibrary.showAllItems(fileBackedManager);
        // Просмотр истории
        System.out.println(MyLibrary.printHistory(fileBackedManager));
    }
}