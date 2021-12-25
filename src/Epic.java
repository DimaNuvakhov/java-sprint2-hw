import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        subTasks = new ArrayList<>();
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
