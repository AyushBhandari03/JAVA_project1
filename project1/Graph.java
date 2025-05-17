package project1;
// Represents the structure and data of the graph including distance matrix
import java.io.*;

public class Graph {
    private final int size;
    private final double[][] matrix;

    public Graph(int size) {
        this.size = size;
        this.matrix = new double[size][size];
    }

    public void loadMatrix(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null && row < size) {
                String[] values = line.split(",", -1);
                for (int col = 1; col < size + 1 && col < values.length; col++) {
                    try {
                        String val = values[col].trim();
                        matrix[row][col - 1] = val.isEmpty() ? Double.POSITIVE_INFINITY : Double.parseDouble(val);
                    } catch (NumberFormatException e) {
                        matrix[row][col - 1] = Double.POSITIVE_INFINITY;
                    }
                }
                row++;
            }
        }
    }

    public double getDistance(int from, int to) {
        return matrix[from][to];
    }

    public int getSize() {
        return size;
    }
}
