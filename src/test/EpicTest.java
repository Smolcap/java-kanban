import model.Epic;
import model.Subtask;
import model.business.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @Test
    public void shouldNotAddEpicYourselfOfSubtask() {
        Epic epic = new Epic("A", "Open a chocolate ");
        final int epicId = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("A", "Throw the wrapper", epicId);
        final int subtaskId = manager.addNewEpic(epic);

        final Subtask savedSubtask = manager.getSubtaskId(subtaskId);

        assertNull(savedSubtask, "Нельзя добавить Epic в Subtask");
    }

}