// DijkstraAlgorithm.java  
// This class implements Dijkstra's algorithm to compute the shortest path  
// between a source and destination node in a weighted graph.  
// It calculates the minimum distance using adjacency data from the Graph class.  
// The algorithm maintains distance, predecessor, and visited arrays.  
// It provides methods to get the shortest path as both a string summary  
// and a list of node indices.  
// Used in conjunction with GUI to display delivery routes on a map.  

package project1;

import java.util.*;

public class DijkstraAlgorithm {

    public static String dijkstra(Graph graph, NodeData nodeData, int src, int dest) {
        PathResult pathResult = computePath(graph, src, dest);

        if (pathResult.path.isEmpty()) {
            return "No path found from " + nodeData.getName(src) + " to " + nodeData.getName(dest);
        }

        StringBuilder result = new StringBuilder();
        result.append(String.format("Minimum Distance from %s to %s: %.2f km\n",
                nodeData.getName(src), nodeData.getName(dest), pathResult.distance));
        result.append("Path: ");

        List<String> namedPath = new ArrayList<>();
        for (int idx : pathResult.path) {
            namedPath.add(nodeData.getName(idx));
        }

        result.append(String.join(" -> ", namedPath));
        return result.toString();
    }

    public static List<Integer> getPathIndices(Graph graph, int src, int dest) {
        return computePath(graph, src, dest).path;
    }

    private static PathResult computePath(Graph graph, int src, int dest) {
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

        List<Integer> path = new ArrayList<>();
        if (dist[dest] != Double.POSITIVE_INFINITY) {
            for (int at = dest; at != -1; at = prev[at]) {
                path.add(at);
            }
            Collections.reverse(path);
        }

        return new PathResult(path, dist[dest]);
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

    private static class PathResult {
        List<Integer> path;
        double distance;

        public PathResult(List<Integer> path, double distance) {
            this.path = path;
            this.distance = distance;
        }
    }
}
