public class Main {
    public static void main(String[] args) {
// 1 Создать менеджера
        Manager manager = new Manager();
// 2 Создать задачу:  Помыть посуду, Помыть тарелки и вилки, NEW, сохранить индефекатор
// 3 Добавить задачу в коллекцию менеджера, сохранить идентификатор
        Task firstTask = manager.createTask("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = manager.createTask("Купить хлеб", "Нужен хлеб \"Литовский\" из Гриднева", TaskStatus.DONE);

// 4 Создаем эпик: Переезд, Собрать все вещи, IN_PROGRESS, сохранить индификатор
// 5 Создать подзадачу для эпика Переезд: Собрать чемодан, Положить в чемодан все необходимое, IN_PROGRESS
// 6 Добавить эпик в коллекцию менеджера, сохранить индификатор
// 7 Добавить подзадачу эпика этого в коллекцию менеджера,сохранить индефекатор
        Epic firstEpic = manager.createEpicAndOneSubTask(
                "Переезд",
                "Собрать все вещи",
                null,  // TODO Убрать этот параметр вообще, он не нужен, статус эпика мы не записываем никуда.
                "Собрать чемодан",
                "Положить в чемодан все необходимое",
                TaskStatus.IN_PROGRESS
        );

// 7.1 Создаю эпик и три подзадачи с разными статусами
        Epic secondEpic = manager.createEpicAndOneSubTask("Обучение", "Обучение JAVA",
                TaskStatus.IN_PROGRESS, "Изучить ArrayList",
                "Научиться добавлять и удалять из ArrayList", TaskStatus.DONE);
        SubTask secondEpicSecondSubTask = manager.addSubTaskIntoEpic(secondEpic, "Изучить private",
                "Понять действие модификатора доступа private", TaskStatus.IN_PROGRESS);
        SubTask secondEpicThirdSubTask = manager.addSubTaskIntoEpic(secondEpic, "Изучить Override",
                "Научиться переопределять методы", TaskStatus.NEW);

// 8 Получение списка всех задач: распечатать id,name,status
        manager.showAllTasks();
// 9 Получение списка всех эпиков: распечатать id,name,status
        manager.showAllEpics();
// 10 Получение списка всех подзадач определённого эпика: id, name, status, подсунуть индефикатор
//        manager.showSpecificTask(secondEpic.getId()); // TODO этот вызов удалить, ниже написан другой вариант - showSubTaskListFromEpicById
        manager.showSubTaskListFromEpicById(secondEpic.getId());

// 11 Получение задачи любого типа по идентификатору.
        manager.showSpecificEntity(firstTask.getId()); // вывод задачи
        manager.showSpecificEntity(firstEpic.getId()); // вывод эпика
        manager.showSpecificEntity(secondEpicThirdSubTask.getId()); // вывод подзадачи

// 12 Обновление задачи любого типа по идентификатору. Новая версия объекта передаётся в виде параметра (задать вопрос).


// 13 После обновления вызвать получение

// 14 Удалить эпик. (не понятно, как удалить эпик)
// 15 Выввать получение всех задач, чтобы убедиться , что эпик удален и все подзадачи.
        // Проверка удаления SubTask
        manager.removesEntityById(secondEpicThirdSubTask.getId());
        manager.showSpecificEntity(secondEpicThirdSubTask.getId());
        manager.showSpecificEntity(secondEpic.getId());

        // Проверка удаления Task
        manager.showSpecificEntity(secondTask.getId());
        manager.removesEntityById(secondTask.getId());
        manager.showSpecificEntity(secondTask.getId());

        // Проверка удаления Epic
        manager.removesEntityById(secondEpic.getId());
        manager.showSpecificEntity(secondEpicSecondSubTask.getId());
        manager.showSpecificEntity(secondEpicThirdSubTask.getId());
        manager.showSpecificEntity(secondEpic.getId());




// 16 Удалить всё
        // TODO В цикле удалить все Task и Epic - подзадачи удалятся автоматом при удалении эпика
// 17 Вызвать получение, чтобы убедиться, что ни одной задачи нет

    }
}
