package mylibrary;

import imanagers.Manager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public class MyLibrary {
    // Методы для проверки
    public static void printTest(String value) {
        System.out.println();
        System.out.println("LOGGER [INFO]: " + value + " : ");
        System.out.println("---");
    }

    public static void showAllItems(Manager manager) {
        System.out.println("== Начало полного списка сущностей ==");
        for (Task task : manager.getAllItems().values()) {
            if (!(task.getClass().getName().equals("tasks.SubTask"))) {
                System.out.println(task);
            }
        }
        System.out.println("== Окончание полного списка сущностей ==\n");
    }

    public static void showAllTasks(Manager manager) {
        System.out.println("== Начало списка всех задач ==");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("== Окончание полного списка задач ==\n");
    }

    public static void showAllEpics(Manager manager) {
        System.out.println("== Начало списка всех эпиков ==");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
        }
        System.out.println("== Окончание списка эпиков ==\n");
    }

    public static void showAllSubTasks(Manager manager) {
        System.out.println("== Начало списка всех подзадач ==");
        for (SubTask subTask : manager.getAllSubtasks()) {
            System.out.println(subTask);
        }
        System.out.println("== Окончание списка подзадач ==\n");
    }

    public static void showSubTaskListFromEpicById(String id, Manager manager) {
        System.out.println("== Вывод списка подзадач для Эпика с id = " + id + "  ==");
        if (manager.getAllItems().containsKey(id)) {
            for (SubTask subTask : manager.getSubTaskListFromEpicById(id)) {
                System.out.println(subTask);
            }
            System.out.println("== Окончание списка подзадач ==\n");
        } else {
            System.out.println("Данных нет");
        }
    }

    public static void showTaskById(String id, Manager manager) {
        if (manager.getAllItems().containsKey(id)) {
            System.out.println(manager.getAllItems().get(id));
        } else {
            System.out.println("Данных нет");
        }
    }

    public static String printHistory(Manager manager) {
        String firstPhrase = "== Начало вывода истории задач ==";
        String secondPhrase = "== Окончание вывода истории задач ==";
        List<Task> lastTasks = manager.history();
        StringBuilder value = new StringBuilder();
        Integer num = 0;
        String verticalTableBorder = "|";
        String horizontalTableBorder = "-------------------------------------"
                + "---------------------------------------------------------"
                + "--------------------------------------------------";
        String table = horizontalTableBorder + "\n" + verticalTableBorder + padLeft("<№>", 4)
                + verticalTableBorder
                + padLeft("<Тип задачи>", 13) + verticalTableBorder
                + padLeft("<id>", 35) + verticalTableBorder
                + padLeft("<Название>", 20)
                + verticalTableBorder + (padLeft("<Описание>", 50) + verticalTableBorder)
                + (padLeft("<Статус>", 15) + verticalTableBorder)
                + "\n" + horizontalTableBorder;

        for (Task tasks : lastTasks) {
            num++;
            value.
                    append("\n").
                    append(verticalTableBorder).
                    append(padLeft(num.toString(), 4)).
                    append(verticalTableBorder).
                    append(padLeft(className(tasks), 13)).
                    append(verticalTableBorder).
                    append(padLeft(tasks.getId(), 35)).
                    append(verticalTableBorder).
                    append(padLeft(tasks.getName(), 20)).
                    append(verticalTableBorder).
                    append(padLeft(tasks.getDescription(), 50)).
                    append(verticalTableBorder).
                    append(padLeft(tasks.getStatus().toString(), 15)).
                    append(verticalTableBorder);
        }
        return firstPhrase + "\n" + table + value + "\n" + horizontalTableBorder + "\n" + secondPhrase + "\n";
    }

    public static String className(Task task) {
        switch (task.getClass().getName()) {
            case "tasks.SubTask":
                return "SubTask";
            case "tasks.Epic":
                return "Epic";
            case "tasks.Task":
                return "Task";
        }
        return null;
    }

    public static void deleteAllTasks(Manager manager) {
        System.out.println("Удаляем все сущности");
        manager.deleteAllTasks();
    }

    public static void deleteTaskById(String id, Manager manager) {
        System.out.println("== Удаление сущности, id = " + id);
        if (manager.deleteTaskById(id)) {
            System.out.println("== Сущность с id = " + id + " удалена");
        } else {
            System.out.println("== Сущность для удаления не найдена, id = " + id + "\n");
        }
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
