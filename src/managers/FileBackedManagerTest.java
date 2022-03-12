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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedManagerTest {

    @Test
    public void shouldAddTaskAndSave() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла и проверка удаления
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        // Добавление задачи в трекер задач
        fileBackedManager.addTask(firstTask);
        // Выключение менеджера
        fileBackedManager = null;
        //Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Поиск задачи в трекере по имени
        Task sameTask = getTaskByName(newFileBackedManager, "Помыть посуду");
        // Проверка, есть ли задача в трекере
        assertNotNull(sameTask);
        // Сверка описания задачи
        assertEquals("Помыть тарелки и вилки", sameTask.getDescription());
        // Сверка статуса задачи
        assertEquals(TaskStatus.NEW, sameTask.getStatus());
    }

    @Test
    public void shouldAddEpicAndSave() {
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
        //Включение нового менеджера
        Manager newFileBackedManager = FileBackedManager.loadFromFile(file);
        // Поиск задачи в трекере по имени
        Epic sameEpic = (Epic) getTaskByName(newFileBackedManager, "Сходить в спортзал");
        // Проверка, есть ли эпик в трекере
        assertNotNull(sameEpic);
        // Сверка описания эпика
        assertEquals("Прокачать 3 группы мышц", sameEpic.getDescription());
    }

    // Проверка метода addTask
    @Test
    public void shouldAddTask() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        // Добавление задачи в трекер задач
        fileBackedManager.addTask(firstTask);
        // Поиск задачи в трекере по имени
        Task sameTask = getTaskByName(fileBackedManager, "Помыть посуду");
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
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла и проверка удаления
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        // Добавление задачи в трекер задач
        fileBackedManager.addTask(firstTask);
        // Создание новой задачи
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE);
        // Обновление задачи
        fileBackedManager.renewTaskById(firstTask.getId(), secondTask);
        // Поиск задачи в трекере по имени
        Task oldTask = getTaskByName(fileBackedManager, "Помыть посуду");
        // Проверка, что задачи нет в трекере
        assertNull(oldTask);
        // Поиск обновленной задачи в трекере по имени
        Task renewedTask = getTaskByName(fileBackedManager, "Купить хлеб");
        // Проверка, есть ли обновленная задача в трекере
        assertNotNull(renewedTask);
        // Сверка описания задачи
        assertEquals("Нужен хлеб \"Литовский\"", renewedTask.getDescription());
        // Сверка статуса задачи
        assertEquals(TaskStatus.DONE, renewedTask.getStatus());
        // Сверка id первой задачи с id обновленной задачи
        assertEquals(firstTask.getId(), renewedTask.getId());
    }

    // Проверка метода renewTaskById некорректный id
    @Test
    public void shouldThrowExceptionWhenIncorrectTaskId() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла и проверка удаления
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        // Добавление задачи в трекер задач
        fileBackedManager.addTask(firstTask);
        // Проверка
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileBackedManager.renewTaskById("1", firstTask);
                    }
                });
        assertEquals("Задача с указанным id в трекер задач не добавлена", ex.getMessage());
    }

    // Проверка метода deleteTaskById стандартная реалиация, удаление задачи
    @Test
    public void shouldDeleteTaskById() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задачи
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        // Добавление задачи в трекер задач
        fileBackedManager.addTask(firstTask);
        // Удаление задачи из трекера проверка, что задача удалена
        assertTrue(fileBackedManager.deleteTaskById(firstTask.getId()));
    }

    // Проверка метода deleteTaskById некорректный id, проверка удаления задачи, подзадачи и эпика
    @Test
    public void shouldDeleteTaskByIncorrectId() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла и проверка удаления
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Удаление задачи из трекера по неверному id
        assertFalse(fileBackedManager.deleteTaskById("1"));
    }

    // Проверка метода addEpic
    @Test
    public void shouldAddEpic() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла и проверка удаления
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпика
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        // Добавление эпика в трекер задач
        fileBackedManager.addEpic(firstEpic);
        // Поиск задачи в трекере по имени
        Epic sameEpic = (Epic) getTaskByName(fileBackedManager, "Сходить в спортзал");
        // Проверка, есть ли эпик в трекере
        assertNotNull(sameEpic);
        // Сверка описания эпика
        assertEquals("Прокачать 3 группы мышц", sameEpic.getDescription());
    }

    // Проверка метода renewTaskById стандартная реализация
    @Test
    public void shouldRenewEpicById() {
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
        // Создание нового эпика
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Обновление эпика
        fileBackedManager.renewTaskById(firstEpic.getId(), secondEpic);
        // Поиск задачи в трекере по имени
        Epic oldEpic = (Epic) getTaskByName(fileBackedManager, "Сходить в спортзал");
        // Проверка, что эпика нет в трекере
        assertNull(oldEpic);
        // Поиск обновленной задачи в трекере по имени
        Epic renewedEpic = (Epic) getTaskByName(fileBackedManager, "Изучение Java");
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
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпика
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        // Добавление задачи в трекер задач
        fileBackedManager.addEpic(firstEpic);
        // Проверка
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileBackedManager.renewTaskById("1", firstEpic);
                    }
                });
        assertEquals("Задача с указанным id в трекер задач не добавлена", ex.getMessage());
    }

    // Проверка метода deleteTaskById стандартная реалиация, удаление эпика
    @Test
    public void shouldDeleteEpicById() {
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
        // Удаление задачи из трекера проверка, что задача удалена
        assertTrue(fileBackedManager.deleteTaskById(firstEpic.getId()));
    }

    // Проверка метода addSubTaskIntoEpic
    @Test
    public void shouldAddSubTaskIntoEpic() {
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
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId());
        // Добавление подзадачи в трекер задач
        fileBackedManager.addSubTaskIntoEpic(firstSubTask);
        // Поиск подзадачи в трекере по имени
        SubTask sameSubTask = (SubTask) getTaskByName(fileBackedManager, "Изучить Дженерики");
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
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId());
        // Добавление подзадачи в трекер задач
        fileBackedManager.addSubTaskIntoEpic(firstSubTask);
        // Создание новой подзадачи
        SubTask secondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, firstEpic.getId());
        // Обновление подзадачи
        fileBackedManager.renewTaskById(firstSubTask.getId(), secondSubTask);
        // Поиск подзадачи в трекере по имени
        SubTask oldTask = (SubTask) getTaskByName(fileBackedManager, "Изучить Дженерики");
        // Проверка, что подзадачи нет в трекере
        assertNull(oldTask);
        // Поиск обновленной подзадачи в трекере по имени
        SubTask renewedSubTask = (SubTask) getTaskByName(fileBackedManager, "Изучить полиморфизм");
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
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпика
        Epic firstEpic = new Epic("Сходить в спортзал", "Прокачать 3 группы мышц");
        // Добавление задачи в трекер задач
        fileBackedManager.addEpic(firstEpic);
        // Создание подзадачи к эпику
        SubTask firstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId());
        // Добавление подзадачи в трекер задач
        fileBackedManager.addSubTaskIntoEpic(firstSubTask);
        // Проверка
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileBackedManager.renewTaskById("1", firstSubTask);
                    }
                });
        assertEquals("Задача с указанным id в трекер задач не добавлена", ex.getMessage());
    }

    // Проверка метода deleteTaskById стандартная реалиация, удаление подзадачи
    @Test
    public void shouldDeleteSubTaskById() {
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
                "Изучить случаи применения дженериков", TaskStatus.NEW, firstEpic.getId());
        // Добавление подзадачи в трекер задач
        fileBackedManager.addSubTaskIntoEpic(firstSubTask);
        // Удаление задачи из трекера проверка, что задача удалена
        assertTrue(fileBackedManager.deleteTaskById(firstSubTask.getId()));
    }

    // Провека метода calcStatus, эпик пустой
    @Test
    public void shouldReturnNewEpicStatusWhenEpicIsEmpty() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпика
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        // Добавление эпика в трекер
        fileBackedManager.addEpic(firstEpic);
        // Проверка статуса при пустом списке подзадач
        assertEquals(TaskStatus.NEW, firstEpic.getStatus());
    }

    // Провека метода calcStatus, у эпика 2 подзадачи со статусом NEW
    @Test
    public void shouldReturnNewEpicStatus() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпика
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        // Добавление эпика в трекер
        fileBackedManager.addEpic(firstEpic);
        // Создание субтасок к эпику, все подзадачи со статусом NEW
        SubTask firstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.NEW, firstEpic.getId());
        SubTask secondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.NEW, firstEpic.getId());
        // Добавление субтасок в трекер задач
        fileBackedManager.addSubTaskIntoEpic(firstSubTask);
        fileBackedManager.addSubTaskIntoEpic(secondSubTask);
        // Проверка статуса эпика, если у эпика две новые подзадачи
        assertEquals(TaskStatus.NEW, firstEpic.getStatus());
    }

    // Провека метода calcStatus, у эпика 2 подзадачи со статусом DONE
    @Test
    public void shouldReturnDoneEpicStatus() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпика
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        // Добавление эпика в трекер
        fileBackedManager.addEpic(firstEpic);
        // Создание субтасок к эпику, все подзадачи со статусом DONE
        SubTask firstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId());
        SubTask secondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.DONE, firstEpic.getId());
        // Добавление субтасок в трекер задач
        fileBackedManager.addSubTaskIntoEpic(firstSubTask);
        fileBackedManager.addSubTaskIntoEpic(secondSubTask);
        // Проверка статуса эпика, если у эпика две новые подзадачи
        assertEquals(TaskStatus.DONE, firstEpic.getStatus());
    }

    // Провека метода calcStatus, у эпика 2 подзадачи одна со статсусом NEW, другая с DONE
    @Test
    public void shouldReturnInProgressEpicStatus() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпика
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        // Добавление эпика в трекер
        fileBackedManager.addEpic(firstEpic);
        // Создание субтасок к эпику, обе подзадачи со статусом DONE и NEW
        SubTask firstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId());
        SubTask secondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.NEW, firstEpic.getId());
        // Добавление субтасок в трекер задач
        fileBackedManager.addSubTaskIntoEpic(firstSubTask);
        fileBackedManager.addSubTaskIntoEpic(secondSubTask);
        // Проверка статуса эпика, если у эпика две новые подзадачи
        assertEquals(TaskStatus.IN_PROGRESS, firstEpic.getStatus());
    }

    // Провека метода calcStatus, у эпика 2 подзадачи со статусом IN_PROGRESS
    @Test
    public void shouldReturnInProgressEpicStatusWhenAllSubTasksAreInProgress() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание эпика
        Epic firstEpic = new Epic("Переезд", "Собрать все вещи");
        // Добавление эпика в трекер
        fileBackedManager.addEpic(firstEpic);
        // Создание субтасок к эпику, обе подзадачи со статусом DONE и NEW
        SubTask firstSubTask = new SubTask("Собрать чемодан",
                "Положить в чемодан все необходимое", TaskStatus.IN_PROGRESS, firstEpic.getId());
        SubTask secondSubTask = new SubTask("Забрать сноуборд",
                "Забрать свой сноуборд из кладовки", TaskStatus.IN_PROGRESS, firstEpic.getId());
        // Добавление субтасок в трекер задач
        fileBackedManager.addSubTaskIntoEpic(firstSubTask);
        fileBackedManager.addSubTaskIntoEpic(secondSubTask);
        // Проверка статуса эпика, если у эпика две новые подзадачи
        assertEquals(TaskStatus.IN_PROGRESS, firstEpic.getStatus());
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
        // Проверка, что список задач пуст
        assertEquals(0, fileBackedManager.getAllTasks().size());
    }

    // Провека метода getAllTasks, стандартная реализация
    @Test
    public void shouldReturnTaskList() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE);
        // Добавлние задач в трекер
        fileBackedManager.addTask(firstTask);
        fileBackedManager.addTask(secondTask);
        // Проверка, что в списке 2 задачи
        assertEquals(2, fileBackedManager.getAllTasks().size());
        // Проверка списка задач
        ArrayList<Task> tasks = fileBackedManager.getAllTasks();
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
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Проверка, что список задач пуст
        assertEquals(0, fileBackedManager.getAllEpics().size());
    }

    // Провека метода getAllEpics, стандартная реализация
    @Test
    public void shouldReturnEpicList() {
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
        // Проверка, что в списке 2 эпика
        assertEquals(2, fileBackedManager.getAllEpics().size());
        // Проверка списка эпиков
        for (Epic epic : fileBackedManager.getAllEpics()) {
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
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Проверка, что список задач пуст
        assertEquals(0, fileBackedManager.getAllSubtasks().size());
    }

    // Провека метода getAllSubTasks, стандартная реализация
    @Test
    public void shouldReturnSubTaskList() {
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
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId());
        // Добавление подзадачи в трекер
        fileBackedManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        // Создание подзадач ко второму эпику
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId());
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId());
        // Добавление подзадач второго эпика
        fileBackedManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        fileBackedManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        // Проверка, что всего 3 подзадачи
        assertEquals(3, fileBackedManager.getAllSubtasks().size());
        // Проверка списка подзадач
        for (SubTask subTask : fileBackedManager.getAllSubtasks()) {
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
                "Положить в чемодан все необходимое", TaskStatus.DONE, firstEpic.getId());
        // Добавление подзадачи в трекер
        fileBackedManager.addSubTaskIntoEpic(firstEpicFirstSubTask);
        // Создание подзадач ко второму эпику
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId());
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId());
        // Добавление подзадач второго эпика
        fileBackedManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        fileBackedManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        // Проверка, что у второго эпика 2 подзадачи
        assertEquals(2, fileBackedManager.getSubTaskListFromEpicById(secondEpic.getId()).size());
        // Проверка списка подзадач определенного эпика
        for (SubTask subTask : fileBackedManager.getSubTaskListFromEpicById(secondEpic.getId())) {
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
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Провека, списка подзадач у эпика, неверный id
        assertEquals(0, fileBackedManager.getSubTaskListFromEpicById("1").size());
    }

    // Проверка метода history. Проверка истории просмотра задач, пустая история
    @Test
    public void shouldReturnEmptyHistory() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Вызов истории
        assertEquals(0, fileBackedManager.history().size());
    }

    // Проверка метода history. Проверка истории просмотра задач, некорректный id
    @Test
    public void shouldThrowExceptionWhenIncorrectId() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Добавлние несуществующей задачи в историю
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileBackedManager.getTaskById("1");
                    }
                });
        assertEquals("Задача с указанным id в трекер задач не добавлена", ex.getMessage());
    }

    // Проверка метода history и getTaskById. Проверка истории просмотра задач, 2 задачи в истории
    @Test
    public void shouldReturnHistory() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE);
        // Добавлние задач в трекер
        fileBackedManager.addTask(firstTask);
        fileBackedManager.addTask(secondTask);
        // Добавляем задачи в историю просмотров
        fileBackedManager.getTaskById(firstTask.getId());
        fileBackedManager.getTaskById(secondTask.getId());
        // Проверка истории
        List<Task> history = fileBackedManager.history();
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

    // Проверка метода deleteAllTasks
    @Test
    public void shouldDeleteAllTasks() {
        // Создание файла
        File file = new File("Data.csv");
        // Удаление файла
        boolean isDelete = file.delete();
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Создание задач
        Task firstTask = new Task("Помыть посуду", "Помыть тарелки и вилки", TaskStatus.NEW);
        Task secondTask = new Task("Купить хлеб", "Нужен хлеб \"Литовский\"", TaskStatus.DONE);
        // Добавлние задач в трекер
        fileBackedManager.addTask(firstTask);
        fileBackedManager.addTask(secondTask);
        //Создаем эпик
        Epic secondEpic = new Epic("Изучение Java", "Изучить язык программирования Java");
        // Добавление эпиков в трекер
        fileBackedManager.addEpic(secondEpic);
        // Создание подзадач ко второму эпику
        SubTask secondEpicFirstSubTask = new SubTask("Изучить Дженерики",
                "Изучить случаи применения дженериков", TaskStatus.NEW, secondEpic.getId());
        SubTask secondEpicSecondSubTask = new SubTask("Изучить полиморфизм",
                "Изучить перегрузку методов", TaskStatus.NEW, secondEpic.getId());
        // Добавление подзадач второго эпика
        fileBackedManager.addSubTaskIntoEpic(secondEpicFirstSubTask);
        fileBackedManager.addSubTaskIntoEpic(secondEpicSecondSubTask);
        // Удаляем все задачи из трекера
        fileBackedManager.deleteAllTasks();
        assertEquals(0, fileBackedManager.getAllItems().size());
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