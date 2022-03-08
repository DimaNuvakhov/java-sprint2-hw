package inmemorymanagers;

import managers.Manager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryManagerTest {

    @Test
    public void taskTest() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);

        // Шаг первый - добавление задачи в трекер
        // Добавление задачи в трекер задач
        inMemoryManager.addTask(firstTask);
        // Поиск задачи в трекере по имени
        Task sameTask = getTaskByName(inMemoryManager, "Помыть посуду");
        // Проверка, есть ли задача в трекере
        assertNotNull(sameTask);
        // Сверка описания задачи
        assertEquals("Помыть тарелки и вилки", sameTask.getDescription());
        // Сверка статуса задачи
        assertEquals(TaskStatus.NEW, sameTask.getStatus());

        // Шаг второй - создание новой задачи и обновление существующей
        // Создание новой задачи
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE);
        // Обновление задачи
        inMemoryManager.renewTaskById(sameTask.getId(), secondTask);
        // Поиск задачи в трекере по имени
        Task oldTask = getTaskByName(inMemoryManager, "Помыть посуду");
        // Проверка, что задачи нет в трекере
        assertNull(oldTask);
        // Поиск обновленной задачи в трекере по имени
        Task renewedTask = getTaskByName(inMemoryManager, "Купить хлеб");
        // Проверка, есть ли обновленная задача в трекере
        assertNotNull(renewedTask);
        // Сверка описания задачи
        assertEquals("Нужен хлеб \"Литовский\"", renewedTask.getDescription());
        // Сверка статуса задачи
        assertEquals(TaskStatus.DONE, renewedTask.getStatus());
        // Сверка id первой задачи с id обновленной задачи
        assertEquals(sameTask.getId(), renewedTask.getId());
        // Удаление задачи из трекера
        inMemoryManager.deleteTaskById(renewedTask.getId());
        assertEquals(0, inMemoryManager.getAllItems().size());
    }

    @Test
    public void epicAndSubTaskTest() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");

        // Шаг первый - добавление эпика в трекер
        // Добавление эпика в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Поиск задачи в трекере по имени
        Epic sameEpic = (Epic) getTaskByName(inMemoryManager, "Переезд");
        // Проверка, есть ли эпик в трекере
        assertNotNull(sameEpic);
        // Сверка описания эпика
        assertEquals("Собрать все вещи", sameEpic.getDescription());

        // Шаг второй - создание нового эпика и обновление существующего
        // Создание нового эпика
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Обновление эпика
        inMemoryManager.renewTaskById(firstEpic.getId(), secondEpic);
        // Поиск задачи в трекере по имени
        Epic oldEpic = (Epic) getTaskByName(inMemoryManager, "Переезд");
        // Проверка, что эпика нет в трекере
        assertNull(oldEpic);
        // Поиск обновленной задачи в трекере по имени
        Task renewedEpic = getTaskByName(inMemoryManager, "Изучение Java");
        // Проверка, есть ли обновленный эпик в трекере
        assertNotNull(renewedEpic);
        // Сверка описания эпика
        assertEquals("Изучить язык программирования Java", renewedEpic.getDescription());
        // Сверка id первого эпика с id обновленного эпика
        assertEquals(sameEpic.getId(), renewedEpic.getId());

        // Шаг третий - добавление подзадачи в эпик
        // Создание подзадачи к эпику
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, renewedEpic.getId());
        // Добавление подзадачи в трекер задач
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        // Проверка наличия эпика у подзадачи
        assertEquals(renewedEpic.getId(), firstSubTask.getEpicId());
        // Поиск подзадачи в трекере по имени
        SubTask sameSubTask = (SubTask) getTaskByName(inMemoryManager, "Изучить Дженерики");
        // Сверка описания задачи
        assertEquals("Изучить случаи применения дженериков", sameSubTask.getDescription());
        // Сверка статуса задачи
        assertEquals(TaskStatus.NEW, sameSubTask.getStatus());

        // Шаг четвертый - создание новой подзадачи и обновление существующей
        // Создание новой подзадачи
        SubTask secondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, renewedEpic.getId());
        // Обновление подзадачи
        inMemoryManager.renewTaskById(sameSubTask.getId(), secondSubTask);
        // Поиск подзадачи в трекере по имени
        Task oldTask = getTaskByName(inMemoryManager, "Изучить Дженерики");
        // Проверка, что подзадачи нет в трекере
        assertNull(oldTask);
        // Поиск обновленной подзадачи в трекере по имени
        Task renewedSubTask = getTaskByName(inMemoryManager, "Изучить полиморфизм");
        // Проверка, есть ли обновленная подзадача в трекере
        assertNotNull(renewedSubTask);
        // Сверка описания задачи
        assertEquals("Изучить перегрузку методов", renewedSubTask.getDescription());
        // Сверка статуса задачи
        assertEquals(TaskStatus.NEW, renewedSubTask.getStatus());
        // Сверка id первой подзадачи с id обновленной подзадачи
        assertEquals(sameSubTask.getId(), renewedSubTask.getId());

        // Шаг пятый - проверка статуса эпика
        // Проверка статуса при пустом списке подзадач
