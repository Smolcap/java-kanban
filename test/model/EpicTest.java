package model;


import model.business.InMemoryTaskManager;
import model.business.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTest {
    InMemoryTaskManager manager;

    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
    }

    @Test
    public void shouldNotAddEpicYourselfOfSubtask() {
        Epic epicTestNumberThree = new Epic("Test №", "Open a chocolate.");
        final int epicIdForEpicTest = manager.addNewEpic(epicTestNumberThree);
        final Epic savedEpic = manager.getEpicId(epicIdForEpicTest);

        Subtask subtask = new Subtask("A", "Throw the wrapper", epicIdForEpicTest);
        final int subtaskId = manager.addNewEpic(savedEpic);

        final Subtask savedSubtask = manager.getSubtaskId(subtaskId);

        Assertions.assertNull(savedSubtask, "Нельзя добавить Epic в Subtask");
    }

    @Test
    public void shouldStatusNewCalculation() {
        Epic epic = new Epic("2", "2");
        int epicId = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("1", "1", epicId);
        int subtaskId = manager.addNewSubtask(subtask);
        subtask.setStatus(Status.NEW);

        manager.updateEpicStatus(epicId);

        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldStatusDoneCalculation() {
        Epic epic2 = new Epic("3", "3");
        int epicId2 = manager.addNewEpic(epic2);

        Subtask subtask2 = new Subtask("2", "2", epicId2);
        int subtaskId2 = manager.addNewSubtask(subtask2);
        subtask2.setStatus(Status.DONE);

        manager.updateEpicStatus(epicId2);

        Assertions.assertEquals(Status.DONE, epic2.getStatus());
    }

    @Test
    public void shouldStatusNewAndDoneCalculation() {
        Epic epic3 = new Epic("4", "4");
        int epicId3 = manager.addNewEpic(epic3);

        Subtask subtask3 = new Subtask("3", "3", epicId3);
        int subtaskId3 = manager.addNewSubtask(subtask3);
        subtask3.setStatus(Status.NEW);

        Subtask subtask4 = new Subtask("4", "4", epicId3);
        int subtaskId4 = manager.addNewSubtask(subtask4);
        subtask4.setStatus(Status.DONE);

        manager.updateEpicStatus(epicId3);

        Assertions.assertEquals(Status.IN_PROGRESS, epic3.getStatus());
    }

    @Test
    public void shouldStatusInProgressCalculation() {
        Epic epic4 = new Epic("5", "5");
        int epicId4 = manager.addNewEpic(epic4);

        Subtask subtask4 = new Subtask("4", "4", epicId4);
        int subtaskId4 = manager.addNewSubtask(subtask4);
        subtask4.setStatus(Status.IN_PROGRESS);

        Subtask subtask5 = new Subtask("5", "5", epicId4);
        int subtaskId5 = manager.addNewSubtask(subtask5);
        subtask4.setStatus(Status.IN_PROGRESS);

        manager.updateEpicStatus(epicId4);

        Assertions.assertEquals(Status.IN_PROGRESS, epic4.getStatus());
    }
}