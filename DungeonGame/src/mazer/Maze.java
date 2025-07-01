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

    public String tile[] = { "1", "2", "3", "4", "4", "4" };
    private ArrayList<LinkedList<Integer>> adjList;
    private int mazeRow;
    private int mazeCol;

    public int[][] maze;
    public int[][] mazeCell;
    public int vertices;
    GamePanel gp;

    public Maze(GamePanel gp) {
        this.gp = gp;
        this.mazeRow = gp.maxScreenRow / 2;
        this.mazeCol = gp.maxScreenCol / 2;
        this.vertices = mazeRow * mazeCol;

        maze = new int[mazeCol][mazeRow];
        mazeCell = new int[mazeCol][mazeRow];
        adjList = new ArrayList<LinkedList<Integer>>();

        for (int i = 0; i < vertices; i++) {
            adjList.add(new LinkedList<Integer>());
        }

        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[x].length; y++) {
                maze[x][y] = 0;
            }
        }

        int i = 0;
        for (int x = 0; x < mazeCell.length; x++) {
            for (int y = 0; y < mazeCell[x].length; y++) {
                mazeCell[x][y] = i++;
            }
        }

        generateMaze();
        
    }

    public void generateMaze() {
        Stack<Integer> cellStack = new Stack<>();
        int x = 0, y = 0;
        int visited = 1;
        maze[x][y] = 1;

        while (visited < vertices) {
            ArrayList<String> possibleDir = new ArrayList<>();

            if (x - 1 >= 0 && maze[x - 1][y] == 0) possibleDir.add("N");
            if (x + 1 < mazeCol && maze[x + 1][y] == 0) possibleDir.add("S");
            if (y - 1 >= 0 && maze[x][y - 1] == 0) possibleDir.add("W");
            if (y + 1 < mazeRow && maze[x][y + 1] == 0) possibleDir.add("E");

            if (!possibleDir.isEmpty()) {
                String dir = direction(possibleDir);

                switch (dir) {
                    case "N":
                        maze[x - 1][y] = 1;
                        cellStack.push(mazeCell[x][y]);
                        connectCells(x, y, x - 1, y);
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
                int target = cellStack.pop();
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

    public void connectCells(int x1, int y1, int x2, int y2) {
        adjList.get(mazeCell[x1][y1]).add(mazeCell[x2][y2]);
        adjList.get(mazeCell[x2][y2]).add(mazeCell[x1][y1]);
    }

    public String direction(ArrayList<String> array) {
        Random r = new Random();
        return array.get(r.nextInt(array.size()));
    }

    public char tileChoice(String tiles) {
        Random r = new Random();
        return tiles.charAt(r.nextInt(tiles.length()));
    }

    public String opMaze() {
        int visualRows = mazeRow * 2 + 1;
        int visualCols = mazeCol * 2 + 1;
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r < visualRows; r++) {
            for (int c = 0; c < visualCols; c++) {
                int tileNum = 0;
                
                if(c ==24&&r==1) {
                	tileNum = Character.getNumericValue(tileChoice("123444"));
                }else if (r % 2 == 1 && c % 2 == 1) {
                    // Maze cell
                    int cellX = r / 2;
                    int cellY = c / 2;
                    tileNum = Character.getNumericValue(tileChoice("123444")); // bias for '4'
                }
                else if (r % 2 == 1 && c % 2 == 0) {
                    // Horizontal wall between cells
                    int cellX = r / 2;
                    int leftY = c / 2 - 1;
                    int rightY = c / 2;
                    if (leftY >= 0 && rightY < mazeRow &&
                        adjList.get(mazeCell[cellX][leftY]).contains(mazeCell[cellX][rightY])) {
                        tileNum = Character.getNumericValue(tileChoice("123444"));
                    }
                }
                else if (r % 2 == 0 && c % 2 == 1) {
                    // Vertical wall between cells
                    int topX = r / 2 - 1;
                    int bottomX = r / 2;
                    int cellY = c / 2;
                    if (topX >= 0 && bottomX < mazeCol &&
                        adjList.get(mazeCell[topX][cellY]).contains(mazeCell[bottomX][cellY])) {
                        tileNum = Character.getNumericValue(tileChoice("123444"));
                    }
                }

                // Borders and remaining walls remain 0
                sb.append(tileNum);
                if (c < visualCols - 1) sb.append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }


    public String saveMazeToPackageFolder(String filename) {
        try {
            String packagePath = new File(Maze.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            File dir = new File(packagePath, "src/res/maps/");

            if (!dir.exists()) {
                dir.mkdirs();
            }

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