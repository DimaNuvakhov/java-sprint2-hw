package inmemorymanagers;

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

public class FileBackedManager extends InMemoryManager {
    File file;

    public FileBackedManager(File file) {
        this.file = file;
        loadFromFile(file);
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTaskIntoEpic(SubTask subTask) {
        super.addSubTaskIntoEpic(subTask);
        save();
    }

    //сохраняем информацию в файл
    public void save() {
        try {
            String header = "id,type,name,status,description,epicId" + "\n";
            Writer fileWriter = new FileWriter(file);
            fileWriter.write(header);
            for (Task task : getAllItems().values()) {
                fileWriter.write(toString(task));
            }
            fileWriter.close();
        } catch (IOException exception) {

        }
    }

    public static void loadFromFile(File file) {
        fromString(load(file));
    }

    public static String load(File file) {
        try {
            return Files.readString(Path.of(file.getPath()));
        } catch (IOException exception) {

        }
        return null;
    }

    public String toString(Task task) {
        String comma = ",";
        String header = "id,type,name,status,description,epicId";
        StringBuilder stringTask = new StringBuilder();
        if (!task.getClass().getName().equals(SUBTASK_NAME)) {
            stringTask.append(task.getId()).append(comma).append(task.getClass().getName()).append(comma).
                    append(task.getName()).append(comma).append(task.getStatus()).append(comma ).
                    append(task.getDescription()).append(comma).append("null").append("\n");
        } else {
            stringTask.append(task.getId()).append(comma).append(task.getClass().getName()).append(comma).
                    append(task.getName()).append(comma).append(task.getStatus()).append(comma).
                    append(task.getDescription()).append(comma).append(((SubTask) task).getEpicId()).append("\n");
        }
        return stringTask.toString();
    }

    public static Task fromString(String value) {
        String[] stringTask = value.split("\n");
        for (int i = 1; i < stringTask.length; i++) {
            String[] lines = stringTask[i].split(",");
            for (int j = 0; j < lines.length; j++) {
                if (lines[1].equals(TASK_NAME)) {
                     Task newTask = new Task(lines[0], lines[2], lines[4],
                            statusFromString(stringTask[3]));
                     return newTask;
                } else if (lines[1].equals(EPIC_NAME)) {
                     Task newEpic = new Epic(lines[0], lines[2], lines[4]);
                    return newEpic;
                } else if (lines[1].equals(SUBTASK_NAME)) {
                     SubTask newSubTask = new SubTask(lines[0], lines[2], lines[4],
                            statusFromString(lines[3]), lines[5]);
                     return newSubTask;
                }
            }
        }
        return null;
    }

    public static TaskStatus statusFromString(String status) {
        if (status.equals("NEW")) {
            return TaskStatus.NEW;
        } else if (status.equals("DONE")) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }
}
