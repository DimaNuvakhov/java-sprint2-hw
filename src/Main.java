import epic.Epic;
import manager.Manager;
import managers.Managers;
import subtask.SubTask;
import task.Task;
import taskstatus.TaskStatus;

public class Main {
    public static void main(String[] args) {
        Manager inMemoryHistoryManager = Managers.getDefault();

        // Создание Task, SubTask и Epic
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\" из Гриднева", TaskStatus.DONE);
        Task thirdTask = new Task("Купить яблоки", "Нужно 20 яблок", TaskStatus.NEW);
        Task fourthTask = new Task("Купить арбузы", "Нужно 2 арбуза", TaskStatus.NEW);
        Task fifthTask = new Task("Купить абрикосы", "Нужно 1 кг. абрикосов", TaskStatus.NEW);
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Обучение", "Обучение JAVA");
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId());
        SubTask firstEpicSecondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.DONE, firstEpic.getId());
        SubTask firstEpicThirdSubTask = new SubTask("Убрать в доме", "Убрать в доме перед отъездом",
                TaskStatus.DONE, firstEpic.getId());
        Task newTask = new Task("Заправиться с утра", "Заехать на заправку", TaskStatus.NEW);

        // Добавление Task, SubTas и Epic
        inMemoryHistoryManager.addTask(firstTask);
        inMemoryHistoryManager.addTask(secondTask);
        inMemoryHistoryManager.addTask(thirdTask);
        inMemoryHistoryManager.addTask(fourthTask);
        inMemoryHistoryManager.addTask(fifthTask);
        inMemoryHistoryManager.addEpic(firstEpic);
        inMemoryHistoryManager.addEpic(secondEpic);
        inMemoryHistoryManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        inMemoryHistoryManager.addSubTaskIntoEpic(firstEpicSecondSubTask);
        inMemoryHistoryManager.addSubTaskIntoEpic(firstEpicThirdSubTask);
        inMemoryHistoryManager.addTask(newTask);

        // Вывод пустой истории
        printTest("Вывод пустой истории");
        System.out.println(inMemoryHistoryManager.printHistory());

        // Вывод одной задачи в истории
        printTest("Вывод одной задачи в истории");
        inMemoryHistoryManager.showTaskById(firstTask.getId());
        System.out.println(inMemoryHistoryManager.printHistory());

        // Вывод той же самой  задачи в истории
        printTest("Вывод той же самой задачи в истории");
        inMemoryHistoryManager.showTaskById(firstTask.getId());
        System.out.println(inMemoryHistoryManager.printHistory());

        // Вывод двух задач в истории
        printTest("Вывод двух задач в истории");
        inMemoryHistoryManager.showTaskById(secondTask.getId());
        System.out.println(inMemoryHistoryManager.printHistory());

        // Вызов повторно первой задачи
        printTest("Вывод повторно первой задачи");
        inMemoryHistoryManager.showTaskById(firstTask.getId());
        System.out.println(inMemoryHistoryManager.printHistory());

        // Вывод трех задач в истории
        printTest("Вывод трех задач в истории");
        inMemoryHistoryManager.showTaskById(thirdTask.getId());
        System.out.println(inMemoryHistoryManager.printHistory());

        // Вызов повторно первой задачи
        printTest("Вывод повторно первой задачи");
        inMemoryHistoryManager.showTaskById(firstTask.getId());
        System.out.println(inMemoryHistoryManager.printHistory());

        // Вывод десяти задач в истории
        printTest("Вывод десяти задач в истории");
        inMemoryHistoryManager.showTaskById(thirdTask.getId());
        inMemoryHistoryManager.showTaskById(fourthTask.getId());
        inMemoryHistoryManager.showTaskById(fifthTask.getId());
        inMemoryHistoryManager.showTaskById(firstEpic.getId());
        inMemoryHistoryManager.showTaskById(secondEpic.getId());
        inMemoryHistoryManager.showTaskById(firstEpicFirstSubTask.getId());
        inMemoryHistoryManager.showTaskById(firstEpicSecondSubTask.getId());
        inMemoryHistoryManager.showTaskById(firstEpicThirdSubTask.getId());
        System.out.println(inMemoryHistoryManager.printHistory());

        // Вывод десяти задач в истории, дублирование задачи из середины истории
        printTest("Вывод десяти задач в истории, дублирование задачи из середины истории");
        inMemoryHistoryManager.showTaskById(fifthTask.getId());
        System.out.println(inMemoryHistoryManager.printHistory());

        // Вывод десяти задач в истории, дублирование задачи из налача истории
        printTest("Вывод десяти задач в истории, дублирование задачи из начала истории");
        inMemoryHistoryManager.showTaskById(firstTask.getId());
        System.out.println(inMemoryHistoryManager.printHistory());

        // Вывод десяти задач в истории, дублирование задачи из конца истории
        printTest("Вывод десяти задач в истории, дублирование задачи из конца истории");
        inMemoryHistoryManager.showTaskById(firstTask.getId());
        System.out.println(inMemoryHistoryManager.printHistory());

        // Вывод десяти задач в истории, добавление новой задачи в историю
        printTest("Вывод десяти задач в истории, добавление новой задачи в историю");
        inMemoryHistoryManager.showTaskById(newTask.getId());
        System.out.println(inMemoryHistoryManager.printHistory());



