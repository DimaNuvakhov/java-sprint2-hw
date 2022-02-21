package inmemorymanagers;

import managers.Manager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.util.UUID;

public class Managers {

//    public static Manager getDefault() {
//        File file = new File("Data.csv");
//        //return new FileBackedManager(file);
//    }

    public static Task createTask(String name, String description, TaskStatus status) {
        String id = UUID.randomUUID().toString().substring(0, 32);
        return new Task(id, name, description, status);
    }

    public static Epic createEpic(String name, String description) {
        String id = UUID.randomUUID().toString().substring(0, 32);
        return new Epic(id, name, description);
    }

    public static SubTask createSubTask(String name, String description, TaskStatus status, String epicId) {
        String id = UUID.randomUUID().toString().substring(0, 32);
        return new SubTask(id, name, description, status, epicId);
    }
}
