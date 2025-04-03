/*Dayton Hannaford,
CEN-3024C-24204

This class houses the VideoGame object in which all added titles are created from.
It represents a game record and includes fields for title, release year, achievements, and computed metrics. */

package org.AchievementManagerMaster;

/**
 * Houses the VideoGame object in which all added titles are created from.
 */

public class VideoGame {
    private int userID;
    private int gameID;
    private String gameTitle;
    private int gameReleaseYear;
    private int numTotalAchievements;
    private int numAchievementsCompleted;
    private boolean gameCompleted;
    private double completionPercentage;

    /**
     * @param userID                the ID of the user associated with this game
     * @param gameID                the unique game identifier
     * @param gameTitle             the title of the game
     * @param gameReleaseYear       the year the game was released
     * @param numTotalAchievements  the total number of achievements available in the game
     */
    public VideoGame(int userID, int gameID, String gameTitle, int gameReleaseYear, int numTotalAchievements) {
        this.userID = userID;
        this.gameID = gameID;
        this.gameTitle = gameTitle;
        this.gameReleaseYear = gameReleaseYear;
        this.numTotalAchievements = numTotalAchievements;
        this.numAchievementsCompleted = 0;
        this.gameCompleted = false;
        this.completionPercentage = 0.0;
    }

    // GETTERS
    public int getUserID() { return userID; }
    public int getGameID() { return gameID; }
    public String getGameTitle() { return gameTitle; }
    public int getGameReleaseYear() { return gameReleaseYear; }
    public int getNumTotalAchievements() { return numTotalAchievements; }
    public int getNumAchievementsCompleted() { return numAchievementsCompleted; }
    public boolean getGameCompleted() { return gameCompleted; }
    public double getCompletionPercentage() { return completionPercentage; }

    // SETTERS
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }
    public void setGameReleaseYear(int gameReleaseYear) {
        this.gameReleaseYear = gameReleaseYear;
    }
    public void setNumTotalAchievements(int numTotalAchievements) {
        this.numTotalAchievements = numTotalAchievements;
    }
    public void setNumAchievementsCompleted(int numAchievementsCompleted) {
        this.numAchievementsCompleted = numAchievementsCompleted;
    }
    public void setGameCompleted(boolean gameCompleted) {
        this.gameCompleted = gameCompleted;
    }
    public void setCompletionPercentage(double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }
}
