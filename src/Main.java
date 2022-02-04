import tasks.Epic;
import managers.Manager;
import inmemorymanagers.Managers;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.List;

public class Main {
    static Manager inMemoryManager = Managers.getDefault();

    public static void main(String[] args) {
        // Создание Task
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\" из Гриднева", TaskStatus.DONE);
        Task thirdTask = new Task("Купить яблоки", "Нужно 20 яблок", TaskStatus.IN_PROGRESS);
        Task fourthTask = new Task("Купить арбузы", "Нужно 2 арбуза", TaskStatus.DONE);
        Task fifthTask = new Task("Купить абрикосы", "Нужно 1 кг. абрикосов", TaskStatus.DONE);
        Task newTask = new Task("Заправиться с утра", "Заехать на заправку", TaskStatus.IN_PROGRESS);
        // Добавление Task в трекер задач
        inMemoryManager.addTask(firstTask);
        inMemoryManager.addTask(secondTask);
        inMemoryManager.addTask(thirdTask);
        inMemoryManager.addTask(fourthTask);
        inMemoryManager.addTask(fifthTask);
        inMemoryManager.addTask(newTask);

        // Создание Epic
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        // Добавление в него SubTask
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId());
        SubTask firstEpicSecondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.DONE, firstEpic.getId());
        SubTask firstEpicThirdSubTask = new SubTask("Убрать в доме", "Убрать в доме перед отъездом",
                TaskStatus.DONE, firstEpic.getId());
        // Добавление Epic и SubTask в трекер задач
        inMemoryManager.addEpic(firstEpic);
        inMemoryManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        inMemoryManager.addSubTaskIntoEpic(firstEpicSecondSubTask);
        inMemoryManager.addSubTaskIntoEpic(firstEpicThirdSubTask);

        // Создание второго Epic
        Epic secondEpic = new Epic("Обучение", "Обучение JAVA");
        // Добавление в него SubTask
        SubTask secondEpicFirstSubTask = new SubTask("Изучить ArrayList",
                "Научиться добавлять и удалять из ArrayList", TaskStatus.NEW, secondEpic.getId());
        SubTask secondEpicSecondSubTask = new SubTask("Изучить private",
                "Понять действие модификатора доступа private", TaskStatus.NEW, secondEpic.getId());
        SubTask secondEpicThirdSubTask = new SubTask("Изучить Override",
                "Научиться переопределять методы", TaskStatus.NEW, secondEpic.getId());
        // Добавление Epic и SubTask в трекер задач
        inMemoryManager.addEpic(secondEpic);
        inMemoryManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondEpicThirdSubTask);

        // Создание третьего эпика без подзадач
        Epic emptyEpic = new Epic("Пустой Эпик", "Эпик без подзадач");
        inMemoryManager.addEpic(emptyEpic);


        // 1 Получение списка всех сущностей
        printTest("Получение списка всех сущностей");
        showAllItems();

        // 2 Получение списка всех задач
        printTest("Получение списка всех задач");
        showAllTasks();

        // 3 Получение списка всех эпиков
        printTest("Получение списка всех эпиков");
        showAllEpics();

        // 4 Получение списка всех подзадач
        printTest("Получение списка всех подзадач");
        showAllSubTasks();

        // 5 Получение списка подзадач определенного эпика
        printTest("Получение списка подзадач определенного эпика");
        showSubTaskListFromEpicById(firstEpic.getId());

        // 6 Обновление задачи любого типа по идентификатору
        // 6.1 Обновление подзадачи определенного эпика
        SubTask testSubtask = new SubTask("Изучить дженерики", "Изучить случаи применения дженериков",
                TaskStatus.DONE, secondEpic.getId());
        printTest("Проверка обновления подзадачи у эпика - вывод эпика до обновления");
        showTaskById(secondEpic.getId());
        printTest("Проверка обновления подзадачи у эпика - выполняем обновление");
        inMemoryManager.renewTaskById(secondEpicFirstSubTask.getId(), testSubtask); // Здесь мы проверяем, изменился ли статус эпика.
        printTest("Проверка обновления подзадачи у эпика - вывод эпика после обновления");
        showTaskById(secondEpic.getId());
        // 6.2 Обновление задачи
        Task testTask = new Task("Помыть машину", "Записаться на автомойку \"Мой - ка\"",
                TaskStatus.NEW);
        printTest("Проверка обновления задачи - вывод задачи до обновления");
        showTaskById(secondTask.getId());
        printTest("Проверка обновления задачи - выполняем обновление");
        inMemoryManager.renewTaskById(secondTask.getId(), testTask);
        printTest("Проверка обновления задачи - вывод задачи после обновления");
        showTaskById(secondTask.getId());
        // 6.3 Обновление эпика
        Epic testEpic = new Epic("Сходить в спортзал", "Потренировать две группы мышц");
        printTest("Проверка обновления эпика - вывод эпика до обновления");
        showTaskById(firstEpic.getId());
        printTest("Проверка обновления эпика - выполняем обновление");
        inMemoryManager.renewTaskById(firstEpic.getId(), testEpic);
        printTest("Проверка обновления эпика - вывод эпика после обновления");
        showTaskById(firstEpic.getId());

        // 7 Просмотр истории задач
        printTest("Вывод пустой истории");
        System.out.println(printHistory());
        printTest("Вывод одной задачи в истории");
        inMemoryManager.getTaskById(firstTask.getId());
        System.out.println(printHistory());
        printTest("Вывод той же самой задачи в истории");
        inMemoryManager.getTaskById(firstTask.getId());
        System.out.println(printHistory());
        printTest("Вывод двух задач в истории");
        inMemoryManager.getTaskById(secondTask.getId());
        System.out.println(printHistory());
        printTest("Вызов повторно первой задачи");
        inMemoryManager.getTaskById(firstTask.getId());
        System.out.println(printHistory());
        printTest("Вызов трех задач в истории");
        inMemoryManager.getTaskById(thirdTask.getId());
        System.out.println(printHistory());
        printTest("Вызов повторно первой задачи");
        inMemoryManager.getTaskById(firstTask.getId());
        System.out.println(printHistory());
        printTest("Вызов шести задач в истории");
        inMemoryManager.getTaskById(fourthTask.getId());
        inMemoryManager.getTaskById(secondEpicThirdSubTask.getId());
        inMemoryManager.getTaskById(firstEpic.getId());
        System.out.println(printHistory());
        printTest("Дублирование задачи из середины истории");
        inMemoryManager.getTaskById(secondEpicThirdSubTask.getId());
        System.out.println(printHistory());
        printTest("Дублирование задачи из начала истории");
        inMemoryManager.getTaskById(secondTask.getId());
        System.out.println(printHistory());
        printTest("Дублирование задачи из конца истории");
        inMemoryManager.getTaskById(secondTask.getId());
        System.out.println(printHistory());

        // 8 Удаление ранее добавленных задач — всех и по идентификатору.
        // 8.1 Удаление ранее добавленных задач по идентификатору.
        // 8.1.1 Проверка удаления Task.
        printTest("Проверка удаления по идентификатору Task");
        showTaskById(firstTask.getId());
        deleteTaskById(firstTask.getId());
        showTaskById(firstTask.getId());
        // 8.1.2 Проверка удаления SubTask.
        printTest("Проверка удаления по идентификатору SubTask");
        showTaskById(secondEpicFirstSubTask.getId());
        deleteTaskById(secondEpicFirstSubTask.getId());
        showTaskById(secondEpicFirstSubTask.getId());
        // 8.1.3 Проверка удаления Epic.
        printTest("Проверка удаления по идентификатору Epic");
        showTaskById(secondEpic.getId());
        deleteTaskById(secondEpic.getId());
        showTaskById(secondEpic.getId());

        // 8.2 Удаление всех ранее добавленных задач.
        printTest("Проверка удаления всех сущностей - вывод полного списка");
        showAllItems();
        printTest("Проверка удаления всех сущностей - удаление сущностей");
        deleteAllTasks();
        printTest("Проверка удаления всех сущностей - вывод полного списка после удаления");
        showAllItems();
    }

    // Методы для проверки
    public static void printTest(String value) {
        System.out.println();
        System.out.println("LOGGER [INFO]: " + value + " : ");
        System.out.println("---");
    }

    public static void showAllItems() {
        System.out.println("== Начало полного списка сущностей ==");
        for (Task task : inMemoryManager.getAllItems().values()) {
            if (!(task.getClass().getName().equals("tasks.SubTask"))) {
                System.out.println(task);
            }
        }
        System.out.println("== Окончание полного списка сущностей ==\n");
    }

    public static void showAllTasks() {
        System.out.println("== Начало списка всех задач ==");
        for (Task task : inMemoryManager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("== Окончание полного списка задач ==\n");
    }

    public static void showAllEpics() {
        System.out.println("== Начало списка всех эпиков ==");
        for (Epic epic : inMemoryManager.getAllEpics()) {
            System.out.println(epic);
        }
        System.out.println("== Окончание списка эпиков ==\n");
    }

    public static void showAllSubTasks() {
        System.out.println("== Начало списка всех подзадач ==");
        for (SubTask subTask : inMemoryManager.getAllSubtasks()) {
            System.out.println(subTask);
        }
        System.out.println("== Окончание списка подзадач ==\n");
    }

    public static void showSubTaskListFromEpicById(String id) {
        System.out.println("== Вывод списка подзадач для Эпика с id = " + id + "  ==");
        if (inMemoryManager.getAllItems().containsKey(id)) {
            for (SubTask subTask : inMemoryManager.getSubTaskListFromEpicById(id)) {
                System.out.println(subTask);
            }
            System.out.println("== Окончание списка подзадач ==\n");
        } else {
            System.out.println("Данных нет");
        }
    }

    public static void showTaskById(String id) {
        if (inMemoryManager.getAllItems().containsKey(id)) {
            System.out.println(inMemoryManager.getAllItems().get(id));
        } else {
            System.out.println("Данных нет");
        }
    }

    public static String printHistory() {
        List<Task> lastTasks = inMemoryManager.history();
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
        return table + value + "\n" + horizontalTableBorder + "\n";
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

    public static void deleteAllTasks() {
        System.out.println("Удаляем все сущности");
        inMemoryManager.deleteAllTasks();
    }

    public static void deleteTaskById(String id) {
        System.out.println("== Удаление сущности, id = " + id);
        if (inMemoryManager.deleteTaskById(id))  {
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
