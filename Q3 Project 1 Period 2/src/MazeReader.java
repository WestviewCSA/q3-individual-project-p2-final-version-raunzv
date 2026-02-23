import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class MazeReader {
    public static void main(String[] args) throws FileNotFoundException {
        String[][][] map = getMap("src/easyMap1.txt");
    }


    public static String[][][] getMap(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);


        int rows    = scanner.nextInt();
        int columns = scanner.nextInt();
        int levels  = scanner.nextInt();


        String[][][] mapping = new String[levels][rows][columns];


        for (int i = 0; i < levels; i++) {
            for (int j = 0; j < rows; j++) {
                String line = scanner.next();
                for (int h = 0; h < columns; h++) {
                    mapping[i][j][h] = line.substring(h, h + 1);
                }
            }
        }


        scanner.close();
        return mapping;
    }
}



