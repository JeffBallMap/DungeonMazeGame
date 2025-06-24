package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class keyHandler implements KeyListener{
	
	public boolean upPress, downPress, leftPress, rightPress;
	private boolean[] keyPressed = new boolean[256];

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		// Prevent key repeat by checking if key was already pressed
		if (keyPressed[code]) {
			return;
		}
		keyPressed[code] = true;
		
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			upPress = true;
		}
		
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			leftPress = true;
		}
		
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			downPress = true;
		}
		
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			rightPress = true;
		}
		
		// Add R key to regenerate maze
		if(code == KeyEvent.VK_R) {
			// This will be handled in GamePanel
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		keyPressed[code] = false;
		
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			upPress = false;
		}
		
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			leftPress = false;
		}
		
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			downPress = false;
		}
		
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			rightPress = false;
		}
	}
	
	public boolean isKeyPressed(int keyCode) {
		return keyPressed[keyCode];
	}

}