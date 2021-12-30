import java.util.HashMap;

public class Manager {
    private final HashMap<String, Task> allTasks = new HashMap<>();

    public void deleteTask(Task task) {
        allTasks.remove(task.getId());
    }

    public Task createTask(String name, String description, TaskStatus taskStatus) {
        Task task = new Task(name, description, taskStatus, this);
        allTasks.put(task.getId(), task);
        return task;
    }

    public void deleteAllTasks() {
        allTasks.clear(); //
    }

    public Epic createEpicAndOneSubTask(String epicName,
                                        String epicDescription,
                                        String subTaskName,
                                        String subTaskDescription,
                                        TaskStatus subTaskStatus) {
        Epic epic = new Epic(epicName,
                epicDescription,
                this
        );
        SubTask subTask = new SubTask(subTaskName, subTaskDescription, subTaskStatus, epic, this);
        allTasks.put(epic.getId(), epic);
        allTasks.put(subTask.getId(), subTask);
        return epic;
    }

    public SubTask addSubTaskIntoEpic(Epic epic, String subTaskName,
                                      String subTaskDescription, TaskStatus subTaskStatus) {
        SubTask subTask = new SubTask(subTaskName, subTaskDescription, subTaskStatus, epic, this);
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

    public void showTaskById(String id) {
        System.out.println("== Начало вывода задачи с id = " + id + "  ==");
        if (allTasks.containsKey(id)) {
            System.out.println(allTasks.get(id));
        } else {
            System.out.println("Данных нет");
        }
        System.out.println();
    }

    public void showSubTaskListFromEpicById(String id) {
        System.out.println("== Вывод списка подзадач для Эпика с id = " + id + "  ==");
        if (allTasks.containsKey(id) && allTasks.get(id).getClass().getName().equals("Epic")) {
            Epic epic = (Epic) allTasks.get(id);
            System.out.println(epic.showSubTaskList());
        } else {
            System.out.println("Данных не найдено");
        }
    }

    public void deleteTaskById(String id) {
        System.out.println("== Удаление сущности, id = " + id);
        if (allTasks.containsKey(id)) {
            allTasks.get(id).delete();
        } else {
            System.out.println("== Сущность для удаления не найдена, id = " + id + "\n");
        }
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

    public void showAllEpics() {
        System.out.println("== Начало списка Эпиков ==");
        for (Task task : allTasks.values()) {
            if (task.getClass().getName().equals("Epic")) {
                System.out.println(task);
            }
        }
        System.out.println("== Окончание списка Эпиков ==\n");
    }

    public void deleteAllTasksInEpicById(String id) {
        if (allTasks.containsKey(id) && allTasks.get(id).getClass().getName().equals("Epic")) {
            Epic epic = (Epic)allTasks.get(id);
            epic.deleteAllSubtaskInEpic();
        }
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
