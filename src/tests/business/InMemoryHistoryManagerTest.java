package business;

import model.Task;
import model.business.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager;
    InMemoryTaskManager manager;

    @BeforeEach
    public void setUp() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
    }

    @Test
    public void shouldBuiltLinkedListOfVersionsAsWellAsTheAddAndRemoveOperationsWorkCorrectly() {
        Task task = new Task("Test №1", "Description №1");
        final int getId = manager.addNewTask(task);
        final Task savedTask = manager.getTaskId(getId);

        Task task2 = new Task("Test №2", "Description №2");
        final int getId2 = manager.addNewTask(task2);
        final Task savedTask2 = manager.getTaskId(getId2);

        inMemoryHistoryManager.add(savedTask);
        inMemoryHistoryManager.add(savedTask2);

        System.out.println("Просмотрели задачи " + inMemoryHistoryManager.getHistory());


        inMemoryHistoryManager.remove(getId);

        System.out.println("Просмотр задач после удаления " + inMemoryHistoryManager.getHistory());

        Assertions.assertFalse(inMemoryHistoryManager.getHistory().contains(savedTask), "Задача не удалилась");
        Assertions.assertTrue(inMemoryHistoryManager.getHistory().contains(savedTask2), "Другая задача была удалена");
    }
}