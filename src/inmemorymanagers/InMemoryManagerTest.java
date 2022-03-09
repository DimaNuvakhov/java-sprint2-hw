package inmemorymanagers;

import managers.Manager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;

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
        // Проверка, что задача удалена
        assertEquals(0, inMemoryManager.getAllTasks().size());
    }

    @Test
    public void epicAndSubTaskTest() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");

        // Шаг первый - добавление эпика в трекер
        // Добавление эпика в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Поиск задачи в трекере по имени
        Epic sameEpic = (Epic) getTaskByName(inMemoryManager, "Сходить в спортзал");
        // Проверка, есть ли эпик в трекере
        assertNotNull(sameEpic);
        // Сверка описания эпика
        assertEquals("Прокачать 3 группы мышц", sameEpic.getDescription());

        // Шаг второй - создание нового эпика и обновление существующего
        // Создание нового эпика
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Обновление эпика
        inMemoryManager.renewTaskById(firstEpic.getId(), secondEpic);
        // Поиск задачи в трекере по имени
        Epic oldEpic = (Epic) getTaskByName(inMemoryManager, "Сходить в спортзал");
        // Проверка, что эпика нет в трекере
        assertNull(oldEpic);
        // Поиск обновленной задачи в трекере по имени
        Epic renewedEpic = (Epic) getTaskByName(inMemoryManager, "Изучение Java");
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
        // Поиск подзадачи в трекере по имени
        SubTask sameSubTask = (SubTask) getTaskByName(inMemoryManager, "Изучить Дженерики");
        // Проверка, есть ли обновленная подзадача в трекере
        assertNotNull(sameSubTask);
        // Сверка описания задачи
        assertEquals("Изучить случаи применения дженериков", sameSubTask.getDescription());
        // Сверка статуса задачи
        assertEquals(TaskStatus.NEW, sameSubTask.getStatus());
        // Проверка наличия эпика у подзадачи
        assertEquals(renewedEpic.getId(), sameSubTask.getEpicId());

        // Шаг четвертый - создание новой подзадачи и обновление существующей
        // Создание новой подзадачи
        SubTask secondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, renewedEpic.getId());
        // Обновление подзадачи
        inMemoryManager.renewTaskById(sameSubTask.getId(), secondSubTask);
        // Поиск подзадачи в трекере по имени
        SubTask oldTask = (SubTask) getTaskByName(inMemoryManager, "Изучить Дженерики");
        // Проверка, что подзадачи нет в трекере
        assertNull(oldTask);
        // Поиск обновленной подзадачи в трекере по имени
        SubTask renewedSubTask = (SubTask) getTaskByName(inMemoryManager, "Изучить полиморфизм");
        // Проверка, есть ли обновленная подзадача в трекере
        assertNotNull(renewedSubTask);
        // Сверка описания задачи
        assertEquals("Изучить перегрузку методов", renewedSubTask.getDescription());
        // Сверка статуса задачи
        assertEquals(TaskStatus.NEW, renewedSubTask.getStatus());
        // Сверка id первой подзадачи с id обновленной подзадачи
        assertEquals(sameSubTask.getId(), renewedSubTask.getId());
        // Удаление подзадачи из трекера
        inMemoryManager.deleteTaskById(renewedSubTask.getId());
        // Проверка, что подзадача удалена
        assertEquals(0, inMemoryManager.getAllSubtasks().size());
        // Удаление эпика из трекера
        inMemoryManager.deleteTaskById(renewedEpic.getId());
        // Проверка, что эпик удален
        assertEquals(0, inMemoryManager.getAllEpics().size());
    }

    @Test
    public void epicStatusTest() {
        // Проверка статуса эпика
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        // Добавление эпика в трекер
        inMemoryManager.addEpic(firstEpic);
        // Проверка статуса при пустом списке подзадач
        assertEquals(TaskStatus.NEW, firstEpic.getStatus());
        // Создание субтасок к эпику, все подзадачи со статусом NEW
        SubTask newFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.NEW, firstEpic.getId());
        SubTask newSecondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.NEW, firstEpic.getId());
        // Добавление субтасок в трекер задач
        inMemoryManager.addSubTaskIntoEpic(newFirstSubTask);
        inMemoryManager.addSubTaskIntoEpic(newSecondSubTask);
        // Проверка статуса эпика, если у эпика две новые подзадачи
        assertEquals(TaskStatus.NEW, firstEpic.getStatus());
        // Создание обновленных подзадач со статусом DONE
        SubTask doneFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId());
        SubTask doneSecondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.DONE, firstEpic.getId());
        // Обновление подзадач эпика
        inMemoryManager.renewTaskById(newFirstSubTask.getId(), doneFirstSubTask);
        inMemoryManager.renewTaskById(newSecondSubTask.getId(), doneSecondSubTask);
        // Проверка статуса эпика, если у эпика две завершенные подзадачи
        assertEquals(TaskStatus.DONE, firstEpic.getStatus());
        // Обновление второй подзадачи на подзадачу со статусом NEW
        inMemoryManager.renewTaskById(doneSecondSubTask.getId(), newSecondSubTask);
        // Проверка статуса эпика, если у эпика две подзадачи со статусами NEW и DONE
        assertEquals(TaskStatus.IN_PROGRESS, firstEpic.getStatus());
        // Создание обновленных подзадач со статусом IN_PROGRESS
        SubTask inProgressFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.IN_PROGRESS, firstEpic.getId());
        SubTask inProgressSecondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.IN_PROGRESS, firstEpic.getId());
        // Обновление подзадач эпика
        inMemoryManager.renewTaskById(doneFirstSubTask.getId(), inProgressFirstSubTask);
        inMemoryManager.renewTaskById(newSecondSubTask.getId(), inProgressSecondSubTask);
        // Проверка статуса эпика, если у эпика две подзадачи со статусами IN_PROGRESS
        assertEquals(TaskStatus.IN_PROGRESS, firstEpic.getStatus());
    }

    @Test
    public void methodTest() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Проверка, что список задач пуст
        assertEquals(0, inMemoryManager.getAllTasks().size());
        //Проверка, что список эпиков пуст
        assertEquals(0, inMemoryManager.getAllEpics().size());
        // Проверка, что список подзадач пуст
        assertEquals(0, inMemoryManager.getAllSubtasks().size());
        // Создание задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE);
        Task thirdTask = new Task("Купить билеты", "Купить билеты на гандбол", TaskStatus.NEW);
        // Добавлние задач в трекер
        inMemoryManager.addTask(firstTask);
        inMemoryManager.addTask(secondTask);
        inMemoryManager.addTask(thirdTask);
        // Создание эпиков
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпиков в трекер
        inMemoryManager.addEpic(firstEpic);
        inMemoryManager.addEpic(secondEpic);
        // Создание подзадач к первому эпику
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId());
        // Добавление подзадачи в трекер
        inMemoryManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        // Создание подзадач ко второму эпику
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId());
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId());
        SubTask secondEpicThirdSubTask = new SubTask("Изучить исключения",
                "Изучить проверяемые исключения", TaskStatus.NEW, secondEpic.getId());
        // Добавление задач второго эпика
        inMemoryManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondEpicThirdSubTask);
        // Проверка, что в списке 3 задачи
        assertEquals(3, inMemoryManager.getAllTasks().size());
        // Проверка списка задач
        ArrayList<Task> tasks = inMemoryManager.getAllTasks(); // Не понимаю, что делать дальше
        for (Task task : inMemoryManager.getAllTasks()) {
            if (task.getId().equals(firstTask.getId())) {
                assertEquals("Помыть посуду", task.getName());
                assertEquals("Помыть тарелки и вилки", task.getDescription());
                assertEquals(TaskStatus.NEW, task.getStatus());
            } else if (task.getId().equals(secondTask.getId())) {
                assertEquals("Купить хлеб", task.getName());
                assertEquals("Нужен хлеб \"Литовский\"", task.getDescription());
                assertEquals(TaskStatus.DONE, task.getStatus());
            } else if (task.getId().equals(thirdTask.getId())) {
                assertEquals("Купить билеты", task.getName());
                assertEquals("Купить билеты на гандбол", task.getDescription());
                assertEquals(TaskStatus.NEW, task.getStatus());
            }
            // Проверка, что в списке 2 эпика
            assertEquals(2, inMemoryManager.getAllEpics().size());
            // Проверка списка эпиков
            for (Epic epic : inMemoryManager.getAllEpics()) {
                if (epic.getId().equals(firstEpic.getId())) {
                    assertEquals("Переезд", epic.getName());
                    assertEquals("Собрать все вещи", epic.getDescription());
                } else if (epic.getId().equals(secondEpic.getId())) {
                    assertEquals("Изучение Java", epic.getName());
                    assertEquals("Изучить язык программирования Java", epic.getDescription());
                }
            }
            // Проврка списка подзадач определенного эпика с неправльным идентификатором
            assertEquals(0, inMemoryManager.getSubTaskListFromEpicById("1").size());
            // Проврка списка подзадач определенного эпика с правльным идентификатором
            assertEquals(3, inMemoryManager.getSubTaskListFromEpicById(secondEpic.getId()).size());
        }
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