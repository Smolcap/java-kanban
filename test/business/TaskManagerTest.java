package business;

import model.Epic;
import model.Subtask;
import model.Task;
import model.business.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    @BeforeEach
    public abstract void setUp() throws IOException;

    @Test
    public void shouldGetListTasks() {
        Task task = new Task("1", "1");
        manager.addNewTask(task);
        List<Task> allTasks = manager.getTasks();

        Assertions.assertEquals(1, allTasks.size(), "Список задач не может быть пустым");
    }

    @Test
    public void shouldGetListEpicsAndSubtasks() {
        Epic epic = new Epic("1", "1");
        int epicId = manager.addNewEpic(epic);
        Subtask subtask = new Subtask("1", "1", epicId);
        manager.addNewSubtask(subtask);

        List<Epic> allEpics = manager.getEpics();
        List<Subtask> allSubtasks = manager.getSubtask();

        Assertions.assertEquals(1, allEpics.size(), "Список задач не может быть пустым");
        Assertions.assertEquals(1, allSubtasks.size(), "Список подзадач не может быть пустым");
    }

    @Test
    public void shouldCleanTasks() {
        Task task = new Task("2", "2");
        int taskId = manager.addNewTask(task);
        manager.cleanTasks();
        Task saveTask = manager.getTaskId(taskId);

        Assertions.assertNull(saveTask, "Список задач должен быть пустым");
    }

    @Test
    public void shouldCleanEpics() {
        Epic epic = new Epic("2", "2");
        int epicId = manager.addNewEpic(epic);
        manager.cleanEpics();

        Epic saveEpic = manager.getEpicId(epicId);

        Assertions.assertNull(saveEpic, "Список эпиков должен быть пуст");
    }

    @Test
    public void shouldCleanSubtasks() {
        Epic epic = new Epic("3", "3");
        int epicId = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("2", "2", epicId);
        int subtaskId = manager.addNewSubtask(subtask);

        manager.cleanSubtasks(epicId);

        Subtask saveSubtask = manager.getSubtaskId(subtaskId);

        Assertions.assertNull(saveSubtask, "Список подзадач должен быть пуст");
    }

    @Test
    public void shouldGetTaskForId() {
        Task task = new Task("3", "3");
        int taskId = manager.addNewTask(task);
        Task saveTask = manager.getTaskId(taskId);

        Assertions.assertEquals(task, saveTask, "Задача должна быть такой же при добавлении");
    }

    @Test
    public void shouldGetEpicAndSubtaskForId() {
        Epic epic = new Epic("4", "4");
        int epicId = manager.addNewEpic(epic);
        Epic saveEpic = manager.getEpicId(epicId);

        Subtask subtask = new Subtask("3", "4", epicId);
        int subtaskId = manager.addNewSubtask(subtask);
        Subtask saveSubtask = manager.getSubtaskId(subtaskId);

        Assertions.assertEquals(epic, saveEpic, "Эпик должен быть такой же при добавлении");
        Assertions.assertEquals(subtask, saveSubtask, "Подзадача должна быть такой же при добавлении");
    }

    @Test
    public void shouldAddNewTask() {
        Task task = new Task("4", "4");
        int taskId = manager.addNewTask(task);

        Assertions.assertNotEquals(0, taskId, "Задача должна быть добавлена");
    }

    @Test
    public void shouldAddNewEpicAndNewSubtask() {
        Epic epic = new Epic("5", "5");
        int epicId = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("5", "5", epicId);
        int subtaskId = manager.addNewSubtask(subtask);

        Assertions.assertNotEquals(0, epicId, "Эпик должен быть добавлен");
        Assertions.assertNotEquals(0, subtaskId, "Подзадача должна быть добавлен");
    }

    @Test
    public void shouldUpdateTask() {
        Task task = new Task("5", "5");
        int taskId = manager.addNewTask(task);

        task.setName("20");

        manager.updateTask(task);

        Task updatedTask = manager.getTaskId(taskId);

        Assertions.assertNotNull(updatedTask, "Обновленная задача не должна быть null");
        Assertions.assertEquals("20", updatedTask.getName(), "Имя задачи должно быть обновлено");
    }

    @Test
    public void shouldUpdateEpic() {
        Epic epic = new Epic("6", "6");
        int epicId = manager.addNewEpic(epic);
        epic.setName("23");
        manager.updateEpic(epic);

        Epic updateEpic = manager.getEpicId(epicId);

        Assertions.assertNotNull(updateEpic, "Обновленный эпик не должен быть null");
        Assertions.assertEquals("23", updateEpic.getName(), "Имя эпика должно быть обновлено");
    }

    @Test
    public void shouldUpdateSubtask() {
        Epic epic = new Epic("6", "6");
        int epicId = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("6", "6", epicId);
        int subtaskId = manager.addNewSubtask(subtask);
        subtask.setName("23");

        manager.updateSubtask(subtask);

        Subtask updateSubtask = manager.getSubtaskId(subtaskId);
        Assertions.assertNotNull(updateSubtask, "Обновленная подзадача не должна быть null");
        Assertions.assertEquals("23", updateSubtask.getName(), "Имя подзадачи должно быть обновлено");
    }

    @Test
    public void shouldRemoveByTaskId() {
        Task task = new Task("6", "6");
        int taskId = manager.addNewTask(task);
        Task saveTask = manager.getTaskId(taskId);

        manager.removeByTaskId(taskId);

        Task removeTask = manager.getTaskId(taskId);

        Assertions.assertNull(removeTask, "Задача должна быть удалена");
    }

    @Test
    public void shouldRemoveByEpicId() {
        Epic epic = new Epic("7", "7");
        int epicId = manager.addNewEpic(epic);
        Epic saveEpic = manager.getEpicId(epicId);
        manager.removeByEpicId(epicId);

        Epic removeEpic = manager.getEpicId(epicId);

        Assertions.assertNull(removeEpic, "Эпик должна быть удалена");
    }

    @Test
    public void shouldRemoveBySubtask() {
        Epic epic = new Epic("8", "8");
        int epicId = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("7", "7", epicId);
        int subtaskId = manager.addNewSubtask(subtask);
        Subtask saveSubtask = manager.getSubtaskId(subtaskId);

        manager.removeBySubtaskId(subtaskId);

        Subtask removeSubtask = manager.getSubtaskId(subtaskId);

        Assertions.assertNull(removeSubtask, "Задача должна быть удалена");
    }
}