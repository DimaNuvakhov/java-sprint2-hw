public class SubTask extends Task{
    private Epic epic;

    public SubTask(String name, String description, TaskStatus status, Epic epic) {
        super(name, description, status);
        epic.addSubTask(this);
        this.epic = epic;
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
                ", epic=" + getEpic().getName() +
                '}';
    }

    public String toString2() {
        return "SubTask{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", epic=" + getEpic().getName() +
                '}';
    }

}


