package extras;
import java.io.*;
import java.util.*;

public class DistanceGraph {
    static final int NUM_NODES = 70;
    static double[][] graph = new double[NUM_NODES][NUM_NODES];
    static String[] nicknames = new String[NUM_NODES];
    static String[] names = new String[NUM_NODES];

    public static void main(String[] args) {
        try {
            loadNames("E:\\Project\\JAVA\\extra work\\Final_Points.csv");
            loadMatrix("E:\\Project\\JAVA\\extra work\\Distance_Matrix.csv");

            Scanner sc = new Scanner(System.in);
            System.out.println("Available Restaurants:");
            for (int i = 0; i < NUM_NODES; i++) {
                if (nicknames[i] != null && nicknames[i].startsWith("Res")) {
                    System.out.println(i + ": " + names[i]);
                }
            }

            System.out.print("Enter restaurant index (0-69): ");
            int source = sc.nextInt();

            System.out.print("Enter destination index (0-69, not a restaurant): ");
            int dest = sc.nextInt();

            if (dest < 0 || dest >= NUM_NODES || nicknames[dest] == null || nicknames[dest].startsWith("Res")) {
                System.out.println("Invalid destination! Must be a non-restaurant node within 0-69 range.");
                return;
            }

            dijkstra(source, dest);
        } catch (IOException e) {
            System.err.println("Error loading data files: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    static void loadNames(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null && i < NUM_NODES) {
                String[] parts = line.split(",", -1); // Keep empty values
                if (parts.length >= 2) {
                    nicknames[i] = parts[0].trim();
                    names[i] = parts[1].trim();
                    i++;
                }
            }
        }
    }

    static void loadMatrix(String filename) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
        String line;
        int row = 0;
        br.readLine(); // skip the header row
        while ((line = br.readLine()) != null && row < NUM_NODES) {
            String[] values = line.split(",", -1);
            for (int col = 1; col < NUM_NODES + 1 && col < values.length; col++) { // skip first column
                try {
                    String val = values[col].trim();
                    graph[row][col - 1] = val.isEmpty() ? Double.POSITIVE_INFINITY : Double.parseDouble(val);
                } catch (NumberFormatException e) {
                    graph[row][col - 1] = Double.POSITIVE_INFINITY;
                }
            }
            row++;
        }
    }
    }

    static void dijkstra(int src, int dest) {
        if (src < 0 || src >= NUM_NODES || dest < 0 || dest >= NUM_NODES) {
            System.out.println("Invalid source or destination index!");
            return;
        }

        double[] dist = new double[NUM_NODES];
        int[] prev = new int[NUM_NODES];
        boolean[] visited = new boolean[NUM_NODES];

        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);
        dist[src] = 0;

        for (int i = 0; i < NUM_NODES; i++) {
            int u = minDistance(dist, visited);
            if (u == -1) break;

            visited[u] = true;

            for (int v = 0; v < NUM_NODES; v++) {
                if (!visited[v] && graph[u][v] < Double.POSITIVE_INFINITY &&
                    dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                    prev[v] = u;
                }
            }
        }

        if (dist[dest] == Double.POSITIVE_INFINITY) {
            System.out.println("No path found from " + names[src] + " to " + names[dest]);
            return;
        }

        System.out.printf("Minimum Distance from %s to %s: %.2f km\n", 
                         names[src], names[dest], dist[dest]);
        System.out.print("Path: ");
        printPath(prev, dest);
        System.out.println();
    }

    static int minDistance(double[] dist, boolean[] visited) {
        double min = Double.POSITIVE_INFINITY;
        int index = -1;

        for (int v = 0; v < NUM_NODES; v++) {
            if (!visited[v] && dist[v] < min) {
                min = dist[v];
                index = v;
            }
        }
        return index;
    }

    static void printPath(int[] prev, int node) {
        if (prev[node] != -1) {
            printPath(prev, prev[node]);
            System.out.print(" -> ");
        }
        System.out.print(names[node]);
    }
}