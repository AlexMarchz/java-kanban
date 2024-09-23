package testManager;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import data.Epic;
import data.SubTask;
import data.Task;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager;
    Epic epic;
    SubTask subTask;
    Task task;

    @BeforeEach
    void beforeEach() {
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


}