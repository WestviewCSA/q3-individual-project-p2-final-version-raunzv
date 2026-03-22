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

    // find a character in a specific maze level
    public static int[] findPositionInLevel(String[][][] map, String target, int level) {
        for (int row = 0; row < map[level].length; row++) {
            for (int col = 0; col < map[level][row].length; col++) {
                if (map[level][row][col].equals(target)) {
                    return new int[] { level, row, col };
                }
            }
        }
        return null;
    }

    // queue-based BFS solver with multi-maze support
    public static String[][][] solveWithQueue(String[][][] map) {
        int levels = map.length;
        int rows = map[0].length;
        int cols = map[0][0].length;

        // find start position (W)
        int[] start = findPosition(map, "W");
        if (start == null)
            return null;

        // find goal — either $ or | (walkway to next maze)
        int[] goal = findPosition(map, "$");
        if (goal == null) {
            goal = findPosition(map, "|");
            if (goal == null)
                return null;
        }

        boolean[][][] visited = new boolean[levels][rows][cols];
        int[][][][] parent = new int[levels][rows][cols][3];

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
        int[] foundGoal = null;

        while (!queue.isEmpty() && !found) {
            int[] current = queue.removeFirst();
            int curLevel = current[0];
            int curRow = current[1];
            int curCol = current[2];

            for (int d = 0; d < 4; d++) {
                int newRow = curRow + dRow[d];
                int newCol = curCol + dCol[d];

                if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols)
                    continue;
                if (visited[curLevel][newRow][newCol])
                    continue;
                String cell = map[curLevel][newRow][newCol];
                if (cell.equals("@"))
                    continue;

                visited[curLevel][newRow][newCol] = true;
                parent[curLevel][newRow][newCol] = new int[] { curLevel, curRow, curCol };

                if (cell.equals("$") || cell.equals("|")) {
                    found = true;
                    foundGoal = new int[] { curLevel, newRow, newCol };
                    break;
                }

                queue.add(new int[] { curLevel, newRow, newCol });
            }
        }

        if (!found)
            return null;

        // trace path from goal back to start
        int[] current = foundGoal;
        while (true) {
            int l = current[0];
            int r = current[1];
            int c = current[2];

            if (l == start[0] && r == start[1] && c == start[2])
                break;

            if (!map[l][r][c].equals("W") && !map[l][r][c].equals("$") && !map[l][r][c].equals("|")) {
                map[l][r][c] = "+";
            }

            current = parent[l][r][c];
        }

        // if we reached a walkway |, continue to next maze level
        if (map[foundGoal[0]][foundGoal[1]][foundGoal[2]].equals("|")) {
            int nextLevel = foundGoal[0] + 1;
            if (nextLevel < levels) {
                // find W in next level or use the walkway position
                int[] nextStart = findPositionInLevel(map, "W", nextLevel);
                if (nextStart == null) {
                    // no W in next level, look for $ directly
                    return solveFromLevel(map, nextLevel, visited, parent);
                }
                // recursively solve next level
                return solveWithQueue(map);
            }
        }

        return map;
    }

    // stack-based DFS solver with multi-maze support
    public static String[][][] solveWithStack(String[][][] map) {
        int levels = map.length;
        int rows = map[0].length;
        int cols = map[0][0].length;

        int[] start = findPosition(map, "W");
        if (start == null)
            return null;

        int[] goal = findPosition(map, "$");
        if (goal == null) {
            goal = findPosition(map, "|");
            if (goal == null)
                return null;
        }

        boolean[][][] visited = new boolean[levels][rows][cols];
        int[][][][] parent = new int[levels][rows][cols][3];

        for (int l = 0; l < levels; l++)
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < cols; c++)
                    parent[l][r][c] = new int[] { -1, -1, -1 };

        LinkedList<int[]> stack = new LinkedList<>();
        stack.add(start);
        visited[start[0]][start[1]][start[2]] = true;

        int[] dRow = { -1, 1, 0, 0 };
        int[] dCol = { 0, 0, 1, -1 };

        boolean found = false;
        int[] foundGoal = null;

        while (!stack.isEmpty() && !found) {
            int[] current = stack.removeLast(); // stack: remove from back (LIFO)
            int curLevel = current[0];
            int curRow = current[1];
            int curCol = current[2];

            for (int d = 0; d < 4; d++) {
                int newRow = curRow + dRow[d];
                int newCol = curCol + dCol[d];

                if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols)
                    continue;
                if (visited[curLevel][newRow][newCol])
                    continue;
                String cell = map[curLevel][newRow][newCol];
                if (cell.equals("@"))
                    continue;

                visited[curLevel][newRow][newCol] = true;
                parent[curLevel][newRow][newCol] = new int[] { curLevel, curRow, curCol };

                if (cell.equals("$") || cell.equals("|")) {
                    found = true;
                    foundGoal = new int[] { curLevel, newRow, newCol };
                    break;
                }

                stack.add(new int[] { curLevel, newRow, newCol });
            }
        }

        if (!found)
            return null;

        // trace path from goal back to start
        int[] current = foundGoal;
        while (true) {
            int l = current[0];
            int r = current[1];
            int c = current[2];

            if (l == start[0] && r == start[1] && c == start[2])
                break;

            if (!map[l][r][c].equals("W") && !map[l][r][c].equals("$") && !map[l][r][c].equals("|")) {
                map[l][r][c] = "+";
            }

            current = parent[l][r][c];
        }

        // if we reached a walkway |, continue to next maze level
        if (map[foundGoal[0]][foundGoal[1]][foundGoal[2]].equals("|")) {
            int nextLevel = foundGoal[0] + 1;
            if (nextLevel < levels) {
                int[] nextStart = findPositionInLevel(map, "W", nextLevel);
                if (nextStart == null) {
                    return solveFromLevel(map, nextLevel, visited, parent);
                }
                return solveWithStack(map);
            }
        }

        return map;
    }

    // helper to solve from a specific level (used after walkway transition)
    private static String[][][] solveFromLevel(String[][][] map, int startLevel,
            boolean[][][] visited, int[][][][] parent) {
        int levels = map.length;
        int rows = map[0].length;
        int cols = map[0][0].length;

        // find W in this level
        int[] start = findPositionInLevel(map, "W", startLevel);
        if (start == null)
            return map;

        int[] goal = findPosition(map, "$");
        if (goal == null)
            return null;

        // reset visited and parent for this level onwards
        for (int l = startLevel; l < levels; l++)
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < cols; c++) {
                    visited[l][r][c] = false;
                    parent[l][r][c] = new int[] { -1, -1, -1 };
                }

        LinkedList<int[]> queue = new LinkedList<>();
        queue.add(start);
        visited[start[0]][start[1]][start[2]] = true;

        int[] dRow = { -1, 1, 0, 0 };
        int[] dCol = { 0, 0, 1, -1 };

        boolean found = false;
        int[] foundGoal = null;

        while (!queue.isEmpty() && !found) {
            int[] current = queue.removeFirst();
            int curLevel = current[0];
            int curRow = current[1];
            int curCol = current[2];

            for (int d = 0; d < 4; d++) {
                int newRow = curRow + dRow[d];
                int newCol = curCol + dCol[d];

                if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols)
                    continue;
                if (visited[curLevel][newRow][newCol])
                    continue;
                String cell = map[curLevel][newRow][newCol];
                if (cell.equals("@"))
                    continue;

                visited[curLevel][newRow][newCol] = true;
                parent[curLevel][newRow][newCol] = new int[] { curLevel, curRow, curCol };

                if (cell.equals("$")) {
                    found = true;
                    foundGoal = new int[] { curLevel, newRow, newCol };
                    break;
                }

                queue.add(new int[] { curLevel, newRow, newCol });
            }
        }

        if (!found)
            return null;

        int[] current = foundGoal;
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
