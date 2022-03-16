package managers;

import imanagers.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedManagerTest {

    // Проверка метода addTask, добавление задачи, восстановление менеджера из файла
    @Test
    public void shouldAddTaskAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла и проверка удаления
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        // Добавление задачи в трекер задач
        fileBackedManager.addTask(firstTask);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Поиск задачи в трекере по имени
        Task sameTask = getTaskByName(newFileBackedManager, "Помыть посуду");
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

    // Проверка метода renewTaskById, обновление задачи, восстановление менеджера из файла
    @Test
    public void shouldRenewTaskByIdAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла и проверка удаления
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        // Добавление задачи в трекер задач
        fileBackedManager.addTask(firstTask);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание новой задачи
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 20, 11, 0), 3);
        // Обновление задачи
        newFileBackedManager.renewTaskById(firstTask.getId(), secondTask);
        // Поиск задачи в трекере по имени
        Task oldTask = getTaskByName(newFileBackedManager, "Помыть посуду");
        // Проверка, что задачи нет в трекере
        assertNull(oldTask);
        // Поиск обновленной задачи в трекере по имени
        Task renewedTask = getTaskByName(newFileBackedManager, "Купить хлеб");
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

    // Проверка метода deleteTaskById, удаление задачи, восстановление менеджера из файла
    @Test
    public void shouldDeleteTaskByIdAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        // Добавление задачи в трекер задач
        fileBackedManager.addTask(firstTask);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Удаление задачи из трекера проверка, что задача удалена
        assertTrue(newFileBackedManager.deleteTaskById(firstTask.getId()));
    }

    // Проверка метода addEpicБ добавлние эпика, восстановление менеджера из файла
    @Test
    public void shouldAddEpicAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпика
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        // Добавление эпика в трекер задач
        fileBackedManager.addEpic(firstEpic);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Провека, что задача одна
        assertEquals(1, newFileBackedManager.getAllItems().size());
        // Поиск задачи в трекере по имени
        Epic sameEpic = (Epic) getTaskByName(newFileBackedManager, "Сходить в спортзал");
        // Проверка, есть ли эпик в трекере
        assertNotNull(sameEpic);
        // Сверка описания эпика
        assertEquals("Прокачать 3 группы мышц", sameEpic.getDescription());
    }

    // Проверка метода renewTaskById, обновление эпика, восстановление менеджера из файла
    @Test
    public void shouldRenewEpicByIdAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпика
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        // Добавление эпика в трекер задач
        fileBackedManager.addEpic(firstEpic);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание нового эпика
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Обновление эпика
        newFileBackedManager.renewTaskById(firstEpic.getId(), secondEpic);
        // Поиск задачи в трекере по имени
        Epic oldEpic = (Epic) getTaskByName(newFileBackedManager, "Сходить в спортзал");
        // Проверка, что эпика нет в трекере
        assertNull(oldEpic);
        // Поиск обновленной задачи в трекере по имени
        Epic renewedEpic = (Epic) getTaskByName(newFileBackedManager, "Изучение Java");
        // Проверка, есть ли обновленный эпик в трекере
        assertNotNull(renewedEpic);
        // Сверка описания эпика
        assertEquals("Изучить язык программирования Java", renewedEpic.getDescription());
        // Сверка id первого эпика с id обновленного эпика
        assertEquals(firstEpic.getId(), renewedEpic.getId());
    }

    // Проверка метода deleteTaskById, удаление эпика, восстановление менеджера из файла
    @Test
    public void shouldDeleteEpicByIdAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпика
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        // Добавление эпика в трекер задач
        fileBackedManager.addEpic(firstEpic);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Удаление задачи из трекера проверка, что задача удалена
        assertTrue(newFileBackedManager.deleteTaskById(firstEpic.getId()));
    }

    // Проверка метода addSubTaskIntoEpicБ добавлние подзадачи, восстановление менеджера из файла
    @Test
    public void shouldAddSubTaskIntoEpicAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпика
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпика в трекер задач
        fileBackedManager.addEpic(firstEpic);
        // Создание подзадачи к эпику
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        // Добавление подзадачи в трекер задач
        fileBackedManager.addSubTaskIntoEpic(firstSubTask);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Поиск подзадачи в трекере по имени
        SubTask sameSubTask = (SubTask) getTaskByName(newFileBackedManager, "Изучить Дженерики");
        // Проверка, есть ли подзадача в трекере
        assertNotNull(sameSubTask);
        // Сверка описания задачи
        assertEquals("Изучить случаи применения дженериков", sameSubTask.getDescription());
        // Сверка статуса задачи
        assertEquals(TaskStatus.NEW, sameSubTask.getStatus());
        // Проверка наличия эпика у подзадачи
        assertEquals(firstEpic.getId(), sameSubTask.getEpicId());
        // Сверка даты задачи
        assertEquals("2022-03-15T10:00", sameSubTask.getStartTime().toString());
        // Сверка продолжительности
        assertEquals(4, sameSubTask.getDuration());
    }

    // Проверка метода renewTaskById, обновлние подзадачи, восстановление менеджера из файла
    @Test
    public void shouldRenewSubTaskByIdAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпика
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпика в трекер задач
        fileBackedManager.addEpic(firstEpic);
        // Создание подзадач к эпику
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        // Добавление подзадачи в трекер задач
        fileBackedManager.addSubTaskIntoEpic(firstSubTask);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание новой подзадачи
        SubTask secondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 16, 10, 0), 3);
        // Обновление подзадачи
        newFileBackedManager.renewTaskById(firstSubTask.getId(), secondSubTask);
        // Поиск подзадачи в трекере по имени
        SubTask oldTask = (SubTask) getTaskByName(newFileBackedManager, "Изучить Дженерики");
        // Проверка, что подзадачи нет в трекере
        assertNull(oldTask);
        // Поиск обновленной подзадачи в трекере по имени
        SubTask renewedSubTask = (SubTask) getTaskByName(newFileBackedManager, "Изучить полиморфизм");
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

    // Проверка метода deleteTaskById, удаление подзадачи, восстановление менеджера из файла
    @Test
    public void shouldDeleteSubTaskByIdAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпика
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпика в трекер задач
        fileBackedManager.addEpic(firstEpic);
        // Создание подзадачи к эпику
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        // Добавление подзадачи в трекер задач
        fileBackedManager.addSubTaskIntoEpic(firstSubTask);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Удаление подзадачи из трекера и проверка, что задача удалена
        assertTrue(newFileBackedManager.deleteTaskById(firstSubTask.getId()));
    }

    // Провека метода getAllTasks, восстановление менеджера из файла
    @Test
    public void shouldReturnTaskListAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 16, 10, 0), 3);
        // Добавлние задач в трекер
        fileBackedManager.addTask(firstTask);
        fileBackedManager.addTask(secondTask);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Проверка, что в списке 2 задачи
        assertEquals(2, newFileBackedManager.getAllTasks().size());
        // Проверка списка задач
        ArrayList<Task> tasks = newFileBackedManager.getAllTasks();
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

    // Провека метода getAllEpics, восстановление менеджера из файла
    @Test
    public void shouldReturnEpicListAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпиков
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпиков в трекер
        fileBackedManager.addEpic(firstEpic);
        fileBackedManager.addEpic(secondEpic);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Проверка, что в списке 2 эпика
        assertEquals(2, newFileBackedManager.getAllEpics().size());
        // Проверка списка эпиков
        ArrayList<Epic> epics = newFileBackedManager.getAllEpics();
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

    // Провека метода getSubTaskListFromEpicById, восстановление менеджера из файла
    @Test
    public void shouldReturnSubTaskListByEpicIdAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпиков
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпиков в трекер
        fileBackedManager.addEpic(firstEpic);
        fileBackedManager.addEpic(secondEpic);
        // Создание подзадач к первому эпику
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId(),
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        // Добавление подзадачи в трекер
        fileBackedManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        // Создание подзадач ко второму эпику
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 22, 10, 0), 4);
        // Добавление подзадач второго эпика
        fileBackedManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        fileBackedManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Проверка, что у второго эпика 2 подзадачи
        assertEquals(2, newFileBackedManager.getSubTaskListFromEpicById(secondEpic.getId()).size());
        // Проверка списка подзадач определенного эпика
        ArrayList<SubTask> subTasks = newFileBackedManager.getSubTaskListFromEpicById(secondEpic.getId());
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

    // Провека метода getAllSubTasks, восстановление менеджера из файла
    @Test
    public void shouldReturnSubTaskListAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпиков
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпиков в трекер
        fileBackedManager.addEpic(firstEpic);
        fileBackedManager.addEpic(secondEpic);
        // Создание подзадач к первому эпику
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId(),
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        // Добавление подзадачи в трекер
        fileBackedManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        // Создание подзадач ко второму эпику
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 22, 10, 0), 4);
        // Добавление подзадач второго эпика
        fileBackedManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        fileBackedManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Проверка, что всего 3 подзадачи
        assertEquals(3, newFileBackedManager.getAllSubtasks().size());
        // Проверка списка подзадач
        ArrayList<SubTask> subTasks = newFileBackedManager.getAllSubtasks();
        for (SubTask subTask : subTasks) {
            if (subTask.getId().equals(firstEpicFirstSubTask.getId())) {
                assertEquals("Собрать чемодан", subTask.getName());
                assertEquals("Положить в чемодан все необходимое", subTask.getDescription());
                assertEquals(TaskStatus.DONE, subTask.getStatus());
                assertEquals(firstEpic.getId(), subTask.getEpicId());
                assertEquals("2022-04-20T10:00", subTask.getStartTime().toString());
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

    // Проверка методов history и getTaskById. Проверка истории, 2 задачи в истории, восстановление менеджера из файла
    @Test
    public void shouldReturnHistoryAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        // Добавлние задач в трекер
        fileBackedManager.addTask(firstTask);
        fileBackedManager.addTask(secondTask);
        // Добавляем задачи в историю просмотров
        fileBackedManager.getTaskById(firstTask.getId());
        fileBackedManager.getTaskById(secondTask.getId());
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Проверка истории
        List<Task> history = newFileBackedManager.history();
        for (int i = history.size(); i >= 0; i--) {
            if (i == 0) {
                assertEquals("Купить хлеб", history.get(i).getName());
                assertEquals("Нужен хлеб \"Литовский\"", history.get(i).getDescription());
                assertEquals(TaskStatus.DONE, history.get(i).getStatus());
                assertEquals("2022-04-20T10:00", history.get(i).getStartTime().toString());
                assertEquals(4, history.get(i).getDuration());
            } else if (i == 1) {
                assertEquals("Помыть посуду", history.get(i).getName());
                assertEquals("Помыть тарелки и вилки", history.get(i).getDescription());
                assertEquals(TaskStatus.NEW, history.get(i).getStatus());
                assertEquals("2022-04-21T10:00", history.get(i).getStartTime().toString());
                assertEquals(4, history.get(i).getDuration());
            }
        }
    }

    // Проверка метода history и getTaskById. Проверка истории, дублирование, восстановление менеджера из файла
    @Test
    public void shouldReturnHistoryWhenDuplicationAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задач
        // Создание задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        // Добавлние задач в трекер
        fileBackedManager.addTask(firstTask);
        fileBackedManager.addTask(secondTask);
        // Добавляем задачи в историю просмотров
        fileBackedManager.getTaskById(firstTask.getId());
        fileBackedManager.getTaskById(secondTask.getId());
        // Добавляем первую задачу снова в историю
        fileBackedManager.getTaskById(firstTask.getId());
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Проверка истории
        List<Task> history = newFileBackedManager.history();
        for (int i = history.size(); i >= 0; i--) {
            if (i == 0) {
                assertEquals("Помыть посуду", history.get(i).getName());
                assertEquals("Помыть тарелки и вилки", history.get(i).getDescription());
                assertEquals(TaskStatus.NEW, history.get(i).getStatus());
                assertEquals("2022-04-21T10:00", history.get(i).getStartTime().toString());
                assertEquals(4, history.get(i).getDuration());
            } else if (i == 1) {
                assertEquals("Купить хлеб", history.get(i).getName());
                assertEquals("Нужен хлеб \"Литовский\"", history.get(i).getDescription());
                assertEquals(TaskStatus.DONE, history.get(i).getStatus());
                assertEquals("2022-04-20T10:00", history.get(i).getStartTime().toString());
                assertEquals(4, history.get(i).getDuration());
            }
        }
    }

    // Проверка метода deleteAllTasks
    @Test
    public void shouldDeleteAllTasksAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        // Добавлние задач в трекер
        fileBackedManager.addTask(firstTask);
        fileBackedManager.addTask(secondTask);
        //Создаем эпик
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпиков в трекер
        fileBackedManager.addEpic(secondEpic);
        // Создание подзадач ко второму эпику
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 23, 10, 0), 4);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 24, 10, 0), 4);
        // Добавление подзадач второго эпика
        fileBackedManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        fileBackedManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Удаляем все задачи из трекера
        newFileBackedManager.deleteAllTasks();
        assertEquals(0, newFileBackedManager.getAllItems().size());
    }

    // Пустой трекер задач
    @Test
    public void shouldReturnZeroWhenNoTasksAndLoad() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Просмотр всех задач
        assertEquals(0, newFileBackedManager.getAllItems().size());
    }

    // Провека метода getAllTasks, пустой список задач
    @Test
    public void shouldReturnZeroWhenTaskListIsEmpty() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Проверка, что список задач пуст
        assertEquals(0, newFileBackedManager.getAllTasks().size());
    }

    // Провека метода getAllSubTasks, пустой список задач
    @Test
    public void shouldReturnZeroWhenSubTaskListIsEmpty() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Проверка, что список задач пуст
        assertEquals(0, newFileBackedManager.getAllSubtasks().size());
    }

    // Проверка метода history. Проверка истории просмотра задач, пустая история
    @Test
    public void shouldReturnZeroIfZeroTasksInHistory() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Выключение менеджера
        fileBackedManager = null;
        // Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Вызов истории
        assertEquals(0, newFileBackedManager.history().size());
    }

    // Проверка работы фалового менеджера
    @Test
    public void shouldReloadManager() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager firstFileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        // Добавлние задачи в трекер
        firstFileBackedManager.addTask(firstTask);
        //Создаем эпик
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпика в трекер
        firstFileBackedManager.addEpic(secondEpic);
        // Выключение менеджера
        firstFileBackedManager = null;
        // Включение нового менеджера
        Manager secondFileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание новой подзадачи
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 24, 10, 0), 4);
        // Добавление новой подзадачи к эпику
        secondFileBackedManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        // Выключение менеджера
        secondFileBackedManager = null;
        // Включение нового менеджера
        Manager thirdFileBackedManager = FileBackedManager.loadFromFile(file);
        for (Task task : thirdFileBackedManager.getAllItems().values()) {
            if (task.getClass().getName().equals("tasks.Task")) {
                assertEquals("Помыть посуду", task.getName());
                assertEquals("Помыть тарелки и вилки", task.getDescription());
                assertEquals(TaskStatus.NEW, task.getStatus());
                assertEquals("2022-04-21T10:00", task.getStartTime().toString());
                assertEquals(4, task.getDuration());
            } else if (task.getClass().getName().equals("tasks.Epic")) {
                assertEquals("Изучение Java", task.getName());
                assertEquals("Изучить язык программирования Java", task.getDescription());
            } else {
                SubTask subTask = (SubTask) task;
                assertEquals("Изучить полиморфизм", subTask.getName());
                assertEquals("Изучить перегрузку методов", subTask.getDescription());
                assertEquals(TaskStatus.NEW, subTask.getStatus());
                assertEquals(secondEpic.getId(), subTask.getEpicId());
                assertEquals("2022-04-24T10:00", subTask.getStartTime().toString());
                assertEquals(4, subTask.getDuration());
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