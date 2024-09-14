package business;

import model.Task;
import model.business.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

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

    @Test
    public void shouldGetHistoryListAndAddHistory() {
        Task task3 = new Task("Test №3", "Description №3");
        final int getId3 = manager.addNewTask(task3);
        final Task savedTask3 = manager.getTaskId(getId3);

        Task task4 = new Task("Test №4", "Description №4");
        final int getId4 = manager.addNewTask(task4);
        final Task savedTask4 = manager.getTaskId(getId4);

        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);

        List<Task> allTask = inMemoryHistoryManager.getHistory();

        System.out.println(inMemoryHistoryManager.getHistory());
        System.out.println("История задач: " + allTask);

        Assertions.assertFalse(allTask.isEmpty(), "История не должна быть пустой");
        Assertions.assertEquals(2, allTask.size(), "Количество задач в истории должно быть 2");
        Assertions.assertTrue(allTask.contains(savedTask3), "История должна содержать задачу 3");
        Assertions.assertTrue(allTask.contains(savedTask4), "История должна содержать задачу 4");
    }

    @Test
    public void shouldRemoveInHistoryManager() {
        Task task5 = new Task("Test №5", "Description №5");
        final int getId5 = manager.addNewTask(task5);
        final Task savedTask5 = manager.getTaskId(getId5);

        inMemoryHistoryManager.add(task5);
        inMemoryHistoryManager.remove(getId5);

        Assertions.assertFalse(inMemoryHistoryManager.getHistory().contains(savedTask5), "Задача должна" +
                " быть удалена");
    }

    @Test
    public void shouldLinkLastAddInEnd() {
        Task task6 = new Task("Test №6", "Description №6");
        final int getId6 = manager.addNewTask(task6);

        Task task7 = new Task("Test №7", "Description №7");
        final int getId7 = manager.addNewTask(task7);

        inMemoryHistoryManager.linkLast(task6);
        inMemoryHistoryManager.linkLast(task7);

        List<Task> tasks = inMemoryHistoryManager.getTasks();

        Assertions.assertEquals(task6, tasks.get(0), "Первая задача должна быть task6");
        Assertions.assertEquals(task7, tasks.get(1), "Вторая задача должна быть task7");
    }

    @Test
    public void shouldGetViewTasks() {
        Task task8 = new Task("Test №8", "Description №8");
        final int getId8 = manager.addNewTask(task8);

        inMemoryHistoryManager.add(task8);

        List<Task> tasks = inMemoryHistoryManager.getTasks();

        Assertions.assertEquals(1, tasks.size(), "Должно быть 1 задача в истории");
    }

    @Test
    public void emptyTaskHistory() {
        Task task9 = new Task("Test №8", "Description №9");
        final int getId9 = manager.addNewTask(task9);

        Assertions.assertTrue(inMemoryHistoryManager.getHistory().isEmpty(), "История должна быть пустая");
    }

    @Test
    public void shouldNotDuplication() {
        Task task10 = new Task("Test №10", "Description №10");
        final int getId10 = manager.addNewTask(task10);

        inMemoryHistoryManager.add(task10);
        inMemoryHistoryManager.add(task10);

        Assertions.assertEquals(1, inMemoryHistoryManager.getHistory().size(), "Содержит одну задачу");
    }

    @Test
    public void shouldDeletionFromHistoryBeginningMiddleEnd() {
        Task task3 = new Task("Test №3", "Description №3");
        final int getId3 = manager.addNewTask(task3);
        final Task savedTask3 = manager.getTaskId(getId3);


        Task task4 = new Task("Test №4", "Description №4");
        final int getId4 = manager.addNewTask(task4);
        final Task savedTask4 = manager.getTaskId(getId4);

        Task task10 = new Task("Test №10", "Description №10");
        final int getId10 = manager.addNewTask(task10);
        final Task savedTask10 = manager.getTaskId(getId10);

        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(task10);

        List<Task> historyBeforeRemoval = inMemoryHistoryManager.getHistory();


        inMemoryHistoryManager.remove(getId4);

        List<Task> historyAfterRemoval = inMemoryHistoryManager.getHistory();

        Assertions.assertEquals(task10, historyAfterRemoval.get(1), "Вторая задача должна быть task10");

        inMemoryHistoryManager.remove(getId10);

        Assertions.assertEquals(task3, historyAfterRemoval.get(0), "Первая задача должна быть task3");
    }
}