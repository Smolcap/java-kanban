package test.business;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import model.business.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("tempTaskManager", ".txt");
        tempFile.deleteOnExit();
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    public void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void shouldSaveNullFile() {
        assertTrue(tempFile.length() == 0, "Файл должен быть пустым");

        manager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(manager.getTasks().isEmpty(), "Менеджер задач не должен " +
                "содержать записи после загрузки пустого файла");
        assertTrue(manager.getEpics().isEmpty(), "Менеджер эпиков не должен " +
                "содержать записи после загрузки из пустого файла");
        assertTrue(manager.getSubtask().isEmpty(), "Менеджер подзадач не должен" +
                " содержать записи после загрузки из пустого файла");

    }

    @Test
    public void shouldSaveManyTask() {
        Task task = new Task("Задача номер 7", "Описание к задаче номер 7");
        final int taskId = manager.addNewTask(task);
        final Task savedTask = manager.getTaskId(taskId);
        task.setStatus(Status.IN_PROGRESS);

        Task task2 = new Task("Задача номер 8", "Описание к задаче номер 8");
        final int taskId2 = manager.addNewTask(task2);
        final Task savedTask2 = manager.getTaskId(taskId2);
        task2.setStatus(Status.NEW);

        manager.save();

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);

        List<Task> loadedTasks = loadedTaskManager.getTasks();

        Assertions.assertEquals(2, loadedTasks.size(), "Количество загружаемых задач должно быть 2");
    }


    @Test
    public void shouldSaveTask() {
        Task task = new Task("Задача номер 20", "Описание к задаче номер 7");
        final int taskId = manager.addNewTask(task);
        final Task savedTask = manager.getTaskId(taskId);
        task.setStatus(Status.IN_PROGRESS);

        Epic epic = new Epic("Эпик номер 25", "Описание к Эпику номер 8");
        final int epicId = manager.addNewEpic(epic);
        final Epic savedEpic = manager.getEpicId(epicId);
        final ArrayList<Epic> epics = manager.getEpics();
        epic.setStatus(Status.NEW);

        Subtask subtask = new Subtask("Подзадача номер 9", "Подзадача номер 9", epicId);
        final int subtaskId = manager.addNewSubtask(subtask);
        final Subtask savedSubtask = manager.getSubtaskId(subtaskId);
        subtask.setStatus(Status.NEW);

        manager.save();

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);

        List<Task> loadedTasks = loadedTaskManager.getTasks();
        List<Epic> loadedEpics = loadedTaskManager.getEpics();
        List<Subtask> loadedSubtask = loadedTaskManager.getSubtask();

        Assertions.assertEquals(1, loadedTasks.size(), "Количество загружаемых задач " +
                "должно быть 1");

        Assertions.assertEquals(1, loadedEpics.size(), "Количество загружаемых задач " +
                "должно быть 1");

        Assertions.assertEquals(1, loadedSubtask.size(), "Количество загружаемых задач " +
                "должно быть 1");
    }

    @Test
    public void shouldSaveAndRecoverySerialization() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tempFile);

        Task task10 = new Task("Задача номер 10", "Описание к задаче номер 10");
        final int taskId = manager.addNewTask(task10);
        final Task savedTask = manager.getTaskId(taskId);
        task10.setStatus(Status.IN_PROGRESS);

        fileBackedTaskManager.save();
        Assertions.assertEquals(manager.getTasks().size(), fileBackedTaskManager.getTasks().size(), "Задача " +
                "должна быть одна");

    }
}