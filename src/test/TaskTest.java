import model.Epic;
import model.Subtask;
import model.Task;
import model.business.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @Test
    public void shouldEqualsClassTaskIfIdEqualsToEachOther() {


        Task task = new Task("А", "Eat Snicker");
        final int taskId = manager.addNewTask(task);

        final Task savedTask = manager.getTaskId(taskId);

        assertNotNull(savedTask, "Задачи нет");
        assertEquals(task, savedTask, "Задачи не совпадают");

        final ArrayList<Task> tasks = manager.getTasks();

        assertEquals(task, tasks.get(0), "Задачи не совпадают");
    }

    @Test
    public void shouldEqualsHeirsByClassTaskEqualsToEachOther() {
        Epic epic = new Epic("A", "Open a chocolate ");
        final int epicId = manager.addNewEpic(epic);

        final Epic savedEpic = manager.getEpicId(epicId);

        assertNotNull(savedEpic, "Задачи нет");
        assertEquals(epic, savedEpic, "Задачи не совпадают");

        final ArrayList<Epic> epics = manager.getEpics();

        assertEquals(epic, epics.get(0), "Задачи не совпадают");

        Subtask subtask = new Subtask("A", "Throw the wrapper", epicId);
        final int subtaskId = manager.addNewSubtask(subtask);

        final Subtask savedSubtask = manager.getSubtaskId(subtaskId);

        assertNotNull(savedSubtask, "Задачи нет");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают");

        final ArrayList<Subtask> subtasks = manager.getSubtask();

        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают");
    }
}