package managers;

import inmemoryhistorymanager.InMemoryHistoryManager;
import manager.Manager;

public class Managers {

    public static Manager getDefault() {
        return new InMemoryHistoryManager();
    }
}
