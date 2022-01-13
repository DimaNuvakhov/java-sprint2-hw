package managers;

import inmemorymanager.InMemoryManager;
import manager.Manager;

public class Managers {

    public static Manager getDefault() {
        return new InMemoryManager();
    }
}
