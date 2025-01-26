package manager;
import data.Epic;
import data.SubTask;
import data.Task;
import org.junit.jupiter.api.*;
import status.Status;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

class FileBackedTaskManagerTest {

    private File file;
    private Task task;
    private Epic epic;
    private SubTask subTask;

    @BeforeEach
    void setUp() throws IOException {
        file = File.createTempFile("test", ".csv");
        task = new Task("имяТаска", "описаниеТаска", Status.IN_PROGRESS);
        epic = new Epic("имяЭпика", "описаниеЭпика", Status.IN_PROGRESS);
        subTask = new SubTask("имяСабТаска", "описаниеСабТаска", Status.IN_PROGRESS, 2);
    }

    /*@Test
    void testLoadFromFile() {
        // Arrange: Add tasks to FileBackedTaskManager
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.addTask(task);
        fileBackedTaskManager.addEpic(epic);
        fileBackedTaskManager.addSubTask(subTask);

        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);
    }

    @Test
    void loadFromEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("emptyTest", ".csv");
        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile(emptyFile);

        assertNotNull(loadFile);
        assertTrue(loadFile.epics.isEmpty());
        assertTrue(loadFile.subTasks.isEmpty());
        assertTrue(loadFile.tasks.isEmpty());
    } */

    @Test
    void testLoadFromFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        fileBackedTaskManager.addTask(task);
        fileBackedTaskManager.addEpic(epic);
        fileBackedTaskManager.addSubTask(subTask);

        assertEquals(1, fileBackedTaskManager.tasks.size());
        assertEquals(1, fileBackedTaskManager.epics.size());
        assertEquals(0, fileBackedTaskManager.subTasks.size()); // не возвращается сабтаск

        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(fileBackedTaskManager.showAllTasks(), fileManager.showAllTasks());
        assertEquals(fileBackedTaskManager.showAllEpics(), fileManager.showAllEpics());
        assertEquals(fileBackedTaskManager.showAllSubTasks(), fileManager.showAllSubTasks());
    }

    @Test
    void loadFromEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("emptyTest", ".csv");
        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile(emptyFile);

        assertNotNull(loadFile);
        assertEquals(0, loadFile.epics.size());
        assertEquals(0, loadFile.subTasks.size());
        assertEquals(0, loadFile.tasks.size());
    }
}