package data;

import manager.TaskType;
import status.Status;

public class SubTask extends Task {

    private final int epicSubTasksId;

    public SubTask(String descriptions, String name, int epicSubTasksId) {
        super(descriptions, name);
        this.epicSubTasksId = epicSubTasksId;
    }

    public SubTask(String name, String descriptions, Status status, int epicSubTasksId) {
        super(name, descriptions, status);
        this.epicSubTasksId = epicSubTasksId;
    }

    public SubTask(int id, String name, Status status, String description, int epicSubTasksId) {
        super(id, name, status, description);
        this.epicSubTasksId = epicSubTasksId;
    }

    public int getEpicId() {
        return epicSubTasksId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }


}
