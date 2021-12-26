public class Main {
    public static void main(String[] args) {
// 1 Создать менеджера
        Manager manager = new Manager();

// 2 Создать задачу:  Помыть посуду, Помыть тарелки и вилки, NEW, сохранить индефекатор
        //Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\" из Гриднева", TaskStatus.DONE);
        Task firstTask = manager.createTask("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
// 3 Добавить задачу в коллекцию менеджера, сохранить идентификатор
//        manager.addOneTaskIntoMap(firstTask.getId(), firstTask);
        manager.addOneTaskIntoMap(secondTask.getId(), secondTask);


// 4 Создаем эпик: Переезд, Собрать все вещи, IN_PROGRESS, сохранить индификатор
        // Epic firstEpic = new Epic("Переезд", "Собрать все вещи", TaskStatus.IN_PROGRESS);
        Epic firstEpic = manager.createEpicAndOneSubTask("Переезд", "Собрать все вещи", TaskStatus.IN_PROGRESS,
                "Собрать чемодан", "Положить в чемодан все необходимое", TaskStatus.IN_PROGRESS);
// 5 Создать подзадачу для эпика Переезд: Собрать чемодан, Положить в чемодан все необходимое, IN_PROGRESS
        //SubTask firstSubTask = new SubTask("Собрать чемодан", "Положить в чемодан все необходимое", TaskStatus.IN_PROGRESS, firstEpic);

// 6 Добавить эпик в коллекцию менеджера, сохранить индификатор
        manager.addOneTaskIntoMap(firstEpic.getId(), firstEpic);
// 7 Добавить подзадачу эпика этого в коллекцию менеджера,сохранить индефекатор
        // manager.addOneTaskIntoMap(firstSubTask.getId(), firstSubTask);
// 7.1 Создаю эпик и три подзадачи с разными статусами
        Epic secondEpic = new Epic("Обучение", "Обучение JAVA", TaskStatus.IN_PROGRESS);
        SubTask firstSubTaskSecondEpic =
                new SubTask("Изучить ArrayList", "Научиться добавлять и удалять из ArrayList",
                        TaskStatus.DONE, secondEpic);

        SubTask secondSubTaskSecondEpic =
                new SubTask("Изучить private", "Понять действие модификатора доступа private",
                        TaskStatus.IN_PROGRESS, secondEpic);

        SubTask thirdSubTaskSecondEpic =
                new SubTask("Изучить Override", "Научиться переопределять методы",
                        TaskStatus.NEW, secondEpic);


        manager.addOneTaskIntoMap(secondEpic.getId(), secondEpic);
        manager.addOneTaskIntoMap(firstSubTaskSecondEpic.getId(), firstSubTaskSecondEpic);
        manager.addOneTaskIntoMap(secondSubTaskSecondEpic.getId(), secondSubTaskSecondEpic);
        manager.addOneTaskIntoMap(thirdSubTaskSecondEpic.getId(), thirdSubTaskSecondEpic);


// 8 Получение списка всех задач: распечатать id,name,status
        System.out.println("");  // косметика, чтобы отделить блок вывода
        manager.showAllTasks();

// 9 Получение списка всех эпиков: распечатать id,name,status
        manager.showAllEpics();
// 10 Получение списка всех подзадач определённого эпика: id, name, status, подсунуть индефикатор

// 11 Получение задачи любого типа по идентификатору.
        System.out.println("");  // косметика, чтобы отделить блок вывода
        manager.showSpecificTask(firstTask.getId()); // вывод задачи
        manager.showSpecificTask(firstEpic.getId()); // вывод эпика
        manager.showSpecificTask(secondEpic.getAnySubTask().getId()); // вывод подзадачи
        manager.showSpecificTask(firstEpic.getAnySubTask().getId()); // вывод подзадачи

// 12 Обновление задачи любого типа по идентификатору. Новая версия объекта передаётся в виде параметра (задать вопрос).


// 13 После обновления вызвать получение

// 14 Удалить эпик. (не понятно, как удалить эпик)

// 15 Выввать получение всех задач, чтобы убедиться , что эпик удален и все подзадачи.
// 16 Удалить всё

// 17 Вызвать получение, чтобы убедиться, что ни одной задачи нет

    }
}
