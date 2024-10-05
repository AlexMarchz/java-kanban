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

    ArrayList<SubTask> findAllSubtaskByEpicId(int id);

    void removeSubTaskById(Integer id);

    void removeEpicById(int id);

    void removeTaskById(int id);

    void clearSubTasks();

    void clearEpics();

    void clearTasks();

    SubTask findSubTaskById(int id);

    Epic findEpicById(int id);

    Task findTaskById(int id);

    ArrayList<SubTask> showAllSubTasks();

    ArrayList<Epic> showAllEpics();

    ArrayList<Task> showAllTasks();

    ArrayList<Task> getHistory();
}