//        // 1 Добавление новой задачи, эпика и подзадачи.
//        // 1.1 Создаем задачу и добавляем в трекер задач.
//        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
//        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\" из Гриднева",
//                TaskStatus.DONE);
//        inMemoryHistoryManager.addTask(firstTask);
//        inMemoryHistoryManager.addTask(secondTask);
//
//        // 1.2 Создаем эпик и добавляем в трекер задач.
//        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
//        Epic secondEpic = new Epic("Обучение", "Обучение JAVA");
//        inMemoryHistoryManager.addEpic(firstEpic);
//        inMemoryHistoryManager.addEpic(secondEpic);
//
//        // 1.2.1 Создаем пустой эпик и добавляем в трекер задач.
//        Epic emptyEpic = new Epic("Пустой Эпик", "Эпик без подзадач");
//        inMemoryHistoryManager.addEpic(emptyEpic);
//
//        // 1.3 Создаем субтаск и добавляем в трекер задач.
//        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
//                "Положить в чемодан все необходимое",
//                TaskStatus.DONE, firstEpic.getId());
//        SubTask firstEpicSecondSubTask = new SubTask("Забрать сноуборд",
//                "Забрать свой сноуборд из кладовки", TaskStatus.DONE, firstEpic.getId());
//        SubTask firstEpicThirdSubTask = new SubTask("Убрать в доме", "Убрать в доме перед отъездом",
//                TaskStatus.DONE, firstEpic.getId());
//        inMemoryHistoryManager.addSubTaskIntoEpic(firstEpicFirstSubTask); // Добавляем субтаск в первый епик
//        inMemoryHistoryManager.addSubTaskIntoEpic(firstEpicSecondSubTask);
//        inMemoryHistoryManager.addSubTaskIntoEpic(firstEpicThirdSubTask);
//
//        SubTask secondEpicFirstSubTask = new SubTask("Изучить ArrayList",
//                "Научиться добавлять и удалять из ArrayList", TaskStatus.NEW, secondEpic.getId());
//        SubTask secondEpicSecondSubTask = new SubTask("Изучить private",
//                "Понять действие модификатора доступа private", TaskStatus.NEW, secondEpic.getId());
//        SubTask secondEpicThirdSubTask = new SubTask("Изучить Override",
//                "Научиться переопределять методы", TaskStatus.NEW, secondEpic.getId());
//        inMemoryHistoryManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
//        inMemoryHistoryManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
//        inMemoryHistoryManager.addSubTaskIntoEpic(secondEpicThirdSubTask);
//
//        // 3 Получение списка всех задач.
//        printTest("Проверка получения списка всех задач");
//        inMemoryHistoryManager.showAllTasks();
//
//        // 4 Получение списка всех эпиков.
//        printTest("Проверка получения списка всех Эпиков");
//        inMemoryHistoryManager.showAllEpics();
//
//        // 5 Получение списка всех подзадач определённого эпика.
//        printTest("Проверка получения всех подзадач определённого эпика");
//        inMemoryHistoryManager.showSubTaskListFromEpicById(secondEpic.getId());
//
//        // 5.1 Получение списка всех подзадач пустого эпика.
//        printTest("Проверка получения всех подзадач пустого эпика");
//        inMemoryHistoryManager.showSubTaskListFromEpicById(emptyEpic.getId());
//
//        // 6 Получение задачи любого типа по идентификатору, проверка истории задач.
//        printTest("История просмотров задач: пустая история");
//        System.out.println(inMemoryHistoryManager.printHistory());
//        printTest("Проверка получения по идентификатору Task");
//        inMemoryHistoryManager.showTaskById(firstTask.getId()); // задача
//        printTest("История просмотров задач: один элемент в истории");
//        System.out.println(inMemoryHistoryManager.printHistory());
//        printTest("Проверка получения по идентификатору Epic");
//        inMemoryHistoryManager.showTaskById(firstEpic.getId()); // эпик
//        printTest("История просмотров задач: два элемента в истории");
//        System.out.println(inMemoryHistoryManager.printHistory());
//        printTest("Проверка получения по идентификатору SubTask");
//        inMemoryHistoryManager.showTaskById(firstEpicFirstSubTask.getId()); // подзадача
//        printTest("История просмотров задач: три элемента в истории");
//        System.out.println(inMemoryHistoryManager.printHistory());
//        printTest("Вызов задач для проверки истории:");
//        inMemoryHistoryManager.showTaskById(secondEpicFirstSubTask.getId());
//        inMemoryHistoryManager.showTaskById(secondEpicSecondSubTask.getId());
//        inMemoryHistoryManager.showTaskById(secondEpicThirdSubTask.getId());
//        inMemoryHistoryManager.showTaskById(secondTask.getId());
//        inMemoryHistoryManager.showTaskById(firstEpicFirstSubTask.getId());
//        inMemoryHistoryManager.showTaskById(firstEpicSecondSubTask.getId());
//        inMemoryHistoryManager.showTaskById(secondEpic.getId());
//        printTest("История просмотров задач: десять элементов в истории");
//        System.out.println(inMemoryHistoryManager.printHistory());
//        printTest("История просмотров задач: добавление одинадцатого элемента");
//        inMemoryHistoryManager.showTaskById(firstEpicThirdSubTask.getId());
//        printTest("История просмотров задач: вытеснение первого элемента при добавлении одинадцатого");
//        System.out.println(inMemoryHistoryManager.printHistory());
//
//        // 7 Обновление задачи любого типа по идентификатору.
//        // 7.1 Обновление подзадачи определенного эпика.
//        SubTask testSubtask = new SubTask("Изучить дженерики", "Изучить случаи применения дженериков",
//                TaskStatus.DONE, secondEpic.getId());
//        printTest("Проверка обновления подзадачи у эпика - вывод эпика до обновления");
//        inMemoryHistoryManager.showTaskById(secondEpic.getId());
//        printTest("Проверка обновления подзадачи у эпика - выполняем обновление");
//        inMemoryHistoryManager.renewTaskById(secondEpicFirstSubTask.getId(), testSubtask); // Здесь мы проверяем, изменился ли статус эпика.
//        printTest("Проверка обновления подзадачи у эпика - вывод эпика после обновления");
//        inMemoryHistoryManager.showTaskById(secondEpic.getId());
//
//        // 7.2 Обновление задачи.
//        Task testTask = new Task("Помыть машину", "Записаться на автомойку \"Мой - ка\"",
//                TaskStatus.NEW);
//        printTest("Проверка обновления задачи - вывод задачи до обновления");
//        inMemoryHistoryManager.showTaskById(secondTask.getId());
//        printTest("Проверка обновления задачи - выполняем обновление");
//        inMemoryHistoryManager.renewTaskById(secondTask.getId(), testTask);
//        printTest("Проверка обновления задачи - вывод задачи после обновления");
//        inMemoryHistoryManager.showTaskById(secondTask.getId());
//
//        // 7.3 Обновление эпика
//        Epic testEpic = new Epic("Сходить в спортзал", "Потренировать две группы мышц");
//        printTest("Проверка обновления эпика - вывод эпика до обновления");
//        inMemoryHistoryManager.showTaskById(firstEpic.getId());
//        printTest("Проверка обновления эпика - выполняем обновление");
//        inMemoryHistoryManager.renewTaskById(firstEpic.getId(), testEpic);
//        printTest("Проверка обновления эпика - вывод эпика после обновления");
//        inMemoryHistoryManager.showTaskById(firstEpic.getId());
//
//        // 8 Удаление ранее добавленных задач — всех и по идентификатору.
//        // 8.1 Удаление ранее добавленных задач по идентификатору.
//        // 8.1.1 Проверка удаления Task.
//        printTest("Проверка удаления по идентификатору Task");
//        inMemoryHistoryManager.showTaskById(firstTask.getId());
//        inMemoryHistoryManager.deleteTaskById(firstTask.getId());
//        inMemoryHistoryManager.showTaskById(firstTask.getId());
//
//        // 8.1.2 Проверка удаления SubTask.
//        printTest("Проверка удаления по идентификатору SubTask");
//        inMemoryHistoryManager.showTaskById(secondEpicFirstSubTask.getId());
//        inMemoryHistoryManager.deleteTaskById(secondEpicFirstSubTask.getId());
//        inMemoryHistoryManager.showTaskById(secondEpicFirstSubTask.getId());
//
//        // 8.1.3 Проверка удаления Epic.
//        printTest("Проверка удаления по идентификатору Epic");
//        inMemoryHistoryManager.showTaskById(secondEpic.getId());
//        inMemoryHistoryManager.deleteTaskById(secondEpic.getId());
//        inMemoryHistoryManager.showTaskById(secondEpic.getId());
//
//        // 8.2 Удаление всех ранее добавленных задач.
//        printTest("Проверка удаления всех сущностей - вывод полного списка");
//        inMemoryHistoryManager.showAllTasks();
//        printTest("Проверка удаления всех сущностей - удаление сущностей");
//        inMemoryHistoryManager.deleteAllTasks();
//        printTest("Проверка удаления всех сущностей - вывод полного списка после удаления");
//        inMemoryHistoryManager.showAllTasks();
//    }
//

    }
    public static void printTest (String value) {
        System.out.println();
        System.out.println("LOGGER [INFO]: " + value + " : ");
        System.out.println("---");
    }
}
