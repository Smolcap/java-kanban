package model.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ManagersTest {
    @Test
    public void shouldReturnsInitializedAndReadyToUseManager() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager,"Менеджер вовзращает не готовый к работе экземпляр");
    }
}