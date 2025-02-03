package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import status.Status;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TaskUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy,HH:mm");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    protected static String taskToString(Task task) {
        String startTime = task.getStartTime() != null ? task.getStartTime().format(formatter) : "";
        String endTime = task.getEndTime() != null ? task.getEndTime().format(formatter) : "";
        String duration = task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "";

        return task.getId() + "," +
                task.getType() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescriptions() + "," +
                startTime + "," +
                endTime + "," +
                duration + "," +
                getEpicId(task) + "\n";

    }

    private static String getEpicId(Task task) {
        switch (task.getType()) {
            case SUBTASK:
                return Integer.toString(((SubTask) task).getEpicId());
            default:
                return "";
        }
    }

    protected static Task taskFromString(String value) {
        String[] lines = value.split(",");
        int id = Integer.parseInt(lines[0]);
        TaskType taskType = TaskType.valueOf(lines[1]);
        String name = lines[2];
        Status status = Status.valueOf(lines[3]);
        String description = lines[4];
        LocalDateTime startTime = LocalDateTime.of(LocalDate.parse(lines[5], dateFormatter),
                LocalTime.parse(lines[6], timeFormatter));
        Duration duration = Duration.ofMinutes(Long.parseLong(lines[9]));

        switch (taskType) {
            case EPIC:
                LocalDateTime endEpicTime = LocalDateTime.of(LocalDate.parse(lines[7], dateFormatter),
                    LocalTime.parse(lines[8], timeFormatter));
                return new Epic(id, name, status, description, startTime, endEpicTime, duration);
            case SUBTASK:
                int epicId = Integer.parseInt(lines[10]);
                return new SubTask(id, name, status, description, epicId, startTime, duration);
            case TASK:
            default:
                return new Task(id, name, status, description, startTime, duration);
        }
    }
}


