package model;

import model.Epic;
import model.Subtask;
import model.Task;
import model.business.InMemoryTaskManager;
import model.business.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class TaskTest {
    InMemoryTaskManager manager;

    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
    }

    @Test
    public void shouldEqualsClassTaskIfIdEqualsToEachOther() {
        Task taskTestNumberOne = new Task("А", "Eat Snicker");
        final int taskId = manager.addNewTask(taskTestNumberOne);
        final Task savedTask = manager.getTaskId(taskId);

        Assertions.assertNotNull(savedTask, "Задачи нет");
        Assertions.assertEquals(taskTestNumberOne, savedTask, "Задачи не совпадают");

        final ArrayList<Task> givesTasks = manager.getTasks();

        Assertions.assertEquals(taskTestNumberOne, givesTasks.get(0), "Задачи не совпадают");
    }

    @Test
    public void shouldEqualsHeirsByClassTaskEqualsToEachOther() {
        Epic epicTestNumberOne = new Epic("Test №1", "Description №1");
        final int epicIdForTaskTest = manager.addNewEpic(epicTestNumberOne);
        final Epic savedEpic = manager.getEpicId(epicIdForTaskTest);

        Assertions.assertNotNull(savedEpic, "Задачи нет");
        Assertions.assertEquals(epicTestNumberOne, savedEpic, "Задачи не совпадают");

        final ArrayList<Epic> givesEpics = manager.getEpics();

        Assertions.assertEquals(epicTestNumberOne, givesEpics.get(0), "Задачи не совпадают");

        Subtask subtaskTestNumberOne = new Subtask("Test №2", "Description №2", epicIdForTaskTest);
        final int subtaskIdForTaskTest = manager.addNewSubtask(subtaskTestNumberOne);

        final Subtask savedSubtask = manager.getSubtaskId(subtaskIdForTaskTest);

        Assertions.assertNotNull(savedSubtask, "Задачи нет");
        Assertions.assertEquals(subtaskTestNumberOne, savedSubtask, "Задачи не совпадают");

        final ArrayList<Subtask> givesSubtask = manager.getSubtask();

        Assertions.assertEquals(subtaskTestNumberOne, givesSubtask.get(0), "Задачи не совпадают");
    }
}