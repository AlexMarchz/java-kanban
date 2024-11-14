package manager;
import data.Task;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final MyLinkedList history = new MyLinkedList();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        remove(task.getId());
        history.linkLast(task);

    }

    @Override
    public void remove(int id){
        history.removeNode(history.nodeMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return history.getTaskList();
    }

    private static class Node {  //Node - основа связанного списка. Сам элемент и две ссылки(next/back)
        private Node back;       //Ссылка на пред-й эл.
        private final Task task;       //Сам объект
        private Node next;       //Ссылка на след-й эл.

        private Node(Task task, Node back, Node next) {
            this.task = task;
            this.back = back;
            this.next = next;
        }
    }

    //базовый LinkedList вставляет объект между первым и последним нодом, а наша реалезация - взять ссылку на
    //последний нод и заполнить её объектом, это и будет нашей кастомной реалезацией.
    //Первый НОД(нуль) -><- Последний нод(нуль). Пришёл объект. Первый НОД(нуль) -> <-"Последний" НОД(Объект) ->
    //<- Последний НОД(нуль). Алгоритм повторяется.

    private static class MyLinkedList {
        private final Map<Integer, Node> nodeMap = new HashMap<>();
        private Node head;
        private Node tail;

        private void linkLast(Task task) {
            Node newNode = new Node(task, tail, null); //узел для подставления

            if (tail == null) {
                head = newNode;
            } else {
                tail.next = newNode;
            }
            tail = newNode;
            nodeMap.put(task.getId(), newNode);
        }

        private void removeNode(Node node) {
            if (node != null) {
                nodeMap.remove(node.task.getId());
                Node back = node.back;
                Node next = node.next;

                if (back != null) {
                    back.next = next;
                }
                if (next != null) {
                    next.back = back;
                }

                node.back = null;
                node.next = null;

                if (node == head) {
                    head = next;
                }
                if (node == tail) {
                    tail = back;
                }
            }
        }

        private List<Task> getTaskList() {
            List<Task> list = new ArrayList<>();
            Node node = head;
            while (node != null) {
                list.add(node.task);
                node = node.next;
            }
            return list;
        }
    }
}
