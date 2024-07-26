import model.Epic;
import model.Subtask;
import model.business.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @Test
    public void shouldNotAddSubtaskYourselfOfEpic() {
        Epic epic = new Epic("A", "Open a chocolate ");
        int subtaskEpic = manager.addNewEpic(epic);

        Subtask subtask = new Subtask("A", "Throw the wrapper", subtaskEpic);
        subtaskEpic = manager.addNewEpic(epic);

        final Subtask savedSubtask = manager.getSubtaskId(subtaskEpic);

        assertNull(savedSubtask, "Подзадача не может быть Epic");
    }
}