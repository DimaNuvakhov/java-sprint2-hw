public class SubTask extends Task{
    private Epic epic;

    public SubTask(String name, String description, TaskStatus status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        epic.addSubTask(this);
    }

    @Override
    public void delete() {
        System.out.println("== Удаление SubTask id = " + this.getId() + " \n");
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


