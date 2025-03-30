/*Dayton Hannaford,
CEN-3024C-24204

This program is designed to serve the purpose of allowing users to track the Video Games they're playing and the Users achievement progress in those titles.
Video Game Achievement Manager allows users add, remove, update and display their Video Game titles, the total number of achievements, total number of achievements completed, as well as the percentage of their game completion.
This can be done manually or in bulk via text/csv files.

This class houses the main entry point for the Video Game Achievement Manager application.
It prompts the user for their UserID and launches the GUI. */

package org.AchievementManagerMaster;

import org.AchievementManagerMaster.GUI.GameManagerGUI;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            boolean connected = false;
            while (!connected) {

                String host = JOptionPane.showInputDialog(null,
                        "Enter MySQL host (Example: localhost):",
                        "Database Connection Info", JOptionPane.QUESTION_MESSAGE);
                if (host == null || host.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "MySQL host is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                String portInput = JOptionPane.showInputDialog(null,
                        "Enter MySQL port (Default: 3306):",
                        "Database Connection Info", JOptionPane.QUESTION_MESSAGE);
                if (portInput == null || portInput.trim().isEmpty()) {
                    portInput = "3306";
                }

                String dbName = JOptionPane.showInputDialog(null,
                        "Enter the name of the database (Used: videogamemanager_main):",
                        "Database Connection Info", JOptionPane.QUESTION_MESSAGE);
                if (dbName == null || dbName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Database name is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                String dbUsername = JOptionPane.showInputDialog(null,
                        "Enter MySQL username:",
                        "Database Connection Info", JOptionPane.QUESTION_MESSAGE);
                if (dbUsername == null) {
                    dbUsername = "";
                }

                String dbPassword = JOptionPane.showInputDialog(null,
                        "Enter MySQL password:",
                        "Database Connection Info", JOptionPane.QUESTION_MESSAGE);
                if (dbPassword == null) {
                    dbPassword = "";
                }

                String dbUrl = "jdbc:mysql://" + host.trim() + ":" + portInput.trim() + "/" + dbName.trim();


                int userID = -1;
                boolean valid = false;
                while (!valid) {
                    String input = JOptionPane.showInputDialog(null,
                            "Please Enter your UserID :", "\uD83C\uDFAE User Login", JOptionPane.QUESTION_MESSAGE);
                    if (input == null) {
                        System.exit(0);
                    }
                    try {
                        userID = Integer.parseInt(input.trim());
                        valid = true;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null,
                                "Invalid UserID. Please enter a valid integer.", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }


                try {
                    GameManagerGUI gui = new GameManagerGUI(userID, dbUrl, dbUsername, dbPassword);
                    gui.setVisible(true);
                    connected = true;

                } catch (RuntimeException ex) {
                    int option = JOptionPane.showConfirmDialog(null,
                            "Unable to connect to the database. Would you like to try again?",
                            "Connection Error!", JOptionPane.YES_NO_OPTION);
                    if (option != JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }

                }

            }

        });

    }

}


