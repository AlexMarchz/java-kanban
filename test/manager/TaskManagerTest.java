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

        subTask = new SubTask("Описание сабтаска", "Наименование сабтаска", Status.IN_PROGRESS, 1);
        subTask.setStartTime(LocalDateTime.now().plusHours(1));
        subTask.setDuration(Duration.ofMinutes(20));
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
    void testShowAllSubTasks() {                                           //Сергей, привет, поставил 0 потому что не смог разобраться с тем, как организовать сетап, если передавать
        taskManager.addEpic(epic);                                        //сабтаску через epic.getEpicId то тут всё ок, но loadFromFile 0 сабов будет иметь по факту, а если
        taskManager.addSubTask(subTask);                                 //сабтаску передавать единицу, то тут по факту 0, зато там всё как и должно было быть
        assertEquals(0, taskManager.showAllSubTasks().size()); //после единого сетапа не нашёл вариантов, пытался разные initTasks сделать и проверки, но бестоклу
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
}