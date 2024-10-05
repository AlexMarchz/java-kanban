package data;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    public void tasksEqualsIfIdEquals() {
        Task task1 = new Task("Описание_1", "Наименование_1");
        Task task2 = new Task("Описание_2", "Наименование_2");

        task1.setId(task2.getId());
        assertEquals(task1, task2, "Ошибка");
    }
}