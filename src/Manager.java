import java.util.HashMap;

public class Manager {
    private HashMap<String, Task> allTasks = new HashMap<>();

    public void setOneTask(String id, Task task) {
        allTasks.put(id, task);
    }

    public HashMap<String, Task> getAllTasks() {
        return allTasks;
    }

    public void showAllTasks() throws ClassNotFoundException {
        System.out.println("== Начало полного списка задач ==");
        for (Task tasks : getAllTasks().values()) {
            if (tasks.getClass() != Class.forName("SubTask")) {
                System.out.println(tasks);
            }
        }
        System.out.println("== Окончание полного списка задач ==");
    }

    public void showSpecificTask(String id) {
//        System.out.println("== Начало вывода задачи с id = " + id + "  ==");
        if (getAllTasks().containsKey(id)) {
            System.out.println(getAllTasks().get(id));
        }
//        System.out.println("== Окончание вывода задачи с id = " + id + "  ==");
    }

    public void taskUpdate(String id, String name, String description, TaskStatus status) {
        if (getAllTasks().containsKey(id)) {
            Task task = new Task(name, description, status);
            getAllTasks().put(id, task);
        } else {
            System.out.println("В коллекции нет указанного id");
        }
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }


}

