package inmemorymanagers;

import managers.Manager;

public class Managers {

    public static Manager getDefault() {
        return new InMemoryManager();
    }
}
