package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import entity.createPlayer;
import tiles.tileManger;

public class GamePanel extends JPanel implements Runnable{
	
	//Screen Settings
	final int ogTileSize = 16;
	final int scale = 2;
	
	public final int TileSize = ogTileSize * scale;
	public final int maxScreenCol = 25;
	public final int maxScreenRow = 20;
	public final int screenWidth = (TileSize * maxScreenCol);
	public final int screenHeight = (TileSize * maxScreenRow);
	
	int fps = 60;
	
	public tileManger tileM = new tileManger(this);	
	keyHandler keyH = new keyHandler();
	Thread gameThread;
	createPlayer player = new createPlayer(this, keyH);
	
	private int mazesCompleted = 0;
	private long gameStartTime;
	private long gameDuration = 10 * 60 * 1000; // 10 minutes in milliseconds
	private boolean gameActive = true;
	private boolean gameStarted = false;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void startGame() {
		if (!gameStarted) {
			gameStartTime = System.currentTimeMillis();
			gameStarted = true;
			gameActive = true;
			mazesCompleted = 0;
		}
	}
	
	public long getRemainingTime() {
		if (!gameStarted) return gameDuration;
		long elapsed = System.currentTimeMillis() - gameStartTime;
		return Math.max(0, gameDuration - elapsed);
	}
	
	public boolean isGameActive() {
		return gameActive && getRemainingTime() > 0;
	}
	
	@Override
	public void run() {
		
		double drawIntrval = 1000000000/fps;
		double nextDrawTim = drawIntrval + System.nanoTime();
		
		while(gameThread != null) {
			//update
			update();
			repaint();
			
			try {

				double remainTim = nextDrawTim - System.nanoTime();
				remainTim = remainTim/1000000;
				
				if(remainTim<0) {
					remainTim = 0;
				}
				
				Thread.sleep((long)remainTim);
				
				nextDrawTim += drawIntrval;
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void update() {
		// Start game on first input
		if (!gameStarted && (keyH.upPress || keyH.downPress || keyH.leftPress || keyH.rightPress)) {
			startGame();
		}
		
		// Check if game time is up
		if (gameStarted && getRemainingTime() <= 0) {
			gameActive = false;
		}
		
		// Only allow game actions if game is active
		if (isGameActive()) {
			// Check for maze regeneration key
			if (keyH.isKeyPressed(KeyEvent.VK_R)) {
				tileM.regenerateMaze();
				player.x = 0;
				player.y = 0;
				player.direction = "still";
			}
			
			player.update();
		}
		
		// Allow restart even when game is over
		if (!gameActive && keyH.isKeyPressed(KeyEvent.VK_SPACE)) {
			restartGame();
		}
	}
	
	public void restartGame() {
		gameStarted = false;
		gameActive = true;
		mazesCompleted = 0;
		tileM.regenerateMaze();
		player.x = 0;
		player.y = 0;
		player.direction = "still";
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		// Draw maze
		tileM.draw(g2);
		
		// Draw player (only if game is active)
		if (isGameActive()) {
			player.draw(g2);
		}
		
		// Draw UI
		drawUI(g2);
		
		g2.dispose();
	}
	
	private void drawUI(Graphics2D g2) {
		// Timer and score
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Arial", Font.BOLD, 18));
		
		long remainingTime = getRemainingTime();
		int minutes = (int) (remainingTime / 60000);
		int seconds = (int) ((remainingTime % 60000) / 1000);
		
		g2.drawString(String.format("Time: %02d:%02d", minutes, seconds), 10, 25);
		g2.drawString("Mazes Completed: " + mazesCompleted, 10, 50);
		
		// Instructions
		g2.setFont(new Font("Arial", Font.BOLD, 14));
		if (!gameStarted) {
			g2.setColor(Color.YELLOW);
			g2.drawString("Press any movement key to start!", 10, screenHeight - 80);
			g2.setColor(Color.WHITE);
			g2.drawString("Goal: Complete as many mazes as possible in 10 minutes!", 10, screenHeight - 60);
			g2.drawString("WASD/Arrow Keys: Move  |  R: New Maze", 10, screenHeight - 40);
			g2.drawString("Reach the bottom-right corner to complete each maze", 10, screenHeight - 20);
		} else if (!gameActive) {
			// Game over screen
			g2.setColor(Color.RED);
			g2.setFont(new Font("Arial", Font.BOLD, 24));
			g2.drawString("TIME'S UP!", screenWidth/2 - 80, screenHeight/2 - 40);
			
			g2.setColor(Color.YELLOW);
			g2.setFont(new Font("Arial", Font.BOLD, 20));
			g2.drawString("Final Score: " + mazesCompleted + " mazes", screenWidth/2 - 100, screenHeight/2);
			
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Arial", Font.BOLD, 16));
			g2.drawString("Press SPACE to play again", screenWidth/2 - 90, screenHeight/2 + 40);
		} else {
			g2.drawString("WASD/Arrow Keys: Move  |  R: New Maze", 10, screenHeight - 40);
			g2.drawString("Goal: Reach bottom-right corner", 10, screenHeight - 20);
		}
		
		// Draw exit indicator (only if game is active)
		if (isGameActive()) {
			g2.setColor(Color.GREEN);
			g2.fillRect(screenWidth - TileSize, screenHeight - TileSize, TileSize/3, TileSize/3);
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Arial", Font.BOLD, 10));
			g2.drawString("EXIT", screenWidth - TileSize + 2, screenHeight - TileSize/2);
		}
	}
	
	public void nextMaze() {
		mazesCompleted++;
		tileM.regenerateMaze();
		player.x = 0;
		player.y = 0;
		player.direction = "still";
	}
}