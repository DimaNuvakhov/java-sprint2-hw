public class SubTask extends Task {
    private final String epicId;

    public SubTask(String name, String description, TaskStatus status, String epicId) {
        super(name, description, status);
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
                '}';
    }
}



