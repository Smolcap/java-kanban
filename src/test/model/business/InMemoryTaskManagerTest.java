package model.business;

import model.Epic;
import model.Subtask;
import model.Task;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @Test
    public void shouldInMemoryTaskManagerAddTasksAndSearchId() {
        Task task = new Task("A", "Eat Snicker");
        final int taskId = manager.addNewTask(task);

        final Task savedTask = manager.getTaskId(taskId);

        assertEquals(task, savedTask, "Сохраненная задача не соответствует добавленной");

        final ArrayList<Task> tasks = manager.getTasks();
        assertTrue(tasks.contains(task), "Список задач не содержит добавленной задачи");

        Epic epic = new Epic("A", "Open a chocolate");
        final int epicId = manager.addNewEpic(epic);

        final Epic savedEpic = manager.getEpicId(epicId);

        assertEquals(epic, savedEpic, "Сохраненная задача Epic не соответствует добавленной");

        final ArrayList<Epic> epics = manager.getEpics();

        assertTrue(epics.contains(epic), "Список Epic не содержит добавленной задачи");

        Subtask subtask = new Subtask("A", "Throw the wrapper", epicId);
        final int subtaskId = manager.addNewSubtask(subtask);

        final Subtask savedSubtask = manager.getSubtaskId(subtaskId);

        assertEquals(subtask, savedSubtask, "Сохраненная задача Subtask не соответствует добавленной");

        final ArrayList<Subtask> subtasks = manager.getSubtask();
        assertTrue(subtasks.contains(subtask), "Список Subtask не содержит добавленной задачи");
    }

    @Test
    public void shouldTasksWithSpecifiedIdAndGeneratedIdDoNotConflictWithTheManager() {
        final int prefId = 1;

        Task task = new Task("A", "Eat Snicker");
        manager.addNewTask(task);
        final Task savedTask = manager.getTaskId(prefId);

        Task task2 = new Task("A", "Eat Snicker");
        final int taskId = manager.addNewTask(task2);

        final Task savedTask2 = manager.getTaskId(taskId);

        assertEquals(task, savedTask, "Сохраненная задача не соответствует добавленной");
        assertEquals(task2, savedTask2, "Сохраненная задача не соответствует добавленной");
        assertNotEquals(savedTask, savedTask2, "Сгенерированный ID " +
                " должен отличаться от заранее определенного ID");
    }

    @Test
    public void shouldImmutabilityOfTaskAddInManager() {
        Task task = new Task("A", "Eat Snicker");

        final int taskId = manager.addNewTask(task);

        final Task savedTask = manager.getTaskId(taskId);

        assertEquals(task.getName(), savedTask.getName(), "Наименование задачь должны совпадать");
        assertEquals(task.getDescription(), savedTask.getDescription(), "Описание задачь должны совпадать");
    }

    @Test
    public void shouldTaskAddInHistoryManagerSaveBackVersion() {
        Task task = new Task("A", "Eat Snicker");

        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(task);
        final ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");

        Task taskUpdate = new Task("A", "Eat Snicker with Nuts");
        Task thisTask = history.get(0);

        assertEquals(task.getName(), thisTask.getName(), "Наименование задач должно совпадать");
        assertEquals(task.getDescription(), thisTask.getDescription(), "Описание должно совпадать");
        assertNotEquals(task,taskUpdate, "Задача не совпадает с изначальной");
    }
}