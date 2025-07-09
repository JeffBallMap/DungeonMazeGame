package mazer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import main.GamePanel;

public class Maze {

    public String tile[] = { "1", "2", "3", "4", "4", "4" };  // Tile options (more 4's to bias)
    private ArrayList<LinkedList<Integer>> adjList;           // Graph adjacency list for maze connectivity
    private int mazeRow;  // Maze rows (half of screen rows)
    private int mazeCol;  // Maze columns (half of screen cols)

    public int[][] maze;      // Maze grid cells (0 = wall, 1 = path)
    public int[][] mazeCell;  // Unique IDs for each cell
    public int vertices;      // Total number of maze cells
    GamePanel gp;              // Reference to main game panel

    public Maze(GamePanel gp) {
        this.gp = gp;
        this.mazeRow = gp.maxScreenRow / 2;  // Half screen rows for maze grid
        this.mazeCol = gp.maxScreenCol / 2;  // Half screen cols for maze grid
        this.vertices = mazeRow * mazeCol;   // Total cells in maze

        maze = new int[mazeCol][mazeRow];    // Initialize maze grid
        mazeCell = new int[mazeCol][mazeRow];// Initialize maze cell IDs
        adjList = new ArrayList<>();          // Initialize adjacency list

        // Create adjacency list for each vertex
        for (int i = 0; i < vertices; i++) {
            adjList.add(new LinkedList<>());
        }

        // Set all maze cells to 0 (unvisited/wall)
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[x].length; y++) {
                maze[x][y] = 0;
            }
        }

        // Assign unique ID to each maze cell
        int i = 0;
        for (int x = 0; x < mazeCell.length; x++) {
            for (int y = 0; y < mazeCell[x].length; y++) {
                mazeCell[x][y] = i++;
            }
        }

        generateMaze();  // Generate maze on initialization
    }

    public void generateMaze() {
        Stack<Integer> cellStack = new Stack<>();
        int x = 0, y = 0;      // Start coordinates
        int visited = 1;
        maze[x][y] = 1;        // Mark start cell visited

        while (visited < vertices) {
            ArrayList<String> possibleDir = new ArrayList<>();

            // Check unvisited neighbors and add directions
            if (x - 1 >= 0 && maze[x - 1][y] == 0) possibleDir.add("N");
            if (x + 1 < mazeCol && maze[x + 1][y] == 0) possibleDir.add("S");
            if (y - 1 >= 0 && maze[x][y - 1] == 0) possibleDir.add("W");
            if (y + 1 < mazeRow && maze[x][y + 1] == 0) possibleDir.add("E");

            if (!possibleDir.isEmpty()) {
                String dir = direction(possibleDir);  // Pick random direction

                switch (dir) {
                    case "N":
                        maze[x - 1][y] = 1;                     // Mark visited
                        cellStack.push(mazeCell[x][y]);        // Save current cell in stack
                        connectCells(x, y, x - 1, y);           // Connect cells in graph
                        x--;
                        break;
                    case "S":
                        maze[x + 1][y] = 1;
                        cellStack.push(mazeCell[x][y]);
                        connectCells(x, y, x + 1, y);
                        x++;
                        break;
                    case "E":
                        maze[x][y + 1] = 1;
                        cellStack.push(mazeCell[x][y]);
                        connectCells(x, y, x, y + 1);
                        y++;
                        break;
                    case "W":
                        maze[x][y - 1] = 1;
                        cellStack.push(mazeCell[x][y]);
                        connectCells(x, y, x, y - 1);
                        y--;
                        break;
                }
                visited++;
            } else {
                // Backtrack if no unvisited neighbors
                int target = cellStack.pop();

                // Find x,y for the cell ID target
                for (int i = 0; i < mazeCell.length; i++) {
                    for (int j = 0; j < mazeCell[i].length; j++) {
                        if (target == mazeCell[i][j]) {
                            x = i;
                            y = j;
                            break;
                        }
                    }
                }
            }
        }
    }

    // Connect two cells in adjacency list (undirected)
    public void connectCells(int x1, int y1, int x2, int y2) {
        adjList.get(mazeCell[x1][y1]).add(mazeCell[x2][y2]);
        adjList.get(mazeCell[x2][y2]).add(mazeCell[x1][y1]);
    }

    // Choose random direction from list
    public String direction(ArrayList<String> array) {
        Random r = new Random();
        return array.get(r.nextInt(array.size()));
    }

    // Random tile choice from string
    public char tileChoice(String tiles) {
        Random r = new Random();
        return tiles.charAt(r.nextInt(tiles.length()));
    }

    // Convert maze to visual string with tiles and spaces
    public String opMaze() {
        int visualRows = mazeRow * 2 + 1;   // Visual rows including walls
        int visualCols = mazeCol * 2 + 1;   // Visual cols including walls
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r < visualRows; r++) {
            for (int c = 0; c < visualCols; c++) {
                int tileNum = 0;  // Default wall

                if (c == gp.maxScreenCol - 1 && r == 1) {
                    tileNum = Character.getNumericValue(tileChoice("123444"));  // Exit tile
                } else if (r % 2 == 1 && c % 2 == 1) {
                    // Maze cell positions
                    int cellX = r / 2;
                    int cellY = c / 2;
                    tileNum = Character.getNumericValue(tileChoice("123444"));  // Biased tiles for paths
                } else if (r % 2 == 1 && c % 2 == 0) {
                    // Horizontal wall positions between cells
                    int cellX = r / 2;
                    int leftY = c / 2 - 1;
                    int rightY = c / 2;
                    if (leftY >= 0 && rightY < mazeRow &&
                        adjList.get(mazeCell[cellX][leftY]).contains(mazeCell[cellX][rightY])) {
                        tileNum = Character.getNumericValue(tileChoice("123444"));  // Path if connected
                    }
                } else if (r % 2 == 0 && c % 2 == 1) {
                    // Vertical wall positions between cells
                    int topX = r / 2 - 1;
                    int bottomX = r / 2;
                    int cellY = c / 2;
                    if (topX >= 0 && bottomX < mazeCol &&
                        adjList.get(mazeCell[topX][cellY]).contains(mazeCell[bottomX][cellY])) {
                        tileNum = Character.getNumericValue(tileChoice("123444"));  // Path if connected
                    }
                }
                // Else tileNum remains 0 = wall

                sb.append(tileNum);
                if (c < visualCols - 1) sb.append(" ");  // Space between tiles
            }
            sb.append("\n");  // New line after each row
        }

        return sb.toString();
    }

    // Save maze string to resource folder
    public String saveMazeToPackageFolder(String filename) {
        try {
            // Get package path
            String packagePath = new File(Maze.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            File dir = new File(packagePath, "src/res/maps/");

            // Create directory if missing
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Write maze string to file
            File file = new File(dir, filename);
            FileWriter writer = new FileWriter(file);
            writer.write(opMaze());
            writer.close();

            System.out.println("Maze saved to: " + file.getAbsolutePath());

            return "/src/res/maps/" + filename;

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

}

//else if (c==gp.maxScreenCol/2 && r==gp.maxScreenRow/2){
	//tileNum = Character.getNumericValue(tileChoice("123444"));                	
//}
