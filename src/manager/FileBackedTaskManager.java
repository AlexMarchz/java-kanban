package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import status.Status;
import exceptions.ManagerSaveException;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final String HEADER = "id,type,name,description, epic\n";

    public FileBackedTaskManager(File file) {
        super(new InMemoryHistoryManager());
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                Task task = taskFromString(line);
                if (task.getType() == TaskType.EPIC) {
                    fileManager.epics.put(task.getId(), (Epic) task);
                } else if (task.getType() == TaskType.SUBTASK) {
                    fileManager.subTasks.put(task.getId(), (SubTask) task);
                    fileManager.epics.get(((SubTask) task).getEpicId()).setSubTaskIds(task.getId());
                } else {
                    fileManager.tasks.put(task.getId(), task);
                }
                fileManager.nextId = Math.max(fileManager.nextId, task.getId());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось загрузить данные из файла");
        }
        return fileManager;
    }

    public void save() {
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException io) {
            throw new ManagerSaveException("Не удалось найти файл");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bufferedWriter.write(HEADER);
            for (Task task : showAllTasks()) {
                bufferedWriter.write(taskToString(task));
            }
            for (Epic epic : showAllEpics()) {
                bufferedWriter.write(taskToString(epic));
            }
            for (SubTask subTask : showAllSubTasks()) {
                bufferedWriter.write(taskToString(subTask));
            }
        } catch (IOException io) {
            throw new ManagerSaveException("Не удалось сохранить файл");
        }

    }

    private static String taskToString(Task task) {
        return task.getId() + "," +
                task.getType() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescriptions() +
                getEpicId(task) + "\n";

    }

    private static Task taskFromString(String value) {
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

    /*private static Task taskFromString(String value) {
        String[] lines = value.split(",");

        if (lines.length < 5) {
            throw new IllegalArgumentException("Некорректное количество элементов в строке");
        }

        int id = Integer.parseInt(lines[0]);
        String taskType = lines[1];
        String name = lines[2];
        Status status = Status.valueOf(lines[3]);
        String description = lines[4];

        switch (taskType) {
            case "EPIC":
                return new Epic(id, name, status, description);
            case "SUBTASK":
                int epicId = Integer.parseInt(lines[5]);
                return new SubTask(id, name, status, description, epicId);
            default:
                return new Task(id, name, status, description);
        }
    } */

    private static String getEpicId(Task task) {
        switch (task.getType()) {
            case SUBTASK:
                return "," + ((SubTask) task).getEpicId();
            default:
                return "";
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubTaskById(Integer id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }
}
