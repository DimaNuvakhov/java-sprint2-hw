import java.util.HashMap;

public class Manager {
    private final HashMap<String, Task> allTasks = new HashMap<>();

    public void addOneTaskIntoMap(String id, Task task) { // TODO этот метод удалить. У нас для добавления есть методы create... и add...
        allTasks.put(id, task);
    }

    public Task createTask(String name, String description, TaskStatus taskStatus) {
        Task task = new Task(name, description, taskStatus);
        addOneTaskIntoMap(task.getId(), task); // TODO заменить на allTasks.put(..
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

    //TODO addSubTaskInEpic очень похож на предыдущий createEpicAndOneSubTask
    // Разница в том, что мы эпик не создаем а передаем через параметр.

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
        System.out.println("== Вывод задачи по произвольному идентификатору = " + id + "  ==");
        if (allTasks.containsKey(id)) {
            System.out.println(allTasks.get(id));
        }
        System.out.println();
//        System.out.println("== Окончание вывода задачи с id = " + id + "  ==");
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

