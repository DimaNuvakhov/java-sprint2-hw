import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Epic extends Task {
    private final HashMap<String, SubTask> subTasks;

    public HashMap<String, SubTask> getSubTasks() {
        return subTasks;
    }

    public Epic(String name, String description, TaskStatus status, Manager manager) {
        super(name,
                description,
                status, // TODO здесь мы вынуждены что-то передать, потому что вызываем конструктор родителя. Передаем туда null
                manager
        );
        subTasks = new HashMap<>();
    }

    public void deleteSubTask(SubTask subTask) {
        subTasks.remove(subTask.getId());
    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
    }

    //@Override
    //public TaskStatus getStatus(SubTask subTask) {

    //}

    @Override
    public TaskStatus getStatus() { // TODO переопределили метод GEt и в нем будем вычислять значение статуса по алгоритму
        TaskStatus taskStatus = null;
        // TODO здесь должен быть код вычисления статуса эпика по статусам подзадач согласно алгоритму
        // нужно в цикле пройтись по всем подзадачам, подсчитать каких статусов сколько
        // потом уже после цикла с помощью условий вычислить правильный статус и засунуть его в taskStatus
        return taskStatus;
    }

    @Override
    public void delete() {
        ArrayList<String> toRemove = new ArrayList(0);
        for (SubTask subTask : subTasks.values()) {
            toRemove.add(subTask.getId());
        }
        for (String id : toRemove) {
            subTasks.get(id).delete();
            subTasks.remove(id);
        }
        getManager().deleteTask(this);
        System.out.println("Удаляем Epic, id = " + this.getId());
    }

    public String showSubTaskList() {
        StringBuilder value = new StringBuilder();
        for (SubTask subTask : subTasks.values()) {
            value.append(subTask.toString()).append("\n");
        }
        return value.toString();
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

