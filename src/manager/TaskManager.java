package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import java.util.List;

public interface TaskManager {

    List<Task> getPriorityTasks();

    void addTask(Task task);

    void addSubTask(SubTask subTask);

    void addEpic(Epic epic);

    List<SubTask> findAllSubtaskByEpicId(int id);

    void removeSubTaskById(Integer id);

    void removeEpicById(int id);

    void removeTaskById(int id);

    void clearSubTasks();

    void clearEpics();

    void clearTasks();

    SubTask findSubTaskById(int id);

    Epic findEpicById(int id);

    Task findTaskById(int id);

    List<SubTask> showAllSubTasks();

    List<Epic> showAllEpics();

    List<Task> showAllTasks();

    List<Task> getHistory();
}
