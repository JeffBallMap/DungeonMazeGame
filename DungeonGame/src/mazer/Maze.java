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

    private void connectCells(int x1, int y1, int x2, int y2) {
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
        StringBuilder printMaze = new StringBuilder();
        printMaze.append("0 ".repeat(gp.maxScreenCol)).append("\n");

        ArrayList<String> walls = new ArrayList<>();
        ArrayList<String> floors = new ArrayList<>();

        for (int x = 0; x < mazeCell.length; x++) {
            StringBuilder wall = new StringBuilder("0 ");
            StringBuilder floor = new StringBuilder();

            for (int y = 0; y < mazeCell[x].length; y++) {
                if (y + 1 != mazeRow && adjList.get(mazeCell[x][y]).contains(mazeCell[x][y + 1])) {
                    wall.append(tileChoice("123444")).append(" ").append(tileChoice("123444")).append(" ");
                } else if (y + 1 == mazeRow) {
                    wall.append("0");
                } else {
                    wall.append("0 ");
                }

                if (x + 1 < mazeRow && adjList.get(mazeCell[x][y]).contains(mazeCell[x + 1][y])) {
                    floor.append("0 ");
                } else {
                    floor.append("0 ").append(tileChoice("1234")).append(" ");
                }
            }
            floor.append("0");

            walls.add(wall.toString());
            if (x != mazeRow - 1) {
                floors.add(floor.toString());
            }
        }

        for (int x = 0; x < walls.size(); x++) {
            printMaze.append(walls.get(x)).append("\n");
            if (x != walls.size() - 1) {
                printMaze.append(floors.get(x)).append("\n");
            }
        }

        printMaze.append("0 ".repeat(mazeRow - 1)).append("0 0 0");

        return printMaze.toString();
    }

    public String saveMazeToPackageFolder(String filename) {
        try {
            String packagePath = new File(Maze.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            File dir = new File(packagePath, "/src/res/maps/");

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, filename);
            FileWriter writer = new FileWriter(file);
            writer.write(opMaze());
            writer.close();

            System.out.println("Maze saved to: " + file.getAbsolutePath());

            return "/mazer/" + filename;

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}