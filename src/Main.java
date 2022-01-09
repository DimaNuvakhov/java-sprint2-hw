import epic.Epic;
import manager.Manager;
import subtask.SubTask;
import task.Task;
import taskstatus.TaskStatus;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        // 1 Добавление новой задачи, эпика и подзадачи.
        // 1.1 Создаем задачу и добавляем в трекер задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\" из Гриднева",
                TaskStatus.DONE);
        manager.addTask(firstTask);
        manager.addTask(secondTask);

        // 1.2 Создаем эпик и добавляем в трекер задач
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Обучение", "Обучение JAVA");
        manager.addEpic(firstEpic);
        manager.addEpic(secondEpic);

        // 1.2.1 Создаем пустой эпик и добавляем в трекер задач
        Epic emptyEpic = new Epic("Пустой Эпик", "Эпик без подзадач");
        manager.addEpic(emptyEpic);

        // 1.3 Создаем субтаск и добавляем в трекер задач
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое",
                TaskStatus.DONE, firstEpic.getId());
        manager.addSubTaskIntoEpic(firstEpicFirstSubTask); // Добавляем субтаск в первый епик

        SubTask secondEpicFirstSubTask = new SubTask("Изучить ArrayList",
                "Научиться добавлять и удалять из ArrayList", TaskStatus.NEW, secondEpic.getId());
        SubTask secondEpicSecondSubTask = new SubTask("Изучить private",
                "Понять действие модификатора доступа private", TaskStatus.NEW, secondEpic.getId());
        SubTask secondEpicThirdSubTask = new SubTask("Изучить Override",
                "Научиться переопределять методы", TaskStatus.NEW, secondEpic.getId());
        manager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        manager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        manager.addSubTaskIntoEpic(secondEpicThirdSubTask);

        // 2 Получение списка всех задач.
        printTest("Проверка получения списка всех задач");
        manager.showAllTasks();

        // 3 Получение списка всех эпиков.
        printTest("Проверка получения списка всех Эпиков");
        manager.showAllEpics();

        // 4 Получение списка всех подзадач определённого эпика.
        printTest("Проверка получения всех подзадач определённого эпика");
        manager.showSubTaskListFromEpicById(secondEpic.getId());

        // 4.1 Получение списка всех подзадач пустого эпика.
        printTest("Проверка получения всех подзадач пустого эпика");
        manager.showSubTaskListFromEpicById(emptyEpic.getId());

        // 5 Получение задачи любого типа по идентификатору.
        printTest("Проверка получения по идентификатору Task");
        manager.showTaskById(firstTask.getId()); // задача
        printTest("Проверка получения по идентификатору Epic");
        manager.showTaskById(firstEpic.getId()); // эпик
        printTest("Проверка получения по идентификатору SubTask");
        manager.showTaskById(firstEpicFirstSubTask.getId()); // подзадача

        // 6 Обновление задачи любого типа по идентификатору.
        // 6.1 Обновление подзадачи определенного эпика
        SubTask testSubtask = new SubTask("Изучить дженерики", "Изучить случаи применения дженериков",
                TaskStatus.DONE, secondEpic.getId());
        printTest("Проверка обновления подзадачи у эпика - вывод эпика до обновления");
        manager.showTaskById(secondEpic.getId());
        printTest("Проверка обновления подзадачи у эпика - выполняем обновление");
        manager.renewTaskById(secondEpicFirstSubTask.getId(), testSubtask); // Здесь мы проверяем, изменился ли статус эпика.
        printTest("Проверка обновления подзадачи у эпика - вывод эпика после обновления");
        manager.showTaskById(secondEpic.getId());

        // 6.2 Обновление задачи
        Task testTask = new Task("Помыть машину", "Записаться на автомойку \"Мой - ка\"",
                TaskStatus.NEW);
        printTest("Проверка обновления задачи - вывод задачи до обновления");
        manager.showTaskById(secondTask.getId());
        printTest("Проверка обновления задачи - выполняем обновление");
        manager.renewTaskById(secondTask.getId(), testTask);
        printTest("Проверка обновления задачи - вывод задачи после обновления");
        manager.showTaskById(secondTask.getId());

        // 6.3 Обновление эпика
        Epic testEpic = new Epic("Сходить в спортзал", "Потренировать две группы мышц");
        printTest("Проверка обновления эпика - вывод эпика до обновления");
        manager.showTaskById(firstEpic.getId());
        printTest("Проверка обновления эпика - выполняем обновление");
        manager.renewTaskById(firstEpic.getId(), testEpic);
        printTest("Проверка обновления эпика - вывод эпика после обновления");
        manager.showTaskById(firstEpic.getId());

        // 7 Удаление ранее добавленных задач — всех и по идентификатору.
        // 7.1 Удаление ранее добавленных задач по идентификатору.
        // 7.1.1 Проверка удаления Task
        printTest("Проверка удаления по идентификатору Task");
        manager.showTaskById(firstTask.getId());
        manager.deleteTaskById(firstTask.getId());
        manager.showTaskById(firstTask.getId());

        // 7.1.3 Проверка удаления SubTask
        printTest("Проверка удаления по идентификатору SubTask");
        manager.showTaskById(secondEpicFirstSubTask.getId());
        manager.deleteTaskById(secondEpicFirstSubTask.getId());
        manager.showTaskById(secondEpicFirstSubTask.getId());

        // 7.1.2 Проверка удаления Epic
        printTest("Проверка удаления по идентификатору Epic");
        manager.showTaskById(secondEpic.getId());
        manager.deleteTaskById(secondEpic.getId());
        manager.showTaskById(secondEpic.getId());

        // 7.2 Удаление всех ранее добавленных задач.
        printTest("Проверка удаления всех сущностей - вывод полного списка");
        manager.showAllTasks();
        printTest("Проверка удаления всех сущностей - удаление сущностей");
        manager.deleteAllTasks();
        printTest("Проверка удаления всех сущностей - вывод полного списка после удаления");
        manager.showAllTasks();
    }

    public static void printTest(String value) {
        System.out.println();
        System.out.println("LOGGER [INFO]: " + value + " : ");
        System.out.println("---");
    }
}
