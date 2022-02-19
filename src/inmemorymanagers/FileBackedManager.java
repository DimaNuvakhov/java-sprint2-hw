package inmemorymanagers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileBackedManager extends InMemoryManager {
    File file;

    public FileBackedManager(File file) {
        this.file = new File("File.csv");
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
            for (Task task : getAllItems().values()) {
                Writer fileWriter = new FileWriter(file);
                fileWriter.write(toString(task));
                fileWriter.close();
            }
        } catch (IOException exception) {

        }
    }

    public static void loadFromFile(File file) {

    }

    public String toString(Task task) {
        String comma = ",";
        String header = "id,type,name,status,description,epicId";
        StringBuilder stringTask = new StringBuilder();
        if (!task.getClass().getName().equals(SUBTASK_NAME)) {
            stringTask.append(task.getId()).append(comma).append(task.getClass().getName()).append(comma)
                    .append(task.getName()).append(comma).append(task.getStatus()).append(comma)
                    .append(task.getDescription()).append(comma).append("null").append("\n");
        } else {
            stringTask.append(task.getId()).append(comma).append(task.getClass().getName()).append(comma)
                    .append(task.getName()).append(comma).append(task.getStatus()).append(comma)
                    .append(task.getDescription()).append(comma).append(((SubTask) task).getEpicId()).append("\n");
        }
        return header + "\n" + stringTask.toString();
    }

    public Task fromString(String value) {
       String[] stringTask = value.split(",");
       for (int i = 0; i < stringTask.length; i++) {
           if (stringTask[1].equals(TASK_NAME)) {
              // Task newTask = new Task()
           }
       }
       return null; // для того, чтобы можно было запушить
    }
}
