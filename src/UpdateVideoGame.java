/*Dayton Hannaford,
CEN-3024C-24204

This class houses the update information logic. It was separated due to making it easier to ensure that when information is updated, it can all be verified to follow any given fields requirements. This should also make it
easier to transition to a database in future phases.*/

import java.time.LocalDate;
import java.util.Scanner;

public class UpdateVideoGame {
    private Scanner scanner;

    public final String RED = "\u001B[31m";
    public final String GREEN = "\u001B[32m";
    public final String YELLOW = "\u001B[33m";
    public final String CYAN = "\u001B[1;96m";
    public final String PURPLE = "\u001B[95m";
    public final String BLINK_ORANGE = "\u001B[5;38;5;208m";
    final String BOLD = "\u001B[1m";

    // ~~~ Reset 'color'
    public final String RESET = "\u001B[0m";

    public UpdateVideoGame(Scanner scanner) {
        this.scanner = scanner;
    }

            public String updateGame(GameManager gameManager) {
                System.out.println(YELLOW + "\nPlease insert the following:" + RESET);


                int userID = promptForInteger("Enter User ID (Integers only): ", 0, Integer.MAX_VALUE);


                int gameID = promptForInteger("Enter Game ID (Integers only): ", 0, Integer.MAX_VALUE);


                VideoGame game = gameManager.findGame(userID, gameID);
                if (game == null) {
                    return RED + "ERROR! Game not found with that combination. Try again" + RESET;
                }


                boolean updating = true;
                while (updating) {
                // Current Info
                System.out.println(BOLD + BLINK_ORANGE + "\nCurrent Game Info:" + RESET);
                System.out.println("Game ID: " + game.getGameID());
                System.out.println("Title: " + game.getGameTitle());
                System.out.println("Release Year: " + game.getGameReleaseYear());
                System.out.println("Total Achievements: " + game.getNumTotalAchievements());
                System.out.println("Achievements Completed: " + game.getNumAchievementsCompleted());
                System.out.println("Completed: " + game.GameCompleted());



                System.out.println(BOLD + CYAN + "\nWhich field would you like to update?" + RESET);
                System.out.println("1. Game ID");
                System.out.println("2. Game Title");
                System.out.println("3. Release Year");
                System.out.println("4. Total Achievements");
                System.out.println("5. Achievements Completed");
                System.out.println("6. Confirm update");

                int choice = promptForInteger("Enter your choice: ", 1, 6);

                switch (choice) {
                    case 1:
                        int newGameID;
                        while (true) {
                            newGameID = promptForInteger("Enter new Game ID: ", 0, Integer.MAX_VALUE);
                            if (!gameManager.isGameIdUniqueForUser(userID, newGameID)) {
                                System.out.println(RED + "ERROR! Game ID not unique for UserID." + RESET);
                            } else {
                                break;
                            }
                        }
                        game.setGameID(newGameID);
                        break;

                    case 2:
                        System.out.print("Enter new Game Title: ");
                        String newTitle = scanner.nextLine();
                        game.setGameTitle(newTitle);
                        break;

                    case 3:
                        int currentYear = LocalDate.now().getYear();
                        int newReleaseYear;
                        while (true) {
                            newReleaseYear = promptForInteger("Enter new Release Year: ", 1959, currentYear);
                            if (newReleaseYear < 1959 || newReleaseYear > currentYear) {
                                System.out.println(RED + "ERROR! Release year must be after 1958 and not in the future." + RESET);
                            } else {
                                break;
                            }
                        }
                        game.setGameReleaseYear(newReleaseYear);
                        break;

                    case 4:
                        int newTotalAchievements;
                        while (true) {
                            newTotalAchievements = promptForInteger("Enter new Total Achievements: ", 0, Integer.MAX_VALUE);
                            if (newTotalAchievements < game.getNumAchievementsCompleted()) {
                                System.out.println(RED + "ERROR! Total Achievements cannot be less than the current Achievements Completed (" + game.getNumAchievementsCompleted() + ")." + RESET);
                            } else {
                                break;
                            }
                        }
                        game.setNumTotalAchievements(newTotalAchievements);
                        break;

                    case 5:
                        int newAchievementsCompleted;
                        while (true) {
                            newAchievementsCompleted = promptForInteger("Enter new Achievements Completed: ", 0, game.getNumTotalAchievements());
                            if (newAchievementsCompleted > game.getNumTotalAchievements()) {
                                System.out.println(RED + "ERROR! Achievements Completed cannot be more than entire Total Achievements (" + game.getNumTotalAchievements() + ")." + RESET);
                            } else {
                                break;
                            }
                        }
                        game.setNumAchievementsCompleted(newAchievementsCompleted);
                        break;

                        case 6:
                            updating = false;
                            return PURPLE + "Update Finished." + RESET;
                        default:
                            return RED + "Not a valid choice." + RESET;
                    }

                    }


                game.setGameCompleted(game.getNumAchievementsCompleted() == game.getNumTotalAchievements());
                return GREEN + "Game updated successfully!" + RESET;
            }

// Added so that validation logic is centralized to a single method for ease of updating
    private int promptForInteger(String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
                if (value < min || value > max) {
                    System.out.println(RED + "ERROR! Not a valid integer" + RESET);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "ERROR! Please enter a valid integer." + RESET);
            }
        }
    }
}
