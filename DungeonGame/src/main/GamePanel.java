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
	
	private int mazeCount = 1;
	
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
		// Check for maze regeneration key
		if (keyH.isKeyPressed(KeyEvent.VK_R)) {
			tileM.regenerateMaze();
			player.x = 0;
			player.y = 0;
			player.direction = "still";
			mazeCount++;
		}
		
		player.update();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		// Draw maze
		tileM.draw(g2);
		
		// Draw player
		player.draw(g2);
		
		// Draw UI
		drawUI(g2);
		
		g2.dispose();
	}
	
	private void drawUI(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Arial", Font.BOLD, 16));
		
		// Draw instructions
		g2.drawString("WASD/Arrow Keys: Move", 10, 25);
		g2.drawString("R: New Maze", 10, 45);
		g2.drawString("Goal: Reach bottom-right corner", 10, 65);
		g2.drawString("Maze #" + mazeCount, screenWidth - 100, 25);
		
		// Draw exit indicator
		g2.setColor(Color.GREEN);
		g2.fillRect(screenWidth - TileSize, screenHeight - TileSize, TileSize/4, TileSize/4);
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Arial", Font.BOLD, 12));
		g2.drawString("EXIT", screenWidth - TileSize + 5, screenHeight - TileSize/2);
	}
	
	public void nextMaze() {
		mazeCount++;
		tileM.regenerateMaze();
		player.x = 0;
		player.y = 0;
		player.direction = "still";
	}
}