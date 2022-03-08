package inmemorymanagers;

import managers.Manager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryManagerTest {

    @Test
    public void test() {
        Manager inMemoryManager = Managers.getDefault();
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        // Добавление задачи в трекер задач
        inMemoryManager.addTask(firstTask);
        for (Task task : inMemoryManager.getAllTasks()) {
            if (task.getId().equals(firstTask.getId())) {
                assertEquals(firstTask, task);
            }
        }
//        // Создание эпика
//        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
//        // Добавление эпика в трекер задач
//        inMemoryManager.addEpic(firstEpic);
//        // Создание субтасок к эпику
//        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
//                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId());
//        SubTask firstEpicSecondSubTask = new SubTask("Забрать сноуборд",
//                "Забрать свой сноуборд из кладовки", TaskStatus.NEW, firstEpic.getId());
//        SubTask firstEpicThirdSubTask = new SubTask("Убрать в доме",
//                "Убрать в доме перед отъездом", TaskStatus.DONE, firstEpic.getId());
//        // Добавление субтасок в трекер задач
//        inMemoryManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
//        inMemoryManager.addSubTaskIntoEpic(firstEpicSecondSubTask);
//        inMemoryManager.addSubTaskIntoEpic(firstEpicThirdSubTask);
//        // Добавление задач в историю
//        inMemoryManager.getTaskById(firstTask.getId());
//        inMemoryManager.getTaskById(firstEpicFirstSubTask.getId());
//        inMemoryManager.getTaskById(firstEpic.getId());
    }
}