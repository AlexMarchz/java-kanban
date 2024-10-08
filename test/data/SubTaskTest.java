package data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {



    @Test
    public void subTasksEqualsIfIdEquals() {
        Epic epic = new Epic("Тестовое описание", "Тестовое имя");

        SubTask subTask1 = new SubTask("Описание_1", "Наименование_1", epic.getId());
        SubTask subTask2 = new SubTask("Описание_2", "Наименование_2", epic.getId());

        subTask1.setId(subTask2.getId());
        assertEquals(subTask1, subTask2, "Ошибка");

    }
}