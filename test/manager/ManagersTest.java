package manager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

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

