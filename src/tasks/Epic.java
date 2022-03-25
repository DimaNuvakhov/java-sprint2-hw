package tasks;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Epic extends Task {
    private HashMap<String, SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description, null, LocalDateTime.now(), 0);
    }

    public HashMap<String, SubTask> getSubTasks() {
        if (subTasks == null) {
            subTasks = new HashMap<>();
        }
        return subTasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", subTasks=" + subTasks.size() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                '}';
    }
}


