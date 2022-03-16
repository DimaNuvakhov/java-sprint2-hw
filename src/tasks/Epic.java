package tasks;

import managers.InMemoryManager;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<String, SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description, null, LocalDateTime.now(), 0);
        subTasks = new HashMap<>();
    }

    public HashMap<String, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", subTasks=" + subTasks +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                '}';
    }
}


