package business;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import model.business.HistoryManager;
import model.business.InMemoryHistoryManager;
import model.business.InMemoryTaskManager;
import model.business.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {
    InMemoryTaskManager manager;
    InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
        inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldInMemoryTaskManagerAddTasksAndSearchId() {
        Task task = new Task("A", "Eat Snicker");

        final int taskId = manager.addNewTask(task);
        final Task savedTask = manager.getTaskId(taskId);

        assertEquals(task, savedTask, "Сохраненная задача не соответствует добавленной");

        final ArrayList<Task> tasks = manager.getTasks();

        Assertions.assertTrue(tasks.contains(task), "Список задач не содержит добавленной задачи");

        Epic epic = new Epic("A", "Open a chocolate");

        final int epicId = manager.addNewEpic(epic);
        final Epic savedEpic = manager.getEpicId(epicId);

        assertEquals(epic, savedEpic, "Сохраненная задача Epic не соответствует добавленной");

        final ArrayList<Epic> epics = manager.getEpics();

        Assertions.assertTrue(epics.contains(epic), "Список Epic не содержит добавленной задачи");

        Subtask subtask = new Subtask("A", "Throw the wrapper", epicId);

        final int subtaskId = manager.addNewSubtask(subtask);
        final Subtask savedSubtask = manager.getSubtaskId(subtaskId);

        assertEquals(subtask, savedSubtask, "Сохраненная задача Subtask не соответствует добавленной");

        final ArrayList<Subtask> subtasks = manager.getSubtask();
        Assertions.assertTrue(subtasks.contains(subtask), "Список Subtask не содержит добавленной задачи");
    }

    @Test
    public void shouldTasksWithSpecifiedIdAndGeneratedIdDoNotConflictWithTheManager() {
        final int prefId = 1;

        Task task = new Task("A", "Eat Snicker");
        final int taskFirsId = manager.addNewTask(task);
        final Task savedTask = manager.getTaskId(prefId);

        Task task2 = new Task("A", "Eat Snicker");

        final int taskId = manager.addNewTask(task2);
        final Task savedTask2 = manager.getTaskId(taskId);

        assertEquals(task, savedTask, "Сохраненная задача не соответствует добавленной");
        assertEquals(task2, savedTask2, "Сохраненная задача не соответствует добавленной");
        Assertions.assertNotEquals(savedTask, savedTask2, "Сгенерированный ID " +
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
        final List<Task> history = historyManager.getHistory();

        Assertions.assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");

        Task taskUpdate = new Task("A", "Eat Snicker with Nuts");
        Task thisTask = history.get(0);

        assertEquals(task.getName(), thisTask.getName(), "Наименование задач должно совпадать");
        assertEquals(task.getDescription(), thisTask.getDescription(), "Описание должно совпадать");
        Assertions.assertNotEquals(task, taskUpdate, "Задача не совпадает с изначальной");
    }

    @Test
    public void shouldNotStoreOldIDsInsideThemselvesDeletedSubtasks() {
        Epic epic = new Epic("Test №1", "Description №1");
        final int epicId = manager.addNewEpic(epic);


        Subtask subtaskTestNumber1 = new Subtask("Test №1", "Description №1", epicId);
        final int subtaskId = manager.addNewSubtask(subtaskTestNumber1);

        System.out.println(manager.allListSubtaskByEpic(epicId));

        Subtask subtaskTestNumber2 = new Subtask("Test №2", "Description №2", epicId);
        final int subtaskId2 = manager.addNewSubtask(subtaskTestNumber2);

        Assertions.assertNotEquals(subtaskId, subtaskId2, "Задачи равны");

        System.out.println(manager.allListSubtaskByEpic(epicId));

        Subtask subtaskTestNumber3 = new Subtask("subtaskTest №3", "Description №3", epicId);
        final int subtaskId3 = manager.addNewSubtask(subtaskTestNumber3);

        manager.removeBySubtaskId(subtaskId);

        System.out.println(manager.allListSubtaskByEpic(epicId));

        Assertions.assertNotEquals(subtaskId3, subtaskId, "Подзадача хранит один и тот же ID");


    }

    @Test
    public void shouldBeNoIrrelevantSubtaskIDsInsideTheEpics() {
        Epic epic = new Epic("Test №2", "Description №2");
        final int epicId = manager.addNewEpic(epic);
        inMemoryHistoryManager.add(epic);

        Subtask subtaskTestNumber1 = new Subtask("subtaskTest №1", "Description №1", epicId);
        final int subtaskId = manager.addNewSubtask(subtaskTestNumber1);
        inMemoryHistoryManager.add(subtaskTestNumber1);

        Subtask subtaskTestNumber2 = new Subtask("subtaskTest №2", "Description №2", epicId);
        final int subtaskId2 = manager.addNewSubtask(subtaskTestNumber2);
        inMemoryHistoryManager.add(subtaskTestNumber2);


        Subtask subtaskTestNumber3 = new Subtask("subtaskTest №3", "Description №3", epicId);
        final int subtaskId3 = manager.addNewSubtask(subtaskTestNumber3);
        inMemoryHistoryManager.add(subtaskTestNumber3);

        System.out.println(manager.allListSubtaskByEpic(epicId));

        manager.removeBySubtaskId(subtaskId2);

        System.out.println(manager.allListSubtaskByEpic(epicId));

        Assertions.assertFalse(epic.getSubtaskId().contains(subtaskId2), "Содержится в списке подзадач эпика");

    }

    @Test
    public void shouldBeUpdateStatusEpic() {
        Epic epic = new Epic("Test №", "Test №");
        final int epicId = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("SubtaskTest #1", "Description", epicId);
        int subtaskId = manager.addNewSubtask(subtask);
        subtask.setStatus(Status.NEW);

        Subtask subtask2 = new Subtask("SubtaskTest #1", "Description", epicId);
        int subtaskId2 = manager.addNewSubtask(subtask);
        subtask2.setStatus(Status.DONE);

        manager.updateEpicStatus(epicId);

        Assertions.assertEquals(Status.IN_PROGRESS, manager.getEpicId(epicId).getStatus());
    }

}