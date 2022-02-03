package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Manager {

    // Добавление задачи
    void addTask(Task task);

    // Добавление эпика
    void addEpic(Epic epic);

    // Добавление подзадачи к определенному эпику
    void addSubTaskIntoEpic(SubTask subTask);

    // Получение списка всех сущностей
    HashMap<String, Task> getAllItems();

    // Получение списка всех задач
    ArrayList<Task> getAllTasks();

    // Получение списка всех эпиков
    ArrayList<Epic> getAllEpics();

    // Получение всех подзадач
    ArrayList<SubTask> getAllSubtasks();

    // Получение определенной задачи по id
    void getTaskById(String id);

    // Получение всех подзадач определенного эпика
    ArrayList<SubTask> getSubTaskListFromEpicById(String id);

    // Удаление всех задач
    void deleteAllTasks();

    // Удаление задачи, эпика и подзадачи по id
    void deleteTaskById(String id);

    // Удаление задачи
    void deleteTask(Task task);

    // Удаление эпика
    void deleteEpic(Epic epic);

    // Удаление подзадачи из эпика
    void deleteSubTaskFromEpic(SubTask subTask);

    // Обновление задачи по id
    void renewTaskById(String oldId, Task task);

    // Вычисление статуса эпика
    TaskStatus calcStatus(Epic epic);

    // История просмотра задач
    List<Task> history();

}

