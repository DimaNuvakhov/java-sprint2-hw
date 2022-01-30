package inmemoryhistorymanager;

import epic.Epic;
import historymanager.HistoryManager;
import manager.Manager;
import subtask.SubTask;
import task.Task;
import taskstatus.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements Manager, HistoryManager {
    private final HashMap<String, Task> allTasks = new HashMap<>();
    private final HashMap<String, Node<Task>> nodes = new HashMap<>(); // J5: добавил тип для Node

    // Класс Node для LinkedList
    private static class Node<E extends Task> {
        public E data;
        public Node<E> next;
        public Node<E> prev;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    // Поля LinkedList
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    // Добавление последнего элемента в LinkedList
    public void addLast(Task element) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }

    // Добавление задачи в LinkedList
    @Override
    public void add(Task task) {
        addLast(task);
        if (nodes.containsKey(task.getId())) {
            remove(task.getId());
        }
        nodes.put(task.getId(), tail);  // J5: косметика - избавился от else
    }

    @Override
    public void remove(String id) {
        // TODO
    }

    @Override
    public List<Task> getHistory() {
        ArrayList<Task> lastTenTasks = new ArrayList<>(10); // J5: устанавливаем размер
        Node<Task> x = tail;
        for (int i = size; x != null && i > size - 10; i--) { // J5: уточнил условие
               lastTenTasks.add(x.data);
               x = x.prev;
        }
        return lastTenTasks;
    }

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
        //System.out.println("== Начало вывода задачи с id = " + id + "  ==");
        if (allTasks.containsKey(id)) {
        //    System.out.println(allTasks.get(id));
            add(allTasks.get(id));
        } else {
            System.out.println("Данных нет");
        }
        //System.out.println();
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
    public String printHistory() {
        List<Task> lastTenTasks = getHistory();
        StringBuilder value = new StringBuilder();
        int num = 0;
        String verticalTableBorder = "|";
        String horizontalTableBorder = "-------------------------------------"
                + "---------------------------------------------------------"
                + "--------------------------------------------------";
        String table = horizontalTableBorder + "\n" + verticalTableBorder + InMemoryHistoryManager.padLeft("<№>", 4)
                + verticalTableBorder
                + InMemoryHistoryManager.padLeft("<Тип задачи>", 13) + verticalTableBorder
                + InMemoryHistoryManager.padLeft("<id>", 35) + verticalTableBorder
                + InMemoryHistoryManager.padLeft("<Название>", 20)
                + verticalTableBorder + (InMemoryHistoryManager.padLeft("<Описание>", 50) + verticalTableBorder)
                + (InMemoryHistoryManager.padLeft("<Статус>", 15) + verticalTableBorder)
                + "\n" + horizontalTableBorder;

        for (Task tasks : lastTenTasks) {
            num++;
            value.
                    append("\n").
                    append(verticalTableBorder).
                    append(InMemoryHistoryManager.padLeft(Integer.toString(num), 4)).
                    append(verticalTableBorder).
                    append(InMemoryHistoryManager.padLeft(className(tasks), 13)).
                    append(verticalTableBorder).
                    append(InMemoryHistoryManager.padLeft(tasks.getId(), 35)).
                    append(verticalTableBorder).
                    append(InMemoryHistoryManager.padLeft(tasks.getName(), 20)).
                    append(verticalTableBorder).
                    append(InMemoryHistoryManager.padLeft(tasks.getDescription(), 50)).
                    append(verticalTableBorder).
                    append(InMemoryHistoryManager.padLeft(tasks.getStatus().toString(), 15)).
                    append(verticalTableBorder);
        }
        return table + value + "\n" + horizontalTableBorder + "\n";
    }

    public String className(Task task) {
        switch (task.getClass().getName()) {
            case "subtask.SubTask":
                return "SubTask";
            case "epic.Epic":
                return "Epic";
            case "task.Task":
                return "Task";
        }
        return null;
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
