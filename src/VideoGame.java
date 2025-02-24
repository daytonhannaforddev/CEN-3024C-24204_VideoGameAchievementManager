/*Dayton Hannaford,
CEN-3024C-24204

This class houses the VideoGame object in which all added titles are created from. */
public class VideoGame {
    private int userID;
    private int gameID;
    private String gameTitle;
    private int gameReleaseYear;
    private int numTotalAchievements;
    private int numAchievementsCompleted;
    private boolean gameCompleted;

    // CONSTRUCTOR
    public VideoGame(int userID, int gameID, String gameTitle, int gameReleaseYear, int numTotalAchievements) {
        this.userID = userID;
        this.gameID = gameID;
        this.gameTitle = gameTitle;
        this.gameReleaseYear = gameReleaseYear;
        this.numTotalAchievements = numTotalAchievements;
        this.numAchievementsCompleted = 0;
        this.gameCompleted = false;
    }

    // GETTERS
    public int getUserID() { return userID; }
    public int getGameID() { return gameID; }
    public String getGameTitle() { return gameTitle; }
    public int getGameReleaseYear() { return gameReleaseYear; }
    public int getNumTotalAchievements() { return numTotalAchievements; }
    public int getNumAchievementsCompleted() { return numAchievementsCompleted; }
    public boolean GameCompleted() { return gameCompleted; }


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


    // CUSTOM ACTION
    public String calculateCompletionPercentage() {
        if (numTotalAchievements == 0) return "0.00%";
        double percentage = (double) numAchievementsCompleted / numTotalAchievements * 100;
        return String.format("%.2f%%", percentage);
    }
}
