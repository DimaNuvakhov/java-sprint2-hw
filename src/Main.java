import epic.Epic;
import manager.Manager;
import managers.Managers;
import subtask.SubTask;
import task.Task;
import taskstatus.TaskStatus;

public class Main {
    public static void main(String[] args) {
        Manager inMemoryManager = Managers.getDefault();

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
        inMemoryManager.addTask(firstTask);
        inMemoryManager.addTask(secondTask);
        inMemoryManager.addTask(thirdTask);
        inMemoryManager.addTask(fourthTask);
        inMemoryManager.addTask(fifthTask);
        inMemoryManager.addEpic(firstEpic);
        inMemoryManager.addEpic(secondEpic);
        inMemoryManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        inMemoryManager.addSubTaskIntoEpic(firstEpicSecondSubTask);
        inMemoryManager.addSubTaskIntoEpic(firstEpicThirdSubTask);
        inMemoryManager.addTask(newTask);

        // Вывод пустой истории
        printTest("Вывод пустой истории");
        System.out.println(inMemoryManager.printHistory());

        // Вывод одной задачи в истории
        printTest("Вывод одной задачи в истории");
        inMemoryManager.showTaskById(firstTask.getId());
        System.out.println(inMemoryManager.printHistory());

        // Вывод той же самой  задачи в истории
        printTest("Вывод той же самой задачи в истории");
        inMemoryManager.showTaskById(firstTask.getId());
        System.out.println(inMemoryManager.printHistory());

        // Вывод двух задач в истории
        printTest("Вывод двух задач в истории");
        inMemoryManager.showTaskById(secondTask.getId());
        System.out.println(inMemoryManager.printHistory());

        // Вызов повторно первой задачи
        printTest("Вывод повторно первой задачи");
        inMemoryManager.showTaskById(firstTask.getId());
        System.out.println(inMemoryManager.printHistory());

        // Вывод трех задач в истории
        printTest("Вывод трех задач в истории");
        inMemoryManager.showTaskById(thirdTask.getId());
        System.out.println(inMemoryManager.printHistory());

        // Вызов повторно первой задачи
        printTest("Вывод повторно первой задачи");
        inMemoryManager.showTaskById(firstTask.getId());
        System.out.println(inMemoryManager.printHistory());

        // Вывод десяти задач в истории
        printTest("Вывод десяти задач в истории");
        inMemoryManager.showTaskById(thirdTask.getId());
        inMemoryManager.showTaskById(fourthTask.getId());
        inMemoryManager.showTaskById(fifthTask.getId());
        inMemoryManager.showTaskById(firstEpic.getId());
        inMemoryManager.showTaskById(secondEpic.getId());
        inMemoryManager.showTaskById(firstEpicFirstSubTask.getId());
        inMemoryManager.showTaskById(firstEpicSecondSubTask.getId());
        inMemoryManager.showTaskById(firstEpicThirdSubTask.getId());
        System.out.println(inMemoryManager.printHistory());

        // Вывод десяти задач в истории, дублирование задачи из середины истории
        printTest("Вывод десяти задач в истории, дублирование задачи из середины истории");
        inMemoryManager.showTaskById(fifthTask.getId());
        System.out.println(inMemoryManager.printHistory());

        // Вывод десяти задач в истории, дублирование задачи из налача истории
        printTest("Вывод десяти задач в истории, дублирование задачи из начала истории");
        inMemoryManager.showTaskById(secondTask.getId());
        System.out.println(inMemoryManager.printHistory());

        // Вывод десяти задач в истории, дублирование задачи из конца истории
        printTest("Вывод десяти задач в истории, дублирование задачи из конца истории");
        inMemoryManager.showTaskById(secondTask.getId());
        System.out.println(inMemoryManager.printHistory());

        // Вывод десяти задач в истории, добав ление новой задачи в историю
        printTest("Добавление новой задачи в историю");
        inMemoryManager.showTaskById(newTask.getId());
        System.out.println(inMemoryManager.printHistory());


//        // 1 Добавление новой задачи, эпика и подзадачи.
//        // 1.1 Создаем задачу и добавляем в трекер задач.
//        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
//        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\" из Гриднева",
//                TaskStatus.DONE);
//        inMemoryManager.addTask(firstTask);
//        inMemoryManager.addTask(secondTask);
//
//        // 1.2 Создаем эпик и добавляем в трекер задач.
//        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
//        Epic secondEpic = new Epic("Обучение", "Обучение JAVA");
//        inMemoryManager.addEpic(firstEpic);
//        inMemoryManager.addEpic(secondEpic);
//
//        // 1.2.1 Создаем пустой эпик и добавляем в трекер задач.
//        Epic emptyEpic = new Epic("Пустой Эпик", "Эпик без подзадач");
//        inMemoryManager.addEpic(emptyEpic);
//
//        // 1.3 Создаем субтаск и добавляем в трекер задач.
//        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
//                "Положить в чемодан все необходимое",
//                TaskStatus.DONE, firstEpic.getId());
//        SubTask firstEpicSecondSubTask = new SubTask("Забрать сноуборд",
//                "Забрать свой сноуборд из кладовки", TaskStatus.DONE, firstEpic.getId());
//        SubTask firstEpicThirdSubTask = new SubTask("Убрать в доме", "Убрать в доме перед отъездом",
//                TaskStatus.DONE, firstEpic.getId());
//        inMemoryManager.addSubTaskIntoEpic(firstEpicFirstSubTask); // Добавляем субтаск в первый епик
//        inMemoryManager.addSubTaskIntoEpic(firstEpicSecondSubTask);
//        inMemoryManager.addSubTaskIntoEpic(firstEpicThirdSubTask);
//
//        SubTask secondEpicFirstSubTask = new SubTask("Изучить ArrayList",
//                "Научиться добавлять и удалять из ArrayList", TaskStatus.NEW, secondEpic.getId());
//        SubTask secondEpicSecondSubTask = new SubTask("Изучить private",
//                "Понять действие модификатора доступа private", TaskStatus.NEW, secondEpic.getId());
//        SubTask secondEpicThirdSubTask = new SubTask("Изучить Override",
//                "Научиться переопределять методы", TaskStatus.NEW, secondEpic.getId());
//        inMemoryManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
//        inMemoryManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
//        inMemoryManager.addSubTaskIntoEpic(secondEpicThirdSubTask);
//
//        // 3 Получение списка всех задач.
//        printTest("Проверка получения списка всех задач");
//        inMemoryManager.showAllTasks();
//
//        // 4 Получение списка всех эпиков.
//        printTest("Проверка получения списка всех Эпиков");
//        inMemoryManager.showAllEpics();
//
//        // 5 Получение списка всех подзадач определённого эпика.
//        printTest("Проверка получения всех подзадач определённого эпика");
//        inMemoryManager.showSubTaskListFromEpicById(secondEpic.getId());
//
//        // 5.1 Получение списка всех подзадач пустого эпика.
//        printTest("Проверка получения всех подзадач пустого эпика");
//        inMemoryManager.showSubTaskListFromEpicById(emptyEpic.getId());
//
//        // 6 Получение задачи любого типа по идентификатору, проверка истории задач.
//        printTest("История просмотров задач: пустая история");
//        System.out.println(inMemoryManager.printHistory());
//        printTest("Проверка получения по идентификатору Task");
//        inMemoryManager.showTaskById(firstTask.getId()); // задача
//        printTest("История просмотров задач: один элемент в истории");
//        System.out.println(inMemoryManager.printHistory());
//        printTest("Проверка получения по идентификатору Epic");
//        inMemoryManager.showTaskById(firstEpic.getId()); // эпик
//        printTest("История просмотров задач: два элемента в истории");
//        System.out.println(inMemoryManager.printHistory());
//        printTest("Проверка получения по идентификатору SubTask");
//        inMemoryManager.showTaskById(firstEpicFirstSubTask.getId()); // подзадача
//        printTest("История просмотров задач: три элемента в истории");
//        System.out.println(inMemoryManager.printHistory());
//        printTest("Вызов задач для проверки истории:");
//        inMemoryManager.showTaskById(secondEpicFirstSubTask.getId());
//        inMemoryManager.showTaskById(secondEpicSecondSubTask.getId());
//        inMemoryManager.showTaskById(secondEpicThirdSubTask.getId());
//        inMemoryManager.showTaskById(secondTask.getId());
//        inMemoryManager.showTaskById(firstEpicFirstSubTask.getId());
//        inMemoryManager.showTaskById(firstEpicSecondSubTask.getId());
//        inMemoryManager.showTaskById(secondEpic.getId());
//        printTest("История просмотров задач: десять элементов в истории");
//        System.out.println(inMemoryManager.printHistory());
//        printTest("История просмотров задач: добавление одинадцатого элемента");
//        inMemoryManager.showTaskById(firstEpicThirdSubTask.getId());
//        printTest("История просмотров задач: вытеснение первого элемента при добавлении одинадцатого");
//        System.out.println(inMemoryManager.printHistory());
//
//        // 7 Обновление задачи любого типа по идентификатору.
//        // 7.1 Обновление подзадачи определенного эпика.
//        SubTask testSubtask = new SubTask("Изучить дженерики", "Изучить случаи применения дженериков",
//                TaskStatus.DONE, secondEpic.getId());
//        printTest("Проверка обновления подзадачи у эпика - вывод эпика до обновления");
//        inMemoryManager.showTaskById(secondEpic.getId());
//        printTest("Проверка обновления подзадачи у эпика - выполняем обновление");
//        inMemoryManager.renewTaskById(secondEpicFirstSubTask.getId(), testSubtask); // Здесь мы проверяем, изменился ли статус эпика.
//        printTest("Проверка обновления подзадачи у эпика - вывод эпика после обновления");
//        inMemoryManager.showTaskById(secondEpic.getId());
//
//        // 7.2 Обновление задачи.
//        Task testTask = new Task("Помыть машину", "Записаться на автомойку \"Мой - ка\"",
//                TaskStatus.NEW);
//        printTest("Проверка обновления задачи - вывод задачи до обновления");
//        inMemoryManager.showTaskById(secondTask.getId());
//        printTest("Проверка обновления задачи - выполняем обновление");
//        inMemoryManager.renewTaskById(secondTask.getId(), testTask);
//        printTest("Проверка обновления задачи - вывод задачи после обновления");
//        inMemoryManager.showTaskById(secondTask.getId());
//
//        // 7.3 Обновление эпика
//        Epic testEpic = new Epic("Сходить в спортзал", "Потренировать две группы мышц");
//        printTest("Проверка обновления эпика - вывод эпика до обновления");
//        inMemoryManager.showTaskById(firstEpic.getId());
//        printTest("Проверка обновления эпика - выполняем обновление");
//        inMemoryManager.renewTaskById(firstEpic.getId(), testEpic);
//        printTest("Проверка обновления эпика - вывод эпика после обновления");
//        inMemoryManager.showTaskById(firstEpic.getId());
//
//        // 8 Удаление ранее добавленных задач — всех и по идентификатору.
//        // 8.1 Удаление ранее добавленных задач по идентификатору.
//        // 8.1.1 Проверка удаления Task.
//        printTest("Проверка удаления по идентификатору Task");
//        inMemoryManager.showTaskById(firstTask.getId());
//        inMemoryManager.deleteTaskById(firstTask.getId());
//        inMemoryManager.showTaskById(firstTask.getId());
//
//        // 8.1.2 Проверка удаления SubTask.
//        printTest("Проверка удаления по идентификатору SubTask");
//        inMemoryManager.showTaskById(secondEpicFirstSubTask.getId());
//        inMemoryManager.deleteTaskById(secondEpicFirstSubTask.getId());
//        inMemoryManager.showTaskById(secondEpicFirstSubTask.getId());
//
//        // 8.1.3 Проверка удаления Epic.
//        printTest("Проверка удаления по идентификатору Epic");
//        inMemoryManager.showTaskById(secondEpic.getId());
//        inMemoryManager.deleteTaskById(secondEpic.getId());
//        inMemoryManager.showTaskById(secondEpic.getId());
//
//        // 8.2 Удаление всех ранее добавленных задач.
//        printTest("Проверка удаления всех сущностей - вывод полного списка");
//        inMemoryManager.showAllTasks();
//        printTest("Проверка удаления всех сущностей - удаление сущностей");
//        inMemoryManager.deleteAllTasks();
//        printTest("Проверка удаления всех сущностей - вывод полного списка после удаления");
//        inMemoryManager.showAllTasks();
//    }
//

    }

    public static void printTest(String value) {
        System.out.println();
        System.out.println("LOGGER [INFO]: " + value + " : ");
        System.out.println("---");
    }
}
