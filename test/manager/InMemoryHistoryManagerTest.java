package manager;

import java.util.List;

import data.Task;
import data.Epic;
import data.SubTask;

import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class InMemoryHistoryManagerTest {
    InMemoryHistoryManager history = new InMemoryHistoryManager();
    static Task task;
    static Epic epic;
    static SubTask subTask;

    @BeforeAll
    static void beforeAll() {
        task = new Task("Описание", "Наименование");
        epic = new Epic("Описание эпика", "Наименование эпика");
        subTask = new SubTask("Описание сабтаска", "Наименование сабтаска", 3);
        task.setId(1);
        epic.setId(2);
        subTask.setId(3);
    }

    @Test
    void add() {
        history.add(task);

        assertNotNull(history.getHistory());
        assertEquals(history.getHistory().getFirst(), task);
    }

    @Test
    void removeFirstTaskFromHistory() {
        history.add(task);
        history.add(epic);
        history.add(subTask);
        history.remove(task.getId());
        assertEquals(history.getHistory(), List.of(epic, subTask));
    }
    @Test
    void removeLastFromTaskHistory() {
        history.add(task);
        history.add(epic);
        history.add(subTask);
        history.remove(subTask.getId());

        assertEquals(history.getHistory(), List.of(task, epic));
    }

    @Test
    void removeMidTaskFromHistory() {
        history.add(task);
        history.add(epic);
        history.add(subTask);
        history.remove(epic.getId());

        assertEquals(history.getHistory(), List.of(task, subTask));
    }

    @Test
    void removeSingleTaskFromHistory() {
        history.add(task);
        history.remove(task.getId());

        assertEquals(history.getHistory(), List.of());
    }

    @Test //Тест завершился бы ошибкой, будь там исключение.Наверное
    void removeTaskFromEmptyHistory() {
        history.remove(1);
        assertEquals(history.getHistory(), List.of());
    }

    @Test
    void checkUniquenessInList() {
        history.add(task);
        history.add(epic);
        history.add(subTask);
        history.add(task);

        assertNotEquals(task, history.getHistory().getFirst());
    }
}