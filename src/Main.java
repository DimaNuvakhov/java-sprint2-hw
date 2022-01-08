public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
    // 1 Добавление новой задачи, эпика и подзадачи.
        // 1.1 Создаем задачу и добавляем в трекер задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\" из Гриднева", TaskStatus.DONE);
        manager.addTask(firstTask);
        manager.addTask(secondTask);

        // 1.2 Создаем эпик и добавляем в трекер задач
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Обучение", "Обучение JAVA");
        manager.addEpic(firstEpic);
        manager.addEpic(secondEpic);

        // 1.3 Создаем субтаск и добавляем в трекер задач
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое",
                TaskStatus.DONE, firstEpic.getId());
        manager.addSubTaskIntoEpic(firstEpicFirstSubTask); // Добавляем субтаск в первый епик

        SubTask secondEpicFirstSubTask = new SubTask("Изучить ArrayList",
                "Научиться добавлять и удалять из ArrayList", TaskStatus.NEW, secondEpic.getId());
        SubTask secondEpicSecondSubTask = new SubTask("Изучить private",
                "Понять действие модификатора доступа private", TaskStatus.DONE, secondEpic.getId());
        SubTask secondEpicThirdSubTask = new SubTask("Изучить Override",
                "Научиться переопределять методы", TaskStatus.NEW, secondEpic.getId());
        manager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        manager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        manager.addSubTaskIntoEpic(secondEpicThirdSubTask);

    // 2 Получение списка всех задач.
        manager.showAllTasks();

    // 3 Получение списка всех эпиков.
        manager.showAllEpics();

    // 4 Получение списка всех подзадач определённого эпика.
        manager.showSubTaskListFromEpicById(secondEpic.getId());

    // 5 Получение задачи любого типа по идентификатору.
        manager.showTaskById(firstTask.getId()); // задача
        manager.showTaskById(firstEpic.getId()); // эпик
        manager.showTaskById(firstEpicFirstSubTask.getId()); // подзадача

    // 6 Обновление задачи любого типа по идентификатору.
        SubTask testSubtask = new SubTask("Изучить дженерики", "Изучить случаи применения дженериков",
                TaskStatus.NEW, secondEpic.getId());
        manager.renewTaskById(secondEpicFirstSubTask.getId(), testSubtask);
        manager.showTaskById(secondEpic.getId());

    // 7 Удаление ранее добавленных задач — всех и по идентификатору.
        // 7.1 Удаление ранее добавленных задач по идентификатору.

        // 7.1.1 Проверка удаления Task
        manager.showTaskById(firstTask.getId());
        manager.deleteTaskById(firstTask.getId());
        manager.showTaskById(firstTask.getId());

        // 7.1.2 Проверка удаления Epic
        manager.showTaskById(firstEpic.getId());
        manager.deleteTaskById(firstEpic.getId());
        manager.showTaskById(firstEpic.getId());

        // 7.1.3 Проверка удаления SubTask
        manager.showTaskById(firstEpicFirstSubTask.getId());

        // 7.2 даление всех ранее добавленных задач.
        manager.deleteAllTasks();
    }
}


