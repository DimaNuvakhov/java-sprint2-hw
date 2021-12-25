public class SubTask extends Task{
    private Epic epic;

    public SubTask(String name, String description, TaskStatus status) {
        super(name, description, status);
        epic = new Epic(name, description, status);
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
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
                ", epic=" + getEpic() +
                '}';
    }
}
