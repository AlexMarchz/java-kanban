package data;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subTaskIds;

    public Epic(String taskDescription, String taskName) {
        super(taskDescription, taskName);
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
}
