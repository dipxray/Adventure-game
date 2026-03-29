package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        // Corrected class name (capital J)
        JFrame window = new JFrame();

        // Set close operation
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Disable resizing
        window.setResizable(false);

        // Set title
        window.setTitle("Adventure Run");

        // Create and add game panel
        GamePanel panel = new GamePanel();
        window.add(panel);

        // Pack the components
        window.pack();

        // Center the window on the screen
        window.setLocationRelativeTo(null); // Fixed typo: RelativeTTo -> RelativeTo

        // Make window visible
        window.setVisible(true);

        panel.setupGame();
        panel.startGameThread();
    }
}