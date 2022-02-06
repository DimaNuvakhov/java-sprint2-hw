package inmemorymanagers;

import tasks.Epic;
import managers.Manager;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryManager implements Manager {
    private final HashMap<String, Task> allTasks = new HashMap<>();
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

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

    // Я не знаю, нужен ли этот метод, возвращающий все задачи, но сделал его
    @Override
    public HashMap<String, Task> getAllItems() {
        return allTasks;
    }

    // Получение списка всех задач
    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : allTasks.values()) {
            if (task.getClass().getName().equals(TASK_NAME)) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    // Получение списка всех эпиков
    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epics = new ArrayList<>();
        for (Task task : allTasks.values()) {
            if (task.getClass().getName().equals(EPIC_NAME)) {
                Epic epic = (Epic) task;
                epics.add(epic);
            }
        }
        return epics;
    }

    // Получение списка всех подзадач
    @Override
    public ArrayList<SubTask> getAllSubtasks() {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Task task : allTasks.values()) {
            if (task.getClass().getName().equals(SUBTASK_NAME)) {
                SubTask subTask = (SubTask) task;
                subTasks.add(subTask);
            }
        }
        return subTasks;
    }

    // Добавление определенной задачи в историю просмотра
    @Override
    public void getTaskById(String id) {
        if (allTasks.containsKey(id)) {
            inMemoryHistoryManager.add(allTasks.get(id));
        }
    }

    // Получение списка всех подзадач определенного эпика
    @Override
    public ArrayList<SubTask> getSubTaskListFromEpicById(String id) {
        ArrayList<SubTask> subTaskListFromEpic = new ArrayList<>();
        if (allTasks.containsKey(id) && allTasks.get(id).getClass().getName().equals(EPIC_NAME)) {
            Epic epic = (Epic) allTasks.get(id);
            for (SubTask subTask : epic.getSubTasks().values()) {
                subTaskListFromEpic.add(subTask);
            }
        }
        return subTaskListFromEpic;
    }

    // Удаление всех задач
    @Override
    public void deleteAllTasks() {
        allTasks.clear();
    }

    // Удаление задачи, эпика и подзадачи по id
    @Override
    public Boolean deleteTaskById(String id) {
        if (!allTasks.containsKey(id)) {
            return false;
        }
        if (allTasks.get(id).getClass().getName().equals(TASK_NAME)) {
            deleteTask(allTasks.get(id));
        } else if (allTasks.get(id).getClass().getName().equals(EPIC_NAME)) {
            Epic epic = (Epic) allTasks.get(id);
            deleteEpic(epic);
        } else {
            SubTask subTask = (SubTask) allTasks.get(id);
            deleteSubTaskFromEpic(subTask);
            allTasks.remove(subTask.getId());
        }
        return true;
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

    // Обновление любой задачи по id
    @Override
    public void renewTaskById(String oldId, Task task) {
        if (allTasks.containsKey(oldId)) {
            if (allTasks.get(oldId).getClass().getName().equals(SUBTASK_NAME)) {
                SubTask subTask = (SubTask) allTasks.get(oldId);
                Epic epic = (Epic) allTasks.get(subTask.getEpicId());
                epic.getSubTasks().remove(oldId);
                allTasks.remove(oldId);
                task.setId(oldId);
                SubTask newSubTask = (SubTask) task;
                epic.getSubTasks().put(task.getId(), newSubTask);
                allTasks.put(newSubTask.getId(), newSubTask);
                epic.setStatus(calcStatus(epic));
            } else if ((allTasks.get(oldId).getClass().getName().equals(EPIC_NAME))) {
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

    // Вычисление статуса эпика
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

    // История просмотра задач
    @Override
    public List<Task> history() {
        return inMemoryHistoryManager.getHistory();
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
