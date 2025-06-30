package mazer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class MazeSolver {

    private int[][] maze;
    private int[][] solution;
    private int rows, cols;
    public String tile[] = { "1", "2", "3", "4", "4", "4" };

    public MazeSolver(String filePath, int maxRows, int maxCols) {
        this.rows = maxRows;
        this.cols = maxCols;
        maze = new int[cols][rows];
        solution = new int[cols][rows];
        loadMap(filePath);
    }
    
    public char tileChoice(String tiles) {
        Random r = new Random();
        return tiles.charAt(r.nextInt(tiles.length()));
    }
    

    private void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;
            String line;

            while ((line = br.readLine()) != null && row < rows) {
                String[] numbers = line.trim().split(" ");
                for (int col = 0; col < cols && col < numbers.length; col++) {
                    maze[col][row] = Integer.parseInt(numbers[col]) == 0 ? 0 : 1;
                }
                row++;
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Error reading maze: " + e.getMessage());
        }
    }

    public boolean solveMaze(int x, int y) {
        // Reached goal
        if (x == cols - 1 && y == rows - 1 && ( maze[x][y] == 1 || maze[x][y] == 2 || maze[x][y] == 3 maze[x][y] == 4) {
            solution[x][y] = 1;
            return true;
        }

        if (isSafe(x, y)) {
            solution[x][y] = 1;

            if (solveMaze(x + 1, y)) return true;
            if (solveMaze(x, y + 1)) return true;
            if (solveMaze(x - 1, y)) return true;
            if (solveMaze(x, y - 1)) return true;

            solution[x][y] = 0; // backtrack
        }

        return false;
    }

    private boolean isSafe(int x, int y) {
        return (x >= 0 && x < cols && y >= 0 && y < rows && maze[x][y] == 1 && solution[x][y] == 0);
    }

    public void printSolution() {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                System.out.print(solution[x][y] + " ");
            }
            System.out.println();
        }
    }
}
