package inmemorymanager;

import epic.Epic;
import manager.Manager;
import subtask.SubTask;
import task.Task;
import taskstatus.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryManager implements Manager {
    private final HashMap<String, Task> allTasks = new HashMap<>();
    private final ArrayList<Task> lastTenTasks = new ArrayList<>();

    // Добавление задачи
    @Override
    public void addTask(Task task) {
        allTasks.put(task.getId(), task);
    }

    // Добавление эпика
    @Override
    public void addEpic(Epic epic) {
        allTasks.put(epic.getId(), epic);
        epic.setStatus(calcStatus(epic));
    }

    // Добавление подзадачи к определенному эпику
    @Override
    public void addSubTaskIntoEpic(SubTask subTask) {
        if (allTasks.containsKey(subTask.getEpicId())) {
            Epic epic = (Epic) allTasks.get(subTask.getEpicId());
            epic.getSubTasks().put(subTask.getId(), subTask);
            allTasks.put(subTask.getId(), subTask);
            epic.setStatus(calcStatus(epic));
        }
    }

    // Получение списка всех задач
    @Override
    public void showAllTasks() {
        System.out.println("== Начало полного списка задач ==");
        for (Task task : allTasks.values()) {
            if (!(task.getClass().getName().equals("subtask.SubTask"))) {
                System.out.println(task);
            }
        }
        System.out.println("== Окончание полного списка задач ==\n");
    }

    // Получение всех эпиков
    @Override
    public void showAllEpics() {
        System.out.println("== Начало списка Эпиков ==");
        for (Task task : allTasks.values()) {
            if (task.getClass().getName().equals("epic.Epic")) {
                System.out.println(task);
            }
        }
        System.out.println("== Окончание списка Эпиков ==\n");
    }

    // Получение определенной задачи по id
    @Override
    public void showTaskById(String id) {
        System.out.println("== Начало вывода задачи с id = " + id + "  ==");
        if (allTasks.containsKey(id)) {
            System.out.println(allTasks.get(id));
        } else {
            System.out.println("Данных нет");
        }
        System.out.println();
    }

    // Получение всех подзадач определенного эпика
    @Override
    public void showSubTaskListFromEpicById(String id) {
        System.out.println("== Вывод списка подзадач для Эпика с id = " + id + "  ==");
        if (allTasks.containsKey(id) && allTasks.get(id).getClass().getName().equals("epic.Epic")) {
            Epic epic = (Epic) allTasks.get(id);
            System.out.println(showSubTaskList(epic));
        } else {
            System.out.println("Данных не найдено");
        }
    }

    // Получение всех подзадач определенного эпика
    @Override
    public String showSubTaskList(Epic epic) {
        StringBuilder value = new StringBuilder();
        for (SubTask subTask : epic.getSubTasks().values()) {
            value.append(subTask.toString()).append("\n");
        }
        return value.toString();
    }

    // Удаление всех задач
    @Override
    public void deleteAllTasks() {
        System.out.println("Удаляем все сущности");
        allTasks.clear(); //
    }

    // Удаление задачи, эпика и подзадачи по id
    @Override
    public void deleteTaskById(String id) {
        System.out.println("== Удаление сущности, id = " + id);
        if (allTasks.containsKey(id)) {
            if (allTasks.get(id).getClass().getName().equals("task.Task")) {
                deleteTask(allTasks.get(id));
            } else if (allTasks.get(id).getClass().getName().equals("epic.Epic")) {
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
    @Override
    public void deleteTask(Task task) {
        allTasks.remove(task.getId());
    }

    // Удаление эпика
    @Override
    public void deleteEpic(Epic epic) {
        ArrayList<String> toRemove = new ArrayList<>();
        for (SubTask subTask : epic.getSubTasks().values()) {
            toRemove.add(subTask.getId());
        }
        for (String id : toRemove) {
            epic.getSubTasks().remove(id);
        }
        for (String id : toRemove) {
            allTasks.remove(id);
        }
        deleteTask(epic);
    }

    // Удаление подзадачи из эпика
    @Override
    public void deleteSubTaskFromEpic(SubTask subTask) {
        if (allTasks.containsKey(subTask.getEpicId())) {
            Epic epic = (Epic) allTasks.get(subTask.getEpicId());
            epic.getSubTasks().remove(subTask.getId());
            epic.setStatus(calcStatus(epic));
        }
    }

    @Override
    public void renewTaskById(String oldId, Task task) {
        if (allTasks.containsKey(oldId)) {
            if (allTasks.get(oldId).getClass().getName().equals("subtask.SubTask")) {
                SubTask subTask = (SubTask) allTasks.get(oldId);
                Epic epic = (Epic) allTasks.get(subTask.getEpicId());
                epic.getSubTasks().remove(oldId);
                allTasks.remove(oldId);
                task.setId(oldId);
                SubTask newSubTask = (SubTask) task;
                epic.getSubTasks().put(task.getId(), newSubTask);
                allTasks.put(newSubTask.getId(), newSubTask);
                epic.setStatus(calcStatus(epic));
            } else if ((allTasks.get(oldId).getClass().getName().equals("epic.Epic"))) {
                Epic oldEpic = (Epic) allTasks.get(oldId);
                Epic newEpic = (Epic) task;
                deleteEpic(oldEpic);
                newEpic.setId(oldId);
                allTasks.put(newEpic.getId(), newEpic);
                newEpic.setStatus(calcStatus(newEpic));
            } else {
                allTasks.remove(oldId);
                task.setId(oldId);
                Task newTask = task;
                allTasks.put(newTask.getId(), newTask);
            }
        }
    }

    @Override
    public TaskStatus calcStatus(Epic epic) {
        int newStatusNumber = 0;
        int inProgressStatusNumber = 0;
        int doneStatusNumber = 0;
        for (SubTask subTask : epic.getSubTasks().values()) {
            if (subTask.getStatus().toString().equals("NEW")) {
                newStatusNumber = newStatusNumber + 1;
            } else if (subTask.getStatus().toString().equals("IN_PROGRESS")) {
                inProgressStatusNumber = inProgressStatusNumber + 1;
            } else if (subTask.getStatus().toString().equals("DONE")) {
                doneStatusNumber = doneStatusNumber + 1;
            }
        }
        if (epic.getSubTasks().size() == 0 || epic.getSubTasks().size() == newStatusNumber) {
            return TaskStatus.NEW;
        } else if (epic.getSubTasks().size() == doneStatusNumber) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }

    @Override
    public ArrayList<Task> history() {
        if (lastTenTasks.size() == 11) {
            lastTenTasks.remove(0);
        }
        return lastTenTasks;
    }

    @Override
    public void getTask(String id) {
        if (allTasks.containsKey(id) && allTasks.get(id).getClass().getName().equals("task.Task")) {
            lastTenTasks.add(allTasks.get(id));
            System.out.println(history());
        } else {
            System.out.println("id не найден");
        }
    }

    @Override
    public void getEpic(String id) {
        if (allTasks.containsKey(id) && allTasks.get(id).getClass().getName().equals("epic.Epic")) {
            Epic epic = (Epic) allTasks.get(id);
            lastTenTasks.add(epic);
            System.out.println(history());
        } else {
            System.out.println("id не найден");
        }
    }


    @Override
    public void getSubTask(String id) {
        if (allTasks.containsKey(id) && allTasks.get(id).getClass().getName().equals("subtask.SubTask")) {
            SubTask subTask = (SubTask) allTasks.get(id);
            lastTenTasks.add(subTask);
            System.out.println(history());
        } else {
            System.out.println("id не найден");
        }
    }


    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
