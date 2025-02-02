package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import status.Status;
import exceptions.ManagerSaveException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final InMemoryHistoryManager history;
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected Set<Task> priority = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    protected int nextId = 1;

    public InMemoryTaskManager(InMemoryHistoryManager history) {
        this.history = history;
    }

    private int generatorId() {
        return nextId++;
    }

    private void updateEpicTime(Epic epic) {
        List<Task> subTasksList = getSubTasksForEpic(epic);
        if (subTasksList.isEmpty()) {
            return;
        }

        Duration totalDuration = Duration.ofMinutes(0);
        LocalDateTime startTime = subTasksList.getFirst().getStartTime(); // Время начала
        LocalDateTime endTime = subTasksList.getLast().getEndTime(); // Время окончания

        for (Task subTask : subTasksList) {
            if (subTask != null) { // Проверяем null
                totalDuration = totalDuration.plus(subTask.getDuration());
                if (subTask.getStartTime() != null && subTask.getStartTime().isBefore(startTime)) {
                    startTime = subTask.getStartTime(); // Обновляем время
                }
                if (subTask.getEndTime() != null && subTask.getEndTime().isAfter(endTime)) {
                    endTime = subTask.getEndTime(); // Обновляем время
                }
            }
        }

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(totalDuration);
    }

    private List<Task> getSubTasksForEpic(Epic epic) {
        List<Task> subTasks = new ArrayList<>();
        for (Task task : this.getPriorityTasks()) {
            if (task.getType().equals(TaskType.SUBTASK) && ((SubTask) task).getEpicId() == epic.getId()) {
                subTasks.add(task);
            }
        }

        subTasks.sort(Comparator.comparing(Task::getStartTime));
        return subTasks;
    }

    protected void addPriority(Task task) {
        if (task.getType().equals(TaskType.EPIC)) {
            return;  // Не добавляем эпики
        }

        if (task.getStartTime() != null && task.getEndTime() != null) {
            for (Task existingTask : priority) {
                if (existingTask.getId() == task.getId()) {
                    priority.remove(existingTask);  // Удаляем старую версию, если она уже есть
                }
            }
            priority.add(task);  // добавляем задачу
        }
    }

    private boolean checkForIntersection(Task task1, Task task2) {
        return !task1.getEndTime().isBefore(task2.getStartTime()) &&
                !task1.getStartTime().isAfter(task2.getEndTime());
    }

    private void relevancePriority(Task task) {
        if (task == null || task.getStartTime() == null) {
            return;
        }

        for (Task existingTask : priority) {
            if (existingTask == task) {
                continue;
            }
            if (checkForIntersection(task, existingTask)) {
                throw new ManagerSaveException("Задачи " + task.getId() + " и " + existingTask.getId() + " пересекаются");
            }
        }
    }

    @Override
    public List<Task> getPriorityTasks() {
        return new ArrayList<>(priority);
    }

    @Override
    public void addTask(Task task) {
        int newId = generatorId();
        task.setId(newId);
        relevancePriority(task);
        addPriority(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        int newId = generatorId();
        subTask.setId(newId);
        Epic epic = epics.get(subTask.getEpicId());
        relevancePriority(subTask);
        addPriority(subTask);
        if (epic != null) {
            epic.setSubTaskIds(newId);
            subTasks.put(subTask.getId(), subTask);
            updateEpicStatus(epic);
            updateEpicTime(epic);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        int newId = generatorId();
        epic.setId(newId);
        epics.put(epic.getId(), epic);
    }

    protected void updateSubTask(SubTask subTask) {
        if ((subTask == null) || (!subTasks.containsKey(subTask.getId()))) {
            return;
        }
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            return;
        }
        subTasks.replace(subTask.getId(), subTask);
        updateEpicStatus(epic);
        updateEpicTime(epic);
        relevancePriority(epic);
        addPriority(subTask);
    }

    protected void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic actualEpic = epics.get(epic.getId());
            actualEpic.setName(epic.getName());
            actualEpic.setDescriptions(epic.getDescriptions());
            epics.replace(epic.getId(), epic);
        }
    }

    protected void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            relevancePriority(task);
            addPriority(task);
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
            updateEpicTime(epic);
            priority.remove(subTask);
            subTasks.remove(id);
            history.remove(id);
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
                priority.remove(subTasks.get(subTask));
                subTasks.remove(subTask);
                history.remove(subTask);
            }
        } else {
            System.out.println("Epic is null");
        }

        epics.remove(id);
        history.remove(id);
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
        history.remove(id);
    }

    @Override
    public void clearSubTasks() {
        for (SubTask subTask : showAllSubTasks()) { // проходимся по всем сабтаскам
            history.remove(subTask.getId()); // удаляем по айди
            priority.remove(subTask);
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            updateEpicStatus(epic);
            updateEpicTime(epic);
        }
    }

    @Override
    public void clearEpics() {
        for (SubTask subTask : showAllSubTasks()) {
            history.remove(subTask.getId());
            priority.remove(subTask);
        }
        subTasks.clear();
        for (Epic epic : showAllEpics()) {
            history.remove(epic.getId());
        }
        epics.clear();
    }

    @Override
    public void clearTasks() {
        for (Task task : showAllTasks()) {
            history.remove(task.getId());
            priority.remove(task);
        }
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