package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import entity.createPlayer;
import mazer.Maze;
import mazer.MazeSolver;
import objet.superObj;
import tiles.tileManger;

public class GamePanel extends JPanel implements Runnable{
	
	// Screen settings
	final int ogTileSize = 16;
	final int scale = 2;
	public int sock = 10;
	
	// Tile size
	public final int TileSize = ogTileSize * scale;
	// Max columns
	public final int maxScreenCol = 25;
	// Max rows
	public final int maxScreenRow = 25;
	// Screen width
	public final int screenWidth = (TileSize * maxScreenCol);
	// Screen height
	public final int screenHeight = (TileSize * maxScreenRow);
	
	// Frames per second
	int fps = 60;
	
	// Maze instance
	Maze maze = new Maze(this);
	// Tile manager
	tileManger tileM= new tileManger(this);	
	// Key handler
	keyHandler keyH = new keyHandler();
	
	// Music sound
	Sound music = new Sound();
	// Effects sound
	Sound sfx = new Sound();
	
	// Collision checker
	public CollisionCheck collcheck = new CollisionCheck(this);
	// Object setter
	public objSetter objSet =new objSetter(this);
	// UI handler
	public UI ui = new UI(this);

	// Game thread
	Thread gameThread;
	
	// Player instance
	createPlayer player = new createPlayer(this,keyH);
	// Objects array
	public superObj Obj[] = new superObj[10];
	// Solution markers list
	public ArrayList<superObj> solnMarkers = new ArrayList<>();

	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Panel size
		this.setBackground(Color.black); // Background color
		this.setDoubleBuffered(true); // Double buffering
		this.addKeyListener(keyH); // Add key listener
		this.setFocusable(true); // Focusable panel
		
		// Initialize MazeSolver
		MazeSolver mS = new MazeSolver("/src/res/maps/maze_output.txt", maxScreenRow, maxScreenCol);
		
		// Solve maze and write solution
		if (mS.solveMaze(1, 1)) {
		    //mS.printSolution(); // Optional print
		    mS.writeSolutionToFile("solution_output.txt"); // Save solution file
		} else {
		    System.out.println("No solution found."); // No solution
		}
	}
	
	public void setUpGame() {
		objSet.setObj(); // Set objects
		objSet.setSolutionMarkers("solution_output.txt"); // Set solution markers
		
		playMusic(0); // Start music
	}

	public void startGameThread() {
		gameThread = new Thread(this); // New thread
		gameThread.start(); // Start thread
	}
	
	@Override
	public void run() {
		double drawIntrval = 1000000000/fps; // Draw interval ns
		double nextDrawTim = drawIntrval + System.nanoTime(); // Next draw time
		
		while(gameThread != null) {
			update(); // Update game state
			repaint(); // Repaint panel
			
			try {
				double remainTim = nextDrawTim - System.nanoTime(); // Remaining time
				remainTim = remainTim/1000000; // Convert to ms
				
				if(remainTim<0) {
					remainTim = 0; // Avoid negative sleep
				}
				
				Thread.sleep((long)remainTim); // Sleep
				
				nextDrawTim += drawIntrval; // Update next draw time
				
			} catch (InterruptedException e) {
				e.printStackTrace(); // Print error
			}
		}
	}
	
	
	public void update() {
		player.update(); // Update player
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Clear screen
		
		Graphics2D g2 = (Graphics2D)g; // Cast graphics
		
		// Debug timing
		long drawStart = 0;
		drawStart=System.nanoTime();
		
		tileM.draw(g2); // Draw tiles
		
		// Draw objects
		for(int i=0; i<Obj.length;i++) {
			if(Obj[i]!=null) {
				Obj[i].draw(g2, this);
			}
		}
		
		// Draw solution markers if toggled
		if (keyH.showSolution) {
		    for (superObj marker : solnMarkers) {
		        if (marker != null) {
		            marker.draw(g2, this);
		        }
		    }
		}
		
		player.draw(g2); // Draw player
		
		ui.draw(g2); // Draw UI
		
		// Draw time debug
		if(keyH.checkDrawTime==true) {
			long drawEnd = System.nanoTime();
			long passed = drawEnd - drawStart;
			g2.setColor(Color.red);
			g2.drawString("Draw Time: " + passed, 100, 100);
			System.out.print(passed+"\n");
		}
		
		g2.dispose(); // Dispose graphics
	}
	
	public void playMusic(int i) {
		music.setFile(i); // Set music file
		music.play(); // Play music
		music.loop(); // Loop music
	}
	
	public void stopMusic() {
		music.stop(); // Stop music
	}
	
	public void playSFX(int i) {
		sfx.setFile(i); // Set sfx file
		sfx.play(); // Play sfx
	}
}
