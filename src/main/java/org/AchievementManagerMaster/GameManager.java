/*Dayton Hannaford,
CEN-3024C-24204 */

package org.AchievementManagerMaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
/**
 * This class houses the core game logic for the Video Game Achievement Manager.
 * It validates input, handles user operations, and communicates with the DatabaseManager
 * to persist game data.
 * <p>
 * Dayton Hannaford, CEN-3024C-24204
 * </p>
 *
 * @author
 * @version 1.0
 */
public class GameManager {

    private DatabaseManager dbManager;
    private int currentUserID;

    /**
     * Constructs a new GameManager with the given user credentials and database connection details.
     *
     * @param userID   the ID of the current user
     * @param url      the URL for the database connection
     * @param username the username for the database connection
     * @param password the password for the database connection
     */
    public GameManager(int userID, String url, String username, String password) {
        this.currentUserID = userID;
        try {
            dbManager = new DatabaseManager(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return a list of VideoGame objects for the current user
     */
    public List<VideoGame> getGamesForUser() {
        return dbManager.getGamesForUser(currentUserID);
    }

    /**
     * @param title                 the title of the video game
     * @param releaseYear           the release year of the video game
     * @param totalAchievements     the total number of achievements available in the game
     * @param achievementsCompleted the number of achievements completed
     * @return an HTML formatted string message indicating the result of the operation
     */
    public String addVideoGame(String title, int releaseYear, int totalAchievements, int achievementsCompleted) {
        if(title == null || title.trim().isEmpty()) {
            return "<html><font color='red'>ERROR! Game title cannot be empty!</font></html>";
        }
        int currentYear = LocalDate.now().getYear();
        if(releaseYear < 1959 || releaseYear > currentYear) {
            return "<html><font color='red'>ERROR! Release year must be between 1959 and the present.</font></html>";
        }
        if(totalAchievements < 0) {
            return "<html><font color='red'>ERROR! Total Achievements cannot be negative!</font></html>";
        }
        if(achievementsCompleted < 0 || achievementsCompleted > totalAchievements) {
            return "<html><font color='red'>ERROR! Achievements Completed is invalid.</font></html>";
        }
        VideoGame vg = new VideoGame(currentUserID, 0, title, releaseYear, totalAchievements);
        vg.setNumAchievementsCompleted(achievementsCompleted);
        boolean success = dbManager.insertGame(vg);
        if(success) {
            return "<html><font color='green'>SUCCESS! Game added successfully.</font></html>";
        } else {
            return "<html><font color='red'>ERROR! Failed to add game to database.</font></html>";
        }
    }

    /**
     * @param gameID the ID of the game to be removed
     * @return an HTML formatted string message indicating the result of the operation
     */
    public String removeVideoGame(int gameID) {
        boolean success = dbManager.deleteGame(currentUserID, gameID);
        if(success) {
            return "<html><font color='green'>SUCCESS! Game removed.</font></html>";
        } else {
            return "<html><font color='red'>ERROR! Game not found.</font></html>";
        }
    }

    /**
     * @param gameID                   the ID of the game to update
     * @param newTitle                 the new title for the game (if provided)
     * @param newReleaseYear           the new release year for the game (if provided)
     * @param newTotalAchievements     the new total number of achievements (if provided)
     * @param newAchievementsCompleted the new number of achievements completed (if provided)
     * @return an HTML formatted string message indicating the result of the update operation
     */
    public String updateVideoGame(int gameID, String newTitle, Integer newReleaseYear, Integer newTotalAchievements, Integer newAchievementsCompleted) {
        VideoGame game = null;

        List<VideoGame> userGames = dbManager.getGamesForUser(currentUserID);

        for(VideoGame vg : userGames) {
            if(vg.getGameID() == gameID) {
                game = vg;
                break;
            }
        }
        if(game == null) {
            return "<html><font color='red'>ERROR! Game not found for the given Game ID.</font></html>";
        }

        if(newTitle != null && !newTitle.trim().isEmpty()) {
            game.setGameTitle(newTitle);
        }
        int currentYear = LocalDate.now().getYear();
        if(newReleaseYear != null && newReleaseYear >= 1959 && newReleaseYear <= currentYear) {
            game.setGameReleaseYear(newReleaseYear);
        }
        if(newTotalAchievements != null && newTotalAchievements >= 0) {
            if(newAchievementsCompleted != null && newAchievementsCompleted > newTotalAchievements) {
                return "<html><font color='red'>ERROR! Achievements Completed cannot be more than Total Achievements.</font></html>";
            }
            game.setNumTotalAchievements(newTotalAchievements);
        }
        if(newAchievementsCompleted != null && newAchievementsCompleted >= 0) {
            if(newTotalAchievements == null && newAchievementsCompleted > game.getNumTotalAchievements()) {
                return "<html><font color='red'>ERROR! Achievements Completed cannot be more than Total Achievements.</font></html>";
            }
            game.setNumAchievementsCompleted(newAchievementsCompleted);
        }
        boolean updated = dbManager.updateGame(game);
        if(updated) {
            return "<html><font color='green'>SUCCESS! Game updated successfully.</font></html>";
        } else {
            return "<html><font color='red'>ERROR! Failed to update game in database.</font></html>";
        }
    }
    /**
     * @param filePath the path to the file containing game data
     * @return an HTML formatted string message indicating the result of the import operation
     */
    public String importVideoGames(String filePath) {
        File file = new File(filePath);
        if(!file.exists()) {
            System.err.println("<html><font color='red'>ERROR: File " + filePath + " does not exist.</font></html>");
            return "<html><font color='red'>ERROR! Import failed. File does not exist.</font></html>";
        }
        String fileName = file.getName().toLowerCase();
        if(!(fileName.endsWith(".csv") || fileName.endsWith(".txt"))) {
            System.err.println("<html><font color='red'>ERROR: Invalid file type. Only CSV or TXT files are supported.</font></html>");
            return "<html><font color='red'>ERROR! Import failed. Unsupported file type.</font></html>";
        }
        boolean anyInvalidData = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int currentYear = LocalDate.now().getYear();
            while ((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if(tokens.length < 4) {
                    System.err.println("<html><font color='red'>ERROR! Not enough fields: </font></html>" + line);
                    anyInvalidData = true;
                    continue;
                }
                try {
                    String gameTitle = tokens[0].trim();
                    int releaseYear = Integer.parseInt(tokens[1].trim());
                    int totalAchievements = Integer.parseInt(tokens[2].trim());
                    int achievementsCompleted = Integer.parseInt(tokens[3].trim());

                    if(gameTitle.isEmpty() || releaseYear < 1959 || releaseYear > currentYear ||
                            totalAchievements < 0 || achievementsCompleted < 0 || achievementsCompleted > totalAchievements) {
                        System.err.println("<html><font color='red'>ERROR Game not added! Invalid data: </font></html>" + line);
                        anyInvalidData = true;
                        continue;
                    }
                    VideoGame vg = new VideoGame(currentUserID, 0, gameTitle, releaseYear, totalAchievements);
                    vg.setNumAchievementsCompleted(achievementsCompleted);
                    if(!dbManager.insertGame(vg)) {
                        anyInvalidData = true;
                    }
                } catch(NumberFormatException ex) {
                    System.err.println("<html><font color='red'>ERROR Game not added! Number format error: </font></html>");
                    anyInvalidData = true;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
            return "<html><font color='red'>ERROR! Import failed due to an IOException.</font></html>";
        }
        if(anyInvalidData) {
            return "<html><font color='red'>ERROR! Some games were not imported due to invalid data.</font></html>";
        } else {
            return "<html><font color='green'>SUCCESS! Games imported successfully!</font></html>";
        }
    }
}

