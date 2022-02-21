package inmemorymanagers;

import exception.ManagerSaveException;
import managers.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;
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

    //сохраняем информацию в файл
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
            fileWriter.close();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка, возможно файл не находится в данной директории");
        }
    }


    public static String load(File file) {
        try {
            return Files.readString(Path.of(file.getPath()));
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка, возможно файл не находится в данной директории");
        }
    }

    public static FileBackedManager loadFromFile(File file) {
        FileBackedManager fileBackedManager = new FileBackedManager(file);
        if (file.exists()) {
            fromString(load(file), fileBackedManager);
        }
        return fileBackedManager;
    }

    public String toString(Task task) {
        String comma = ",";
        String header = "id,type,name,status,description,epicId";
        StringBuilder stringTask = new StringBuilder();
        if (!task.getClass().getName().equals(SUBTASK_NAME)) {
            stringTask.append(task.getId()).append(comma).append(task.getClass().getName()).append(comma).
                    append(task.getName()).append(comma).append(task.getStatus()).append(comma).
                    append(task.getDescription()).append(comma).append("null").append("\n");
        } else {
            stringTask.append(task.getId()).append(comma).append(task.getClass().getName()).append(comma).
                    append(task.getName()).append(comma).append(task.getStatus()).append(comma).
                    append(task.getDescription()).append(comma).append(((SubTask) task).getEpicId()).append("\n");
        }
        return stringTask.toString();
    }

    static String toString(HistoryManager manager) {
        String comma = ",";
        StringBuilder stringHistory = new StringBuilder();
        for (Task task : manager.getHistory()) {
           stringHistory.append(task.getId()).append(comma);
        }
        return stringHistory.toString();
    }

    static List<String> fromString(String value) {
        List<String> ids = new ArrayList<>();
        String[] stringHistory = value.split(",");
        for (String id : stringHistory) {
            ids.add(id);
        }
        return ids;
    }

    public static void fromString(String value, FileBackedManager fileBackedManager) {
        managerStatus = false;
        String[] stringTask = value.split("\n");
        for (int i = 1; i < stringTask.length; i++) {
            String[] lines = stringTask[i].split(",");
            if (lines[1].equals(TASK_NAME)) {
                Task newTask = new Task(lines[0], lines[2], lines[4], statusFromString(lines[3]));
                fileBackedManager.addTask(newTask);
            } else if (lines[1].equals(EPIC_NAME)) {
                Epic newEpic = new Epic(lines[0], lines[2], lines[4]);
                fileBackedManager.addEpic(newEpic);
            } else if (lines[1].equals(SUBTASK_NAME)) {
                SubTask newSubTask = new SubTask(lines[0], lines[2], lines[4],
                        statusFromString(lines[3]), lines[5]);
                fileBackedManager.addSubTaskIntoEpic(newSubTask);
            }
        }
    }

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
