package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import exceptions.ManagerSaveException;

import java.io.*;

import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final String HEADER = "id,type,name,description,start,end,duration,epic\n";

    public FileBackedTaskManager(File file) {
        super(Managers.getDefaultHistory());
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                Task task = TaskUtils.taskFromString(line);
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

    private void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bufferedWriter.write(HEADER);
            for (Task task : showAllTasks()) {
                bufferedWriter.write(TaskUtils.taskToString(task));
            }
            for (Epic epic : showAllEpics()) {
                bufferedWriter.write(TaskUtils.taskToString(epic));
            }
            for (SubTask subTask : showAllSubTasks()) {
                bufferedWriter.write(TaskUtils.taskToString(subTask));
            }
        } catch (IOException io) {
            throw new ManagerSaveException("Не удалось сохранить файл");
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
