package model.business;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> saveIdsAndNodes = new HashMap<>();

    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (saveIdsAndNodes.containsKey(task.getId())) {
            Node<Task> getNodeForTask = saveIdsAndNodes.get(task.getId());
            removeNode(getNodeForTask);
        }

        linkLast(task);

        saveIdsAndNodes.put(task.getId(), tail);
    }

    @Override
    public void remove(int id) {
        if (!saveIdsAndNodes.containsKey(id)) {
            System.out.println("Задача с Id " + id + " не найдена");
        } else {
            Node<Task> nodeTask = saveIdsAndNodes.get(id);
            removeNode(nodeTask);
            System.out.println("Задача успешно удалена из истории");
        }
    }

    @Override
    public void linkLast(Task task) {
        if (task == null) {
            System.out.println("Задача не может быть null");
        }

        Node<Task> oldTail = tail;
        Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> current = head;

        while (current != null) {
            tasks.add(current.data);
            current = current.next;
        }
        return tasks;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        node.prev = null;
        node.next = null;
        size--;
    }

    private static class Node<T> {
        T data;
        Node<T> prev;
        Node<T> next;

        private Node(Node<T> prev, T data, Node<T> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}


