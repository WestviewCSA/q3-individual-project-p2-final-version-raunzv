import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class MazeReader {
    public static void main(String[] args) throws FileNotFoundException {
        String[][][] map = getTextBasedMap("src/easyMap1.txt");
        printMap(map);


        boolean queueBase = false;
        boolean stackBase = false;
        boolean optimal = false;
        boolean showTime = false;
        boolean inCoord = false;
        boolean outCoord = false;


        for(String arg : args) {
            if(arg.equals("--Stack")) {
                stackBase = true;
            }
            if(arg.equals("--Queue")) {
                queueBase = true;
            }
            if(arg.equals("--Opt")) {
                optimal = true;
            }
            if(arg.equals("--Time")) {
                showTime = true;
            }
            if(arg.equals("--Incoordinate")) {
                inCoord = true;
            }
            if(arg.equals("--Outcoordinate")) {
                outCoord = true;
            }
            if(arg.equals("--Help")) {
                System.out.println("Help");
            }
        }


        if(stackBase == true && queueBase == true) {
            System.exit(-1);
        }
    }


    public static String[][][] getTextBasedMap(String fileName) throws FileNotFoundException {
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


    public static void printMap(String[][][] map) {
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                for(int h = 0; h < map[i][j].length; h++) {
                    System.out.print(map[i][j][h]);
                }
                System.out.println();
            }
        }
    }
}



