package testManager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import data.Epic;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.InMemoryHistoryManager;


class EpicTest {

    TaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

    @Test
    public void epicsEqualsIfIdEquals() {

        Epic epic1 = new Epic("Описание_1", "Наименование_1");
        Epic epic2 = new Epic("Описание_2", "Наименование_2");

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        epic1.setId(epic2.getId());
        assertEquals(epic1, epic2, "Ошибка");

    }
}