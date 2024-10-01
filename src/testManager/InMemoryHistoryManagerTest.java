package testManager;

import java.util.ArrayList;

import data.Task;
import manager.InMemoryHistoryManager;

import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class InMemoryHistoryManagerTest {
    InMemoryHistoryManager history = new InMemoryHistoryManager();
    static Task task;
    static ArrayList<Task> historyList;

    @BeforeAll
    static void beforeAll() {
        task = new Task("Описание", "Наименование");
        historyList = new ArrayList<>();
    }

    @Test
    void add() {
        history.add(task);
        historyList = history.getHistory();

        assertNotNull(historyList);
        assertEquals(1, historyList.size());
    }

    @Test
    void shouldReturn10TasksWhenAddMore() {
        for (int i = 0; i < 15; i++) {
            history.add(task);
        }
        historyList = history.getHistory();
        assertEquals(10, historyList.size());
    }

}