package manager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;

class ManagersTest {
    private File file;

    @Test
    void shouldNotNull() {
        assertNotNull(Managers.getDefault(file));
    }

    @Test
    void shouldNotNullHistory() {
        assertNotNull(Managers.getDefaultHistory());
    }

}

