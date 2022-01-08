import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<String, SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description, null);
        subTasks = new HashMap<>();
    }

    public HashMap<String, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public TaskStatus getStatus() {
        TaskStatus taskStatus = null;
        int newStatusNumber = 0;
        int inProgressStatusNumber = 0;
        int doneStatusNumber = 0;
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getStatus().toString().equals("NEW")) {
                newStatusNumber = newStatusNumber + 1;
            } else if (subTask.getStatus().toString().equals("IN_PROGRESS")) {
                inProgressStatusNumber = inProgressStatusNumber + 1;
            } else if (subTask.getStatus().toString().equals("DONE")) {
                doneStatusNumber = doneStatusNumber + 1;
            }
        }
        if (subTasks.size() == 0 || subTasks.size() == newStatusNumber) {
            return TaskStatus.NEW;
        } else if (subTasks.size() == doneStatusNumber) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        StringBuilder detail = new StringBuilder();
        String verticalTableBorder = "|";
        String horizontalTableBorder = "-------------------------------------"
                + "---------------------------------------------------------------------------------------------";
        String table = verticalTableBorder + Manager.padLeft("<id>", 35) + verticalTableBorder
                + Manager.padLeft("<Название>", 20)
                + verticalTableBorder + (Manager.padLeft("<Описание>", 50) + verticalTableBorder)
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
                    append(Manager.padLeft(tasks.getId(), 35)).
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


