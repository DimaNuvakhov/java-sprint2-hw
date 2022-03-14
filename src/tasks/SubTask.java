package tasks;

import java.time.LocalDateTime;

public class SubTask extends Task {
    private final String epicId;

    public SubTask(String name, String description, TaskStatus status, String epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, TaskStatus status, String epicId, LocalDateTime startTime,
                   Integer duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public String getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", epicId=" + getEpicId() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                '}';
    }
}



