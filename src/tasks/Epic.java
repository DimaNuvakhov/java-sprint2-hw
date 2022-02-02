package tasks;

import inmemorymanagers.InMemoryManager;

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
    public String toString() {
        StringBuilder detail = new StringBuilder();
        String verticalTableBorder = "|";
        String horizontalTableBorder = "-------------------------------------"
                + "---------------------------------------------------------------------------------------------";
        String table = verticalTableBorder + InMemoryManager.padLeft("<id>", 35) + verticalTableBorder
                + InMemoryManager.padLeft("<Название>", 20)
                + verticalTableBorder + (InMemoryManager.padLeft("<Описание>", 50) + verticalTableBorder)
                + (InMemoryManager.padLeft("<Статус>", 20) + verticalTableBorder);
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
                    append(InMemoryManager.padLeft(tasks.getId(), 35)).
                    append(verticalTableBorder).
                    append(InMemoryManager.padLeft(tasks.getName(), 20)).
                    append(verticalTableBorder).
                    append(InMemoryManager.padLeft(tasks.getDescription(), 50)).
                    append(verticalTableBorder).
                    append(InMemoryManager.padLeft(tasks.getStatus().toString(), 20)).
                    append(verticalTableBorder);
        }
        return title + detail + "\n" + horizontalTableBorder;
    }
}