//        assertEquals(TaskStatus.NEW, firstEpic.getStatus());
//        // Создание субтасок к эпику, все подзадачи со статусом NEW
//        SubTask newFirstSubTask = new SubTask("Собрать чемодан",
//                "Положить в чемодан все необходимое", TaskStatus.NEW, firstEpic.getId());
//        SubTask newSecondSubTask = new SubTask("Забрать сноуборд",
//                "Забрать свой сноуборд из кладовки", TaskStatus.NEW, firstEpic.getId());
//        // Добавление субтасок в трекер задач
//        inMemoryManager.addSubTaskIntoEpic(newFirstSubTask);
//        inMemoryManager.addSubTaskIntoEpic(newSecondSubTask);
//        // Проверка статуса эпика, если у эпика две новые подзадачи
//        assertEquals(TaskStatus.NEW, firstEpic.getStatus());
//        // Создание обновленных подзадач со статусом DONE
//        SubTask doneFirstSubTask = new SubTask("Собрать чемодан",
//                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId());
//        SubTask doneSecondSubTask = new SubTask("Забрать сноуборд",
//                "Забрать свой сноуборд из кладовки", TaskStatus.DONE, firstEpic.getId());
//        // Обновление подзадач эпика
//        inMemoryManager.renewTaskById(newFirstSubTask.getId(), doneFirstSubTask);
//        inMemoryManager.renewTaskById(newSecondSubTask.getId(), doneSecondSubTask);
//        // Проверка статуса эпика, если у эпика две завершенные подзадачи
//        assertEquals(TaskStatus.DONE, firstEpic.getStatus());
//        // Обновление второй подзадачи на подзадачу со статусом NEW
//        inMemoryManager.renewTaskById(doneSecondSubTask.getId(), newSecondSubTask);
//        // Проверка статуса эпика, если у эпика две подзадачи со статусами NEW и DONE
//        assertEquals(TaskStatus.IN_PROGRESS, firstEpic.getStatus());
//        // Создание обновленных подзадач со статусом IN_PROGRESS
//        SubTask inProgressFirstSubTask = new SubTask("Собрать чемодан",
//                "Положить в чемодан все необходимое", TaskStatus.IN_PROGRESS, firstEpic.getId());
//        SubTask inProgressSecondSubTask = new SubTask("Забрать сноуборд",
//                "Забрать свой сноуборд из кладовки", TaskStatus.IN_PROGRESS, firstEpic.getId());
//        // Обновление подзадач эпика
//        inMemoryManager.renewTaskById(doneFirstSubTask.getId(), inProgressFirstSubTask);
//        inMemoryManager.renewTaskById(newSecondSubTask.getId(), inProgressSecondSubTask);
//        // Проверка статуса эпика, если у эпика две подзадачи со статусами IN_PROGRESS
//        assertEquals(TaskStatus.IN_PROGRESS, firstEpic.getStatus());
    }

    public Task getTaskByName(Manager inMemoryManager, String name) {
        for (Task task : inMemoryManager.getAllItems().values()) {
            if (task.getName().equals(name)) {
                return task;
            }
        }
        return null;
    }
}