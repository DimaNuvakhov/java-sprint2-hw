package inmemorymanagers;

import managers.Manager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryManagerTest {

    // Проверка добавления задачи
    @Test
    public void shouldAddTask() {
        Manager inMemoryManager = Managers.getDefault();
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
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
    }

    // Проверка обновления задачи
    @Test
    public void shouldRenewTaskById() {
        Manager inMemoryManager = Managers.getDefault();
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
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
    }

    // Проверка удаления задачи
    @Test
    public void shouldDeleteTaskById() {
        // Создание менеджера
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
        // Удаление задачи из трекера
        inMemoryManager.deleteTaskById(sameTask.getId());
        // Проверка, что задача удалена
        assertEquals(0, inMemoryManager.getAllTasks().size());
    }

    @Test
    public void shouldDeleteTaskByIncorrectId() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Удаление задачи из трекера по неверному id
        assertFalse(inMemoryManager.deleteTaskById("1"));
    }

    @Test
    public void shouldAddEpic() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        // Добавление эпика в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Поиск задачи в трекере по имени
        Epic sameEpic = (Epic) getTaskByName(inMemoryManager, "Сходить в спортзал");
        // Проверка, есть ли эпик в трекере
        assertNotNull(sameEpic);
        // Сверка описания эпика
        assertEquals("Прокачать 3 группы мышц", sameEpic.getDescription());
    }

    @Test
    public void shouldRenewEpicById() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        // Добавление эпика в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Поиск задачи в трекере по имени
        Epic sameEpic = (Epic) getTaskByName(inMemoryManager, "Сходить в спортзал");
        // Проверка, есть ли эпик в трекере
        assertNotNull(sameEpic);
        // Сверка описания эпика
        assertEquals("Прокачать 3 группы мышц", sameEpic.getDescription());
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
    }

    @Test
    public void shouldDeleteEpicById() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        // Добавление эпика в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Поиск задачи в трекере по имени
        Epic sameEpic = (Epic) getTaskByName(inMemoryManager, "Сходить в спортзал");
        // Проверка, есть ли эпик в трекере
        assertNotNull(sameEpic);
        // Сверка описания эпика
        assertEquals("Прокачать 3 группы мышц", sameEpic.getDescription());
        // Удаление эпика из трекера
        inMemoryManager.deleteTaskById(sameEpic.getId());
        // Проверка, что эпик удален
        assertEquals(0, inMemoryManager.getAllEpics().size());
    }

    @Test
    public void shouldAddSubTaskIntoEpic() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпика в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Поиск задачи в трекере по имени
        Epic sameEpic = (Epic) getTaskByName(inMemoryManager, "Изучение Java");
        // Проверка, есть ли эпик в трекере
        assertNotNull(sameEpic);
        // Сверка описания эпика
        assertEquals("Изучить язык программирования Java", sameEpic.getDescription());
        // Создание подзадачи к эпику
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, sameEpic.getId());
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
        assertEquals(sameEpic.getId(), sameSubTask.getEpicId());
    }

    @Test
    public void shouldRenewSubTask() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпика в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Поиск задачи в трекере по имени
        Epic sameEpic = (Epic) getTaskByName(inMemoryManager, "Изучение Java");
        // Проверка, есть ли эпик в трекере
        assertNotNull(sameEpic);
        // Сверка описания эпика
        assertEquals("Изучить язык программирования Java", sameEpic.getDescription());
        // Создание подзадачи к эпику
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, sameEpic.getId());
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
        assertEquals(sameEpic.getId(), sameSubTask.getEpicId());
        // Создание новой подзадачи
        SubTask secondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, sameEpic.getId());
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
    }

    @Test
    public void shouldDeleteSubTaskById() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпика в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Поиск задачи в трекере по имени
        Epic sameEpic = (Epic) getTaskByName(inMemoryManager, "Изучение Java");
        // Проверка, есть ли эпик в трекере
        assertNotNull(sameEpic);
        // Сверка описания эпика
        assertEquals("Изучить язык программирования Java", sameEpic.getDescription());
        // Создание подзадачи к эпику
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, sameEpic.getId());
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
        assertEquals(sameEpic.getId(), sameSubTask.getEpicId());
        // Удаление подзадачи из трекера
        inMemoryManager.deleteTaskById(sameSubTask.getId());
        // Проверка, что задача удалена
        assertEquals(0, inMemoryManager.getAllSubtasks().size());
    }

    @Test
    public void shouldReturnNewEpicStatusWhenEpicIsEmpty() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        // Добавление эпика в трекер
        inMemoryManager.addEpic(firstEpic);
        // Проверка статуса при пустом списке подзадач
        assertEquals(TaskStatus.NEW, firstEpic.getStatus());
    }

    @Test
    public void shouldReturnNewEpicStatus() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        // Добавление эпика в трекер
        inMemoryManager.addEpic(firstEpic);
        // Создание субтасок к эпику, все подзадачи со статусом NEW
        SubTask firstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.NEW, firstEpic.getId());
        SubTask secondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.NEW, firstEpic.getId());
        // Добавление субтасок в трекер задач
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondSubTask);
        // Проверка статуса эпика, если у эпика две новые подзадачи
        assertEquals(TaskStatus.NEW, firstEpic.getStatus());
    }

    @Test
    public void shouldReturnDoneEpicStatus() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        // Добавление эпика в трекер
        inMemoryManager.addEpic(firstEpic);
        // Создание субтасок к эпику, все подзадачи со статусом DONE
        SubTask firstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId());
        SubTask secondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.DONE, firstEpic.getId());
        // Добавление субтасок в трекер задач
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondSubTask);
        // Проверка статуса эпика, если у эпика две новые подзадачи
        assertEquals(TaskStatus.DONE, firstEpic.getStatus());
    }

    @Test
    public void shouldReturnInProgressEpicStatus() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        // Добавление эпика в трекер
        inMemoryManager.addEpic(firstEpic);
        // Создание субтасок к эпику, обе подзадачи со статусом DONE и NEW
        SubTask firstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId());
        SubTask secondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.NEW, firstEpic.getId());
        // Добавление субтасок в трекер задач
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondSubTask);
        // Проверка статуса эпика, если у эпика две новые подзадачи
        assertEquals(TaskStatus.IN_PROGRESS, firstEpic.getStatus());
    }

    @Test
    public void shouldReturnInProgressEpicStatusWhenAllSubTasksAreInProgress() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        // Добавление эпика в трекер
        inMemoryManager.addEpic(firstEpic);
        // Создание субтасок к эпику, обе подзадачи со статусом DONE и NEW
        SubTask firstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.IN_PROGRESS, firstEpic.getId());
        SubTask secondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.IN_PROGRESS, firstEpic.getId());
        // Добавление субтасок в трекер задач
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondSubTask);
        // Проверка статуса эпика, если у эпика две новые подзадачи
        assertEquals(TaskStatus.IN_PROGRESS, firstEpic.getStatus());
    }

    @Test
    public void shouldReturnZeroWhenTaskListIsEmpty() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Проверка, что список задач пуст
        assertEquals(0, inMemoryManager.getAllTasks().size());
    }

    @Test
    public void shouldReturnTaskList() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE);
        // Добавлние задач в трекер
        inMemoryManager.addTask(firstTask);
        inMemoryManager.addTask(secondTask);
        // Проверка, что в списке 2 задачи
        assertEquals(2, inMemoryManager.getAllTasks().size());
        // Проверка списка задач
        ArrayList<Task> tasks = inMemoryManager.getAllTasks();
        for (Task task : tasks) {
            if (task.getId().equals(firstTask.getId())) {
                assertEquals("Помыть посуду", task.getName());
                assertEquals("Помыть тарелки и вилки", task.getDescription());
                assertEquals(TaskStatus.NEW, task.getStatus());
            } else if (task.getId().equals(secondTask.getId())) {
                assertEquals("Купить хлеб", task.getName());
                assertEquals("Нужен хлеб \"Литовский\"", task.getDescription());
                assertEquals(TaskStatus.DONE, task.getStatus());
            }
        }
    }

    @Test
    public void shouldReturnZeroWhenEpicListIsEmpty() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Проверка, что список задач пуст
        assertEquals(0, inMemoryManager.getAllEpics().size());
    }

    @Test
    public void shouldReturnEpicList() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпиков
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпиков в трекер
        inMemoryManager.addEpic(firstEpic);
        inMemoryManager.addEpic(secondEpic);
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
    }

    @Test
    public void shouldReturnZeroWhenSubTaskListIsEmpty() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Проверка, что список задач пуст
        assertEquals(0, inMemoryManager.getAllSubtasks().size());
    }

    @Test
    public void shouldReturnSubTaskList() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
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
        // Добавление подзадач второго эпика
        inMemoryManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        // Проверка, что всего 3 подзадачи
        assertEquals(3, inMemoryManager.getAllSubtasks().size());
        // Проверка списка подзадач
        for (SubTask subTask : inMemoryManager.getAllSubtasks()) {
            if (subTask.getId().equals(firstEpicFirstSubTask.getId())) {
                assertEquals("Собрать чемодан", subTask.getName());
                assertEquals("Положить в чемодан все необходимое", subTask.getDescription());
                assertEquals(TaskStatus.DONE, subTask.getStatus());
                assertEquals(firstEpic.getId(), subTask.getEpicId());
            } else if (subTask.getId().equals(secondEpicFirstSubTask.getId())) {
                assertEquals("Изучить Дженерики", subTask.getName());
                assertEquals("Изучить случаи применения дженериков", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(secondEpic.getId(), subTask.getEpicId());
            } else if (subTask.getId().equals(secondEpicSecondSubTask.getId())) {
                assertEquals("Изучить полиморфизм", subTask.getName());
                assertEquals("Изучить перегрузку методов", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(secondEpic.getId(), subTask.getEpicId());
            }
        }
    }

    @Test
    public void shouldReturnSubTaskListByEpicId() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
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
        // Добавление подзадач второго эпика
        inMemoryManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        // Проверка, что у второго эпика 2 подзадачи
        assertEquals(2, inMemoryManager.getSubTaskListFromEpicById(secondEpic.getId()).size());
        // Проверка списка подзадач определенного эпика
        for (SubTask subTask : inMemoryManager.getSubTaskListFromEpicById(secondEpic.getId())) {
            if (subTask.getId().equals(secondEpicFirstSubTask.getId())) {
                assertEquals("Изучить Дженерики", subTask.getName());
                assertEquals("Изучить случаи применения дженериков", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(secondEpic.getId(), subTask.getEpicId());
            } else if (subTask.getId().equals(secondEpicSecondSubTask.getId())) {
                assertEquals("Изучить полиморфизм", subTask.getName());
                assertEquals("Изучить перегрузку методов", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(secondEpic.getId(), subTask.getEpicId());
            }
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