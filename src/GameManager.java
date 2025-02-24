/*Dayton Hannaford,
CEN-3024C-24204

This class houses a bulk of the actions that users can take to track the Video Games they're playing and the Users achievement progress in those titles.
Game Manager.java allows users add, remove, and display their Video Game titles, the total number of achievements, total number of achievements completed, as well as the percentage of their game completion. While info updates are housed in
another class, that class is called here via the main menu.*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class GameManager {
//~~~ Main colors ~~~
    public final String RED = "\u001B[31m";
    public final String GREEN = "\u001B[32m";
    public final String YELLOW = "\u001B[33m";
    public final String CYAN = "\u001B[1;96m";
    public final String PURPLE = "\u001B[95m";
    public final String BLINK_ORANGE = "\u001B[5;38;5;208m";
    final String BOLD = "\u001B[1m";

    // ~~~ Reset 'color'
    public final String RESET = "\u001B[0m";

// Storing data temporarily for phase 1
    private Map<Integer, List<VideoGame>> games;
    private Scanner scanner;

    public GameManager() {
        games = new HashMap<>();
        scanner = new Scanner(System.in);
    }


    public int mainMenu() {
        boolean quit = false;
        while (!quit) {
            System.out.println(PURPLE + "\nMind the dust..." + RESET);
            System.out.println(PURPLE + "!! Phase 1 !!" + RESET);


            System.out.println(CYAN + "-----------------------------------------------------" + RESET);
            System.out.println(CYAN + "** Welcome to the Video Game Achievement Manager **" + RESET);
            System.out.println(CYAN + "_____________________________________________________" + RESET);


            System.out.println(BLINK_ORANGE + "\n** Please select an option from the below menu! **" + RESET);
            System.out.println(BLINK_ORANGE + "-------------------------------------" + RESET);


            System.out.println("1: Add Game");
            System.out.println("2: Remove Game");
            System.out.println("3: Update Game");
            System.out.println("4: Display all Games for a given UserID");
            System.out.println("5: Import Games from File");
            System.out.println("6: Exit Program");

            System.out.print("Enter the number for your given choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(RED + "ERROR! Not a valid option." + RESET);
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println(addVideoGame());
                    break;

                case 2:
                    System.out.println(removeVideoGame());
                    break;

                case 3:
                    System.out.println(updateVideoGame());
                    break;

                case 4:
                    System.out.println(listVideoGames());
                    break;

                case 5:
                    System.out.println(importVideoGames());
                    break;

                case 6:
                    quit = true;
                    System.out.println(BLINK_ORANGE + "Thank you for using Video Game Achievement Manager");
                    System.out.println(RED + "Exiting..." + RESET);
                    break;

                default:
                    System.out.println(RED + "ERROR! Not a valid option." + RESET);
            }
        }
        return 0;
    }


    public String addVideoGame() {
        System.out.println("\nEnter Game Information:");


        int userID;
        while (true) {
            System.out.print("Enter User ID (Integers only): ");
            try {

                userID = Integer.parseInt(scanner.nextLine().trim());
                if (userID < 0) {
                    System.out.println(RED + "ERROR! User ID cannot be negative!" + RESET);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "ERROR! Not a valid User ID." + RESET);
            }
        }


        int gameID;
        while (true) {
            System.out.print("Enter Game ID (Integers only): ");
            try {
                gameID = Integer.parseInt(scanner.nextLine().trim());
                if (gameID < 0) {
                    System.out.println(RED + "ERROR! Game ID cannot be negative!" + RESET);
                } else if (!isGameIdUniqueForUser(userID, gameID)) {
                    System.out.println(RED + "ERROR! GameID already exists for the given User ID." + RESET);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "ERROR! Not a valid integer for Game ID." + RESET);
            }
        }


            System.out.print("Enter Game Title: ");
            String title = scanner.nextLine();


        int releaseYear;
        int currentYear = LocalDate.now().getYear();
        while (true) {
            System.out.print("Enter Game Release Year (Integers only): ");
            try {
                releaseYear = Integer.parseInt(scanner.nextLine().trim());
                if (releaseYear < 1959 || releaseYear > currentYear) {
                    System.out.println(RED + "ERROR! Release year must between 1959 - Present." + RESET);
                } else {
                    break;
                }


            } catch (NumberFormatException e) {
                System.out.println(RED + "ERROR! Not a valid Release Year." + RESET);
            }
        }


        int totalAchievements;
        while (true) {
            System.out.print("Enter Total Achievements (Integers only): ");
            try {
                totalAchievements = Integer.parseInt(scanner.nextLine().trim());
                if (totalAchievements < 0) {
                    System.out.println(RED + "ERROR! Total Achievements cannot be negative!" + RESET);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "ERROR! Please enter a valid integer for Total Achievements." + RESET);
            }
        }


        int achievementsCompleted;
        while (true) {
            System.out.print("Enter Number of Achievements Completed (Integers only): ");
            try {
                achievementsCompleted = Integer.parseInt(scanner.nextLine().trim());
                if (achievementsCompleted < 0) {
                    System.out.println(RED + "ERROR! Achievements Completed cannot be negative!" + RESET);
                } else if (achievementsCompleted > totalAchievements) {
                    System.out.println(RED + "ERROR! Achievements Completed cannot be more than Total Achievements!" + RESET);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "ERROR! Not a valid integer for Achievements Completed." + RESET);
            }
        }

        boolean gameCompleted = (achievementsCompleted == totalAchievements);

        VideoGame vg = new VideoGame(userID, gameID, title, releaseYear, totalAchievements);
        vg.setNumAchievementsCompleted(achievementsCompleted);
        vg.setGameCompleted(gameCompleted);
        addGame(vg);

        return GREEN + "SUCCESS! Game added successfully." + RESET;
    }


    public String removeVideoGame() {
        try {
            System.out.print("\nEnter User ID for the game to remove: ");
            int userID = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Game ID to remove: ");
            int gameID = Integer.parseInt(scanner.nextLine());

            if (removeGame(userID, gameID)) {
                return GREEN + "SUCCESS! Game removed." + RESET;
            } else {
                return RED + "ERROR! Game not found." + RESET;
            }
        } catch (NumberFormatException e) {
            return RED + "ERROR! Invalid input. Please try again." + RESET;
        }
    }


    public String listVideoGames() {
        try {
            System.out.print("\nEnter User ID to list their video games: ");
            int userID = Integer.parseInt(scanner.nextLine());
            return displayUserGames(userID);
        } catch (NumberFormatException e) {
            return RED + "ERROR! Invalid input. Please try again." + RESET;
        }
    }


    public String importVideoGames() {
        System.out.print("\nEnter the absolute file path to import video games from: ");
        String filePath = scanner.nextLine();
        if (loadGamesFromFile(filePath)) {
            return GREEN + "SUCCESS! Games imported successfully!." + RESET;
        } else {
            return RED + "ERROR! Import failed! Please check the file, or file path and try again." + RESET;
            }
    }

    public void addGame(VideoGame game) {
        int userID = game.getUserID();
        List<VideoGame> userGames = games.getOrDefault(userID, new ArrayList<>());
        userGames.add(game);
        games.put(userID, userGames);
    }

    public boolean removeGame(int userID, int gameID) {
        List<VideoGame> userGames = games.get(userID);
        if (userGames != null) {
            Iterator<VideoGame> iter = userGames.iterator();
            while (iter.hasNext()) {
                VideoGame vg = iter.next();
                if (vg.getGameID() == gameID) {
                    iter.remove();
                    return true;
                }
            }
        }
        return false;
    }


    public String displayUserGames(int userID) {
        StringBuilder sb = new StringBuilder();
        List<VideoGame> userGames = games.get(userID);
        if (userGames == null || userGames.isEmpty()) {
            sb.append(YELLOW + "UserID: " + RED + userID + RESET + YELLOW + " not found" + RESET);
        } else {
            sb.append(BOLD + GREEN + "\nSuccess! Games belonging to User ID: " + RESET + PURPLE + userID + RESET + GREEN + ":" + RESET + "\n");
            for (VideoGame vg : userGames) {

// recheck game completed boolean

                vg.setGameCompleted(vg.getNumAchievementsCompleted() == vg.getNumTotalAchievements());

                sb.append

                         (BOLD + "\n Game ID: " + RESET + vg.getGameID() +
                          BOLD + "\n Title: " + RESET + vg.getGameTitle() +
                          BOLD + "\n Release Year: " + RESET + vg.getGameReleaseYear() +
                          BOLD + "\n Total Achievements: " + RESET + vg.getNumTotalAchievements() +
                          BOLD + "\n Achievements Completed: " + RESET + vg.getNumAchievementsCompleted() +
                          BOLD + "\n Achievements Completed Percentage: " + RESET + vg.calculateCompletionPercentage() +
                          BOLD + "\n Completed: " + RESET + vg.GameCompleted() +
                                 "\n\n ");
                }
            }
        return sb.toString();
    }

    public boolean loadGamesFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println(RED + "ERROR: File " + filePath + " does not exist." + RESET);
            return false;
            }

        String fileName = file.getName().toLowerCase();
        if (!(fileName.endsWith(".csv") || fileName.endsWith(".txt"))) {
            System.err.println(RED + "ERROR: Invalid file type. Only CSV or TXT files are supported." + RESET);
            return false;
        }


                        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                            String line;
                            int currentYear = LocalDate.now().getYear();
                            while ((line = br.readLine()) != null) {
                                if (line.trim().isEmpty()) continue;
                                String[] tokens = line.split(",");

                                if (tokens.length < 6) {
                                    System.err.println(RED + "ERROR! Not enough fields: " + line + RESET);
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


                                    if (userID < 0 || gameID < 0 || releaseYear < 0 || totalAchievements < 0 || achievementsCompleted < 0) {
                                        System.err.println(RED + "ERROR Game not added! No numbers can be less than 0: " + line + "was not added!" + RESET);
                                        continue;
                                    }


                                    if (releaseYear < 1959 || releaseYear > currentYear) {
                                        System.err.println(RED + "ERROR Game not added! Release year must be after 1958 and not in the future: " + line + "was not added!" + RESET);
                                        continue;
                                    }


                                    if (achievementsCompleted > totalAchievements) {
                                        System.err.println(RED + "ERROR Game not added! Achievements completed greater than total: " + line + "was not added!" + RESET);
                                        continue;
                                    }


                                    if (!isGameIdUniqueForUser(userID, gameID)) {
                                        System.err.println(RED + "ERROR Game not added! Game ID not unique for user " + userID + "): " + line + "was not added!" + RESET);
                                        continue;
                                    }


                                    VideoGame vg = new VideoGame(userID, gameID, gameTitle, releaseYear, totalAchievements);
                                    vg.setNumAchievementsCompleted(achievementsCompleted);
                                    vg.setGameCompleted(gameCompleted);
                                    addGame(vg);
                                } catch (NumberFormatException ex) {
                                    System.err.println(RED + "ERROR Game not added! Number format error: " + line + " was not added!" + RESET);
                                }
                            }


                            return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }

// Separate method for ensuring that a user can have the same game as another, but a user cannot have the same game twice
    public boolean isGameIdUniqueForUser(int userID, int gameID) {
        List<VideoGame> userGames = games.get(userID);
        if (userGames != null) {
            for (VideoGame vg : userGames) {
                if (vg.getGameID() == gameID) {
                    return false;
                }
            }

        }
        return true;
        }


    // Used for finding the right game to update
    public VideoGame findGame(int userID, int gameID) {
        List<VideoGame> userGames = games.get(userID);
        if (userGames != null) {
            for (VideoGame vg : userGames) {
                if (vg.getGameID() == gameID) {
                    return vg;
                }
            }
        }
        return null;
        }

    // Separated so update logic is disconnected for ease
    public String updateVideoGame() {
        UpdateVideoGame updater = new UpdateVideoGame(scanner);
        return updater.updateGame(this);
    }



}
