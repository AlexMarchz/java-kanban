import Data.SubTask;
import Data.Epic;
import Manager.Manager;
import Status.Status;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Epic epic1 = new Epic("Epic 1", "Цель такая-то");
        manager.addEpic(epic1);

        System.out.println("Добавили эпическую цель");
        System.out.println(epic1);
        SubTask subtask1 = new SubTask("SubTask1", "Текст ", 1);
        manager.addSubTask(subtask1);
        System.out.println("Добавили первую подэпическую задачу");
        System.out.println(subtask1);
        SubTask subtask2 = new SubTask("SubTask2", "Задача следующая", 1);
        manager.addSubTask(subtask2);
        System.out.println("Добавили вторую подэпическую задачу");
        System.out.println(subtask2);
        subtask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subtask1);
        System.out.println("Статус изменён: В ПРОЦЕССЕ/ IN_PROGRESS");
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println("Проверяем статус эпика после изменения статуса сабтаска");
        System.out.println(epic1);
        subtask2.setStatus(Status.DONE);
        manager.updateSubTask(subtask2);
        System.out.println("Поменяли статус второго сабтаска на ВЫПОЛНЕН/ DONE");
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println("Проверяем статус эпика после изменения статуса второго сабтаска");
        System.out.println(epic1);
        subtask1.setStatus(Status.DONE);
        manager.updateSubTask(subtask1);
        System.out.println("Выставили статус ВЫПОЛНЕН/DONE первому сабтаску" + subtask1.getStatus());
        System.out.println(subtask1);
        System.out.println(subtask2);

        System.out.println("Проверяем статус первого эпика после выполнения всех его сабтасков" + epic1.getStatus());
        System.out.println(epic1);

        System.out.println("Отчистились ли сабтаски");
        System.out.println(manager.showAllSubTasks());

        System.out.println("peepee-poopoo");
    }
}
