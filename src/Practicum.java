import model.Task;
import model.business.*;

import java.util.*;

public class Practicum {

    public static void main(String[] args) {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        InMemoryTaskManager manager = new InMemoryTaskManager(Managers.getDefaultHistory());

        Task task = new Task("Test №1", "Description №1");
        final int getId = manager.addNewTask(task);
        final Task savedTask = manager.getTaskId(getId);

        Task task2 = new Task("Test №2", "Description №2");
        final int getId2 = manager.addNewTask(task2);
        final Task savedTask2 = manager.getTaskId(getId2);

        inMemoryHistoryManager.add(savedTask);
        inMemoryHistoryManager.add(savedTask2);

        System.out.println("Просмотрели задачи " + inMemoryHistoryManager.getHistory());

        System.out.println("Удаляем задачу с ID: " + getId);
        inMemoryHistoryManager.remove(getId);

        System.out.println("Просмотр задач после удаления " + inMemoryHistoryManager.getHistory());

    }
}