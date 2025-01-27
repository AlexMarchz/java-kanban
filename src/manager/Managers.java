package manager;

import java.io.File;

public class Managers {

    public static FileBackedTaskManager getDefaultFileManager(File file) {
        return FileBackedTaskManager.loadFromFile(file);
    }

    public static TaskManager getDefault(File file) {
        return new FileBackedTaskManager(file);
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
