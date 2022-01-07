public class SubTask extends Task {
    private Epic epic;

    public SubTask(String name, String description, TaskStatus status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        epic.getSubTasks().put(this.getId(), this);
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", epic=" + epic.getName() +
                '}';
    }
}



