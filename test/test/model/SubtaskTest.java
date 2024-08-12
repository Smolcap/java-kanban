package test.model;

import model.Epic;
import model.Subtask;
import model.business.InMemoryTaskManager;
import model.business.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubtaskTest {
    InMemoryTaskManager manager;

    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
    }

    @Test
    public void shouldNotAddSubtaskYourselfOfEpic() {
        Epic epicTestNumberTwo = new Epic("Test №3", "Description №3");
        final int epicIdForSubtaskTest = manager.addNewEpic(epicTestNumberTwo);

        Subtask subtaskTestNumberTwo = new Subtask("Test №4", "Description №4", epicIdForSubtaskTest);
        final int subtaskIdForSubtaskTest = manager.addNewEpic(epicTestNumberTwo);

        final Subtask savedSubtask = manager.getSubtaskId(subtaskIdForSubtaskTest);

        Assertions.assertNull(savedSubtask, "Подзадача не может быть Epic");
    }
}