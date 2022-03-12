package managers;

import imanagers.HistoryManager;
import imanagers.Manager;

public class Managers {

    public static Manager getDefault() {
        return new InMemoryManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
