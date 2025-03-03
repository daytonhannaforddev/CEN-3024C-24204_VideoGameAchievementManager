package org.AchievementManagerMaster;

/*Dayton Hannaford,
CEN-3024C-24204

This program is designed to serve the purpose of allowing users to track the Video Games they're playing and the Users achievement progress in those titles.
Video Game Achievement Manager allows users add, remove, update and display their Video Game titles, the total number of achievements, total number of achievements completed, as well as the percentage of their game completion.
This can be done manually or in bulk via text/csv files. */


public class Main {
    public int run(String[] args) {
        GameManager gameManager = new GameManager();
        return gameManager.mainMenu();
    }

    public static void main(String[] args) {
        Main app = new Main();
        int exitCode = app.run(args);
        System.exit(exitCode);
    }
}
