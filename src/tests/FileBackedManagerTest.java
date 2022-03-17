package tests;

import imanagers.Manager;
import managers.FileBackedManager;
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
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        fileBackedManager.addTask(firstTask);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        Task sameTask = getTaskByName(newFileBackedManager, "Помыть посуду");
        assertNotNull(sameTask);
        assertEquals("Помыть тарелки и вилки", sameTask.getDescription());
        assertEquals(TaskStatus.NEW, sameTask.getStatus());
        assertEquals("2022-03-15T10:00", sameTask.getStartTime().toString());
        assertEquals(3, sameTask.getDuration());
    }

    // Проверка метода renewTaskById, обновление задачи, восстановление менеджера из файла
    @Test
    public void shouldRenewTaskByIdAndLoad() {
        File file = new File("Data.csv");
         boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        fileBackedManager.addTask(firstTask);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 20, 11, 0), 3);
        newFileBackedManager.renewTaskById(firstTask.getId(), secondTask);
        Task oldTask = getTaskByName(newFileBackedManager, "Помыть посуду");
        assertNull(oldTask);
        Task renewedTask = getTaskByName(newFileBackedManager, "Купить хлеб");
        assertNotNull(renewedTask);
        assertEquals("Нужен хлеб \"Литовский\"", renewedTask.getDescription());
        assertEquals(TaskStatus.DONE, renewedTask.getStatus());
        assertEquals(firstTask.getId(), renewedTask.getId());
        assertEquals("2022-03-20T11:00", renewedTask.getStartTime().toString());
        assertEquals(3, renewedTask.getDuration());
    }

    // Проверка метода deleteTaskById, удаление задачи, восстановление менеджера из файла
    @Test
    public void shouldDeleteTaskByIdAndLoad() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        fileBackedManager.addTask(firstTask);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        assertTrue(newFileBackedManager.deleteTaskById(firstTask.getId()));
    }

    // Проверка метода addEpic, добавлние эпика, восстановление менеджера из файла
    @Test
    public void shouldAddEpicAndLoad() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        fileBackedManager.addEpic(firstEpic);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        assertEquals(1, newFileBackedManager.getAllItems().size());
        Epic sameEpic = (Epic) getTaskByName(newFileBackedManager, "Сходить в спортзал");
        assertNotNull(sameEpic);
        assertEquals("Прокачать 3 группы мышц", sameEpic.getDescription());
    }

    // Проверка метода renewTaskById, обновление эпика, восстановление менеджера из файла
    @Test
    public void shouldRenewEpicByIdAndLoad() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        fileBackedManager.addEpic(firstEpic);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        newFileBackedManager.renewTaskById(firstEpic.getId(), secondEpic);
        Epic oldEpic = (Epic) getTaskByName(newFileBackedManager, "Сходить в спортзал");
        assertNull(oldEpic);
        Epic renewedEpic = (Epic) getTaskByName(newFileBackedManager, "Изучение Java");
        assertNotNull(renewedEpic);
        assertEquals("Изучить язык программирования Java", renewedEpic.getDescription());
        assertEquals(firstEpic.getId(), renewedEpic.getId());
    }

    // Проверка метода deleteTaskById, удаление эпика, восстановление менеджера из файла
    @Test
    public void shouldDeleteEpicByIdAndLoad() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        fileBackedManager.addEpic(firstEpic);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        assertTrue(newFileBackedManager.deleteTaskById(firstEpic.getId()));
    }

    // Проверка метода addSubTaskIntoEpic, добавление подзадачи, восстановление менеджера из файла
    @Test
    public void shouldAddSubTaskIntoEpicAndLoad() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        fileBackedManager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        fileBackedManager.addSubTaskIntoEpic(firstSubTask);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        SubTask sameSubTask = (SubTask) getTaskByName(newFileBackedManager, "Изучить Дженерики");
        assertNotNull(sameSubTask);
        assertEquals("Изучить случаи применения дженериков", sameSubTask.getDescription());
        assertEquals(TaskStatus.NEW, sameSubTask.getStatus());
        assertEquals(firstEpic.getId(), sameSubTask.getEpicId());
        assertEquals("2022-03-15T10:00", sameSubTask.getStartTime().toString());
        assertEquals(4, sameSubTask.getDuration());
    }

    // Проверка метода renewTaskById, обновление подзадачи, восстановление менеджера из файла
    @Test
    public void shouldRenewSubTaskByIdAndLoad() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        fileBackedManager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        fileBackedManager.addSubTaskIntoEpic(firstSubTask);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        SubTask secondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 16, 10, 0), 3);
        newFileBackedManager.renewTaskById(firstSubTask.getId(), secondSubTask);
        SubTask oldTask = (SubTask) getTaskByName(newFileBackedManager, "Изучить Дженерики");
        assertNull(oldTask);
        SubTask renewedSubTask = (SubTask) getTaskByName(newFileBackedManager, "Изучить полиморфизм");
        assertNotNull(renewedSubTask);
        assertEquals("Изучить перегрузку методов", renewedSubTask.getDescription());
        assertEquals(TaskStatus.NEW, renewedSubTask.getStatus());
        assertEquals(firstSubTask.getId(), renewedSubTask.getId());
        assertEquals("2022-03-16T10:00", renewedSubTask.getStartTime().toString());
        assertEquals(3, renewedSubTask.getDuration());
    }

    // Проверка метода deleteTaskById, удаление подзадачи, восстановление менеджера из файла
    @Test
    public void shouldDeleteSubTaskByIdAndLoad() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Epic firstEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        fileBackedManager.addEpic(firstEpic);
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId(),
                LocalDateTime.of(2022, 3, 15, 10, 0), 4);
        fileBackedManager.addSubTaskIntoEpic(firstSubTask);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        assertTrue(newFileBackedManager.deleteTaskById(firstSubTask.getId()));
    }

    // Проверка метода getAllTasks, восстановление менеджера из файла
    @Test
    public void shouldReturnTaskListAndLoad() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 3, 15, 10, 0), 3);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 3, 16, 10, 0), 3);
        fileBackedManager.addTask(firstTask);
        fileBackedManager.addTask(secondTask);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        assertEquals(2, newFileBackedManager.getAllTasks().size());
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

    // Проверка метода getAllEpics, восстановление менеджера из файла
    @Test
    public void shouldReturnEpicListAndLoad() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        fileBackedManager.addEpic(firstEpic);
        fileBackedManager.addEpic(secondEpic);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        assertEquals(2, newFileBackedManager.getAllEpics().size());
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

    // Проверка метода getSubTaskListFromEpicById, восстановление менеджера из файла
    @Test
    public void shouldReturnSubTaskListByEpicIdAndLoad() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        fileBackedManager.addEpic(firstEpic);
        fileBackedManager.addEpic(secondEpic);
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId(),
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        fileBackedManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 22, 10, 0), 4);
        fileBackedManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        fileBackedManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        assertEquals(2, newFileBackedManager.getSubTaskListFromEpicById(secondEpic.getId()).size());
        ArrayList<SubTask> subTasks = newFileBackedManager.getSubTaskListFromEpicById(secondEpic.getId());
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

    // Проверка метода getAllSubTasks, восстановление менеджера из файла
    @Test
    public void shouldReturnSubTaskListAndLoad() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        fileBackedManager.addEpic(firstEpic);
        fileBackedManager.addEpic(secondEpic);
        SubTask firstEpicFirstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId(),
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        fileBackedManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 22, 10, 0), 4);
        fileBackedManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        fileBackedManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        assertEquals(3, newFileBackedManager.getAllSubtasks().size());
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
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        fileBackedManager.addTask(firstTask);
        fileBackedManager.addTask(secondTask);
        fileBackedManager.getTaskById(firstTask.getId());
        fileBackedManager.getTaskById(secondTask.getId());
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
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
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        fileBackedManager.addTask(firstTask);
        fileBackedManager.addTask(secondTask);
        fileBackedManager.getTaskById(firstTask.getId());
        fileBackedManager.getTaskById(secondTask.getId());
        fileBackedManager.getTaskById(firstTask.getId());
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
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

    // Проверка метода deleteAllTasks, удаление всех задач
    @Test
    public void shouldDeleteAllTasksAndLoad() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE,
                LocalDateTime.of(2022, 4, 20, 10, 0), 4);
        fileBackedManager.addTask(firstTask);
        fileBackedManager.addTask(secondTask);
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        fileBackedManager.addEpic(secondEpic);
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 23, 10, 0), 4);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 24, 10, 0), 4);
        fileBackedManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        fileBackedManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        newFileBackedManager.deleteAllTasks();
        assertEquals(0, newFileBackedManager.getAllItems().size());
    }

    // Пустой трекер задач
    @Test
    public void shouldReturnZeroWhenNoTasksAndLoad() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        assertEquals(0, newFileBackedManager.getAllItems().size());
    }

    // Проверка метода getAllTasks, пустой список задач
    @Test
    public void shouldReturnZeroWhenTaskListIsEmpty() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        assertEquals(0, newFileBackedManager.getAllTasks().size());
    }

    // Проверка метода getAllSubTasks, пустой список задач
    @Test
    public void shouldReturnZeroWhenSubTaskListIsEmpty() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        assertEquals(0, newFileBackedManager.getAllSubtasks().size());
    }

    // Проверка метода history. Проверка истории просмотра задач, пустая история
    @Test
    public void shouldReturnZeroIfZeroTasksInHistory() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        fileBackedManager = null;
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        assertEquals(0, newFileBackedManager.history().size());
    }

    // Проверка правильности работы, загрузка менеджера из файла два раза
    @Test
    public void shouldReloadManager() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        Manager firstFileBackedManager = FileBackedManager.loadFromFile(file);
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW,
                LocalDateTime.of(2022, 4, 21, 10, 0), 4);
        firstFileBackedManager.addTask(firstTask);
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        firstFileBackedManager.addEpic(secondEpic);
        firstFileBackedManager = null;
        Manager secondFileBackedManager = FileBackedManager.loadFromFile(file);
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId(),
                LocalDateTime.of(2022, 4, 24, 10, 0), 4);
        secondFileBackedManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        secondFileBackedManager = null;
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

    private Task getTaskByName(Manager inMemoryManager, String name) {
        for (Task task : inMemoryManager.getAllItems().values()) {
            if (task.getName().equals(name)) {
                return task;
            }
        }
        return null;
    }
}