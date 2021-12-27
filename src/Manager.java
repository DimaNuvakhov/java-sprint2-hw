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

    public Epic createEpicAndOneSubTask(String epicName,
                                        String epicDescription,
                                        TaskStatus epicStatus, // TODO убрать этот параметр
                                        String subTaskName,
                                        String subTaskDescription,
                                        TaskStatus subTaskStatus) {
        Epic epic = new Epic(epicName,
                epicDescription,
                epicStatus, // TODO убрать этот параметр
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

    public void showSpecificTask(String id) {
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
            Epic epic = (Epic)allTasks.get(id);
            System.out.println(epic.showSubTaskList());
        } else {
            System.out.println("Данных не найдено");
        }
        System.out.println();
    }

    public void removesEntityById(String id) {
        System.out.println("== Удаление сущности, id = " + id);
        if (allTasks.containsKey(id)) {
            allTasks.get(id).delete();
        } else {
            System.out.println("== Сущность для удаления не найдена, id = " + id + "\n");
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

    public void printAllSubTasks(String id) {
        String verticalTableBorder = "|";
        String horizontalTableBorder = "-------------------------------------"
                + "---------------------------------------------------------------------------------------------";
        System.out.println(verticalTableBorder + Manager.padLeft("<id>", 35) + verticalTableBorder
                + Manager.padLeft("<Название>", 20)
                + verticalTableBorder + (Manager.padLeft("<Описание>", 50) + verticalTableBorder)
                + (Manager.padLeft("<Статус>", 20) + verticalTableBorder));
        if (allTasks.containsKey(id)) {
           // for (Task task : allTasks.get(id)) { Не понимаю! Мы должны по id найти эпик в мапе, а затем пройтись по
            // мапе из субтасков и распечатать, но я не могу найти эпик, по какой то причине, я все перепробовал
            //}
        }
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}

