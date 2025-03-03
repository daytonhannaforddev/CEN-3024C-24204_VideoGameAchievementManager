package org.AchievementManagerMaster;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private Main mainInstance;

    @BeforeEach
    void setUp() {
        mainInstance = new Main();
    }

    @Test
    void testRunMethod() {
        String[] args = {}; // Empty arguments
        int result = mainInstance.run(args);
        assertEquals(0, result, "Expected: 0 on exit.");
    }

    @Test
    void testMainMethod() {
        assertDoesNotThrow(() -> Main.main(new String[]{}), "Expected: execution without throwing exceptions.");
    }
}
