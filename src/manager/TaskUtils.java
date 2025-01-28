package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import status.Status;

public class TaskUtils {
    protected static String taskToString(Task task) {
        return task.getId() + "," +
                task.getType() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescriptions() +
                getEpicId(task) + "\n";

    }

    private static String getEpicId(Task task) {
        switch (task.getType()) {
            case SUBTASK:
                return "," + ((SubTask) task).getEpicId();
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

        switch (taskType) {
            case EPIC:
                return new Epic(id, name, status, description);
            case SUBTASK:
                int epicId = Integer.parseInt(lines[5]);
                return new SubTask(id, name, status, description, epicId);
            case TASK:
            default:
                return new Task(id, name, status, description);
        }
    }
}


