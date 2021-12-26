import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<String, SubTask> subTasks;

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        subTasks = new HashMap<>();
    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
    }

    public SubTask getAnySubTask() {
        for (SubTask subTask : subTasks.values()) {
            return subTask;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder detail = new StringBuilder();
        String verticalTableBorder = "|";
        String horizontalTableBorder = "-------------------------------------"
                + "---------------------------------------------------------";
        ;
        String table = verticalTableBorder + Manager.padLeft("<Название>", 20) + verticalTableBorder +
                (Manager.padLeft("<Описание>", 50) + verticalTableBorder)
                + (Manager.padLeft("<Статус>", 20) + verticalTableBorder);
        String title = "Epic{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", subTasks=" + subTasks.size() +
                '}' + "\n" + horizontalTableBorder + "\n" + table + "\n" + horizontalTableBorder;

        for (SubTask subTask : subTasks.values()) {

            detail.
                    append("\n").
                    append(verticalTableBorder).
                    append(Manager.padLeft(subTask.getName(), 20)).
                    append(verticalTableBorder).
                    append(Manager.padLeft(subTask.getDescription(), 50)).
                    append(verticalTableBorder).
                    append(Manager.padLeft(subTask.getStatus().toString(), 20)).
                    append(verticalTableBorder);
        }
        return title + detail + "\n" + horizontalTableBorder;
    }
}

