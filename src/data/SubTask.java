package data;

import manager.TaskType;
import status.Status;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final int epicSubTasksId;

    public SubTask(String descriptions, String name, Duration duration, LocalDateTime startTime, int epicSubTasksId) {
        super(descriptions, name,  duration, startTime);
        this.epicSubTasksId = epicSubTasksId;
    }

    public SubTask(String name, String descriptions, Status status, int epicSubTasksId) {
        super(name, descriptions, status);
        this.epicSubTasksId = epicSubTasksId;
    }

    public SubTask(int id, String name, Status status, String description, int epicSubTasksId, LocalDateTime startTime, Duration duration) {
        super(id, name, status, description, startTime, duration);
        this.epicSubTasksId = epicSubTasksId;
    }

    public SubTask(String taskDescription, String taskName, int epicSubTasksId) {
        super(taskDescription, taskName);
        this.epicSubTasksId = epicSubTasksId;
    }

    public int getEpicId() {
        return epicSubTasksId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }


}
