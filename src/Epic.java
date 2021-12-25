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
        System.out.println();
        String detail = "";
        String verticalTableBorder = "|";
        String horizontalTableBorder = "-------------------------------------"
                + "---------------------------------------------------------";;
        String table = verticalTableBorder + padLeft("<Название>", 20) + verticalTableBorder +
                (padLeft("<Описание>", 50) + verticalTableBorder)
                + (padLeft("<Статус>", 20) + verticalTableBorder);
        String title = "Epic{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", subTasks=" + subTasks.size() +
                '}' + "\n" + horizontalTableBorder + "\n" + table + "\n" + horizontalTableBorder;

        for (SubTask tasks : subTasks.values()) {

            detail = detail + "\n" + verticalTableBorder + padLeft(tasks.getName(), 20) + verticalTableBorder
                    + padLeft(tasks.getDescription(), 50) +  verticalTableBorder
                    + padLeft(tasks.getStatus().toString(), 20) + verticalTableBorder;
        }
        return title + detail + "\n" + horizontalTableBorder;
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}

