package project1;

import java.util.*;

public class DijkstraAlgorithm {
    public static String dijkstra(Graph graph, NodeData nodeData, int src, int dest) {
        int size = graph.getSize();
        double[] dist = new double[size];
        int[] prev = new int[size];
        boolean[] visited = new boolean[size];

        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);
        dist[src] = 0;

        for (int i = 0; i < size; i++) {
            int u = minDistance(dist, visited);
            if (u == -1) break;

            visited[u] = true;

            for (int v = 0; v < size; v++) {
                if (!visited[v] && graph.getDistance(u, v) < Double.POSITIVE_INFINITY &&
                        dist[u] + graph.getDistance(u, v) < dist[v]) {
                    dist[v] = dist[u] + graph.getDistance(u, v);
                    prev[v] = u;
                }
            }
        }

        if (dist[dest] == Double.POSITIVE_INFINITY) {
            return "No path found from " + nodeData.getName(src) + " to " + nodeData.getName(dest);
        }

        StringBuilder result = new StringBuilder();
        result.append(String.format("Minimum Distance from %s to %s: %.2f km\n",
                nodeData.getName(src), nodeData.getName(dest), dist[dest]));
        result.append("Path: ");
        result.append(getPathString(prev, nodeData, dest));

        return result.toString();
    }

    private static int minDistance(double[] dist, boolean[] visited) {
        double min = Double.POSITIVE_INFINITY;
        int index = -1;

        for (int v = 0; v < dist.length; v++) {
            if (!visited[v] && dist[v] < min) {
                min = dist[v];
                index = v;
            }
        }
        return index;
    }

    private static String getPathString(int[] prev, NodeData nodeData, int node) {
        List<String> path = new ArrayList<>();
        while (node != -1) {
            path.add(nodeData.getName(node));
            node = prev[node];
        }
        Collections.reverse(path);
        return String.join(" -> ", path);
    }
}




// package project1;
// // Contains the implementation of Dijkstra's algorithm for shortest path calculation
// import java.util.*;

// public class DijkstraAlgorithm {
//     public static void dijkstra(Graph graph, NodeData nodeData, int src, int dest) {
//         int size = graph.getSize();
//         double[] dist = new double[size];
//         int[] prev = new int[size];
//         boolean[] visited = new boolean[size];

//         Arrays.fill(dist, Double.POSITIVE_INFINITY);
//         Arrays.fill(prev, -1);
//         dist[src] = 0;

//         for (int i = 0; i < size; i++) {
//             int u = minDistance(dist, visited);
//             if (u == -1) break;

//             visited[u] = true;

//             for (int v = 0; v < size; v++) {
//                 if (!visited[v] && graph.getDistance(u, v) < Double.POSITIVE_INFINITY &&
//                         dist[u] + graph.getDistance(u, v) < dist[v]) {
//                     dist[v] = dist[u] + graph.getDistance(u, v);
//                     prev[v] = u;
//                 }
//             }
//         }

//         if (dist[dest] == Double.POSITIVE_INFINITY) {
//             System.out.println("No path found from " + nodeData.getName(src) + " to " + nodeData.getName(dest));
//             return;
//         }

//         System.out.printf("Minimum Distance from %s to %s: %.2f km\n",
//                 nodeData.getName(src), nodeData.getName(dest), dist[dest]);
//         System.out.print("Path: ");
//         printPath(prev, nodeData, dest);
//         System.out.println();
//     }

//     private static int minDistance(double[] dist, boolean[] visited) {
//         double min = Double.POSITIVE_INFINITY;
//         int index = -1;

//         for (int v = 0; v < dist.length; v++) {
//             if (!visited[v] && dist[v] < min) {
//                 min = dist[v];
//                 index = v;
//             }
//         }
//         return index;
//     }

//     private static void printPath(int[] prev, NodeData nodeData, int node) {
//         if (prev[node] != -1) {
//             printPath(prev, nodeData, prev[node]);
//             System.out.print(" -> ");
//         }
//         System.out.print(nodeData.getName(node));
//     }
// }

