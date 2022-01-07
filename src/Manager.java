import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private final HashMap<String, Task> allTasks = new HashMap<>();

    // Создание Task
    public Task createTask(String name, String description, TaskStatus taskStatus) {
        Task task = new Task(name, description, taskStatus);
        allTasks.put(task.getId(), task);
        return task;
    }

    // Создание Epic и SubTask
    public Epic createEpicAndOneSubTask(String epicName,
                                        String epicDescription,
                                        String subTaskName,
                                        String subTaskDescription,
                                        TaskStatus subTaskStatus) {
        Epic epic = new Epic(epicName, epicDescription);
        SubTask subTask = new SubTask(subTaskName, subTaskDescription, subTaskStatus, epic);
        allTasks.put(epic.getId(), epic);
        allTasks.put(subTask.getId(), subTask);
        return epic;
    }

    // Добавление Subtask к определенному Epic
    public SubTask addSubTaskIntoEpic(Epic epic, String subTaskName,
                                      String subTaskDescription, TaskStatus subTaskStatus) {
        SubTask subTask = new SubTask(subTaskName, subTaskDescription, subTaskStatus, epic);
        allTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    // Получение списка всех Task
    public void showAllTasks() {
        System.out.println("== Начало полного списка задач ==");
        for (Task task : allTasks.values()) {
            if (!(task.getClass().getName().equals("SubTask"))) {
                System.out.println(task);
            }
        }
        System.out.println("== Окончание полного списка задач ==\n");
    }

    // Получение всех эпиков
    public void showAllEpics() {
        System.out.println("== Начало списка Эпиков ==");
        for (Task task : allTasks.values()) {
            if (task.getClass().getName().equals("Epic")) {
                System.out.println(task);
            }
        }
        System.out.println("== Окончание списка Эпиков ==\n");
    }

    // Получение определенной Task по id
    public void showTaskById(String id) {
        System.out.println("== Начало вывода задачи с id = " + id + "  ==");
        if (allTasks.containsKey(id)) {
            System.out.println(allTasks.get(id));
        } else {
            System.out.println("Данных нет");
        }
        System.out.println();
    }

    // Получение всех Subtask определенного Epic по id
    public void showSubTaskListFromEpicById(String id) {
        System.out.println("== Вывод списка подзадач для Эпика с id = " + id + "  ==");
        if (allTasks.containsKey(id) && allTasks.get(id).getClass().getName().equals("Epic")) {
            Epic epic = (Epic) allTasks.get(id);
            System.out.println(showSubTaskList(epic));
        } else {
            System.out.println("Данных не найдено");
        }
    }

    // Получение всех Subtask определенного Epic
    public String showSubTaskList(Epic epic) {
        StringBuilder value = new StringBuilder();
        for (SubTask subTask : epic.getSubTasks().values()) {
            value.append(subTask.toString()).append("\n");
        }
        return value.toString();
    }

    // Удаление всех задач
    public void deleteAllTasks() {
        allTasks.clear(); //
    }

    // Удаление Task, Epic, SubTask по id
    public void deleteTaskById(String id) {
        System.out.println("== Удаление сущности, id = " + id);
        if (allTasks.containsKey(id)) {
            if (allTasks.get(id).getClass().getName().equals("Task")) {
                deleteTask(allTasks.get(id));
            } else if (allTasks.get(id).getClass().getName().equals("Epic")) {
                Epic epic = (Epic) allTasks.get(id);
                deleteEpic(epic);
            } else {
                SubTask subTask = (SubTask) allTasks.get(id);
                deleteSubTaskFromEpic(subTask);
                allTasks.remove(subTask.getId());
            }
        } else {
            System.out.println("== Сущность для удаления не найдена, id = " + id + "\n");
        }
    }

    // Удаление задачи
    public void deleteTask(Task task) {
        allTasks.remove(task.getId());
    }

    // Удаляем Эпик
    public void deleteEpic(Epic epic) {
        ArrayList<String> toRemove = new ArrayList<>();
        for (SubTask subTask : epic.getSubTasks().values()) {
            toRemove.add(subTask.getId());
        }
        for (String id : toRemove) {
            epic.getSubTasks().remove(id);
        }
        deleteTask(epic);
        System.out.println("Удаляем Epic, id = " + epic.getId());
        System.out.println();
    }

    // Удаляем SubTask из Epic
    public void deleteSubTaskFromEpic(SubTask subTask) {
        subTask.getEpic().getSubTasks().remove(subTask.getId());
    }

    public void setTaskName(String id, String name) {
        System.out.println("== Обновление имени сущности, id = " + id);
        if (allTasks.containsKey(id)) {
            allTasks.get(id).setName(name);
        }
    }

    public void setTaskDescription(String id, String description) {
        System.out.println("== Обновление описания сущности, id = " + id);
        if (allTasks.containsKey(id)) {
            allTasks.get(id).setDescription(description);
        }
    }

    public void setTaskStatus(String id, TaskStatus taskStatus) {
        System.out.println("== Обновление статуса сущности, id = " + id);
        if (allTasks.containsKey(id)) {
            allTasks.get(id).setStatus(taskStatus);
        }
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
