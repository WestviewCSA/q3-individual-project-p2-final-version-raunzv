import java.io.FileNotFoundException;

public class p2 {

    public static void main(String[] args) {
        // check for --Help first
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--Help")) {
                printHelp();
                System.exit(0);
            }
        }

        boolean stackMode = false;
        boolean queueMode = false;
        boolean optMode = false;
        boolean showTime = false;
        boolean inCoord = false;
        boolean outCoord = false;

        // parse command-line arguments
        for (int i = 0; i < args.length - 1; i++) {
            switch (args[i]) {
                case "--Stack":
                    stackMode = true;
                    break;
                case "--Queue":
                    queueMode = true;
                    break;
                case "--Opt":
                    optMode = true;
                    break;
                case "--Time":
                    showTime = true;
                    break;
                case "--Incoordinate":
                    inCoord = true;
                    break;
                case "--Outcoordinate":
                    outCoord = true;
                    break;
                default:
                    break;
            }
        }

        // validate exactly one mode is set
        int modeCount = 0;
        if (stackMode)
            modeCount++;
        if (queueMode)
            modeCount++;
        if (optMode)
            modeCount++;

        if (modeCount != 1) {
            try {
                throw new IllegalCommandLineInputsException(
                        "Must specify exactly one of --Stack, --Queue, or --Opt");
            } catch (IllegalCommandLineInputsException e) {
                System.out.println(e.getMessage());
                System.exit(-1);
            }
        }

        String fileName = args[args.length - 1];

        try {
            String[][][] map = MazeReader.getTextBasedMap(fileName);

            double startTime = System.nanoTime();

            String[][][] solvedMap = null;

            if (queueMode) {
                solvedMap = MazeSolver.solveWithQueue(map);
            }
            // TODO: stack and optimal solvers

            double endTime = System.nanoTime();

            if (solvedMap == null) {
                System.out.println("The Wolverine Store is closed.");
            } else {
                MazeReader.printMap(solvedMap);
            }

            if (showTime) {
                double seconds = (endTime - startTime) / 1000000000.0;
                System.out.println("Total Runtime: " + seconds + " seconds");
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            System.exit(-1);
        }
    }

    public static void printHelp() {
        System.out.println("Wolverine's Quest for the Diamond Wolverine Coin");
        System.out.println("Usage: java p2 [options] <mapfile>");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --Stack          Use stack-based pathfinding");
        System.out.println("  --Queue          Use queue-based pathfinding");
        System.out.println("  --Opt            Use optimal pathfinding");
        System.out.println("  --Time           Print runtime of the search");
        System.out.println("  --Incoordinate   Input is coordinate-based format");
        System.out.println("  --Outcoordinate  Output in coordinate-based format");
        System.out.println("  --Help           Print this help message");
    }
}
