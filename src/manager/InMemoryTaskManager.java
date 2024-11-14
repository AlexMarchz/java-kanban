package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import status.Status;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private InMemoryHistoryManager history;
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Task> tasks = new HashMap<>();

    private int generatorId = 0;

    public InMemoryTaskManager(InMemoryHistoryManager history) {
        this.history = history;
    }

    @Override
    public void addTask(Task task) {
        int newId = ++generatorId;
        task.setId(newId);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        int newId = ++generatorId;
        subTask.setId(newId);
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            epic.setSubTaskIds(newId);
            subTasks.put(subTask.getId(), subTask);
            updateEpicStatus(epic);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        int newId = ++generatorId;
        epic.setId(newId);
        epics.put(epic.getId(), epic);
    }

    private void updateSubTask(SubTask subTask) {
        if ((subTask == null) || (!subTasks.containsKey(subTask.getId()))) {
            return;
        }
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            return;
        }
        subTasks.replace(subTask.getId(), subTask);
        updateEpicStatus(epic);
    }

    private void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic actualEpic = epics.get(epic.getId());
            actualEpic.setName(epic.getName());
            actualEpic.setDescriptions(epic.getDescriptions());
            epics.replace(epic.getId(), epic);
        }
    }

    private void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    private void updateEpicStatus(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }
        List<SubTask> epicSubTasks = new ArrayList<>();
        int countNew = 0;
        int countDone = 0;
        for (int i = 0; i < epic.getSubTaskIds().size(); i++) {
            epicSubTasks.add(subTasks.get(epic.getSubTaskIds().get(i)));
        }
        for (SubTask subtask : epicSubTasks) {
            if (subtask.getStatus().equals(Status.DONE)) {
                countDone++;
            } else if (subtask.getStatus().equals(Status.NEW)) {
                countNew++;
            } else {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
            if (countNew == epicSubTasks.size()) {
                epic.setStatus(Status.NEW);
            } else if (countDone == epicSubTasks.size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    @Override
    public void removeSubTaskById(Integer id) {
        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            Epic epic = epics.get(subTask.getEpicId());
            epic.removeSubTask(id);
            updateEpicStatus(epic);
            subTasks.remove(id);
        } else {
            System.out.println("SubTask is null");
        }
    }

    @Override
    public void removeEpicById(int id) {
        if (!epics.containsKey(id)) {
            return;
        }

        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subTask : epic.getSubTaskIds()) {
                subTasks.remove(subTask);
            }
        } else {
            System.out.println("Epic is null");
        }

        epics.remove(id);
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void clearSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            updateEpicStatus(epic);
        }
    }

    @Override
    public void clearEpics() {
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public List<SubTask> findAllSubtaskByEpicId(int id) {
        if (!epics.containsKey(id)) {
            return new ArrayList<>();
        }

        Epic epic = epics.get(id);
        if (epic != null) {
            ArrayList<SubTask> tasks = new ArrayList<>();
            for (Integer subtaskId : epic.getSubTaskIds()) {
                tasks.add(subTasks.get(subtaskId));
            }
            return tasks;
        } else {
            System.out.println("Epic is null");
        }
        return new ArrayList<>();
    }

    @Override
    public SubTask findSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        history.add(subTask);
        return subTask;
    }

    @Override
    public Epic findEpicById(int id) {
        Epic epic = epics.get(id);
        history.add(epic);
        return epic;
    }

    @Override
    public Task findTaskById(int id) {
        Task task = tasks.get(id);
        history.add(task);
        return task;
    }

    @Override
    public List<SubTask> showAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> showAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Task> showAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }
}