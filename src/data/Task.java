package data;

import manager.TaskType;
import status.Status;
import java.util.Objects;
import java.time.Duration;
import java.time.LocalDateTime;

public class Task {

    protected Duration duration;
    protected LocalDateTime startTime;
    protected int id;
    protected String name;
    protected Status status;
    protected String descriptions;

    public Task(String description, String name, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.descriptions = description;
        status = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String descriptions, Status status) {
        this.name = name;
        this.descriptions = descriptions;
        this.status = status;
    }

    public Task(int id, String name, Status status, String descriptions, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.descriptions = descriptions;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String description, String name) {
        this.name = name;
        this.descriptions = description;
        status = Status.NEW;
    }

    public LocalDateTime getEndTime() {
        if (duration == null) {
            return startTime;
        }
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public Duration getDuration() {
        return duration;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return id == task.id && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }

    @Override
    public String toString() {
        return "Data.Task{" +
                "descriptions='" + descriptions + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
