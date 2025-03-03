/*Dayton Hannaford,
CEN-3024C-24204

Game Manager tests for a multitude of methods utilized in making the class and overall program function.*/


package org.AchievementManagerMaster;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    // ~~~ Colors ~~~
    public final String RED = "\u001B[31m";
    public final String GREEN = "\u001B[32m";
    public final String YELLOW = "\u001B[33m";
    public final String CYAN = "\u001B[1;96m";
    public final String PURPLE = "\u001B[95m";
    public final String BLINK_ORANGE = "\u001B[5;38;5;208m";
    public final String ORANGE = "\u001B[38;5;208m";
    final String BOLD = "\u001B[1m";
    public final String RESET = "\u001B[0m";

    private GameManager gameManager;


    @BeforeEach
    void setUp() {
        System.out.println("\n" + ORANGE + "========= Setting up GameManager for Testing =========" + RESET);
        gameManager = new GameManager();
        System.out.println(ORANGE + "======================================================\n" + RESET);
    }


    @Test
    void testAddGame() {
        System.out.println(CYAN + "===== TEST: Add Game =====" + RESET);
        VideoGame game = new VideoGame(1, 101, "Test Game", 2022, 10);
        gameManager.addGame(game);

        VideoGame foundGame = gameManager.findGame(1, 101);
        System.out.println(PURPLE + "Added game: " + game.getGameTitle() + RESET +
                "\n" + GREEN + "Found: " + (foundGame != null ? foundGame.getGameTitle() : "null") + RESET);

        assertNotNull(foundGame, "Game should be found after being added.");
        System.out.println(CYAN + "===== END TEST: Add Game =====\n" + RESET);
    }

    @Test
    void testRemoveGame() {
        System.out.println(CYAN + "===== TEST: Remove Game =====" + RESET);

        VideoGame game = new VideoGame(1, 101, "Test Game", 2022, 10);
        gameManager.addGame(game);

        boolean removed = gameManager.removeGame(1, 101);
        VideoGame foundGame = gameManager.findGame(1, 101);

        System.out.println(YELLOW + "Game removed: " + removed + RESET +
                "\n" + GREEN + "Found after removal: " + (foundGame == null ? "null" : "still exists") + RESET);

        assertTrue(removed, "Game should be removed successfully.");
        assertNull(foundGame, "Game should not exist after removal.");

        System.out.println(CYAN + "===== END TEST: Remove Game =====\n" + RESET);
    }


    @Test
    void testDisplayUserGames() {
        System.out.println(CYAN + "===== TEST: Display User Games =====" + RESET);
        VideoGame game = new VideoGame(1, 101, "Test Game", 2022, 10);
        gameManager.addGame(game);

        String result = gameManager.displayUserGames(1);
        System.out.println(GREEN + "Display result:\n" + result + RESET);

        assertTrue(result.contains("Test Game"), "Display should include added game title.");
        System.out.println(CYAN + "===== END TEST: Display User Games =====\n" + RESET);
    }


    @Test
    void testLoadGamesFromFileSuccess() throws IOException {
        System.out.println(CYAN + "===== TEST: Load Games (Valid File) =====" + RESET);
        File tempFile = File.createTempFile("test_games", ".csv");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("1,101,GameTitle,2020,10,5\n");
        }

        boolean loaded = gameManager.loadGamesFromFile(tempFile.getAbsolutePath());
        System.out.println(PURPLE + "File loaded: " + loaded + RESET);

        assertTrue(loaded, "File should load successfully.");
        System.out.println(CYAN + "===== END TEST: Load Games (Valid File) =====\n" + RESET);
    }


    @Test
    void testLoadGamesFromFileFileNotFound() {
        System.out.println(CYAN + "===== TEST: Load Games (File Not Found) =====" + RESET);
        boolean loaded = gameManager.loadGamesFromFile("nonexistent.csv");
        System.out.println(PURPLE + "File loaded: " + loaded + RESET);

        assertFalse(loaded, "Should return false for missing file.");
        System.out.println(CYAN + "===== END TEST: Load Games (File Not Found) =====\n" + RESET);
    }


    @Test
    void testLoadGamesFromFileInvalidFormat() throws IOException {
        System.out.println(CYAN + "===== TEST: Load Games (Invalid Data) =====" + RESET);
        File tempFile = File.createTempFile("test_invalid_games", ".csv");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("INVALID DATA\n");
        }

        boolean loaded = gameManager.loadGamesFromFile(tempFile.getAbsolutePath());
        System.out.println(PURPLE + "File loaded: " + loaded + RESET);

        assertFalse(loaded, "Should fail on invalid data format.");
        System.out.println(CYAN + "===== END TEST: Load Games (Invalid Data) =====\n" + RESET);
    }


    @Test
    void testIsGameIdUniqueForUser() {
        System.out.println(CYAN + "===== TEST: isGameIdUniqueForUser =====" + RESET);
        VideoGame game = new VideoGame(1, 101, "Test Game", 2022, 10);
        gameManager.addGame(game);

        boolean uniqueBeforeAdding = gameManager.isGameIdUniqueForUser(1, 102);
        boolean uniqueAfterAdding = gameManager.isGameIdUniqueForUser(1, 101);

        System.out.println(GREEN + "Unique before adding: " + uniqueBeforeAdding + RESET +
                " | " + RED + "Unique after adding: " + uniqueAfterAdding + RESET);

        assertFalse(uniqueAfterAdding, "Game ID should not be unique after adding.");
        assertTrue(uniqueBeforeAdding, "Different Game ID should be unique.");
        System.out.println(CYAN + "===== END TEST: isGameIdUniqueForUser =====\n" + RESET);
    }


    @Test
    void testFindGame() {
        System.out.println(CYAN + "===== TEST: findGame =====" + RESET);
        VideoGame game = new VideoGame(1, 101, "Test Game", 2022, 10);
        gameManager.addGame(game);

        VideoGame foundGame = gameManager.findGame(1, 101);
        VideoGame notFoundGame = gameManager.findGame(1, 999);

        System.out.println(YELLOW + "Found game: " + (foundGame != null ? foundGame.getGameTitle() : "null") + RESET);
        System.out.println(GREEN + "Nonexistent game found: " + (notFoundGame != null ? notFoundGame.getGameTitle() : "null") + RESET);

        assertNotNull(foundGame, "Game should be found.");
        assertNull(notFoundGame, "Nonexistent game should return null.");
        System.out.println(CYAN + "===== END TEST: findGame =====\n" + RESET);
    }


    @Test
    void testUpdateGame() {
        System.out.println(CYAN + "===== TEST: updateGame =====" + RESET);


        VideoGame game = new VideoGame(1, 101, "Old Title", 2022, 10);
        gameManager.addGame(game);


        VideoGame foundGame = gameManager.findGame(1, 101);
        assertNotNull(foundGame, "Game should exist before update.");


        System.out.println(YELLOW + "Updating game details..." + RESET);
        foundGame.setGameTitle("New Title");
        foundGame.setNumAchievementsCompleted(5);


        VideoGame updatedGame = gameManager.findGame(1, 101);
        System.out.println(PURPLE + "Updated Title: " + updatedGame.getGameTitle() + RESET +
                "\n" + GREEN + "Updated Achievements: " + updatedGame.getNumAchievementsCompleted() + RESET);

        assertEquals("New Title", updatedGame.getGameTitle(), "Game title should be updated.");
        assertEquals(5, updatedGame.getNumAchievementsCompleted(), "Completed achievements should be updated.");
        System.out.println(CYAN + "===== END TEST: updateGame =====\n" + RESET);
    }

    @Test
    void testCalculateCompletionPercentage() {
        System.out.println(CYAN + "===== TEST: Calculate Completion Percentage =====" + RESET);


        VideoGame game = new VideoGame(1, 101, "Completion Test", 2022, 10);
        game.setNumAchievementsCompleted(5);

        System.out.println(YELLOW + "Expected: 50.00%" + RESET);
        System.out.println(GREEN + "Actual:   " + game.calculateCompletionPercentage() + RESET);

        assertEquals("50.00%", game.calculateCompletionPercentage(), "Completion percentage should be 50.00%.");
        System.out.println(CYAN + "===== END TEST: Calculate Completion Percentage =====\n" + RESET);
    }
}
