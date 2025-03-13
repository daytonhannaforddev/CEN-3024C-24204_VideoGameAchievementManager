package org.AchievementManagerMaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class GameManager {

    private final Map<Integer, List<VideoGame>> games;

    public GameManager() {
        games = new HashMap<>();
    }

    public List<VideoGame> getGamesForUser(int userID) {
        return games.getOrDefault(userID, new ArrayList<>());
    }

    // Refactored
    public String addVideoGame(int userID, int gameID, String title, int releaseYear, int totalAchievements, int achievementsCompleted) {
        if(userID < 0) {
            return "<html><font color='red'>ERROR! User ID cannot be negative!</font></html>";
        }
        if(gameID < 0) {
            return "<html><font color='red'>ERROR! Game ID cannot be negative!</font></html>";
        }
        if(!isGameIdUniqueForUser(userID, gameID)) {
            return "<html><font color='red'>ERROR! Game ID already exists for the given User ID.</font></html>";
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
        boolean gameCompleted = (achievementsCompleted == totalAchievements);
        VideoGame vg = new VideoGame(userID, gameID, title, releaseYear, totalAchievements);
        vg.setNumAchievementsCompleted(achievementsCompleted);
        vg.setGameCompleted(gameCompleted);
        addGame(vg);
        return "<html><font color='green'>SUCCESS! Game added successfully.</font></html>";
    }

    // Refactored
    public String removeVideoGame(int userID, int gameID) {
        if(removeGame(userID, gameID)) {
            return "<html><font color='green'>SUCCESS! Game removed.</font></html>";
        } else {
            return "<html><font color='red'>ERROR! Game not found.</font></html>";
        }
    }

// Refactored
    public String importVideoGames(String filePath) {
        if(loadGamesFromFile(filePath)) {
            return "<html><font color='green'>SUCCESS! Games imported successfully!</font></html>";
        } else {
            return "<html><font color='red'>ERROR! Import failed! Please check the file or file path and try again.</font></html>";
        }
    }

// Update logic refactored
    public String updateVideoGame(int userID, int gameID, Integer newGameID, String newTitle, Integer newReleaseYear, Integer newTotalAchievements, Integer newAchievementsCompleted) {
        VideoGame game = findGame(userID, gameID);
        if(game == null) {
            return "<html><font color='red'>ERROR! Game not found for the given User ID and Game ID.</font></html>";
        }

        if(newGameID != null && newGameID >= 0) {
            if(newGameID != gameID && !isGameIdUniqueForUser(userID, newGameID)) {
                return "<html><font color='red'>ERROR! New Game ID is not unique for this User.</font></html>";
            }
            game.setGameID(newGameID);
        }
        if(newTitle != null && !newTitle.trim().isEmpty()) {
            game.setGameTitle(newTitle);
        }
        int currentYear = LocalDate.now().getYear();
        if(newReleaseYear != null && newReleaseYear >= 1959 && newReleaseYear <= currentYear) {
            game.setGameReleaseYear(newReleaseYear);
        }
        if(newTotalAchievements != null && newTotalAchievements >= 0) {
            // Makes sure updating total achievements, ensures that the current achievementsCompleted is not greater than the new total.
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

        game.setGameCompleted(game.getNumAchievementsCompleted() == game.getNumTotalAchievements());
        return "<html><font color='green'>SUCCESS! Game updated successfully.</font></html>";
    }



    public void addGame(VideoGame game) {
        int userID = game.getUserID();
        List<VideoGame> userGames = games.getOrDefault(userID, new ArrayList<>());
        userGames.add(game);
        games.put(userID, userGames);
    }


    public boolean removeGame(int userID, int gameID) {
        List<VideoGame> userGames = games.get(userID);
        if(userGames != null) {
            Iterator<VideoGame> iter = userGames.iterator();
            while(iter.hasNext()) {
                VideoGame vg = iter.next();
                if(vg.getGameID() == gameID) {
                    iter.remove();
                    return true;
                }
            }
        }
        return false;
    }



    public boolean loadGamesFromFile(String filePath) {
        File file = new File(filePath);
        if(!file.exists()) {
            System.err.println("<html><font color='red'>ERROR: File " + filePath + " does not exist.</font></html>");
            return false;
        }
        String fileName = file.getName().toLowerCase();
        if(!(fileName.endsWith(".csv") || fileName.endsWith(".txt"))) {
            System.err.println("<html><font color='red'>ERROR: Invalid file type. Only CSV or TXT files are supported.</font></html>");
            return false;
        }
        boolean anyInvalidData = false;
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int currentYear = LocalDate.now().getYear();
            while((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if(tokens.length < 6) {
                    System.err.println("<html><font color='red'>ERROR! Not enough fields: </font></html>" + line);
                    anyInvalidData = true;
                    continue;
                }
                try {
                    int userID = Integer.parseInt(tokens[0].trim());
                    int gameID = Integer.parseInt(tokens[1].trim());
                    String gameTitle = tokens[2].trim();
                    int releaseYear = Integer.parseInt(tokens[3].trim());
                    int totalAchievements = Integer.parseInt(tokens[4].trim());
                    int achievementsCompleted = Integer.parseInt(tokens[5].trim());
                    boolean gameCompleted = (achievementsCompleted == totalAchievements);
                    if(userID < 0 || gameID < 0 || releaseYear < 0 || totalAchievements < 0 || achievementsCompleted < 0) {
                        System.err.println("<html><font color='red'>ERROR Game not added! No numbers can be less than 0: </font></html>");
                        anyInvalidData = true;
                        continue;
                    }
                    if(releaseYear < 1959 || releaseYear > currentYear) {
                        System.err.println("<html><font color='red'>ERROR Game not added! Release year must be after 1958 and not in the future: </font></html>");
                        anyInvalidData = true;
                        continue;
                    }
                    if(achievementsCompleted > totalAchievements) {
                        System.err.println("<html><font color='red'>ERROR Game not added! Achievements completed greater than total: </font></html>");
                        anyInvalidData = true;
                        continue;
                    }
                    if(!isGameIdUniqueForUser(userID, gameID)) {
                        System.err.println("<html><font color='red'>ERROR Game not added! Game ID not unique for user " + userID + "): </font></html>");
                        anyInvalidData = true;
                        continue;
                    }
                    VideoGame vg = new VideoGame(userID, gameID, gameTitle, releaseYear, totalAchievements);
                    vg.setNumAchievementsCompleted(achievementsCompleted);
                    vg.setGameCompleted(gameCompleted);
                    addGame(vg);
                } catch(NumberFormatException ex) {
                    System.err.println("<html><font color='red'>ERROR Game not added! Number format error: </font></html>");
                    anyInvalidData = true;
                }
            }
            return !anyInvalidData;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isGameIdUniqueForUser(int userID, int gameID) {
        List<VideoGame> userGames = games.get(userID);
        if(userGames != null) {
            for(VideoGame vg : userGames) {
                if(vg.getGameID() == gameID) {
                    return false;
                }
            }
        }
        return true;
    }

    public VideoGame findGame(int userID, int gameID) {
        List<VideoGame> userGames = games.get(userID);
        if(userGames != null) {
            for(VideoGame vg : userGames) {
                if(vg.getGameID() == gameID) {
                    return vg;
                }
            }
        }
        return null;
    }
}
