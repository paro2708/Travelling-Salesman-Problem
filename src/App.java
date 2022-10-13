import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The App class contains the driver code for the TSP algorithm. It takes the input file
 * and decides whether to parse it symmetrically or asymmetrically based on the file
 * extension.
 *
 * @author Ananth R
 * @author Paromita R
 * @author Danish K
 * @Author Chaitanya K
 */
public class App implements Runnable {
    static String fileName;
    static int startingCity = 0;
    static List<Integer> path = new ArrayList<>();
    static String finalPath = "";
    /**
     * The main TSP logic is called from here. A 2D matrix containing the cities and
     * their distances, is sent as an input.
     * @param matrix Contains the distances in matrix format
     */
    public static Runnable tspTask(double matrix[][]) {
        
        TSPGreedy solver = new TSPGreedy(matrix, startingCity);
        double x = solver.findMinRoute();
        System.out.println("Tour: " + x);
        path = solver.getMinRoute();
        for (int city : path) {
            // SHOW IN JPANEL ::::::: ::::::
            finalPath += city + "=>";
        }
        System.out.print(finalPath);
        SymmetricParser sp = null;
		try {
			sp = new SymmetricParser(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
        sp.showPoints(finalPath);
        return null;
    }

    /**
     * Fetches the content of the file and creates a list out of it.
     * @param fileName
     * @return List<String> Containing the contents of the file
     */
    public static List<String> readFile(String fileName) {
        List<String> readResult = new ArrayList<>();
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                readResult.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return readResult;
    }

    /**
     * This method decides whether the graph is symmetrical or asymmetrical. It is done by
     * looking at the file extension. Based on the data, the matrix is formed accordingly.
     */
    @Override
    public void run() {
        if (fileName.contains("atsp")) {
            List<String> readResult = readFile(fileName);
            AsymmetricParser asymmetricParser = new AsymmetricParser();
            double[][] matrix = asymmetricParser.generateMatrix(readResult);
            tspTask(matrix);
        } else {
            double[][] matrix;
            try {
                matrix = new SymmetricParser(fileName).resultArray();
                tspTask(matrix);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Thread.interrupted()) {
            return;
        }
    }

    /**
     * This is the driver code. It passes the filename. It also ensures that the algorithm does
     * not run for more than 300 seconds.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        int MAX_EXECUTION_TIME = 10;
        fileName = "zi929.tsp";
        Future future = executor.submit(new App());
        try {
            future.get(MAX_EXECUTION_TIME, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
//        	System.exit(0);
        	executor.shutdown();
        }
        
    }
}
