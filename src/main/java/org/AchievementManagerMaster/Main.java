package org.AchievementManagerMaster;
import org.AchievementManagerMaster.GUI.GameManagerGUI;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

/*import javax.swing.UIManager;
import com.formdev.flatlaf.FlatDarkLaf;*/


/*Dayton Hannaford,
CEN-3024C-24204

This program is designed to serve the purpose of allowing users to track the Video Games they're playing and the Users achievement progress in those titles.
Video Game Achievement Manager allows users add, remove, update and display their Video Game titles, the total number of achievements, total number of achievements completed, as well as the percentage of their game completion.
This can be done manually or in bulk via text/csv files. */


public class Main {
    public static void main(String[] args) {
     /*   try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/

        SwingUtilities.invokeLater(() -> {
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

            GameManagerGUI gui = new GameManagerGUI(userID);
            gui.setVisible(true);
        });
    }
}
