import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MazeReader {

    // read text-based map format
    public static String[][][] getTextBasedMap(String fileName)
            throws FileNotFoundException, IncorrectMapFormatException,
            IncompleteMapException, IllegalMapCharacterException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        // check first line has three positive numbers
        if (!scanner.hasNextInt()) {
            scanner.close();
            throw new IncorrectMapFormatException("First line must start with three positive numbers");
        }
        int rows = scanner.nextInt();

        if (!scanner.hasNextInt()) {
            scanner.close();
            throw new IncorrectMapFormatException("First line must have three positive numbers");
        }
        int columns = scanner.nextInt();

        if (!scanner.hasNextInt()) {
            scanner.close();
            throw new IncorrectMapFormatException("First line must have three positive numbers");
        }
        int levels = scanner.nextInt();

        if (rows <= 0 || columns <= 0 || levels <= 0) {
            scanner.close();
            throw new IncorrectMapFormatException("Dimensions must be positive non-zero numbers");
        }

        String[][][] mapping = new String[levels][rows][columns];

        for (int i = 0; i < levels; i++) {
            for (int j = 0; j < rows; j++) {
                if (!scanner.hasNext()) {
                    scanner.close();
                    throw new IncompleteMapException("Not enough rows in maze level " + i);
                }
                String line = scanner.next();
                if (line.length() < columns) {
                    scanner.close();
                    throw new IncompleteMapException(
                            "Row " + j + " in level " + i + " has " + line.length()
                                    + " characters, expected " + columns);
                }
                for (int h = 0; h < columns; h++) {
                    String ch = line.substring(h, h + 1);
                    if (!ch.equals(".") && !ch.equals("@") && !ch.equals("W")
                            && !ch.equals("$") && !ch.equals("|")) {
                        scanner.close();
                        throw new IllegalMapCharacterException(
                                "Illegal character '" + ch + "' at row " + j + " col " + h);
                    }
                    mapping[i][j][h] = ch;
                }
            }
        }

        scanner.close();
        return mapping;
    }

    // read coordinate-based map format
    public static String[][][] getCoordinateBasedMap(String fileName)
            throws FileNotFoundException, IncorrectMapFormatException,
            IllegalMapCharacterException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        if (!scanner.hasNextInt()) {
            scanner.close();
            throw new IncorrectMapFormatException("First line must start with three positive numbers");
        }
        int rows = scanner.nextInt();

        if (!scanner.hasNextInt()) {
            scanner.close();
            throw new IncorrectMapFormatException("First line must have three positive numbers");
        }
        int columns = scanner.nextInt();

        if (!scanner.hasNextInt()) {
            scanner.close();
            throw new IncorrectMapFormatException("First line must have three positive numbers");
        }
        int levels = scanner.nextInt();

        if (rows <= 0 || columns <= 0 || levels <= 0) {
            scanner.close();
            throw new IncorrectMapFormatException("Dimensions must be positive non-zero numbers");
        }

        // initialize all cells to open
        String[][][] mapping = new String[levels][rows][columns];
        for (int l = 0; l < levels; l++)
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < columns; c++)
                    mapping[l][r][c] = ".";

        // read each coordinate line
        while (scanner.hasNext()) {
            String ch = scanner.next();
            if (!ch.equals(".") && !ch.equals("@") && !ch.equals("W")
                    && !ch.equals("$") && !ch.equals("|")) {
                scanner.close();
                throw new IllegalMapCharacterException("Illegal character '" + ch + "'");
            }
            if (!scanner.hasNextInt())
                break;
            int row = scanner.nextInt();
            if (!scanner.hasNextInt())
                break;
            int col = scanner.nextInt();
            if (!scanner.hasNextInt())
                break;
            int level = scanner.nextInt();

            if (row < 0 || row >= rows || col < 0 || col >= columns || level < 0 || level >= levels) {
                continue; // skip out-of-bounds coordinates
            }
            mapping[level][row][col] = ch;
        }

        scanner.close();
        return mapping;
    }

    // print map in text format
    public static void printMap(String[][][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                for (int h = 0; h < map[i][j].length; h++) {
                    System.out.print(map[i][j][h]);
                }
                System.out.println();
            }
        }
    }

    // print solved path in coordinate format
    public static void printCoordinatePath(String[][][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                for (int h = 0; h < map[i][j].length; h++) {
                    if (map[i][j][h].equals("+")) {
                        System.out.println("+ " + j + " " + h + " " + i);
                    }
                }
            }
        }
    }
}
