package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import status.Status;

import java.util.ArrayList;

public interface TaskManager {
    void addTask(Task task);

    void addSubTask(SubTask subTask);

    void addEpic(Epic epic);

    void updateEpicStatus(Epic epic);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void updateTask(Task task);

    void removeSubTaskById(Integer id);

    void removeEpicById(int id);

    void removeTaskById(int id);

    void clearSubTasks();

    void clearEpics();

    void clearTasks();
}
