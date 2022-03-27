package managers;

import imanagers.HistoryManager;
import imanagers.Manager;

import java.io.File;

public class Managers {

    public static Manager getDefault() {
        return new InMemoryManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Manager getDefaultFileManager() {
        File file = new File("Data.csv");
        boolean isDelete = file.delete();
        return new FileBackedManager(file);
    }

    public static Manager getDefaultHttpManager() {
        return new HTTPTaskManager();
    }
}
