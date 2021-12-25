import java.util.HashMap;

public class Manager {
    private HashMap<String, Task> allTasks = new HashMap<>();

    public void setAllTasks(String id, Task someTask) {
        allTasks.put(id, someTask);
    }

    public HashMap<String, Task> getAllTasks() {
        return allTasks;
    }

    public void showAllTasks() {
        for (Task tasks : getAllTasks().values()) {
            System.out.println(tasks);
        }
    }

    public void showSpecificTask(String id) {
        if (getAllTasks().containsKey(id)) {
            System.out.println(getAllTasks().get(id));
        }
    }

    public void taskUpdate(String id, String name, String description, TaskStatus status) {
        if (getAllTasks().containsKey(id)) {
            Task task = new Task(name, description, status);
            getAllTasks().put(id, task);
        } else {
            System.out.println("В коллекции нет указанного id");
        }
    }


}
