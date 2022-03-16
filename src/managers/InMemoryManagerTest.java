package managers;

import imanagers.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryManagerTest {

    @Test
    public void shouldAddTaskWithStartTimeAndDuration() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
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
        // Сверка даты задачи
        assertEquals("2022-03-15T10:00", sameTask.getStartTime().toString());
        // Сверка продолжительности
        assertEquals(3, sameTask.getDuration());
    }

    @Test
    public void shouldRenewTaskWithStartTimeAndDurationById() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        // Добавление задачи в трекер задач
        inMemoryManager.addTask(firstTask);
        // Создание новой задачи
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 20, 11, 0), 3);
        // Обновление задачи
        inMemoryManager.renewTaskById(firstTask.getId(), secondTask);
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
        assertEquals(firstTask.getId(), renewedTask.getId());
        // Сверка даты задачи
        assertEquals("2022-03-20T11:00", renewedTask.getStartTime().toString());
        // Сверка продолжительности
        assertEquals(3, renewedTask.getDuration());
    }

    // Проверка метода renewTaskById некорректный id
    @Test
    public void shouldThrowExceptionWhenIncorrectTaskId() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        // Добавление задачи в трекер задач
        inMemoryManager.addTask(firstTask);
        // Проверка
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        inMemoryManager.renewTaskById("1", firstTask);
                    }
                });
        assertEquals("Задача с указанным id в трекер задач не добавлена", ex.getMessage());
    }

    // Проверка метода deleteTaskById стандартная реалиация, удаление задачи
    @Test
    public void shouldDeleteTaskById() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        // Добавление задачи в трекер задач
        inMemoryManager.addTask(firstTask);
        // Удаление задачи из трекера проверка, что задача удалена
        assertTrue(inMemoryManager.deleteTaskById(firstTask.getId()));
    }

    @Test
    public void shouldRenewSubTaskWithStartTimeAndDurationById() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпика в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Создание подзадач к эпику
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        // Добавление подзадачи в трекер задач
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        // Создание новой подзадачи
        SubTask secondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 16, 10, 0), 3);
        // Обновление подзадачи
        inMemoryManager.renewTaskById(firstSubTask.getId(), secondSubTask);
        // Поиск подзадачи в трекере по имени
        SubTask oldTask = (SubTask) getTaskByName(inMemoryManager, "Изучить Дженерики");
        // Проверка, что подзадачи нет в трекере
        assertNull(oldTask);
        // Поиск обновленной подзадачи в трекере по имени
        SubTask renewedSubTask = (SubTask) getTaskByName(inMemoryManager, "Изучить полиморфизм");
        // Проверка, есть ли обновленная подзадача в трекере
        assertNotNull(renewedSubTask);
        // Сверка описания подзадачи
        assertEquals("Изучить перегрузку методов", renewedSubTask.getDescription());
        // Сверка статуса подзадачи
        assertEquals(TaskStatus.NEW, renewedSubTask.getStatus());
        // Сверка id первой подзадачи с id обновленной подзадачи
        assertEquals(firstSubTask.getId(), renewedSubTask.getId());
        // Сверка даты задачи
        assertEquals("2022-03-16T10:00", renewedSubTask.getStartTime().toString());
        // Сверка продолжительности
        assertEquals(3, renewedSubTask.getDuration());
    }

    @Test
    public void shouldReturnSubTaskWithStartTimeAndDurationListByEpicIdAndLoad() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпиков
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпиков в трекер
        inMemoryManager.addEpic(firstEpic);
        inMemoryManager.addEpic(secondEpic);
        // Создание подзадач к первому эпику
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId(),
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        // Добавление подзадачи в трекер
        inMemoryManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        // Создание подзадач ко второму эпику
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 22, 10, 0), 4);
        // Добавление подзадач второго эпика
        inMemoryManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        // Проверка, что у второго эпика 2 подзадачи
        assertEquals(2, inMemoryManager.getSubTaskListFromEpicById(secondEpic.getId()).size());
        // Проверка списка подзадач определенного эпика
        ArrayList<SubTask> subTasks = inMemoryManager.getSubTaskListFromEpicById(secondEpic.getId());
        // Проверяем дату начала эпиков
        assertEquals("2022-04-20T10:00", firstEpic.getStartTime().toString());
        assertEquals("2022-04-21T10:00", secondEpic.getStartTime().toString());
        // Проверяем, что сумма продолжительности подзадач равна продолжительности эпика
        assertEquals(8, secondEpic.getDuration());
        for (SubTask subTask : subTasks) {
            if (subTask.getId().equals(secondEpicFirstSubTask.getId())) {
                assertEquals("Изучить Дженерики", subTask.getName());
                assertEquals("Изучить случаи применения дженериков", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(secondEpic.getId(), subTask.getEpicId());
                assertEquals("2022-04-21T10:00", subTask.getStartTime().toString());
                assertEquals(4, subTask.getDuration());
            } else {
                assertEquals("Изучить полиморфизм", subTask.getName());
                assertEquals("Изучить перегрузку методов", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(secondEpic.getId(), subTask.getEpicId());
                assertEquals("2022-04-22T10:00", subTask.getStartTime().toString());
                assertEquals(4, subTask.getDuration());
            }
        }
    }

    // Проверка метода addEpic
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

    // Проверка метода renewTaskById стандартная реализация
    @Test
    public void shouldRenewEpicById() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        // Добавление эпика в трекер задач
        inMemoryManager.addEpic(firstEpic);
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
        assertEquals(firstEpic.getId(), renewedEpic.getId());
    }

    // Проверка метода renewTaskById некорректный id эпика
    @Test
    public void shouldThrowExceptionWhenIncorrectEpicId() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        // Добавление задачи в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Проверка
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        inMemoryManager.renewTaskById("1", firstEpic);
                    }
                });
        assertEquals("Задача с указанным id в трекер задач не добавлена", ex.getMessage());
    }

    // Проверка метода deleteTaskById стандартная реалиация, удаление эпика
    @Test
    public void shouldDeleteEpicById() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        // Добавление эпика в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Удаление задачи из трекера проверка, что задача удалена
        assertTrue(inMemoryManager.deleteTaskById(firstEpic.getId()));
    }

    // Проверка метода deleteTaskById некорректный id, проверка удаления задачи, подзадачи и эпика
    @Test
    public void shouldDeleteTaskByIncorrectId() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Удаление задачи из трекера по неверному id
        assertFalse(inMemoryManager.deleteTaskById("1"));
    }


    // ПОСЛЕ ТОГО КАК ДОПИШУ УДАЛИТЬ ВСЕ ЧТО НИЖЕ ЭТОЙ СТРОКИ
    // Проверка метода addTask
    @Test
    public void shouldAddTask() {
        // Создание менеджера
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

    // Проверка метода renewTaskById стандартная реализация
    @Test
    public void shouldRenewTaskById() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        // Добавление задачи в трекер задач
        inMemoryManager.addTask(firstTask);
        // Создание новой задачи
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE);
        // Обновление задачи
        inMemoryManager.renewTaskById(firstTask.getId(), secondTask);
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
        assertEquals(firstTask.getId(), renewedTask.getId());
    }










    // Проверка метода addSubTaskIntoEpic
    @Test
    public void shouldAddSubTaskIntoEpic() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпика в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Создание подзадачи к эпику
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId());
        // Добавление подзадачи в трекер задач
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        // Поиск подзадачи в трекере по имени
        SubTask sameSubTask = (SubTask) getTaskByName(inMemoryManager, "Изучить Дженерики");
        // Проверка, есть ли подзадача в трекере
        assertNotNull(sameSubTask);
        // Сверка описания задачи
        assertEquals("Изучить случаи применения дженериков", sameSubTask.getDescription());
        // Сверка статуса задачи
        assertEquals(TaskStatus.NEW, sameSubTask.getStatus());
        // Проверка наличия эпика у подзадачи
        assertEquals(firstEpic.getId(), sameSubTask.getEpicId());
    }

    // Проверка метода renewTaskById стандартная реализация
    @Test
    public void shouldRenewSubTaskById() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпика в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Создание подзадачи к эпику
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId());
        // Добавление подзадачи в трекер задач
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        // Создание новой подзадачи
        SubTask secondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, firstEpic.getId());
        // Обновление подзадачи
        inMemoryManager.renewTaskById(firstSubTask.getId(), secondSubTask);
        // Поиск подзадачи в трекере по имени
        SubTask oldTask = (SubTask) getTaskByName(inMemoryManager, "Изучить Дженерики");
        // Проверка, что подзадачи нет в трекере
        assertNull(oldTask);
        // Поиск обновленной подзадачи в трекере по имени
        SubTask renewedSubTask = (SubTask) getTaskByName(inMemoryManager, "Изучить полиморфизм");
        // Проверка, есть ли обновленная подзадача в трекере
        assertNotNull(renewedSubTask);
        // Сверка описания подзадачи
        assertEquals("Изучить перегрузку методов", renewedSubTask.getDescription());
        // Сверка статуса подзадачи
        assertEquals(TaskStatus.NEW, renewedSubTask.getStatus());
        // Сверка id первой подзадачи с id обновленной подзадачи
        assertEquals(firstSubTask.getId(), renewedSubTask.getId());
    }

    // Проверка метода renewTaskById некорректный id подзадачи
    @Test
    public void shouldThrowExceptionWhenIncorrectSubTaskId() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        // Добавление задачи в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Создание подзадачи к эпику
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId());
        // Добавление подзадачи в трекер задач
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        // Проверка
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        inMemoryManager.renewTaskById("1", firstSubTask);
                    }
                });
        assertEquals("Задача с указанным id в трекер задач не добавлена", ex.getMessage());
    }

    // Проверка метода deleteTaskById стандартная реалиация, удаление подзадачи
    @Test
    public void shouldDeleteSubTaskById() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпика в трекер задач
        inMemoryManager.addEpic(firstEpic);
        // Создание подзадачи к эпику
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId());
        // Добавление подзадачи в трекер задач
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        // Удаление задачи из трекера проверка, что задача удалена
        assertTrue(inMemoryManager.deleteTaskById(firstSubTask.getId()));
    }

    // Провека метода calcStatus, эпик пустой
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

    // Провека метода calcStatus, у эпика 2 подзадачи со статусом NEW
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

    // Провека метода calcStatus, у эпика 2 подзадачи со статусом DONE
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

    // Провека метода calcStatus, у эпика 2 подзадачи одна со статсусом NEW, другая с DONE
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

    // Провека метода calcStatus, у эпика 2 подзадачи со статусом IN_PROGRESS
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

    // Провека метода getAllTasks, пустой список задач
    @Test
    public void shouldReturnZeroWhenTaskListIsEmpty() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Проверка, что список задач пуст
        assertEquals(0, inMemoryManager.getAllTasks().size());
    }

    // Провека метода getAllTasks, стандартная реализация
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

    // Провека метода getAllEpics, пустой список задач
    @Test
    public void shouldReturnZeroWhenEpicListIsEmpty() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Проверка, что список задач пуст
        assertEquals(0, inMemoryManager.getAllEpics().size());
    }

    // Провека метода getAllEpics, стандартная реализация
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
        ArrayList<Epic> epics = inMemoryManager.getAllEpics();
        for (Epic epic : epics) {
            if (epic.getId().equals(firstEpic.getId())) {
                assertEquals("Переезд", epic.getName());
                assertEquals("Собрать все вещи", epic.getDescription());
            } else if (epic.getId().equals(secondEpic.getId())) {
                assertEquals("Изучение Java", epic.getName());
                assertEquals("Изучить язык программирования Java", epic.getDescription());
            }
        }
    }

    // Провека метода getAllSubTasks, пустой список задач
    @Test
    public void shouldReturnZeroWhenSubTaskListIsEmpty() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Проверка, что список задач пуст
        assertEquals(0, inMemoryManager.getAllSubtasks().size());
    }

    // Провека метода getAllSubTasks, стандартная реализация
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
        ArrayList<SubTask> subTasks = inMemoryManager.getAllSubtasks();
        for (SubTask subTask : subTasks) {
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
            } else {
                assertEquals("Изучить полиморфизм", subTask.getName());
                assertEquals("Изучить перегрузку методов", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(secondEpic.getId(), subTask.getEpicId());
            }
        }
    }

    // Провека метода getSubTaskListFromEpicById, стандартная реализация
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
        ArrayList<SubTask> subTasks = inMemoryManager.getSubTaskListFromEpicById(secondEpic.getId());
        for (SubTask subTask : subTasks) {
            if (subTask.getId().equals(secondEpicFirstSubTask.getId())) {
                assertEquals("Изучить Дженерики", subTask.getName());
                assertEquals("Изучить случаи применения дженериков", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(secondEpic.getId(), subTask.getEpicId());
            } else {
                assertEquals("Изучить полиморфизм", subTask.getName());
                assertEquals("Изучить перегрузку методов", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(secondEpic.getId(), subTask.getEpicId());
            }
        }
    }

    // Провека метода getSubTaskListFromEpicById, пустой эпик
    @Test
    public void shouldReturnSubTaskListByEpicIdWhenEpicIsEmpty() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Провека, списка подзадач у эпика, неверный id
        assertEquals(0, inMemoryManager.getSubTaskListFromEpicById("1").size());
    }

    // Проверка метода history. Проверка истории просмотра задач, пустая история
    @Test
    public void shouldReturnEmptyHistory() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Вызов истории
        assertEquals(0, inMemoryManager.history().size());
    }

    // Проверка метода history. Проверка истории просмотра задач, некорректный id
    @Test
    public void shouldThrowExceptionWhenIncorrectId() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Добавлние несуществующей задачи в историю
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        inMemoryManager.getTaskById("1");
                    }
                });
        assertEquals("Задача с указанным id в трекер задач не добавлена", ex.getMessage());
    }

    // Проверка метода history и getTaskById. Проверка истории просмотра задач, 2 задачи в истории
    @Test
    public void shouldReturnHistory() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE);
        // Добавлние задач в трекер
        inMemoryManager.addTask(firstTask);
        inMemoryManager.addTask(secondTask);
        // Добавляем задачи в историю просмотров
        inMemoryManager.getTaskById(firstTask.getId());
        inMemoryManager.getTaskById(secondTask.getId());
        // Проверка истории
        List<Task> history = inMemoryManager.history();
        for (int i = history.size(); i >= 0; i--) {
            if (i == 0) {
                assertEquals("Купить хлеб", history.get(i).getName());
                assertEquals("Нужен хлеб \"Литовский\"", history.get(i).getDescription());
                assertEquals(TaskStatus.DONE, history.get(i).getStatus());
            } else if (i == 1) {
                assertEquals("Помыть посуду", history.get(i).getName());
                assertEquals("Помыть тарелки и вилки", history.get(i).getDescription());
                assertEquals(TaskStatus.NEW, history.get(i).getStatus());
            }
        }
    }

    // Проверка метода history и getTaskById. Проверка истории просмотра задач, в истории нет задач
    @Test
    public void shouldReturnZeroIfZeroTasksInHistory() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Проверка
        assertEquals(0, inMemoryManager.history().size());
    }

    // Проверка метода history и getTaskById. Проверка истории просмотра задач, дублирование
    @Test
    public void shouldReturnHistoryWhenDuplication() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE);
        // Добавлние задач в трекер
        inMemoryManager.addTask(firstTask);
        inMemoryManager.addTask(secondTask);
        // Добавляем задачи в историю просмотров
        inMemoryManager.getTaskById(firstTask.getId());
        inMemoryManager.getTaskById(secondTask.getId());
        // Добавляем первую задачу снова в историю
        inMemoryManager.getTaskById(firstTask.getId());
        // Проверка истории
        List<Task> history = inMemoryManager.history();
        for (int i = history.size(); i >= 0; i--) {
            if (i == 0) {
                assertEquals("Помыть посуду", history.get(i).getName());
                assertEquals("Помыть тарелки и вилки", history.get(i).getDescription());
                assertEquals(TaskStatus.NEW, history.get(i).getStatus());
            } else if (i == 1) {
                assertEquals("Купить хлеб", history.get(i).getName());
                assertEquals("Нужен хлеб \"Литовский\"", history.get(i).getDescription());
                assertEquals(TaskStatus.DONE, history.get(i).getStatus());
            }
        }
    }

    // Проверка метода deleteAllTasks
    @Test
    public void shouldDeleteAllTasks() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE);
        // Добавлние задач в трекер
        inMemoryManager.addTask(firstTask);
        inMemoryManager.addTask(secondTask);
        //Создаем эпик
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпиков в трекер
        inMemoryManager.addEpic(secondEpic);
        // Создание подзадач ко второму эпику
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId());
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId());
        // Добавление подзадач второго эпика
        inMemoryManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        // Удаляем все задачи из трекера
        inMemoryManager.deleteAllTasks();
        assertEquals(0, inMemoryManager.getAllItems().size());
    }

    @Test
    public void shouldReturnPrioritizedTasks() {
        // Созадние менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 20, 11, 0), 4);
        Task thirdTask = new Task("Помыть машину", "Помыть машину на мойке \"Мой-ка\"",
                TaskStatus.DONE, LocalDateTime.of(2022, 3, 20, 15, 0), 5);
        // Добавлние задач в трекер
        inMemoryManager.addTask(secondTask);
        inMemoryManager.addTask(thirdTask);
        inMemoryManager.addTask(firstTask);
        // Вызов отсортированного списка
        TreeSet<Task> sortedTasks = inMemoryManager.getPrioritizedTasks();
        int i = 0;
        for (Task task : sortedTasks) {
            if (i == 0) {
                assertEquals("Помыть посуду", task.getName());
                assertEquals("Помыть тарелки и вилки", task.getDescription());
                assertEquals(TaskStatus.NEW, task.getStatus());
                assertEquals("2022-03-15T10:00", task.getStartTime().toString());
                assertEquals(3, task.getDuration());
            } else if (i == 1) {
                assertEquals("Купить хлеб", task.getName());
                assertEquals("Нужен хлеб \"Литовский\"", task.getDescription());
                assertEquals(TaskStatus.DONE, task.getStatus());
                assertEquals("2022-03-20T11:00", task.getStartTime().toString());
                assertEquals(4, task.getDuration());
            } else if (i == 2) {
                assertEquals("Помыть машину", task.getName());
                assertEquals("Помыть машину на мойке \"Мой-ка\"", task.getDescription());
                assertEquals(TaskStatus.DONE, task.getStatus());
                assertEquals("2022-03-20T15:00", task.getStartTime().toString());
                assertEquals(5, task.getDuration());
            }
            i++;
        }
    }

    @Test
    public void shouldThrowExceptionWhenFindIntersectionBetweenTasks() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 20, 10, 0), 3);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 20, 11, 0), 4);
        inMemoryManager.addTask(firstTask);
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        inMemoryManager.addTask(secondTask);
                    }
                });
        assertEquals("Ошибка, задача не может быть добавлена в трекер" +
                " т.к. она пересекается во времени с ранее добавленой", ex.getMessage());
    }

    // Проверка, когда пересекается задача и подзадача
    @Test
    public void shouldThrowExceptionWhenFindIntersectionBetweenTaskAndSubtask() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        // Добавление задачи
        inMemoryManager.addTask(firstTask);
        // Создание эпика
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпика в трекер
        inMemoryManager.addEpic(secondEpic);
        // Создание подзадачи к эпику
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 9, 0), 2);
        // Добавление подзадачи к эпику
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        inMemoryManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
                    }
                });
        assertEquals("Ошибка, задача не может быть добавлена в трекер" +
                " т.к. она пересекается во времени с ранее добавленой", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenFindIntersectionBetweenSubtasks() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпика в трекер
        inMemoryManager.addEpic(secondEpic);
        // Создание подзадачи к эпику
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 11, 0), 4);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 9, 0), 4);
        inMemoryManager.addTask(secondEpicFirstSubTask);
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        inMemoryManager.addTask(secondEpicSecondSubTask);
                    }
                });
        assertEquals("Ошибка, задача не может быть добавлена в трекер" +
                " т.к. она пересекается во времени с ранее добавленой", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenFindIntersectionBetweenSubtasks11111() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание эпика
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпика в трекер
        inMemoryManager.addEpic(secondEpic);
        // Создание подзадачи к эпику
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 11, 0), 4);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId());
        inMemoryManager.addTask(secondEpicFirstSubTask);
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        inMemoryManager.addTask(secondEpicSecondSubTask);
                    }
                });
        assertEquals("Ошибка, задача не может быть добавлена в трекер" +
                " т.к. она пересекается во времени с ранее добавленой", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenFindIntersectionWhenRenewTask() {
        // Создание менеджера
        Manager inMemoryManager = Managers.getDefault();
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        // Добавление задачи в трекер задач
        inMemoryManager.addTask(firstTask);
        // Создание новой задачи
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 15, 11, 0), 3);
        // Обновление задачи
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        inMemoryManager.renewTaskById(firstTask.getId(), secondTask);
                    }
                });
        assertEquals("Ошибка, задача не может быть добавлена в трекер" +
                " т.к. она пересекается во времени с ранее добавленой", ex.getMessage());
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
