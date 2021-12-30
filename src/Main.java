public class Main {
    public static void main(String[] args) {
//   Создать менеджера
        Manager manager = new Manager();

// 1 Добавление новой задачи, эпика и подзадачи.
// 1.1 Создаем две задачи
        Task firstTask = manager.createTask("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = manager.createTask("Купить хлеб",
                "Нужен хлеб \"Литовский\" из Гриднева",
                TaskStatus.DONE);

// 1.2 Создаем эпик и две подзадачи с разными статусами
        Epic firstEpic = manager.createEpicAndOneSubTask(
                "Переезд",
                "Собрать все вещи",
                "Собрать чемодан",
                "Положить в чемодан все необходимое",
                TaskStatus.DONE
        );
        SubTask firstEpicSecondSubTask = manager.addSubTaskIntoEpic(firstEpic, "Вынести мусор",
                "Вынести мусор из гаража и из спальни",
                TaskStatus.DONE);

// 1.3 Создаем эпик и три подзадачи с разными статусами
        Epic secondEpic = manager.createEpicAndOneSubTask("Обучение", "Обучение JAVA",
                "Изучить ArrayList", "Научиться добавлять и удалять из ArrayList",
                TaskStatus.NEW);
        SubTask secondEpicSecondSubTask = manager.addSubTaskIntoEpic(secondEpic, "Изучить private",
                "Понять действие модификатора доступа private", TaskStatus.DONE);
        SubTask secondEpicThirdSubTask = manager.addSubTaskIntoEpic(secondEpic, "Изучить Override",
                "Научиться переопределять методы", TaskStatus.NEW);

// 2 Получение списка всех задач.
        manager.showAllTasks();
// 3 Получение списка всех эпиков.
        manager.showAllEpics();
// 4 Получение списка всех подзадач определённого эпика.
        manager.showSubTaskListFromEpicById(secondEpic.getId());
// 5 Получение задачи любого типа по идентификатору.
        manager.showTaskById(firstTask.getId()); // вывод задачи
        manager.showTaskById(firstEpic.getId()); // вывод эпика
        manager.showTaskById(secondEpicThirdSubTask.getId()); // вывод подзадачи

// 6 Обновление задачи любого типа по идентификатору.

        // 6.1 Проверка обновления имени
        manager.setTaskName(firstTask.getId(), "Помыть машину");
        manager.showTaskById(firstTask.getId());

        // 6.2 Проверка обновления описания
        manager.setTaskDescription(firstEpic.getId(), "Собрать самое необходимое, остальное оставить");
        manager.showTaskById(firstEpic.getId());

        // 6.3 Проверка обновления статуса
        manager.setTaskStatus(secondEpicSecondSubTask.getId(), TaskStatus.NEW);
        manager.showTaskById(secondEpicSecondSubTask.getId());

        // 6.4 Проверка, что у эпика изменился статус
        manager.showTaskById(secondEpic.getId());

// 7 Удаление ранее добавленных задач — всех и по идентификатору.
// 7.1 Удаление ранее добавленных задач по идентификатору.

        // 7.1.1 Проверка удаления SubTask
        manager.deleteTaskById(secondEpicThirdSubTask.getId());
        manager.showTaskById(secondEpicThirdSubTask.getId());
        manager.showTaskById(secondEpic.getId());

        // 7.1.2 Проверка удаления Task
        manager.showTaskById(secondTask.getId());
        manager.deleteTaskById(secondTask.getId());
        manager.showTaskById(secondTask.getId());

        // 7.1.3 Проверка удаления Epic
        manager.deleteTaskById(secondEpic.getId());
        manager.showTaskById(secondEpicSecondSubTask.getId());
        manager.showTaskById(secondEpicThirdSubTask.getId());
        manager.showTaskById(secondEpic.getId());

        // 7.1.4 Проверка удаления последней подзадачи у Epic
        manager.showTaskById(firstEpic.getId());
        manager.deleteAllTasksInEpicById(firstEpic.getId()) ;
        manager.showTaskById(firstEpic.getId());

// 7.2 Удаление всех ранее добавленных задач
        manager.deleteAllTasks();
    }
}

