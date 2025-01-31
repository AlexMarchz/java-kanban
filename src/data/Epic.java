package data;

import java.util.ArrayList;
import status.Status;
import manager.TaskType;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subTaskIds;
    private LocalDateTime endTime;

    public Epic(String taskDescription, String taskName, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(taskDescription, taskName, duration, startTime);
        this.endTime = endTime;
        subTaskIds = new ArrayList<>();
    }

    public Epic(String description, String name) {
        super(description, name);
        subTaskIds = new ArrayList<>();
    }

    public Epic(String name, String descriptions, Status status) {
        super(name, descriptions, status);
        subTaskIds = new ArrayList<>();
    }

    public Epic(int id, String name, Status status, String descriptions, LocalDateTime startTime,
                LocalDateTime endTime, Duration duration) {
        super(id, name, status, descriptions, startTime, duration);
        subTaskIds = new ArrayList<>();
        this.endTime = endTime;
    }

    public void setSubTaskIds(int id) {
        subTaskIds.add(id);
    }

    public List<Integer> getSubTaskIds() {
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
