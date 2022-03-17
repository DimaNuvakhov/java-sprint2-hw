package tests;

import imanagers.Manager;
import managers.Managers;
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

    // Проверка метода addTask, добавление задачи
    @Test
    public void shouldAddTask() {
        Manager inMemoryManager = Managers.getDefault();
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        inMemoryManager.addTask(firstTask);
        Task sameTask = getTaskByName(inMemoryManager, "Помыть посуду");
        assertNotNull(sameTask);
        assertEquals("Помыть тарелки и вилки", sameTask.getDescription());
        assertEquals(TaskStatus.NEW, sameTask.getStatus());
        assertEquals("2022-03-15T10:00", sameTask.getStartTime().toString());
        assertEquals(3, sameTask.getDuration());
    }

    // Проверка метода renewTaskById, обновление задачи
    @Test
    public void shouldRenewTaskById() {
        Manager inMemoryManager = Managers.getDefault();
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        inMemoryManager.addTask(firstTask);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 20, 11, 0), 3);
        inMemoryManager.renewTaskById(firstTask.getId(), secondTask);
        Task oldTask = getTaskByName(inMemoryManager, "Помыть посуду");
        assertNull(oldTask);
        Task renewedTask = getTaskByName(inMemoryManager, "Купить хлеб");
        assertNotNull(renewedTask);
        assertEquals("Нужен хлеб \"Литовский\"", renewedTask.getDescription());
        assertEquals(TaskStatus.DONE, renewedTask.getStatus());
        assertEquals(firstTask.getId(), renewedTask.getId());
        assertEquals("2022-03-20T11:00", renewedTask.getStartTime().toString());
        assertEquals(3, renewedTask.getDuration());
    }

    // Некорректный id задачи
    @Test
    public void shouldThrowExceptionWhenIncorrectTaskId() {
        Manager inMemoryManager = Managers.getDefault();
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        inMemoryManager.addTask(firstTask);
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

    // Проверка метода deleteTaskById, удаление задачи по id
    @Test
    public void shouldDeleteTaskById() {
        Manager inMemoryManager = Managers.getDefault();
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        inMemoryManager.addTask(firstTask);
        assertTrue(inMemoryManager.deleteTaskById(firstTask.getId()));
    }

    // Проверка метода addEpic
    @Test
    public void shouldAddEpic() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        inMemoryManager.addEpic(firstEpic);
        Epic sameEpic = (Epic) getTaskByName(inMemoryManager, "Сходить в спортзал");
        assertNotNull(sameEpic);
        assertEquals("Прокачать 3 группы мышц", sameEpic.getDescription());
    }

    // Проверка метода renewTaskById, обновление эпика
    @Test
    public void shouldRenewEpicById() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        inMemoryManager.addEpic(firstEpic);
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        inMemoryManager.renewTaskById(firstEpic.getId(), secondEpic);
        Epic oldEpic = (Epic) getTaskByName(inMemoryManager, "Сходить в спортзал");
        assertNull(oldEpic);
        Epic renewedEpic = (Epic) getTaskByName(inMemoryManager, "Изучение Java");
        assertNotNull(renewedEpic);
        assertEquals("Изучить язык программирования Java", renewedEpic.getDescription());
        assertEquals(firstEpic.getId(), renewedEpic.getId());
    }

    // Некорректный id эпика
    @Test
    public void shouldThrowExceptionWhenIncorrectEpicId() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        inMemoryManager.addEpic(firstEpic);
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

    // Проверка метода deleteTaskById, удаление эпика
    @Test
    public void shouldDeleteEpicById() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        inMemoryManager.addEpic(firstEpic);
        assertTrue(inMemoryManager.deleteTaskById(firstEpic.getId()));
    }

    // Проверка метода addSubTaskIntoEpic, добавление подзадачи
    @Test
    public void shouldAddSubTaskIntoEpic() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        inMemoryManager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        SubTask sameSubTask = (SubTask) getTaskByName(inMemoryManager, "Изучить Дженерики");
        assertNotNull(sameSubTask);
        assertEquals("Изучить случаи применения дженериков", sameSubTask.getDescription());
        assertEquals(TaskStatus.NEW, sameSubTask.getStatus());
        assertEquals(firstEpic.getId(), sameSubTask.getEpicId());
        assertEquals("2022-03-15T10:00", sameSubTask.getStartTime().toString());
        assertEquals(4, sameSubTask.getDuration());
    }

    // // Проверка метода renewTaskById, обновление подзадачи
    @Test
    public void shouldRenewSubTaskById() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        inMemoryManager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        SubTask secondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 16, 10, 0), 3);
        inMemoryManager.renewTaskById(firstSubTask.getId(), secondSubTask);
        SubTask oldTask = (SubTask) getTaskByName(inMemoryManager, "Изучить Дженерики");
        assertNull(oldTask);
        SubTask renewedSubTask = (SubTask) getTaskByName(inMemoryManager, "Изучить полиморфизм");
        assertNotNull(renewedSubTask);
        assertEquals("Изучить перегрузку методов", renewedSubTask.getDescription());
        assertEquals(TaskStatus.NEW, renewedSubTask.getStatus());
        assertEquals(firstSubTask.getId(), renewedSubTask.getId());
        assertEquals("2022-03-16T10:00", renewedSubTask.getStartTime().toString());
        assertEquals(3, renewedSubTask.getDuration());
    }

    // Некорректный id подзадачи
    @Test
    public void shouldThrowExceptionWhenIncorrectSubTaskId() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        inMemoryManager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
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

    // Проверка метода getSubTaskListFromEpicById
    @Test
    public void shouldReturnSubTaskListByEpicIdAndLoad() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        inMemoryManager.addEpic(firstEpic);
        inMemoryManager.addEpic(secondEpic);
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId(),
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        inMemoryManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 22, 10, 0), 4);
        inMemoryManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        assertEquals(2, inMemoryManager.getSubTaskListFromEpicById(secondEpic.getId()).size());
        ArrayList<SubTask> subTasks = inMemoryManager.getSubTaskListFromEpicById(secondEpic.getId());
        assertEquals("2022-04-20T10:00", firstEpic.getStartTime().toString());
        assertEquals("2022-04-21T10:00", secondEpic.getStartTime().toString());
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

    // Проверка метода deleteTaskById, удаление подзадачи
    @Test
    public void shouldDeleteSubTaskById() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        inMemoryManager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        assertTrue(inMemoryManager.deleteTaskById(firstSubTask.getId()));
    }

    // Проверка метода calcStatus, пустой эпик
    @Test
    public void shouldReturnNewEpicStatusWhenEpicIsEmpty() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        inMemoryManager.addEpic(firstEpic);
        assertEquals(TaskStatus.NEW, firstEpic.getStatus());
    }

    // Проверка метода calcStatus, у эпика 2 подзадачи со статусом NEW
    @Test
    public void shouldReturnNewEpicStatus() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        inMemoryManager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        SubTask secondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 16, 10, 0), 4);
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondSubTask);
        assertEquals(TaskStatus.NEW, firstEpic.getStatus());
    }

    // Проверка метода calcStatus, у эпика 2 подзадачи со статусом DONE
    @Test
    public void shouldReturnDoneEpicStatus() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        inMemoryManager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        SubTask secondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.DONE, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 16, 10, 0), 4);
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondSubTask);
        assertEquals(TaskStatus.DONE, firstEpic.getStatus());
    }

    // Проверка метода calcStatus, у эпика 2 подзадачи одна со статсусом NEW, другая с DONE
    @Test
    public void shouldReturnInProgressEpicStatus() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        inMemoryManager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        SubTask secondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 16, 10, 0), 4);
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondSubTask);
        assertEquals(TaskStatus.IN_PROGRESS, firstEpic.getStatus());
    }

    // Проверка метода calcStatus, у эпика 2 подзадачи со статусом IN_PROGRESS
    @Test
    public void shouldReturnInProgressEpicStatusWhenAllSubTasksAreInProgress() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        inMemoryManager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.IN_PROGRESS, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        SubTask secondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.IN_PROGRESS, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 16, 10, 0), 4);
        inMemoryManager.addSubTaskIntoEpic(firstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondSubTask);
        assertEquals(TaskStatus.IN_PROGRESS, firstEpic.getStatus());
    }

    // Проверка метода deleteTaskById некорректный id, проверка удаления задачи, подзадачи и эпика
    @Test
    public void shouldDeleteTaskByIncorrectId() {
        Manager inMemoryManager = Managers.getDefault();
        assertFalse(inMemoryManager.deleteTaskById("1"));
    }

    // Проверка метода getAllTasks, пустой список задач
    @Test
    public void shouldReturnZeroWhenTaskListIsEmpty() {
        Manager inMemoryManager = Managers.getDefault();
        assertEquals(0, inMemoryManager.getAllTasks().size());
    }

    // Проверка метода getAllTasks
    @Test
    public void shouldReturnTaskList() {
        Manager inMemoryManager = Managers.getDefault();
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 16, 10, 0), 3);
        inMemoryManager.addTask(firstTask);
        inMemoryManager.addTask(secondTask);
        assertEquals(2, inMemoryManager.getAllTasks().size());
        ArrayList<Task> tasks = inMemoryManager.getAllTasks();
        for (Task task : tasks) {
            if (task.getId().equals(firstTask.getId())) {
                assertEquals("Помыть посуду", task.getName());
                assertEquals("Помыть тарелки и вилки", task.getDescription());
                assertEquals(TaskStatus.NEW, task.getStatus());
                assertEquals("2022-03-15T10:00", task.getStartTime().toString());
                assertEquals(3, task.getDuration());
            } else if (task.getId().equals(secondTask.getId())) {
                assertEquals("Купить хлеб", task.getName());
                assertEquals("Нужен хлеб \"Литовский\"", task.getDescription());
                assertEquals(TaskStatus.DONE, task.getStatus());
                assertEquals("2022-03-16T10:00", task.getStartTime().toString());
                assertEquals(3, task.getDuration());
            }
        }
    }

    // Проверка метода getAllEpics, пустой список эпиков
    @Test
    public void shouldReturnZeroWhenEpicListIsEmpty() {
        Manager inMemoryManager = Managers.getDefault();
        assertEquals(0, inMemoryManager.getAllEpics().size());
    }

    // Проверка метода getAllEpics, стандартная реализация
    @Test
    public void shouldReturnEpicList() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        inMemoryManager.addEpic(firstEpic);
        inMemoryManager.addEpic(secondEpic);
        assertEquals(2, inMemoryManager.getAllEpics().size());
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

    // Проверка метода getAllSubTasks, пустой список задач
    @Test
    public void shouldReturnZeroWhenSubTaskListIsEmpty() {
        Manager inMemoryManager = Managers.getDefault();
        assertEquals(0, inMemoryManager.getAllSubtasks().size());
    }

    // Проверка метода getAllSubTasks, стандартная реализация
    @Test
    public void shouldReturnSubTaskList() {
        Manager inMemoryManager = Managers.getDefault();
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        inMemoryManager.addEpic(firstEpic);
        inMemoryManager.addEpic(secondEpic);
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        inMemoryManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 22, 10, 0), 4);
        inMemoryManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        assertEquals(3, inMemoryManager.getAllSubtasks().size());
        ArrayList<SubTask> subTasks = inMemoryManager.getAllSubtasks();
        for (SubTask subTask : subTasks) {
            if (subTask.getId().equals(firstEpicFirstSubTask.getId())) {
                assertEquals("Собрать чемодан", subTask.getName());
                assertEquals("Положить в чемодан все необходимое", subTask.getDescription());
                assertEquals(TaskStatus.DONE, subTask.getStatus());
                assertEquals(firstEpic.getId(), subTask.getEpicId());
                assertEquals("2022-03-15T10:00", subTask.getStartTime().toString());
                assertEquals(4, subTask.getDuration());
            } else if (subTask.getId().equals(secondEpicFirstSubTask.getId())) {
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

    // Проверка метода getSubTaskListFromEpicById, пустой эпик
    @Test
    public void shouldReturnSubTaskListByEpicIdWhenEpicIsEmpty() {
        Manager inMemoryManager = Managers.getDefault();
        assertEquals(0, inMemoryManager.getSubTaskListFromEpicById("1").size());
    }

    // Проверка метода history. Проверка истории просмотра задач, пустая история
    @Test
    public void shouldReturnEmptyHistory() {
        Manager inMemoryManager = Managers.getDefault();
        assertEquals(0, inMemoryManager.history().size());
    }

    // Проверка метода history. Проверка истории просмотра задач, некорректный id
    @Test
    public void shouldThrowExceptionWhenIncorrectId() {
        Manager inMemoryManager = Managers.getDefault();
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
        Manager inMemoryManager = Managers.getDefault();
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 16, 10, 0), 3);
        inMemoryManager.addTask(firstTask);
        inMemoryManager.addTask(secondTask);
        inMemoryManager.getTaskById(firstTask.getId());
        inMemoryManager.getTaskById(secondTask.getId());
        List<Task> history = inMemoryManager.history();
        for (int i = history.size(); i >= 0; i--) {
            if (i == 0) {
                assertEquals("Купить хлеб", history.get(i).getName());
                assertEquals("Нужен хлеб \"Литовский\"", history.get(i).getDescription());
                assertEquals(TaskStatus.DONE, history.get(i).getStatus());
                assertEquals("2022-03-16T10:00", history.get(i).getStartTime().toString());
                assertEquals(3, history.get(i).getDuration());
            } else if (i == 1) {
                assertEquals("Помыть посуду", history.get(i).getName());
                assertEquals("Помыть тарелки и вилки", history.get(i).getDescription());
                assertEquals(TaskStatus.NEW, history.get(i).getStatus());
                assertEquals("2022-03-15T10:00", history.get(i).getStartTime().toString());
                assertEquals(3, history.get(i).getDuration());
            }
        }
    }

    // Проверка метода history и getTaskById. Проверка истории просмотра задач, в истории нет задач
    @Test
    public void shouldReturnZeroIfZeroTasksInHistory() {
        Manager inMemoryManager = Managers.getDefault();
        assertEquals(0, inMemoryManager.history().size());
    }

    // Проверка метода history и getTaskById. Проверка истории просмотра задач, дублирование
    @Test
    public void shouldReturnHistoryWhenDuplication() {
        Manager inMemoryManager = Managers.getDefault();
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 16, 10, 0), 3);
        inMemoryManager.addTask(firstTask);
        inMemoryManager.addTask(secondTask);
        inMemoryManager.getTaskById(firstTask.getId());
        inMemoryManager.getTaskById(secondTask.getId());
        inMemoryManager.getTaskById(firstTask.getId());
        List<Task> history = inMemoryManager.history();
        for (int i = history.size(); i >= 0; i--) {
            if (i == 0) {
                assertEquals("Помыть посуду", history.get(i).getName());
                assertEquals("Помыть тарелки и вилки", history.get(i).getDescription());
                assertEquals(TaskStatus.NEW, history.get(i).getStatus());
                assertEquals("2022-03-15T10:00", history.get(i).getStartTime().toString());
                assertEquals(3, history.get(i).getDuration());
            } else if (i == 1) {
                assertEquals("Купить хлеб", history.get(i).getName());
                assertEquals("Нужен хлеб \"Литовский\"", history.get(i).getDescription());
                assertEquals(TaskStatus.DONE, history.get(i).getStatus());
                assertEquals("2022-03-16T10:00", history.get(i).getStartTime().toString());
                assertEquals(3, history.get(i).getDuration());
            }
        }
    }

    // Проверка метода deleteAllTasks
    @Test
    public void shouldDeleteAllTasks() {
        Manager inMemoryManager = Managers.getDefault();
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 16, 10, 0), 3);
        inMemoryManager.addTask(firstTask);
        inMemoryManager.addTask(secondTask);
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        inMemoryManager.addEpic(secondEpic);
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 22, 10, 0), 4);
        inMemoryManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        inMemoryManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        inMemoryManager.deleteAllTasks();
        assertEquals(0, inMemoryManager.getAllItems().size());
    }

    // Проверка того, что все задачи выводятся в порядке приоритета
    @Test
    public void shouldReturnPrioritizedTasks() {
        Manager inMemoryManager = Managers.getDefault();
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 20, 11, 0), 4);
        Task thirdTask = new Task("Помыть машину", "Помыть машину на мойке \"Мой-ка\"",
                TaskStatus.DONE, LocalDateTime.of(2022, 3, 20, 15, 0), 5);
        inMemoryManager.addTask(secondTask);
        inMemoryManager.addTask(thirdTask);
        inMemoryManager.addTask(firstTask);
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

    // Проверка пересечения задач
    @Test
    public void shouldThrowExceptionWhenFindIntersectionBetweenTasks() {
        Manager inMemoryManager = Managers.getDefault();
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

    // Проверка пересечения задачи и подзадачи
    @Test
    public void shouldThrowExceptionWhenFindIntersectionBetweenTaskAndSubtask() {
        Manager inMemoryManager = Managers.getDefault();
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        inMemoryManager.addTask(firstTask);
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        inMemoryManager.addEpic(secondEpic);
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 9, 0), 2);
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

    // Проверка пересечения подзадач
    @Test
    public void shouldThrowExceptionWhenFindIntersectionBetweenSubtasks() {
        Manager inMemoryManager = Managers.getDefault();
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        inMemoryManager.addEpic(secondEpic);
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

    // Проверка пересечения, когда задача обновляется
    @Test
    public void shouldThrowExceptionWhenFindIntersectionWhenRenewTask() {
        Manager inMemoryManager = Managers.getDefault();
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        inMemoryManager.addTask(firstTask);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 15, 11, 0), 3);
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

    // Проверка пересечения, когда подзадача обновляется
    @Test
    public void shouldThrowExceptionWhenFindIntersectionWhenRenewSubTask() {
        Manager inMemoryManager = Managers.getDefault();
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        inMemoryManager.addEpic(secondEpic);
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 11, 0), 4);
        inMemoryManager.addTask(secondEpicFirstSubTask);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 9, 0), 4);
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        inMemoryManager.renewTaskById(secondEpicFirstSubTask.getId(), secondEpicSecondSubTask);
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
