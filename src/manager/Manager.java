package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import status.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Task> tasks = new HashMap<>();

    private int generatorId = 0;

    public void addTask(Task task) {
        int newId = ++generatorId;
        task.setId(newId);
        tasks.put(task.getId(), task);
    }

    public void addSubTask(SubTask subTask) {
        int newId = ++generatorId;
        subTask.setId(newId);
        Epic epic = epics.get(subTask.getEpicId());
        epic.setSubTaskIds(newId);
        subTasks.put(subTask.getId(), subTask);
        updateEpicStatus(epic);
    }

    public void addEpic(Epic epic) {
        int newId = ++generatorId;
        epic.setId(newId);
        epics.put(epic.getId(), epic);
    }

    private void updateEpicStatus(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }
        ArrayList<SubTask> epicSubTasks = new ArrayList<>();
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

    public void updateSubTask(SubTask subTask) {
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

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic actualEpic = epics.get(epic.getId());
            actualEpic.setName(epic.getName());
            actualEpic.setDescriptions(epic.getDescriptions());
            epics.replace(epic.getId(), epic);
        }
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

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

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void clearSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            updateEpicStatus(epic);
        }
    }

    public void clearEpics() {
        subTasks.clear();
        epics.clear();
    }

    public void clearTasks() {
        tasks.clear();
    }

    public ArrayList<SubTask> findAllSubtaskByEpicId(int id) {
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

    public SubTask findSubTaskById(int id) {
        return subTasks.get(id);
    }

    public Epic findEpicById(int id) {
        return epics.get(id);
    }

    public Task findTaskById(int id) {
        return tasks.get(id);
    }

    public ArrayList<SubTask> showAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> showAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Task> showAllTasks() {
        return new ArrayList<>(tasks.values());
    }
}