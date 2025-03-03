/*Dayton Hannaford,
CEN-3024C-24204

Update video game tests for a multitude of methods utilized in making the class and overall program function. These are tested slightly differently than the others, using Mockito to emulate scanner based user input.
This is due to the fact that all user input is managed by scanner input during the first few phases until the database is implemented. Update logic is separated between VideoGameTest and UpdateVideoGameTest for future
ease of separation.*/

package org.AchievementManagerMaster;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateVideoGameTest {

    // ~~~ Colors ~~~
    public final String RED = "\u001B[31m";
    public final String GREEN = "\u001B[32m";
    public final String YELLOW = "\u001B[33m";
    public final String CYAN = "\u001B[1;96m";
    public final String PURPLE = "\u001B[95m";
    public final String RESET = "\u001B[0m";

    private UpdateVideoGame updateVideoGame;
    private GameManager mockGameManager;

    @BeforeEach
    void setUp() {
        System.out.println("\n" + YELLOW + "========= Setting up UpdateVideoGameTest =========" + RESET);
        mockGameManager = mock(GameManager.class);
        System.out.println(YELLOW + "==================================================\n" + RESET);
    }

    @Test
    void testUpdateGame_GameNotFound() {
        System.out.println(CYAN + "===== TEST: Update Game (Game Not Found) =====" + RESET);

        // Simulated Input - Inserts similar to a scanner
        String simulatedInput = "1\n101\n"; // userID, gameID
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        updateVideoGame = new UpdateVideoGame(new Scanner(System.in));

        // Mocking: findGame returns null
        when(mockGameManager.findGame(1, 101)).thenReturn(null);

        String result = updateVideoGame.updateGame(mockGameManager);
        System.out.println(GREEN + "\nResult: " + result + RESET);

        System.out.println(YELLOW + "Expected an error containing: 'ERROR! Game not found with that combination. Try again'" + RESET);
        System.out.println(PURPLE + "Actual:   " + result + RESET);

        assertTrue(result.contains("ERROR! Game not found with that combination. Try again"),
                "Should return an error message when the game is not found.");

        System.out.println(CYAN + "===== END TEST: Update Game (Game Not Found) =====\n" + RESET);
    }

    @Test
    void testUpdateGame_ChangeTitle() {
        System.out.println(CYAN + "===== TEST: Update Game (Change Title) =====" + RESET);

        // Spy uses actual logic from VideoGame
        VideoGame spyGame = spy(new VideoGame(1, 101, "Old Title", 2022, 10));
        String simulatedInput = "1\n101\n2\nNew Title\n6\n"; // userID, gameID, option=2 (title), "New Title", option=6 (confirm)
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        updateVideoGame = new UpdateVideoGame(new Scanner(System.in));
        when(mockGameManager.findGame(1, 101)).thenReturn(spyGame);

        String result = updateVideoGame.updateGame(mockGameManager);

        System.out.println(YELLOW + "\nOld Title:  Old Title" + RESET);
        System.out.println(GREEN + "New Title:  " + spyGame.getGameTitle() + RESET);

        System.out.println(YELLOW + "Expected Title: 'New Title'" + RESET);
        System.out.println(PURPLE + "Actual Title:   " + spyGame.getGameTitle() + RESET);

        assertEquals("New Title", spyGame.getGameTitle(), "Game title should be updated.");
        assertTrue(result.contains("Update Finished"), "Should return success message after updating.");

        System.out.println(CYAN + "===== END TEST: Update Game (Change Title) =====\n" + RESET);
    }

    @Test
    void testUpdateGame_ChangeAchievementsCompleted() {
        System.out.println(CYAN + "===== TEST: Update Game (Change Achievements Completed) =====" + RESET);

        VideoGame spyGame = spy(new VideoGame(1, 101, "Test Game", 2022, 10));
        String simulatedInput = "1\n101\n5\n5\n6\n"; // userID, gameID, option=5 (achievements completed), "5", option=6 (confirm)
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        updateVideoGame = new UpdateVideoGame(new Scanner(System.in));
        when(mockGameManager.findGame(1, 101)).thenReturn(spyGame);

        String result = updateVideoGame.updateGame(mockGameManager);

        System.out.println(YELLOW + "\nOld Completed: 0" + RESET);
        System.out.println(GREEN + "New Completed: " + spyGame.getNumAchievementsCompleted() + RESET);

        System.out.println(YELLOW + "Expected Completed: 5" + RESET);
        System.out.println(PURPLE + "Actual Completed:   " + spyGame.getNumAchievementsCompleted() + RESET);

        assertEquals(5, spyGame.getNumAchievementsCompleted(), "Completed achievements should be updated.");
        assertFalse(spyGame.GameCompleted(), "Game should not be marked as completed yet.");
        assertTrue(result.contains("Update Finished"), "Should return success message.");

        System.out.println(CYAN + "===== END TEST: Update Game (Change Achievements Completed) =====\n" + RESET);
    }

    @Test
    void testUpdateGame_CompleteGame() {
        System.out.println(CYAN + "===== TEST: Update Game (Complete All Achievements) =====" + RESET);

        VideoGame spyGame = spy(new VideoGame(1, 101, "Test Game", 2022, 10));
        String simulatedInput = "1\n101\n5\n10\n6\n"; // userID, gameID, option=5 (achievements completed), "10", option=6 (confirm)
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        updateVideoGame = new UpdateVideoGame(new Scanner(System.in));
        when(mockGameManager.findGame(1, 101)).thenReturn(spyGame);

        String result = updateVideoGame.updateGame(mockGameManager);

        System.out.println(YELLOW + "\nOld Completed: 0" + RESET);
        System.out.println(GREEN + "New Completed: " + spyGame.getNumAchievementsCompleted() + RESET);

        System.out.println(YELLOW + "Expected Completed: 10" + RESET);
        System.out.println(PURPLE + "Actual Completed:   " + spyGame.getNumAchievementsCompleted() + RESET);

        assertEquals(10, spyGame.getNumAchievementsCompleted(), "Completed achievements should be updated.");
        assertTrue(spyGame.GameCompleted(), "Game should be marked as completed.");
        assertTrue(result.contains("Update Finished"), "Should return success message.");

        System.out.println(CYAN + "===== END TEST: Update Game (Complete All Achievements) =====\n" + RESET);
    }
}
