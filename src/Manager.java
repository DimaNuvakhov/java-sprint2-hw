import java.util.HashMap;
import java.util.Objects;

public class Manager {
    private HashMap<String, Task> allTasks = new HashMap<>();

    public void addOneTaskIntoMap(String id, Task task) {
        allTasks.put(id, task);
    }

    public Task createTask(String name, String description, TaskStatus taskStatus) {
        Task task = new Task(name, description, taskStatus);
        addOneTaskIntoMap(task.getId(), task);
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


    /*public SubTask addSubTaskInEpic(String id, String subTaskName,
                                    String subTaskDescription, TaskStatus subTaskStatus) throws ClassNotFoundException {
        for (Task specificTask : allTasks.values()) {
            if (specificTask.getClass() == Class.forName("Epic")) {
                if (Objects.equals(specificTask.getId(), id)) {
                    SubTask subTask = new SubTask(subTaskName, subTaskDescription, subTaskStatus, specificTask);
                }
            }
        }
    }*/
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public void showAllTasks() throws ClassNotFoundException {
        System.out.println("== Начало полного списка задач ==");
        for (Task task : allTasks.values()) {
            if (task.getClass() != Class.forName("SubTask")) {
                System.out.println(task);
            }
        }
        System.out.println("== Окончание полного списка задач ==");
    }

    public void showSpecificTask(String id) {
//        System.out.println("== Начало вывода задачи с id = " + id + "  ==");
        if (allTasks.containsKey(id)) {
            System.out.println(allTasks.get(id));
        }
//        System.out.println("== Окончание вывода задачи с id = " + id + "  ==");
    }


    public void showAllEpics() throws ClassNotFoundException {
        for (Task task : allTasks.values()) {
            if (task.getClass() == Class.forName("Epic")) {
                System.out.println(task);
            }
        }
    }

    /*public void addNewTask(String id, String newTaskName, String newTaskDescription, TaskStatus newTaskStatus)
            throws ClassNotFoundException {
        if (allTasks.containsKey(id)) {
            if (allTasks.get(id).getClass() != Class.forName("SubTask")) {
                Task task = new Task(newTaskName, newTaskDescription, newTaskStatus));
                allTasks.put(id, task);
            } else {
                SubTask subTask = new SubTask()
            }
            Task updatedTask = new Task();
            addOneTaskIntoMap();
        }
    }*
     !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}

