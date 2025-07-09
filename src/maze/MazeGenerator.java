package maze;

import java.util.*;

public class MazeGenerator {
    private int width;
    private int height;
    private int[][] maze;
    private Random random;
    
    // Directions: North, East, South, West
    private final int[][] directions = {{0, -2}, {2, 0}, {0, 2}, {-2, 0}};
    
    public MazeGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.random = new Random();
        this.maze = new int[width][height];
    }
    
    public int[][] generateMaze() {
        // Initialize maze with all walls (1 = wall, 0 = path)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                maze[x][y] = 1;
            }
        }
        
        // Start from top-left corner (1,1) to ensure player spawn area is clear
        generateMazeRecursive(1, 1);
        
        // Ensure start position is clear
        maze[0][0] = 0;
        maze[1][0] = 0;
        maze[0][1] = 0;
        
        // Create exit at bottom-right
        maze[width-1][height-1] = 0;
        maze[width-2][height-1] = 0;
        maze[width-1][height-2] = 0;
        
        return maze;
    }
    
    private void generateMazeRecursive(int x, int y) {
        maze[x][y] = 0; // Mark current cell as path
        
        // Create list of directions and shuffle them
        List<int[]> dirs = new ArrayList<>(Arrays.asList(directions));
        Collections.shuffle(dirs, random);
        
        for (int[] dir : dirs) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            
            // Check if the new position is within bounds and is a wall
            if (nx > 0 && nx < width - 1 && ny > 0 && ny < height - 1 && maze[nx][ny] == 1) {
                // Remove wall between current cell and new cell
                maze[x + dir[0] / 2][y + dir[1] / 2] = 0;
                generateMazeRecursive(nx, ny);
            }
        }
    }
    
    public void printMaze() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(maze[x][y] == 1 ? "█" : " ");
            }
            System.out.println();
        }
    }
}