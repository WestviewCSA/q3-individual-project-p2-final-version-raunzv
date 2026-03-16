import java.util.LinkedList;

public class MazeSolver {

    // find a character (W, $, |) in the 3D map, returns {level, row, col}
    public static int[] findPosition(String[][][] map, String target) {
        for (int level = 0; level < map.length; level++) {
            for (int row = 0; row < map[level].length; row++) {
                for (int col = 0; col < map[level][row].length; col++) {
                    if (map[level][row][col].equals(target)) {
                        return new int[] { level, row, col };
                    }
                }
            }
        }
        return null;
    }

    // queue-based BFS solver
    public static String[][][] solveWithQueue(String[][][] map) {
        int levels = map.length;
        int rows = map[0].length;
        int cols = map[0][0].length;

        int[] start = findPosition(map, "W");
        int[] goal = findPosition(map, "$");
        if (start == null || goal == null)
            return null;

        boolean[][][] visited = new boolean[levels][rows][cols];
        int[][][][] parent = new int[levels][rows][cols][3];

        // initialize parent to -1
        for (int l = 0; l < levels; l++)
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < cols; c++)
                    parent[l][r][c] = new int[] { -1, -1, -1 };

        LinkedList<int[]> queue = new LinkedList<>();
        queue.add(start);
        visited[start[0]][start[1]][start[2]] = true;

        // directions: N, S, E, W
        int[] dRow = { -1, 1, 0, 0 };
        int[] dCol = { 0, 0, 1, -1 };

        boolean found = false;

        while (!queue.isEmpty() && !found) {
            int[] current = queue.removeFirst();
            int curLevel = current[0];
            int curRow = current[1];
            int curCol = current[2];

            for (int d = 0; d < 4; d++) {
                int newRow = curRow + dRow[d];
                int newCol = curCol + dCol[d];

                if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                    continue;
                }

                if (visited[curLevel][newRow][newCol])
                    continue;
                String cell = map[curLevel][newRow][newCol];
                if (cell.equals("@"))
                    continue;

                visited[curLevel][newRow][newCol] = true;
                parent[curLevel][newRow][newCol] = new int[] { curLevel, curRow, curCol };

                if (cell.equals("$")) {
                    found = true;
                    break;
                }

                queue.add(new int[] { curLevel, newRow, newCol });
            }
        }

        if (!found)
            return null;

        // trace path from goal back to start
        int[] current = goal;
        while (true) {
            int l = current[0];
            int r = current[1];
            int c = current[2];

            if (l == start[0] && r == start[1] && c == start[2])
                break;

            if (!map[l][r][c].equals("W") && !map[l][r][c].equals("$")) {
                map[l][r][c] = "+";
            }

            current = parent[l][r][c];
        }

        return map;
    }
}
