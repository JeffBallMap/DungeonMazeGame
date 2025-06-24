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
	private boolean gameActive = false; // Start inactive for home screen
	private boolean gameStarted = false;
	private boolean showHomeScreen = true; // New flag for home screen
	
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
			showHomeScreen = false;
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
		// Handle home screen input
		if (showHomeScreen) {
			if (keyH.isKeyPressed(KeyEvent.VK_SPACE)) {
				startGame();
			}
			return;
		}
		
		// Start game on first movement input (if not already started)
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
		if (!gameActive && !showHomeScreen && keyH.isKeyPressed(KeyEvent.VK_SPACE)) {
			restartGame();
		}
	}
	
	public void restartGame() {
		gameStarted = false;
		gameActive = false;
		showHomeScreen = true;
		mazesCompleted = 0;
		tileM.regenerateMaze();
		player.x = 0;
		player.y = 0;
		player.direction = "still";
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		// Always draw maze (for home screen and game)
		tileM.draw(g2);
		
		// Draw player (always visible now)
		player.draw(g2);
		
		// Draw UI
		drawUI(g2);
		
		g2.dispose();
	}
	
	private void drawUI(Graphics2D g2) {
		if (showHomeScreen) {
			// Home screen UI
			g2.setColor(new Color(0, 0, 0, 180)); // Semi-transparent overlay
			g2.fillRect(0, 0, screenWidth, screenHeight);
			
			// Title
			g2.setColor(Color.YELLOW);
			g2.setFont(new Font("Arial", Font.BOLD, 36));
			String title = "MAZE RUNNER";
			int titleWidth = g2.getFontMetrics().stringWidth(title);
			g2.drawString(title, (screenWidth - titleWidth) / 2, screenHeight / 2 - 100);
			
			// Subtitle
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Arial", Font.BOLD, 18));
			String subtitle = "Complete as many mazes as possible in 10 minutes!";
			int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
			g2.drawString(subtitle, (screenWidth - subtitleWidth) / 2, screenHeight / 2 - 60);
			
			// Instructions
			g2.setFont(new Font("Arial", Font.BOLD, 16));
			String[] instructions = {
				"WASD or Arrow Keys: Move",
				"R: Generate new maze",
				"Goal: Reach the bottom-right corner",
				"",
				"Press SPACE to start!"
			};
			
			for (int i = 0; i < instructions.length; i++) {
				if (i == instructions.length - 1) {
					g2.setColor(Color.GREEN);
					g2.setFont(new Font("Arial", Font.BOLD, 20));
				}
				int instrWidth = g2.getFontMetrics().stringWidth(instructions[i]);
				g2.drawString(instructions[i], (screenWidth - instrWidth) / 2, screenHeight / 2 + (i * 25));
			}
			
			// Draw exit indicator
			g2.setColor(Color.GREEN);
			g2.fillRect(screenWidth - TileSize, screenHeight - TileSize, TileSize/3, TileSize/3);
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Arial", Font.BOLD, 10));
			g2.drawString("EXIT", screenWidth - TileSize + 2, screenHeight - TileSize/2);
			
		} else {
			// Game UI (timer and score)
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Arial", Font.BOLD, 18));
			
			long remainingTime = getRemainingTime();
			int minutes = (int) (remainingTime / 60000);
			int seconds = (int) ((remainingTime % 60000) / 1000);
			
			g2.drawString(String.format("Time: %02d:%02d", minutes, seconds), 10, 25);
			g2.drawString("Mazes Completed: " + mazesCompleted, 10, 50);
			
			// Instructions during game
			g2.setFont(new Font("Arial", Font.BOLD, 14));
			if (!gameActive && gameStarted) {
				// Game over screen
				g2.setColor(new Color(0, 0, 0, 180)); // Semi-transparent overlay
				g2.fillRect(0, 0, screenWidth, screenHeight);
				
				g2.setColor(Color.RED);
				g2.setFont(new Font("Arial", Font.BOLD, 24));
				String gameOver = "TIME'S UP!";
				int gameOverWidth = g2.getFontMetrics().stringWidth(gameOver);
				g2.drawString(gameOver, (screenWidth - gameOverWidth) / 2, screenHeight/2 - 40);
				
				g2.setColor(Color.YELLOW);
				g2.setFont(new Font("Arial", Font.BOLD, 20));
				String finalScore = "Final Score: " + mazesCompleted + " mazes";
				int scoreWidth = g2.getFontMetrics().stringWidth(finalScore);
				g2.drawString(finalScore, (screenWidth - scoreWidth) / 2, screenHeight/2);
				
				g2.setColor(Color.WHITE);
				g2.setFont(new Font("Arial", Font.BOLD, 16));
				String restart = "Press SPACE to return to home screen";
				int restartWidth = g2.getFontMetrics().stringWidth(restart);
				g2.drawString(restart, (screenWidth - restartWidth) / 2, screenHeight/2 + 40);
			} else {
				g2.setColor(Color.WHITE);
				g2.drawString("WASD/Arrow Keys: Move  |  R: New Maze", 10, screenHeight - 40);
				g2.drawString("Goal: Reach bottom-right corner", 10, screenHeight - 20);
			}
			
			// Draw exit indicator (during active game)
			if (isGameActive()) {
				g2.setColor(Color.GREEN);
				g2.fillRect(screenWidth - TileSize, screenHeight - TileSize, TileSize/3, TileSize/3);
				g2.setColor(Color.WHITE);
				g2.setFont(new Font("Arial", Font.BOLD, 10));
				g2.drawString("EXIT", screenWidth - TileSize + 2, screenHeight - TileSize/2);
			}
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