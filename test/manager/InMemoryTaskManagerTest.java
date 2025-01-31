package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import status.Status;
import exceptions.ManagerSaveException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager;
    Epic epic;
    SubTask subTask;
    Task task;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        epic = new Epic("Описание эпической задачи", "Наименование эпической задачи");
        subTask = new SubTask("Описание сабтаска", "Наименование сабтаска", epic.getId());
        subTask.setStartTime(LocalDateTime.now().plusHours(1));
        subTask.setDuration(Duration.ofMinutes(20));
        task = new Task("Описание таска", "Наименование таска");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(20));
    }

    @Test
    void shouldAddTasksAndFindById() {
        taskManager.addTask(task);

        assertNotNull(taskManager.findTaskById(task.getId()));
        assertNotNull(taskManager.showAllTasks());

    }

    @Test
    void shouldAddEpicAndFindById() {
        taskManager.addEpic(epic);

        assertNotNull(taskManager.findEpicById(epic.getId()));
        assertNotNull(taskManager.showAllEpics());
    }

    @Test
    void shouldAddSubtaskAndFindById() {
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("Описание", "Наименование", epic.getId());
        taskManager.addSubTask(subTask1);

        assertNotNull(taskManager.findSubTaskById(subTask1.getId()));
        assertNotNull(taskManager.showAllSubTasks());
    }

    @Test
    void shouldEqualsTasksWithSameId() {
        Task task1 = new Task("Описание 1", "Наименование 1");
        Task task2 = new Task("Описание 2", "Наименование 2");

        task2.setId(task1.getId());assertEquals(task1, task2);
    }

    @Test
    void testClearTasks() {
        taskManager.addTask(task);
        taskManager.clearTasks();

        List<Task> task = taskManager.showAllTasks();
        assertTrue(task.isEmpty());
    }

    @Test
    void testClearEpics() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        taskManager.clearEpics();

        List<SubTask> subTasks = taskManager.showAllSubTasks();
        assertTrue(subTasks.isEmpty());

        List<Epic> epic = taskManager.showAllEpics();
        assertTrue(epic.isEmpty());
    }

    @Test
    public void testClearSubtask() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);

        taskManager.clearSubTasks();

        List<Integer> subTasksIds = epic.getSubTaskIds();
        assertTrue(subTasksIds.isEmpty());

        List<SubTask> subTasks = taskManager.showAllSubTasks();
        assertTrue(subTasks.isEmpty());
    }

    @Test
    void checkOfRemovingOfTaskById() {
        taskManager.addTask(task);
        assertNotNull(taskManager.findTaskById(task.getId()));

        taskManager.removeTaskById(task.getId());
        assertEquals(0, taskManager.showAllTasks().size());
    }

    @Test
    void checkOfRemovingOfEpicById() {
        taskManager.addEpic(epic);
        assertNotNull(taskManager.findEpicById(epic.getId()));

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
    public void testFindAllSubtasksByValidEpicId() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);

        assertEquals(taskManager.showAllSubTasks(), taskManager.findAllSubtaskByEpicId(epic.getId()));
    }

    @Test
    public void testShowAllSubTasks() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);

        List<SubTask> actual = taskManager.showAllSubTasks();
        int expectedSize = 1;

        assertEquals(expectedSize, actual.size());
    }

    @Test
    public void testShowAllTasks() {
        taskManager.addTask(task);

        List<Task> actual = taskManager.showAllTasks();
        int expectedSize = 1;

        assertEquals(expectedSize, actual.size());
    }

    @Test
    public void testShowAllEpics() {
        taskManager.addEpic(epic);

        List<Epic> actual = taskManager.showAllEpics();
        int expectedSize = 1;

        assertEquals(expectedSize, actual.size());
    }

    @Test
    void checkOfPriority() {
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(20));
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        subTask.setStartTime(LocalDateTime.now().minusMinutes(60));
        subTask.setDuration(Duration.ofMinutes(15));
        subTask.setStatus(Status.NEW);
        taskManager.addSubTask(subTask);
        assertEquals(taskManager.getPriorityTasks(), List.of(subTask, task));
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
        assertEquals(taskManager.getHistory(), List.of(epic, task));
    }
}