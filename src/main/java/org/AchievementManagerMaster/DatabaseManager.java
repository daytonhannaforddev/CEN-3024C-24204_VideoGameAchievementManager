/*Dayton Hannaford,
CEN-3024C-24204

This class handles database connectivity and operations for the Video Game Achievement Manager.
It provides methods for inserting, updating, deleting, and retrieving game records from the database. */

package org.AchievementManagerMaster;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Handles database connectivity and operations.
 *
 */
public class DatabaseManager {
    private Connection conn;

    /**
     * @param url      the database URL (must not be null or blank)
     * @param username the database username
     * @param password the database password
     * @throws SQLException           if a database access error occurs or the URL is invalid
     * @throws IllegalArgumentException if the provided URL is null or empty
     */
    public DatabaseManager(String url, String username, String password) throws SQLException {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("Database URL should not be blank!");
        }
        conn = DriverManager.getConnection(url, username, password);
    }

    /**
     * @param game the VideoGame object representing the game to insert
     * @return true if the game was successfully inserted; false otherwise
     */
    public boolean insertGame(VideoGame game) {
        String sql = "INSERT INTO games (user_id, title, release_year, total_achievements, achievements_completed) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, game.getUserID());
            stmt.setString(2, game.getGameTitle());
            stmt.setInt(3, game.getGameReleaseYear());
            stmt.setInt(4, game.getNumTotalAchievements());
            stmt.setInt(5, game.getNumAchievementsCompleted());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    game.setGameID(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param game the VideoGame object containing the updated game information
     * @return true if the game was successfully updated; false otherwise
     */
    public boolean updateGame(VideoGame game) {
        String sql = "UPDATE games SET title = ?, release_year = ?, total_achievements = ?, achievements_completed = ? " +
                "WHERE user_id = ? AND game_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, game.getGameTitle());
            stmt.setInt(2, game.getGameReleaseYear());
            stmt.setInt(3, game.getNumTotalAchievements());
            stmt.setInt(4, game.getNumAchievementsCompleted());
            stmt.setInt(5, game.getUserID());
            stmt.setInt(6, game.getGameID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param userID the user ID associated with the game
     * @param gameID the game ID of the game to be deleted
     * @return true if the game was successfully deleted; false otherwise
     */
    public boolean deleteGame(int userID, int gameID) {
        String sql = "DELETE FROM games WHERE user_id = ? AND game_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, gameID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param userID the user ID used to retrieve game records
     * @return a List of VideoGame objects associated with the given user; an empty list if no records are found
     */
    public List<VideoGame> getGamesForUser(int userID) {
        List<VideoGame> games = new ArrayList<>();
        String sql = "SELECT game_id, user_id, title, release_year, total_achievements, achievements_completed, " +
                "completion_percentage, game_completed FROM games WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int gameID = rs.getInt("game_id");
                    String title = rs.getString("title");
                    int releaseYear = rs.getInt("release_year");
                    int totalAchievements = rs.getInt("total_achievements");
                    int achievementsCompleted = rs.getInt("achievements_completed");
                    double completionPercentage = rs.getDouble("completion_percentage");
                    boolean gameCompleted = rs.getBoolean("game_completed");

                    VideoGame game = new VideoGame(userID, gameID, title, releaseYear, totalAchievements);
                    game.setNumAchievementsCompleted(achievementsCompleted);
                    game.setCompletionPercentage(completionPercentage);
                    game.setGameCompleted(gameCompleted);
                    games.add(game);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return games;
    }
}
