package testManager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import manager.Managers;

class ManagersTest {
    @Test
    void shouldNotNull() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    void shouldNotNullHistory() {
        assertNotNull(Managers.getDefaultHistory());
    }

}

