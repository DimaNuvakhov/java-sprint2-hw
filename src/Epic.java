import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private HashMap<String, SubTask> subTasks;

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        subTasks = new HashMap<>();
    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
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

        for (SubTask tasks : subTasks.values()) {

            detail.
                    append("\n").
                    append(verticalTableBorder).
                    append(Manager.padLeft(tasks.getName(), 20)).
                    append(verticalTableBorder).
                    append(Manager.padLeft(tasks.getDescription(), 50)).
                    append(verticalTableBorder).
                    append(Manager.padLeft(tasks.getStatus().toString(), 20)).
                    append(verticalTableBorder);
        }
        return title + detail + "\n" + horizontalTableBorder;
    }
}

