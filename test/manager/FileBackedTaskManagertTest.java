package manager;
import data.Epic;
import data.SubTask;
import data.Task;
import org.junit.jupiter.api.*;
import status.Status;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

class FileBackedTaskManagerTest {

    private File file;
    private Task task;
    private Epic epic;
    private SubTask subTask;
    private FileBackedTaskManager fileBackedTaskManager;

    @BeforeEach
    void setUp() throws IOException {
        file = File.createTempFile("test", ".csv");
        task = new Task("имяТаска", "описаниеТаска", Status.IN_PROGRESS);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(20));
        epic = new Epic("имяЭпика", "описаниеЭпика", Status.IN_PROGRESS);
        subTask = new SubTask("имяСабТаска", "описаниеСабТаска", Status.IN_PROGRESS, 1);
        subTask.setStartTime(LocalDateTime.now().plusHours(1));
        subTask.setDuration(Duration.ofMinutes(20));
        fileBackedTaskManager = new FileBackedTaskManager(file);
    }

    @Test
    void testLoadFromFile() {
        fileBackedTaskManager.addTask(task);
        fileBackedTaskManager.addEpic(epic);
        fileBackedTaskManager.addSubTask(subTask);

        assertEquals(1, fileBackedTaskManager.tasks.size());
        assertEquals(1, fileBackedTaskManager.epics.size());
        assertEquals(1, fileBackedTaskManager.subTasks.size());

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