/*Dayton Hannaford,
CEN-3024C-24204

This class houses the GUI for the Video Game Achievement Manager.
It enables users to add, remove, update, import games, and logout to re-enter a different UserID. */

package org.AchievementManagerMaster.GUI;

import org.AchievementManagerMaster.GameManager;
import org.AchievementManagerMaster.VideoGame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class GameManagerGUI extends JFrame {
    private final int currentUserID;
    private final GameManager gameManager;
    private DefaultTableModel tableModel;
    private JTable gameTable;
    private JLabel noGamesLabel;
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;

    public GameManagerGUI(int userID, String dbUrl, String dbUsername, String dbPassword) {
        this.currentUserID = userID;
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.gameManager = new GameManager(currentUserID, dbUrl, dbUsername, dbPassword);

        setTitle("\uD83C\uDFAE Video Game Achievement Manager - Current User: " + currentUserID);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initComponents();
        refreshGameList();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        noGamesLabel = new JLabel("INFO: No games available for user: " + currentUserID, SwingConstants.CENTER);
        noGamesLabel.setForeground(Color.RED);
        noGamesLabel.setVisible(false);
        mainPanel.add(noGamesLabel, BorderLayout.NORTH);

        String[] columns = { "Game ID", "Title", "Release Year", "Total Achievements",
                "Achievements Completed", "Percentage Complete", "Completed" };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Integer.class;
                    case 1: return String.class;
                    case 2: return Integer.class;
                    case 3: return Integer.class;
                    case 4: return Integer.class;
                    case 5: return String.class;
                    case 6: return String.class;
                    default: return Object.class;
                }
            }
        };

        gameTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gameTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Game");
        JButton removeButton = new JButton("Remove Game");
        JButton updateButton = new JButton("Update Game");
        JButton importButton = new JButton("Import Games");
        JButton refreshButton = new JButton("Refresh");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(importButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        gameTable.setAutoCreateRowSorter(true);
        gameTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        addButton.addActionListener((ActionEvent e) -> showAddGameDialog());
        removeButton.addActionListener((ActionEvent e) -> showRemoveGameDialog());
        updateButton.addActionListener((ActionEvent e) -> showUpdateGameDialog());
        importButton.addActionListener((ActionEvent e) -> showImportDialog());
        refreshButton.addActionListener((ActionEvent e) -> refreshGameList());
        logoutButton.addActionListener((ActionEvent e) -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                int newUserID = promptForUserID();
                if(newUserID != -1){
                    GameManagerGUI newGui = new GameManagerGUI(newUserID, dbUrl, dbUsername, dbPassword);
                    newGui.setVisible(true);
                }
            });
        });
    }

    private void refreshGameList() {
        tableModel.setRowCount(0);
        List<VideoGame> userGames = gameManager.getGamesForUser();
        if (userGames != null && !userGames.isEmpty()) {
            noGamesLabel.setVisible(false);
            for (VideoGame game : userGames) {
                Object[] row = new Object[]{
                        game.getGameID(),
                        game.getGameTitle(),
                        game.getGameReleaseYear(),
                        game.getNumTotalAchievements(),
                        game.getNumAchievementsCompleted(),
                        String.format("%.2f%%", game.getCompletionPercentage()),
                        game.getGameCompleted() ? "Yes" : "No"
                };
                tableModel.addRow(row);
            }
        } else {
            noGamesLabel.setVisible(true);
        }
    }

    private int promptForUserID() {
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
        return userID;
    }

    private void showAddGameDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField titleField = new JTextField();
        JTextField releaseYearField = new JTextField();
        JTextField totalAchField = new JTextField();
        JTextField achCompletedField = new JTextField();

        panel.add(new JLabel("Game Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Release Year (Integers Only):"));
        panel.add(releaseYearField);
        panel.add(new JLabel("Total Achievements (Integers Only):"));
        panel.add(totalAchField);
        panel.add(new JLabel("Achievements Completed (Integers Only):"));
        panel.add(achCompletedField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add a New Game",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText().trim();
                int releaseYear = Integer.parseInt(releaseYearField.getText().trim());
                int totalAchievements = Integer.parseInt(totalAchField.getText().trim());
                int achievementsCompleted = Integer.parseInt(achCompletedField.getText().trim());

                String response = gameManager.addVideoGame(title, releaseYear, totalAchievements, achievementsCompleted);
                JOptionPane.showMessageDialog(this, response);
                refreshGameList();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please check input & try again", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showRemoveGameDialog() {
        int[] selectedRows = gameTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Select one or more games to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove the selected game(s)?",
                "WARNING MULTIPLE GAMES SELECTED!", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            for (int viewRow : selectedRows) {
                int modelRow = gameTable.convertRowIndexToModel(viewRow);
                int gameID = (int) tableModel.getValueAt(modelRow, 0);
                gameManager.removeVideoGame(gameID);
            }
            JOptionPane.showMessageDialog(this, "Selected game(s) removed.");
            refreshGameList();
        }
    }

    private void showUpdateGameDialog() {
        int selectedRow = gameTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a game to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int gameID = (int) tableModel.getValueAt(selectedRow, 0);
        List<VideoGame> userGames = gameManager.getGamesForUser();
        VideoGame selectedGame = userGames.get(selectedRow);

        JTextField newTitleField = new JTextField(selectedGame.getGameTitle());
        JTextField newReleaseYearField = new JTextField(String.valueOf(selectedGame.getGameReleaseYear()));
        JTextField newTotalAchField = new JTextField(String.valueOf(selectedGame.getNumTotalAchievements()));
        JTextField newAchCompletedField = new JTextField(String.valueOf(selectedGame.getNumAchievementsCompleted()));

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("New Title:"));
        panel.add(newTitleField);
        panel.add(new JLabel("New Release Year:"));
        panel.add(newReleaseYearField);
        panel.add(new JLabel("New Total Achievements:"));
        panel.add(newTotalAchField);
        panel.add(new JLabel("New Achievements Completed:"));
        panel.add(newAchCompletedField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Game", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String newTitle = newTitleField.getText().trim();
                Integer newReleaseYear = Integer.parseInt(newReleaseYearField.getText().trim());
                Integer newTotalAch = Integer.parseInt(newTotalAchField.getText().trim());
                Integer newAchCompleted = Integer.parseInt(newAchCompletedField.getText().trim());

                String response = gameManager.updateVideoGame(gameID, newTitle, newReleaseYear, newTotalAch, newAchCompleted);
                JOptionPane.showMessageDialog(this, response);
                refreshGameList();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please check input & try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showImportDialog() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            String response = gameManager.importVideoGames(filePath);
            JOptionPane.showMessageDialog(this, response);
            refreshGameList();
        }
    }
}

