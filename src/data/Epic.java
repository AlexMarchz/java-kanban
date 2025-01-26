package data;

import java.util.ArrayList;
import status.Status;
import manager.TaskType;

public class Epic extends Task {
    private final ArrayList<Integer> subTaskIds;

    public Epic(String description, String name) {
        super(description, name);
        subTaskIds = new ArrayList<>();
    }
    public Epic(String name, String descriptions, Status status) {
        super(name, descriptions, status);
        subTaskIds = new ArrayList<>();
    }

    public Epic(int id, String name, Status status, String descriptions) {
        super(id, name, status, descriptions);
        subTaskIds = new ArrayList<>();
    }


    public void setSubTaskIds(int id) {
        subTaskIds.add(id);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void removeSubTask(Integer id) {
        subTaskIds.remove(id);
    }

    public void clearSubTasks() {
        subTaskIds.clear();
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }
}
