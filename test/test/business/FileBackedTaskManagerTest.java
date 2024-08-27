package test.business;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import model.business.FileBackedTaskManager;
import model.business.HistoryManager;
import model.business.InMemoryHistoryManager;
import model.business.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager;
    FileBackedTaskManager manager;
    File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("tempFile", ".txt", null);
        inMemoryHistoryManager = new InMemoryHistoryManager();
        manager = new FileBackedTaskManager(tempFile);
    }

    @Test
    public void shouldFileBackedTaskManagerAddTasksAndSearchId() {
        Task task = new Task("Задача номер 1", "Описание к задаче номер 1");
        final int taskId = manager.addNewTask(task);
        final Task savedTask = manager.getTaskId(taskId);
        final ArrayList<Task> tasks = manager.getTasks();

        Epic epic = new Epic("Эпик номер 1", "Описание к Эпику номер 1");
        final int epicId = manager.addNewEpic(epic);
        final Epic savedEpic = manager.getEpicId(epicId);
        final ArrayList<Epic> epics = manager.getEpics();

        Subtask subtask = new Subtask("Подзадача номер 1", "Подзадача номер 1", epicId);
        final int subtaskId = manager.addNewSubtask(subtask);
        final Subtask savedSubtask = manager.getSubtaskId(subtaskId);
        final ArrayList<Subtask> subtasks = manager.getSubtask();

        assertEquals(task, savedTask, "Сохраненная задача не соответствует добавленной");

        Assertions.assertTrue(tasks.contains(task), "Список задач не содержит добавленной задачи");

        assertEquals(epic, savedEpic, "Сохраненная задача Epic не соответствует добавленной");

        Assertions.assertTrue(epics.contains(epic), "Список Epic не содержит добавленной задачи");

        assertEquals(subtask, savedSubtask, "Сохраненная задача Subtask не соответствует добавленной");

        Assertions.assertTrue(subtasks.contains(subtask), "Список Subtask не содержит добавленной задачи");
    }

    @Test
    public void shouldImmutabilityOfTaskAddInManager() {
        Task task = new Task("Задача номер 2", "Задача номер 2");
        final int taskId = manager.addNewTask(task);
        final Task savedTask = manager.getTaskId(taskId);

        assertEquals(task.getName(), savedTask.getName(), "Наименование задачь должны совпадать");
        assertEquals(task.getDescription(), savedTask.getDescription(), "Описание задачь должны совпадать");
    }

    @Test
    public void shouldTasksWithSpecifiedIdAndGeneratedIdDoNotConflictWithTheManager() {
        Task task = new Task("Здача номер 3", "Задача с описанием номер 3");
        final int taskFirsId = manager.addNewTask(task);
        final Task savedTask = manager.getTaskId(taskFirsId);

        Task task2 = new Task("Задача номер 4", "Задача с описание номер 4");
        final int taskId = manager.addNewTask(task2);
        final Task savedTask2 = manager.getTaskId(taskId);

        assertEquals(task, savedTask, "Сохраненная задача не соответствует добавленной");
        assertEquals(task2, savedTask2, "Сохраненная задача не соответствует добавленной");
        Assertions.assertNotEquals(savedTask, savedTask2, "Сгенерированный ID " +
                " должен отличаться от заранее определенного ID");
    }

    @Test
    public void shouldTaskAddInHistoryManagerSaveBackVersion() {
        Task task = new Task("Задача номер 5", "Задача с описанием номер 5");

        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();

        Task taskUpdate = new Task("Задача номер 6", "Задача с описанием номер 6");
        Task thisTask = history.get(0);

        Assertions.assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");

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

        System.out.println(manager.allListSubtaskByEpic(epicId));

        Subtask subtaskTestNumber3 = new Subtask("subtaskTest №3", "Description №3", epicId);
        final int subtaskId3 = manager.addNewSubtask(subtaskTestNumber3);

        manager.removeBySubtaskId(subtaskId);

        System.out.println(manager.allListSubtaskByEpic(epicId));

        Assertions.assertNotEquals(subtaskId, subtaskId2, "Задачи равны");
        Assertions.assertNotEquals(subtaskId3, subtaskId, "Подзадача хранит один и тот же ID");
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
    public void shouldBeCheckingTheStatus() {
        Task task = new Task("Задача номер 7", "Описание к задаче номер 7");
        final int taskId = manager.addNewTask(task);
        final Task savedTask = manager.getTaskId(taskId);
        task.setStatus(Status.IN_PROGRESS);

        Epic epic = new Epic("Эпик номер 8", "Описание к Эпику номер 8");
        final int epicId = manager.addNewEpic(epic);
        final Epic savedEpic = manager.getEpicId(epicId);
        final ArrayList<Epic> epics = manager.getEpics();
        epic.setStatus(Status.IN_PROGRESS);

        Subtask subtask = new Subtask("Подзадача номер 9", "Подзадача номер 9", epicId);
        final int subtaskId = manager.addNewSubtask(subtask);
        final Subtask savedSubtask = manager.getSubtaskId(subtaskId);
        subtask.setStatus(Status.NEW);

        manager.save();

        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(tempFile);

        Assertions.assertEquals(manager, fileBackedTaskManager1, "Содержание файлов отличается");

        Assertions.assertEquals(manager, fileBackedTaskManager1, "Содержание файлов отличается");


        Assertions.assertEquals(manager, fileBackedTaskManager1, "Содержание файлов отличается");

        Assertions.assertEquals(manager, fileBackedTaskManager1, "Содержание файлов отличается");

    }

    @Test
    public void shouldSaveNullFile() {
        manager.save();
        FileBackedTaskManager loadFromFile = FileBackedTaskManager.loadFromFile(tempFile);

        Assertions.assertTrue(loadFromFile.getTasks().isEmpty(), "Менеджер задач не должен " +
                "содержать записи после загрузки пустого файла");
        Assertions.assertTrue(loadFromFile.getEpics().isEmpty(), "Менеджер эпиков не должен " +
                "содержать записи после загрузки из пустого файла");
        Assertions.assertTrue(loadFromFile.getSubtask().isEmpty(), "Менеджер подзадач не должен" +
                " содержать записи после загрузки из пустого файла");

    }

    @Test

    public void shouldSaveTask() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tempFile);

        Task task = new Task("Задача номер 7", "Описание к задаче номер 7");
        final int taskId = manager.addNewTask(task);
        final Task savedTask = manager.getTaskId(taskId);
        task.setStatus(Status.IN_PROGRESS);

        Epic epic = new Epic("Эпик номер 8", "Описание к Эпику номер 8");
        final int epicId = manager.addNewEpic(epic);
        final Epic savedEpic = manager.getEpicId(epicId);
        final ArrayList<Epic> epics = manager.getEpics();
        epic.setStatus(Status.IN_PROGRESS);

        Subtask subtask = new Subtask("Подзадача номер 9", "Подзадача номер 9", epicId);
        final int subtaskId = manager.addNewSubtask(subtask);
        final Subtask savedSubtask = manager.getSubtaskId(subtaskId);
        subtask.setStatus(Status.NEW);

        fileBackedTaskManager.save();

        Assertions.assertFalse(fileBackedTaskManager.getTasks().isEmpty(), "Менеджер задач должен сохранять " +
                "Таски ");
        Assertions.assertFalse(fileBackedTaskManager.getEpics().isEmpty(), "Менеджер задач должен сохранять" +
                "Епики");
        Assertions.assertFalse(fileBackedTaskManager.getSubtask().isEmpty(), "Менеджер задач должен сохранять" +
                "Сабтаски");
    }

}