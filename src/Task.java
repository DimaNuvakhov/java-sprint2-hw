import java.util.UUID;

public class Task {
    private String id;
    private String name;
    private String description;
    private TaskStatus status;
    private Manager manager;

    public Task(String name, String description, TaskStatus status, Manager manager) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = UUID.randomUUID().toString().substring(0, 32);
        this.manager = manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Manager getManager() {
        return manager;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void delete() {
        getManager().deleteTask(this);
        System.out.println("Удаляем Task, id = " + this.getId());
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}


