package inmemorymanagers;

import managers.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<String, Node<Task>> nodes = new HashMap<>();

    // Класс Node для LinkedList
    private static class Node<E extends Task> {
        public E data;
        public Node<E> next;
        public Node<E> prev;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    // Поля LinkedList
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    // Добавление последнего элемента в LinkedList
    public void addLast(Task element) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }

    // Добавление задачи в LinkedList
    @Override
    public void add(Task task) {
        if (tail != null && task.equals(tail.data)) {
            return;
        }
        addLast(task);
        if (nodes.containsKey(task.getId())) {
            remove(task.getId());
        }
        nodes.put(task.getId(), tail);
    }

    // Удаление задачи из LinkedList по id
    @Override
    public void remove(String id) {
        removeNode(nodes.get(id));
    }

    // Удаление задачи из LinkedList
    public void removeNode(Node<Task> node) {
        final Node<Task> next = node.next;
        final Node<Task> prev = node.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.data = null;
        size--;
    }

    // Получение истории просмотра задач
    @Override
    public List<Task> getHistory() {
        ArrayList<Task> lastTasks = new ArrayList<>();
        Node<Task> x = tail;
        for (int i = size; x != null; i--) {
            lastTasks.add(x.data);
            x = x.prev;
        }
        return lastTasks;
    }


}
