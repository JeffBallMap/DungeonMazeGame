package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class keyHandler implements KeyListener {

    public boolean upPress, downPress, leftPress, rightPress; // Movement flags
    public boolean checkDrawTime = false; // Debug draw time
    public boolean showSolution = false;  // Toggle solution visibility

    @Override
    public void keyTyped(KeyEvent e) {} // Not used

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) upPress = true; // Up pressed
        if (code == KeyEvent.VK_A) leftPress = true; // Left pressed
        if (code == KeyEvent.VK_S) downPress = true; // Down pressed
        if (code == KeyEvent.VK_D) rightPress = true; // Right pressed

        if (code == KeyEvent.VK_T) { 
            checkDrawTime = !checkDrawTime; // Toggle debug draw time
        }

        if (code == KeyEvent.VK_H) {
            showSolution = !showSolution; // Toggle solution markers
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) upPress = false; // Up released
        if (code == KeyEvent.VK_A) leftPress = false; // Left released
        if (code == KeyEvent.VK_S) downPress = false; // Down released
        if (code == KeyEvent.VK_D) rightPress = false; // Right released
    }
}
