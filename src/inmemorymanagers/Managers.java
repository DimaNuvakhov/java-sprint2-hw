package inmemorymanagers;

import managers.HistoryManager;
import managers.Manager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.util.UUID;

public class Managers {

    public static Manager getDefault() {
        return new InMemoryManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
