import inmemorymanagers.FileBackedManager;
import mylibrary.MyLibrary;
import tasks.Epic;
import managers.Manager;
import inmemorymanagers.Managers;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        // Создаю файл
        File file = new File("Data.csv");
        // Удаление файла и проверка удаления
        boolean isDelete = file.delete();
        System.out.println(isDelete);
        // Создаю менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создали задачу, эпик и подзадачу
        Task firstTask = Managers.createTask("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Epic firstEpic = Managers.createEpic("Переезд", "Собрать все вещи");
        SubTask firstEpicFirstSubTask = Managers.createSubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId());
        SubTask firstEpicSecondSubTask = Managers.createSubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.NEW, firstEpic.getId());
        SubTask firstEpicThirdSubTask = Managers.createSubTask("Убрать в доме",
                "Убрать в доме перед отъездом", TaskStatus.DONE, firstEpic.getId());
        Epic secondEpic = Managers.createEpic("Обучение", "Обучение JAVA");
        SubTask secondEpicFirstSubTask = Managers.createSubTask("Изучить ArrayList",
                "Научиться добавлять и удалять из ArrayList", TaskStatus.NEW, secondEpic.getId());
        SubTask secondEpicSecondSubTask = Managers.createSubTask("Изучить private",
                "Понять действие модификатора доступа private", TaskStatus.NEW, secondEpic.getId());
        SubTask secondEpicThirdSubTask = Managers.createSubTask("Изучить Override",
                "Научиться переопределять методы", TaskStatus.NEW, secondEpic.getId());
        // Добавляем все вышесозданное в трекер задач
        fileBackedManager.addTask(firstTask);
        fileBackedManager.addEpic(firstEpic);
        fileBackedManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        fileBackedManager.addSubTaskIntoEpic(firstEpicSecondSubTask);
        fileBackedManager.addSubTaskIntoEpic(firstEpicThirdSubTask);
        // Добавляю задачи в историю
        fileBackedManager.getTaskById(firstTask.getId());
        fileBackedManager.getTaskById(firstEpicFirstSubTask.getId());
        fileBackedManager.getTaskById(firstEpic.getId());
        // Просмотр всех задач
        MyLibrary.showAllItems(fileBackedManager);
        // Просмотр истории
        System.out.println(MyLibrary.printHistory(fileBackedManager));
    }
}