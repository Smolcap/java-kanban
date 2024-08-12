package business;

import model.business.Managers;
import model.business.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {
    @Test
    public void shouldReturnsInitializedAndReadyToUseManager() {
        TaskManager manager = Managers.getDefault();
        Assertions.assertNotNull(manager, "Менеджер вовзращает не готовый к работе экземпляр");
    }
}