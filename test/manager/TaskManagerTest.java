package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import status.Status;
import exceptions.ManagerSaveException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() throws IOException {
        taskManager = createTaskManager();
        initTasks();
    }

    protected void initTasks() {
        task = new Task("Описание таска", "Наименование таска", Status.IN_PROGRESS);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(20));

        epic = new Epic("Описание эпика", "Наименование эпика", Status.IN_PROGRESS);

        subTask = new SubTask("Описание сабтаска", "Наименование сабтаска", Status.IN_PROGRESS, 2);
        subTask.setStartTime(LocalDateTime.now().plusHours(1));
        subTask.setDuration(Duration.ofMinutes(20));
    }


    @Test
    void addTask() {
        taskManager.addTask(task);
        assertNotNull(taskManager.showAllTasks());
        assertEquals(task, taskManager.showAllTasks().getFirst());
        assertEquals(1, taskManager.showAllTasks().size());
    }

    @Test
    void addEpic() {
        taskManager.addEpic(epic);
        assertNotNull(taskManager.showAllEpics());
        assertEquals(epic, taskManager.showAllEpics().getFirst());
        assertEquals(1, taskManager.showAllEpics().size());
    }

    @Test
    void addSubTask() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        assertEquals(subTask.getEpicId(), epic.getId());
        assertNotNull(taskManager.showAllSubTasks());
        assertEquals(subTask, taskManager.showAllSubTasks().getFirst());
        assertEquals(1, taskManager.showAllSubTasks().size());
    }

    @Test
    void findTaskById() {
        taskManager.addTask(task);
        assertEquals(task, taskManager.findTaskById(task.getId()));
    }

    @Test
    void findEpicById() {
        taskManager.addEpic(epic);
        assertEquals(epic, taskManager.findEpicById(epic.getId()));
    }

    @Test
    void findSubTaskById() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        assertEquals(subTask, taskManager.findSubTaskById(subTask.getId()));
    }

    @Test
    void checkOfRemovingOfTask() {
        taskManager.addTask(task);
        assertNotNull(taskManager.findTaskById(task.getId()));
        taskManager.removeTaskById(task.getId());
        assertEquals(0, taskManager.showAllTasks().size());
    }

    @Test
    void checkOfRemovingOfSubTask() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addTask(subTask);
        taskManager.removeSubTaskById(subTask.getId());
        assertEquals(0, taskManager.showAllSubTasks().size());
    }

    @Test
    void checkOfRemovingOfEpic() {
        taskManager.addEpic(epic);
        assertNotNull(taskManager.findEpicById(epic.getId()));
        taskManager.removeEpicById(epic.getId());
        assertEquals(0, taskManager.showAllEpics().size());
    }

    @Test
    void shouldEqualsTasksWithSameId() {
        Task task1 = new Task("Описание 1", "Наименование 1");
        Task task2 = new Task("Описание 2", "Наименование 2");
        task2.setId(task1.getId());
        assertEquals(task1, task2);
    }

    @Test
    void testClearTasks() {
        taskManager.addTask(task);
        taskManager.clearTasks();
        assertTrue(taskManager.showAllTasks().isEmpty());
    }

    @Test
    void testClearEpics() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        taskManager.clearEpics();
        assertTrue(taskManager.showAllSubTasks().isEmpty());
        assertTrue(taskManager.showAllEpics().isEmpty());
    }

    @Test
    void testClearSubtask() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        taskManager.clearSubTasks();
        assertTrue(epic.getSubTaskIds().isEmpty());
        assertTrue(taskManager.showAllSubTasks().isEmpty());
    }

    @Test
    void checkOfRemovingOfTaskById() {
        taskManager.addTask(task);
        taskManager.removeTaskById(task.getId());
        assertEquals(0, taskManager.showAllTasks().size());
    }

    @Test
    void checkOfRemovingOfEpicById() {
        taskManager.addEpic(epic);
        taskManager.removeEpicById(epic.getId());
        assertEquals(0, taskManager.showAllEpics().size());
    }

    @Test
    void checkOfRemovingOfSubTaskById() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        taskManager.removeSubTaskById(subTask.getId());
        assertEquals(0, taskManager.showAllSubTasks().size());
    }

    @Test
    void testFindAllSubtasksByValidEpicId() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        assertEquals(taskManager.showAllSubTasks(), taskManager.findAllSubtaskByEpicId(epic.getId()));
    }

    @Test
    void testShowAllSubTasks() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        assertEquals(1, taskManager.showAllSubTasks().size());
    }

    @Test
    void testShowAllTasks() {
        taskManager.addTask(task);
        assertEquals(1, taskManager.showAllTasks().size());
    }

    @Test
    void testShowAllEpics() {
        taskManager.addEpic(epic);
        assertEquals(1, taskManager.showAllEpics().size());
    }

    @Test
    void checkOfPriority() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        subTask.setStartTime(LocalDateTime.now().minusMinutes(60));
        subTask.setDuration(Duration.ofMinutes(15));
        subTask.setStatus(Status.NEW);
        taskManager.addSubTask(subTask);
        assertEquals(List.of(subTask, task), taskManager.getPriorityTasks());
    }

    @Test
    void checkForIntersection() {
        epic.setStartTime(LocalDateTime.of(2024, 8, 25, 18, 0));
        epic.setDuration(Duration.ofMinutes(30));
        taskManager.addEpic(epic);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(60));
        taskManager.addTask(task);
        subTask.setStartTime(LocalDateTime.now().plusMinutes(30));
        subTask.setDuration(Duration.ofMinutes(60));
        assertThrows(ManagerSaveException.class, () -> taskManager.addSubTask(subTask));
    }

    @Test
    void checkOfHistory() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        taskManager.findEpicById(epic.getId());
        taskManager.findTaskById(task.getId());
        assertEquals(List.of(epic, task), taskManager.getHistory());
    }

    @Test
    void shouldUpdateSubTask() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);

        SubTask updatedSubTask = new SubTask("Обновленное описание", "Обновленное название", Status.DONE, epic.getId());
        updatedSubTask.setId(subTask.getId());
        updatedSubTask.setStartTime(subTask.getStartTime().plusHours(2));
        updatedSubTask.setDuration(Duration.ofMinutes(30));

        taskManager.updateSubTask(updatedSubTask);

        SubTask result = taskManager.findSubTaskById(subTask.getId());
        assertNotNull(result);
        assertEquals(updatedSubTask.getName(), result.getName());
        assertEquals(updatedSubTask.getDescriptions(), result.getDescriptions());
        assertEquals(updatedSubTask.getStatus(), result.getStatus());
        assertEquals(updatedSubTask.getStartTime(), result.getStartTime());
        assertEquals(updatedSubTask.getDuration(), result.getDuration());
    }

    @Test
    void shouldUpdateEpic() {
        taskManager.addEpic(epic);

        Epic updatedEpic = new Epic("Обновленное описание эпика", "Обновленное название эпика", Status.DONE);
        updatedEpic.setId(epic.getId());

        taskManager.updateEpic(updatedEpic);

        Epic result = taskManager.findEpicById(epic.getId());
        assertNotNull(result);
        assertEquals(updatedEpic.getName(), result.getName());
        assertEquals(updatedEpic.getDescriptions(), result.getDescriptions());
    }

    @Test
    void shouldUpdateTask() {
        taskManager.addTask(task);

        Task updatedTask = new Task("Обновленное описание задачи", "Обновленное название задачи", Status.DONE);
        updatedTask.setId(task.getId());
        updatedTask.setStartTime(task.getStartTime().plusHours(2));
        updatedTask.setDuration(Duration.ofMinutes(45));

        taskManager.updateTask(updatedTask);

        Task result = taskManager.findTaskById(task.getId());
        assertNotNull(result);
        assertEquals(updatedTask.getName(), result.getName());
        assertEquals(updatedTask.getDescriptions(), result.getDescriptions());
        assertEquals(updatedTask.getStatus(), result.getStatus());
        assertEquals(updatedTask.getStartTime(), result.getStartTime());
        assertEquals(updatedTask.getDuration(), result.getDuration());
    }
}