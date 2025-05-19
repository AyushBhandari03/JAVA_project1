// Graph.java
// ----------
// Represents the graph using an adjacency matrix.
// - Stores distances between nodes (edges) as a 2D array.
// - Loads the distance matrix from a CSV file.
// - Empty cells or errors are treated as infinite distances.
// - Provides access to matrix values via getDistance() and getSize().

package project1;

import java.io.*;

public class Graph {
    private final int size;
    private final double[][] matrix;

    public Graph(int size) {
        this.size = size;
        this.matrix = new double[size][size];
    }

    public void loadMatrix(String file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int row = 0;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null && row < size) {
                String[] vals = line.split(",", -1);
                for (int col = 1; col < size + 1 && col < vals.length; col++) {
                    try {
                        String val = vals[col].trim();
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
