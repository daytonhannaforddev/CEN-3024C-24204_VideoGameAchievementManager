/*Dayton Hannaford,
CEN-3024C-24204

Video Game class tests for a multitude of methods utilized in making the class and overall program function. Also contains methods for UpdatingVideoGame class as to keep getter and setters separate from update logic.*/

package org.AchievementManagerMaster;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VideoGameTest {

    // ~~~ Colors ~~~
    public final String RED = "\u001B[31m";
    public final String GREEN = "\u001B[32m";
    public final String YELLOW = "\u001B[33m";
    public final String CYAN = "\u001B[1;96m";
    public final String PURPLE = "\u001B[95m";
    public final String RESET = "\u001B[0m";

    private VideoGame videoGame;

    @BeforeEach
    void setUp() {
        System.out.println("\n" + YELLOW + "========= Setting up VideoGame for Testing =========" + RESET);
        videoGame = new VideoGame(1, 101, "Test Game", 2022, 10);
        System.out.println(YELLOW + "=====================================================\n" + RESET);
    }

    @Test
    void testGetUserID() {
        System.out.println(CYAN + "===== TEST: getUserID =====" + RESET);
        System.out.println(GREEN + "Expected: 1" + RESET);
        System.out.println(PURPLE + "Actual:   " + videoGame.getUserID() + RESET);
        assertEquals(1, videoGame.getUserID(), "User ID should be 1.");
        System.out.println(CYAN + "===== END TEST: getUserID =====\n" + RESET);
    }

    @Test
    void testGetGameID() {
        System.out.println(CYAN + "===== TEST: getGameID =====" + RESET);
        System.out.println(GREEN + "Expected: 101" + RESET);
        System.out.println(PURPLE + "Actual:   " + videoGame.getGameID() + RESET);
        assertEquals(101, videoGame.getGameID(), "Game ID should be 101.");
        System.out.println(CYAN + "===== END TEST: getGameID =====\n" + RESET);
    }

    @Test
    void testGetGameTitle() {
        System.out.println(CYAN + "===== TEST: getGameTitle =====" + RESET);
        System.out.println(GREEN + "Expected: 'Test Game'" + RESET);
        System.out.println(PURPLE + "Actual:   '" + videoGame.getGameTitle() + "'" + RESET);
        assertEquals("Test Game", videoGame.getGameTitle(), "Game title should be 'Test Game'.");
        System.out.println(CYAN + "===== END TEST: getGameTitle =====\n" + RESET);
    }

    @Test
    void testGetGameReleaseYear() {
        System.out.println(CYAN + "===== TEST: getGameReleaseYear =====" + RESET);
        System.out.println(GREEN + "Expected: 2022" + RESET);
        System.out.println(PURPLE + "Actual:   " + videoGame.getGameReleaseYear() + RESET);
        assertEquals(2022, videoGame.getGameReleaseYear(), "Game release year should be 2022.");
        System.out.println(CYAN + "===== END TEST: getGameReleaseYear =====\n" + RESET);
    }

    @Test
    void testGetNumTotalAchievements() {
        System.out.println(CYAN + "===== TEST: getNumTotalAchievements =====" + RESET);
        System.out.println(GREEN + "Expected: 10" + RESET);
        System.out.println(PURPLE + "Actual:   " + videoGame.getNumTotalAchievements() + RESET);
        assertEquals(10, videoGame.getNumTotalAchievements(), "Total achievements should be 10.");
        System.out.println(CYAN + "===== END TEST: getNumTotalAchievements =====\n" + RESET);
    }

    @Test
    void testGetNumAchievementsCompleted() {
        System.out.println(CYAN + "===== TEST: getNumAchievementsCompleted =====" + RESET);
        System.out.println(GREEN + "Expected: 0" + RESET);
        System.out.println(PURPLE + "Actual:   " + videoGame.getNumAchievementsCompleted() + RESET);
        assertEquals(0, videoGame.getNumAchievementsCompleted(), "Initially, completed achievements should be 0.");
        System.out.println(CYAN + "===== END TEST: getNumAchievementsCompleted =====\n" + RESET);
    }

    @Test
    void testGameCompletedInitiallyFalse() {
        System.out.println(CYAN + "===== TEST: gameCompleted (Initially) =====" + RESET);
        System.out.println(GREEN + "Expected: false" + RESET);
        System.out.println(PURPLE + "Actual:   " + videoGame.GameCompleted() + RESET);
        assertFalse(videoGame.GameCompleted(), "Game should not be completed initially.");
        System.out.println(CYAN + "===== END TEST: gameCompleted (Initially) =====\n" + RESET);
    }

    @Test
    void testSetGameTitle() {
        System.out.println(CYAN + "===== TEST: setGameTitle =====" + RESET);
        String oldTitle = videoGame.getGameTitle();

        System.out.println(YELLOW + "Old Title: '" + oldTitle + "'" + RESET);
        videoGame.setGameTitle("New Title");
        System.out.println(GREEN + "New Title: '" + videoGame.getGameTitle() + "'" + RESET);

        assertEquals("New Title", videoGame.getGameTitle(), "Game title should update to 'New Title'.");
        System.out.println(CYAN + "===== END TEST: setGameTitle =====\n" + RESET);
    }

    @Test
    void testSetNumAchievementsCompleted() {
        System.out.println(CYAN + "===== TEST: setNumAchievementsCompleted =====" + RESET);
        int oldCompleted = videoGame.getNumAchievementsCompleted();

        System.out.println(YELLOW + "Old Completed: " + oldCompleted + RESET);
        videoGame.setNumAchievementsCompleted(5);
        System.out.println(GREEN + "New Completed: " + videoGame.getNumAchievementsCompleted() + RESET);

        assertEquals(5, videoGame.getNumAchievementsCompleted(), "Completed achievements should be 5.");
        System.out.println(CYAN + "===== END TEST: setNumAchievementsCompleted =====\n" + RESET);
    }

    @Test
    void testSetGameCompleted() {
        System.out.println(CYAN + "===== TEST: setGameCompleted =====" + RESET);
        videoGame.setNumAchievementsCompleted(10);
        videoGame.setGameCompleted(videoGame.getNumAchievementsCompleted() == videoGame.getNumTotalAchievements());

        System.out.println(GREEN + "Achievements Completed: " + videoGame.getNumAchievementsCompleted() + RESET);
        System.out.println(GREEN + "Total Achievements: " + videoGame.getNumTotalAchievements() + RESET);
        System.out.println(PURPLE + "Game Completed?: " + videoGame.GameCompleted() + RESET);

        assertTrue(videoGame.GameCompleted(), "Game should be marked as completed.");
        System.out.println(CYAN + "===== END TEST: setGameCompleted =====\n" + RESET);
    }

    @Test
    void testCalculateCompletionPercentage() {
        System.out.println(CYAN + "===== TEST: calculateCompletionPercentage =====" + RESET);
        videoGame.setNumAchievementsCompleted(5);

        System.out.println(YELLOW + "Achievements Completed: " + videoGame.getNumAchievementsCompleted() +
                "/" + videoGame.getNumTotalAchievements() + RESET);
        System.out.println(GREEN + "Calculated: " + videoGame.calculateCompletionPercentage() + RESET);

        assertEquals("50.00%", videoGame.calculateCompletionPercentage(), "Completion percentage should be 50.00%.");
        System.out.println(CYAN + "===== END TEST: calculateCompletionPercentage =====\n" + RESET);
    }

    @Test
    void testCalculateCompletionPercentage_ZeroAchievements() {
        System.out.println(CYAN + "===== TEST: calculateCompletionPercentage (Zero Achievements) =====" + RESET);
        VideoGame newGame = new VideoGame(2, 102, "Another Game", 2023, 0);

        System.out.println(YELLOW + "Achievements Completed: " + newGame.getNumAchievementsCompleted() +
                "/" + newGame.getNumTotalAchievements() + RESET);
        System.out.println(GREEN + "Calculated: " + newGame.calculateCompletionPercentage() + RESET);

        assertEquals("0.00%", newGame.calculateCompletionPercentage(), "Completion percentage should be 0.00% when no achievements exist.");
        System.out.println(CYAN + "===== END TEST: calculateCompletionPercentage (Zero Achievements) =====\n" + RESET);
    }
}
