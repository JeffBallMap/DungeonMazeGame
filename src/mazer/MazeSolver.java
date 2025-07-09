package mazer;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class MazeSolver {

    private int[][] maze;       // Maze grid representation (cols x rows)
    private int[][] solution;   // Solution path grid (cols x rows)
    private int rows, cols;     // Maze dimensions
    public String tile[] = { "1", "2", "3", "4", "4", "4" };  // Tile choices for maze visualization

    // Constructor: initializes maze size and loads maze from file
    public MazeSolver(String filePath, int maxRows, int maxCols) {
        this.rows = maxRows;
        this.cols = maxCols;
        maze = new int[cols][rows];
        solution = new int[cols][rows];
        loadMap(filePath);
    }
    
    // Randomly picks a tile character from a given string of tile options
    public char tileChoice(String tiles) {
        Random r = new Random();
        return tiles.charAt(r.nextInt(tiles.length()));
    }
    
    // Loads maze layout from a resource file and populates the maze array
    private void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            if (is == null) {
                System.out.println("Could not find file: " + filePath);
                return;
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;
            String line;

            // Read each line until rows limit
            while ((line = br.readLine()) != null && row < rows) {
                String[] numbers = line.trim().split(" ");
                // Parse each number in line up to columns limit
                for (int col = 0; col < cols && col < numbers.length; col++) {
                    // Convert '0' to 0, else assign a random tile number for non-zero values
                    maze[col][row] = Integer.parseInt(numbers[col]) == 0 ? 0 : tileChoice("123444");
                }
                row++;
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Error reading maze: " + e.getMessage());
        }
    }

    // Recursive backtracking solver for the maze starting from (x,y)
    public boolean solveMaze(int x, int y) {
        // Check if goal cell (bottom right exit) reached and it is walkable
        if (x == cols - 1 && y == 1 && maze[x][y] != 0){
            solution[x][y] = 1;  // Mark solution path at goal
            return true;
        }

        // Check if current cell is safe to proceed
        if (isSafe(x, y)) {
            solution[x][y] = 1;  // Mark current cell as part of solution path

            // Explore all 4 directions: right, down, left, up recursively
            if (solveMaze(x + 1, y)) return true;
            if (solveMaze(x, y + 1)) return true;
            if (solveMaze(x - 1, y)) return true;
            if (solveMaze(x, y - 1)) return true;

            // Backtrack: unmark this cell
            solution[x][y] = 0;
        }

        return false;  // No solution
    }

    // Checks if inbounds, walkable and unvisited in solution
    private boolean isSafe(int x, int y) {
        return (x >= 0 && x < cols && y >= 0 && y < rows && maze[x][y] != 0 && solution[x][y] == 0);
    }

    // Prints the solution grid to console(debug only)
    public void printSolution() {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                System.out.print(solution[x][y] + " ");
            }
            System.out.println();
        }
    }
    
    // Writes the solution path grid to a specified output file
    public void writeSolutionToFile(String outputPath) {
        try (FileWriter writer = new FileWriter(outputPath)) {
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    writer.write(solution[x][y] + " ");
                }
                writer.write("\n");
            }
            System.out.println("Solution written to: " + outputPath);
        } catch (IOException e) {
            System.out.println("Error writing solution file: " + e.getMessage());
        }
    }
}
