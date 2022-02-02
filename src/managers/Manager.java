package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

public interface Manager {
    void addTask(Task task);

    // Добавление эпика
    void addEpic(Epic epic);

    // Добавление подзадачи к определенному эпику
    void addSubTaskIntoEpic(SubTask subTask);

    // Получение списка всех задач
    void showAllTasks();

    // Получение всех эпиков
    void showAllEpics();

    // Получение определенной задачи по id
    void showTaskById(String id);

    // Получение всех подзадач определенного эпика
    void showSubTaskListFromEpicById(String id);

    // Получение всех подзадач определенного эпика
    String showSubTaskList(Epic epic);

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

    void renewTaskById(String oldId, Task task);

    TaskStatus calcStatus(Epic epic);

    String printHistory();
}

