package main;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {

        JFrame window = new JFrame();               // Create window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit on close
        window.setResizable(false);                // Disable resize
        window.setTitle("Dungeon Maze");           // Set title

        GamePanel gamePanel = new GamePanel();     // Create game panel
        window.add(gamePanel);                     // Add panel to window

        window.pack();                             // Fit window to panel

        window.setLocationRelativeTo(null);        // Center window
        window.setVisible(true);                   // Show window

        gamePanel.setUpGame();                     // Setup game
        gamePanel.startGameThread();               // Start loop
    }
}
