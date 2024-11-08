package test.model;

import model.Epic;
import model.Subtask;
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
        Epic epicTestNumberThree = new Epic("Test №", "Open a chocolate ");
        final int epicIdForEpicTest = manager.addNewEpic(epicTestNumberThree);
        final Epic savedEpic = manager.getEpicId(epicIdForEpicTest);

        Subtask subtask = new Subtask("A", "Throw the wrapper", epicIdForEpicTest);
        final int subtaskId = manager.addNewEpic(savedEpic);

        final Subtask savedSubtask = manager.getSubtaskId(subtaskId);

        Assertions.assertNull(savedSubtask, "Нельзя добавить Epic в Subtask");
    }
}