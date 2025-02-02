package manager;

import data.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File file;

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(file);
    }

    @BeforeEach
    void setUp() throws IOException {
        file = File.createTempFile("test", ".csv");
        taskManager = createTaskManager();
        initTasks();
    }

    @Test
    void testLoadFromFile() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);

        assertEquals(1, taskManager.showAllTasks().size());
        assertEquals(1, taskManager.showAllEpics().size());
        assertEquals(1, taskManager.showAllSubTasks().size());

        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(taskManager.showAllTasks(), fileManager.showAllTasks());
        assertEquals(taskManager.showAllEpics(), fileManager.showAllEpics());
        assertEquals(taskManager.showAllSubTasks(), fileManager.showAllSubTasks());
    }

    @Test
    void loadFromEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("emptyTest", ".csv");
        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile(emptyFile);

        assertNotNull(loadFile);
        assertEquals(0, loadFile.showAllTasks().size());
        assertEquals(0, loadFile.showAllEpics().size());
        assertEquals(0, loadFile.showAllSubTasks().size());
    }
}