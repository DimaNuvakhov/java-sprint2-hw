package managers;

import exception.ManagerSaveException;
import imanagers.HistoryManager;
import tasks.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedManager extends InMemoryManager {
    File file;
    static boolean managerStatus = true;

    public FileBackedManager(File file) {
        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        if (managerStatus) {
            save();
        }
    }

    @Override
    public void getTaskById(String id) {
        super.getTaskById(id);
        if (managerStatus) {
            save();
        }
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        if (managerStatus) {
            save();
        }
    }

    @Override
    public void addSubTaskIntoEpic(SubTask subTask) {
        super.addSubTaskIntoEpic(subTask);
        if (managerStatus) {
            save();
        }
    }

    @Override
    public void renewTaskById(String oldId, Task task) {
        super.renewTaskById(oldId, task);
        if (managerStatus) {
            save();
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        if (managerStatus) {
            save();
        }
    }

    @Override
    public Boolean deleteTaskById(String id) {
       boolean isDelete = super.deleteTaskById(id);
        if (managerStatus && isDelete) {
            save();
        }
        return isDelete;
    }

    // Сохраняем информацию в файл
    public void save() {
        try {
            String header = "id,type,name,status,description,epicId" + "\n";
            Writer fileWriter = new FileWriter(file);
            fileWriter.write(header);
            for (Task task : getAllItems().values()) {
                if (task.getClass().getName().equals(TASK_NAME)) {
                    fileWriter.write(toString(task));
                } else if (task.getClass().getName().equals(EPIC_NAME)) {
                    Epic epic = (Epic) task;
                    fileWriter.write(toString(epic));
                    for (SubTask subTask : epic.getSubTasks().values()) {
                        fileWriter.write(toString(subTask));
                    }
                }
            }
            fileWriter.write(toString(inMemoryHistoryManager));
            fileWriter.close();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка, возможно файл не находится в данной директории");
        }
    }

    // Считываю файл из директории
    public static String load(File file) {
        try {
            return Files.readString(Path.of(file.getPath()));
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка, возможно файл не находится в данной директории");
        }
    }

    // Заполняю трекер задач задачами, подзадачами и эпкиами, которые я считал из файла
    public static FileBackedManager loadFromFile(File file) {
        FileBackedManager fileBackedManager = new FileBackedManager(file);
        if (file.exists()) {
            managerStatus = false;
            String[] stringTask = load(file).split("\n");
            makeManager(stringTask, fileBackedManager);
            managerStatus = true;
        }
        return fileBackedManager;
    }

    // Создаю менеджер из информации, которая была считана из файла
    public static void makeManager(String[] stringTask, FileBackedManager fileBackedManager) {
        boolean isEmptyLine = false;
        for (int i = 1; i < stringTask.length; i++) {
            if (!stringTask[i].isEmpty() && !isEmptyLine) {
                if (fromString(stringTask[i]).getClass().getName().equals(TASK_NAME)) {
                    Task task = fromString(stringTask[i]);
                    fileBackedManager.addTask(task);
                } else if (fromString(stringTask[i]).getClass().getName().equals(EPIC_NAME)) {
                    Epic epic = (Epic) fromString(stringTask[i]);
                    fileBackedManager.addEpic(epic);
                } else if (fromString(stringTask[i]).getClass().getName().equals(SUBTASK_NAME)) {
                    SubTask subTask = (SubTask) fromString(stringTask[i]);
                    fileBackedManager.addSubTaskIntoEpic(subTask);
                }
            } else {
                isEmptyLine = true;
            }
            if (i == stringTask.length - 1 && isEmptyLine) {
                for (String id : historyFromString(stringTask[i])) {
                    fileBackedManager.getTaskById(id);
                }
            }
        }
    }

    // Делаю из задачи строку
    public String toString(Task task) {
        String comma = ",";
        StringBuilder stringTask = new StringBuilder();
        if (task.getClass().getName().equals(TASK_NAME)) {
            stringTask.append(task.getId()).append(comma).append(TaskType.TASK).append(comma).
                    append(task.getName()).append(comma).append(task.getStatus()).append(comma).
                    append(task.getDescription()).append(comma).append("null").append("\n");
        } else if (task.getClass().getName().equals(EPIC_NAME)) {
            stringTask.append(task.getId()).append(comma).append(TaskType.EPIC).append(comma).
                    append(task.getName()).append(comma).append(task.getStatus()).append(comma).
                    append(task.getDescription()).append(comma).append("null").append("\n");
        } else if (task.getClass().getName().equals(SUBTASK_NAME)) {
            stringTask.append(task.getId()).append(comma).append(TaskType.SUBTASK).append(comma).
                    append(task.getName()).append(comma).append(task.getStatus()).append(comma).
                    append(task.getDescription()).append(comma).append(((SubTask) task).getEpicId()).append("\n");
        }
        return stringTask.toString();
    }

    // Сохраняю идентификаторы истории задач в строку
    public static String toString(HistoryManager manager) {
        String comma = "";
        StringBuilder stringHistory = new StringBuilder();
        for (Task task : manager.getHistory()) {
            stringHistory.append(comma).append(task.getId());
            if (comma.equals("")) { comma = ",";}
        }
        return "\n" + stringHistory;
    }

    // Возвращаю лист из идентификаторов
    public static List<String> historyFromString(String value) {
        List<String> ids = new ArrayList<>();
        String[] stringHistory = value.split(",");
        for (int i = stringHistory.length - 1; i >= 0; i--) {
            ids.add(stringHistory[i]);
        }
        return ids;
    }

    // Делаю задачу, эпик или подзадачу из строки
    public static Task fromString(String value) {
        String[] lines = value.split(",");
        if (lines[1].equals(TaskType.TASK.toString())) {
            Task taskFromFile = new Task(lines[2], lines[4], statusFromString(lines[3]));
            taskFromFile.setId(lines[0]);
            return taskFromFile;
        } else if (lines[1].equals(TaskType.EPIC.toString())) {
            Epic epicFromFile = new Epic(lines[2], lines[4]);
            epicFromFile.setId(lines[0]);
            return epicFromFile;
        } else if (lines[1].equals(TaskType.SUBTASK.toString())) {
            SubTask subTaskFromFile = new SubTask(lines[2], lines[4],
                    statusFromString(lines[3]), lines[5]);
            subTaskFromFile.setId(lines[0]);
            return subTaskFromFile;
        }
        return null;
    }

    // Создаю тип статус из типа стринг
    public static TaskStatus statusFromString(String status) {
        switch (status) {
            case "NEW":
                return TaskStatus.NEW;
            case "DONE":
                return TaskStatus.DONE;
            case "IN_PROGRESS":
                return TaskStatus.IN_PROGRESS;
        }
        return null;
    }
}
