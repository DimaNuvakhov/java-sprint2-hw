import java.util.HashMap;

public class Manager {
    private final HashMap<String, Task> allTasks = new HashMap<>();

    public Task createTask(String name, String description, TaskStatus taskStatus) {
        Task task = new Task(name, description, taskStatus);
        allTasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpicAndOneSubTask(String epicName, String epicDescription,
                                        TaskStatus epicStatus, String subTaskName,
                                        String subTaskDescription, TaskStatus subTaskStatus) {
        Epic epic = new Epic(epicName, epicDescription, epicStatus);
        SubTask subTask = new SubTask(subTaskName, subTaskDescription, subTaskStatus, epic);
        allTasks.put(epic.getId(), epic);
        allTasks.put(subTask.getId(), subTask);
        return epic;
    }

    public SubTask addSubTaskIntoEpic(Epic epic, String subTaskName,
                                      String subTaskDescription, TaskStatus subTaskStatus) {
        SubTask subTask = new SubTask(subTaskName, subTaskDescription, subTaskStatus, epic);
        allTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    public void showAllTasks() {
        System.out.println("== Начало полного списка задач ==");
        for (Task task : allTasks.values()) {
            if (!(task.getClass().getName().equals("SubTask"))) {
                System.out.println(task);
            }
        }
        System.out.println("== Окончание полного списка задач ==\n");
    }

    public void showSpecificTask(String id) {
        System.out.println("== Начало вывода задачи с id = " + id + "  ==");
        if (allTasks.containsKey(id)) {
            System.out.println(allTasks.get(id));
        }
        System.out.println();
    }

    public void showAllEpics() {
        System.out.println("== Начало списка Эпиков ==");
        for (Task task : allTasks.values()) {
            if (task.getClass().getName().equals("Epic")) {
                System.out.println(task);
            }
        }
        System.out.println("== Окончание списка Эпиков ==\n");
    }

    public void deleteOneEntityById(String id) {
        if (allTasks.containsKey(id)) {
            allTasks.get(id).delete();
        }
    }

    public void deleteAll() {
        for (Task task : allTasks.values()) {
            if (!(task.getClass().getName().equals("SubTask"))) {
                task.delete();
            }
        }
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}

