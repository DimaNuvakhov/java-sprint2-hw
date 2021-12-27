public class SubTask extends Task{
    private Epic epic;

    public SubTask(String name, String description, TaskStatus status, Epic epic, Manager manager) {
        super(name, description, status, manager);
        this.epic = epic;
        epic.addSubTask(this);
    }

    @Override
    public void delete() {
//        epic.deleteSubTask(this);
//        getManager().deleteSubTask(this);
        System.out.println("Удаляем SubTask");
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


