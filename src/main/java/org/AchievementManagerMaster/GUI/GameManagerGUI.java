package org.AchievementManagerMaster.GUI;
import org.AchievementManagerMaster.GameManager;
import org.AchievementManagerMaster.VideoGame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/*Dayton Hannaford,
CEN-3024C-24204

This class houses the GUI & update information logic. Update logic was originally in its own class file but was separated making it
easier to transition to a database in future phases.*/
public class GameManagerGUI extends JFrame {
    private int currentUserID;
    private GameManager gameManager;
    private DefaultTableModel tableModel;
    private JTable gameTable;
    private JLabel noGamesLabel;

    public GameManagerGUI(int userID) {
        this.currentUserID = userID;
        this.gameManager = new GameManager();
        setTitle("\uD83C\uDFAE Video Game Achievement Manager - Current User: " + currentUserID );
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
        };
        gameTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gameTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Game");
        JButton removeButton = new JButton("Remove Game");
        JButton updateButton = new JButton("Update Game");
        JButton importButton = new JButton("Import Games");
        JButton refreshButton = new JButton("Refresh List");

        // Button Colors
        /*addButton.setBackground(Color.GREEN);
        addButton.setForeground(Color.BLACK);

        removeButton.setBackground(Color.RED);
        removeButton.setForeground(Color.BLACK);

        updateButton.setBackground(Color.MAGENTA);
        updateButton.setForeground(Color.BLACK);

        importButton.setBackground(Color.ORANGE);
        importButton.setForeground(Color.BLACK);

        refreshButton.setBackground(Color.YELLOW);
        refreshButton.setForeground(Color.BLACK);*/

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(importButton);
        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        addButton.addActionListener((ActionEvent e) -> showAddGameDialog());
        removeButton.addActionListener((ActionEvent e) -> showRemoveGameDialog());
        updateButton.addActionListener((ActionEvent e) -> showUpdateGameDialog());
        importButton.addActionListener((ActionEvent e) -> showImportDialog());
        refreshButton.addActionListener((ActionEvent e) -> refreshGameList());
    }

    private void refreshGameList() {
        tableModel.setRowCount(0);
        List<VideoGame> userGames = gameManager.getGamesForUser(currentUserID);
        if (userGames != null && !userGames.isEmpty()) {
            noGamesLabel.setVisible(false);
            for (VideoGame game : userGames) {

                game.setGameCompleted(game.getNumAchievementsCompleted() == game.getNumTotalAchievements());

                Object[] row = new Object[]{
                        game.getGameID(),

                        game.getGameTitle(),

                        game.getGameReleaseYear(),

                        game.getNumTotalAchievements(),

                        game.getNumAchievementsCompleted(),

                        game.calculateCompletionPercentage(),

                        game.GameCompleted() ? "Yes" : "No"
                };
                tableModel.addRow(row);
            }
        } else {

            noGamesLabel.setVisible(true);
        }
    }

    private void showAddGameDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField gameIDField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField releaseYearField = new JTextField();
        JTextField totalAchField = new JTextField();
        JTextField achCompletedField = new JTextField();

        panel.add(new JLabel("Game ID (Integers Only):"));
        panel.add(gameIDField);

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
                int gameID = Integer.parseInt(gameIDField.getText().trim());

                String title = titleField.getText().trim();

                int releaseYear = Integer.parseInt(releaseYearField.getText().trim());

                int totalAchievements = Integer.parseInt(totalAchField.getText().trim());

                int achievementsCompleted = Integer.parseInt(achCompletedField.getText().trim());

                String response = gameManager.addVideoGame(currentUserID, gameID, title, releaseYear, totalAchievements, achievementsCompleted);

                JOptionPane.showMessageDialog(this, response);

                refreshGameList();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please check input & try again", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showRemoveGameDialog() {
        int selectedRow = gameTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a game to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int gameID = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove the selected game?",
                "Removal Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String response = gameManager.removeVideoGame(currentUserID, gameID);
            JOptionPane.showMessageDialog(this, response);
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
        List<VideoGame> userGames = gameManager.getGamesForUser(currentUserID);

        VideoGame selectedGame = userGames.get(selectedRow);

        JTextField newGameIDField = new JTextField(String.valueOf(selectedGame.getGameID()));

        JTextField newTitleField = new JTextField(selectedGame.getGameTitle());

        JTextField newReleaseYearField = new JTextField(String.valueOf(selectedGame.getGameReleaseYear()));

        JTextField newTotalAchField = new JTextField(String.valueOf(selectedGame.getNumTotalAchievements()));

        JTextField newAchCompletedField = new JTextField(String.valueOf(selectedGame.getNumAchievementsCompleted()));

        JPanel panel = new JPanel(new GridLayout(0, 2));

        panel.add(new JLabel("New Game ID:"));
        panel.add(newGameIDField);

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
                Integer newGameID = Integer.parseInt(newGameIDField.getText().trim());

                String newTitle = newTitleField.getText().trim();

                Integer newReleaseYear = Integer.parseInt(newReleaseYearField.getText().trim());

                Integer newTotalAch = Integer.parseInt(newTotalAchField.getText().trim());

                Integer newAchCompleted = Integer.parseInt(newAchCompletedField.getText().trim());

                String response = gameManager.updateVideoGame(currentUserID, gameID, newGameID, newTitle, newReleaseYear, newTotalAch, newAchCompleted);

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
