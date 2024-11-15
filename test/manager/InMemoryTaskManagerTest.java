package manager;

import data.Epic;
import data.SubTask;
import data.Task;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        task = new Task("Описание таска", "Наименование таска");
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

        assertNull(taskManager.findTaskById(task.getId()));
    }

    @Test
    void testClearEpics() {
        taskManager.addEpic(epic);
        taskManager.addTask(subTask);
        taskManager.clearEpics();

        assertNull(taskManager.findEpicById(epic.getId()));
    }

    @Test
    public void testClearSubtask() {
        taskManager.addEpic(epic);
        taskManager.addTask(subTask);

        taskManager.clearSubTasks();

        assertNull(taskManager.findSubTaskById(subTask.getId()));
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
        int expectedSize = 0;

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
}