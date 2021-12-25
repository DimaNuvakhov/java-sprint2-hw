public class Main {
    public static void main(String[] args) {
        String id = "";

    // 1 Создать менеджера
        Manager manager = new Manager();

    // 2 Создать задачу:  Помыть посуду, Помыть тарелки и вилки, NEW, сохранить индефекатор
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);

    // 3 Добавить задачу в коллекцию менеджера, сохранить идентификатор
        fillInTheTable(manager, id, firstTask);

    // 4 Создаем эпик: Переезд, Собрать все вещи, IN_PROGRESS, сохранить индификатор
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи", TaskStatus.IN_PROGRESS);

    // 5 Создать подзадачу для эпика Переезд: Собрать чемодан, Положить в чемодан все необходимое, IN_PROGRESS
        SubTask firstSubTask = new SubTask("Собрать чемодан", "Положить в чемодан все необходимое", TaskStatus.IN_PROGRESS);

    // 6 Добавить эпик в коллекцию менеджера, сохранить индификатор
        fillInTheTable(manager, id, firstEpic);

    // 7 Добавить подзадачу эпика этого в коллекцию менеджера,сохранить индефекатор
        fillInTheTable(manager, id, firstSubTask);

        // 8 Получение списка всех задач: распечатать id,name,status
        manager.showAllTasks();

    // 9 Получение списка всех эпиков: распечатать id,name,status
    // 10 Получение списка всех подзадач определённого эпика: id, name, status, подсунуть индефикатор
    // 11 Получение задачи любого типа по идентификатору.
        manager.showSpecificTask(id);
    // 12 Обновление задачи любого типа по идентификатору. Новая версия объекта передаётся в виде параметра (задать вопрос).





    // 13 После обновления вызвать получение
    // 14 Удалить эпик. (не понятно, как удалить эпик)
    // 15 Выввать получение всех задач, чтобы убедиться , что эпик удален и все подзадачи.
    // 16 Удалить всё
    // 17 Вызвать получение, чтобы убедиться, что ни одной задачи нет

    }

    public static void fillInTheTable(Manager manager, String id, Task someTask) {
        id = someTask.getId();
        manager.setAllTasks(id, someTask);
    }



}
