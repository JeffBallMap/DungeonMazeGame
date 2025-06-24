package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
	
	tileManger tileM= new tileManger(this);	
	keyHandler keyH = new keyHandler();
	Thread gameThread;
	createPlayer player = new createPlayer(this,keyH);
	
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
		
		player.update();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		tileM.draw(g2);
		
		player.draw(g2);
		
		g2.dispose();
		
	
	}
	
}
