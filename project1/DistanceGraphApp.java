// package project1;
// import java.util.*;
// // Main class to execute the DistanceGraph application
// // Handles user interaction and orchestrates the use of other utility classes
// public class DistanceGraphApp {
//     public static void main(String[] args) {
//         final int NUM_NODES = 70;
//         Graph graph = new Graph(NUM_NODES);
//         NodeData nodeData = new NodeData(NUM_NODES);

//         try {
//             nodeData.loadNames("E:\\Project\\JAVA\\extra_work\\Final_Points.csv");
//             graph.loadMatrix("E:\\Project\\JAVA\\extra_work\\updated_weights_matrix.csv");

//             Scanner sc = new Scanner(System.in);

// // Step 1: Show Restaurants in 3 Columns
// System.out.println("\nAvailable Restaurants:");
// List<Integer> restaurantIndices = new ArrayList<>();
// for (int i = 0; i < NUM_NODES; i++) {
//     String nick = nodeData.getNickname(i);
//     if (nick != null && nick.startsWith("Res")) {
//         restaurantIndices.add(i);
//     }
// }

// for (int i = 0; i < restaurantIndices.size(); i++) {
//     int index = restaurantIndices.get(i);
//     System.out.printf("%-35s", index + ": " + nodeData.getName(index));
//     if ((i + 1) % 3 == 0) System.out.println();
// }
// if (restaurantIndices.size() % 3 != 0) System.out.println(); // line break if needed

// System.out.print("\nEnter restaurant index (50-69): ");
// int source = sc.nextInt();

// // Step 2: Show Destinations in 3 Columns
// System.out.println("\nAvailable Destination Points:");
// List<Integer> destinationIndices = new ArrayList<>();
// for (int i = 0; i < NUM_NODES; i++) {
//     String nick = nodeData.getNickname(i);
//     if (nick != null && !nick.startsWith("Res")) {
//         destinationIndices.add(i);
//     }
// }

// for (int i = 0; i < destinationIndices.size(); i++) {
//     int index = destinationIndices.get(i);
//     System.out.printf("%-35s", index + ": " + nodeData.getName(index));
//     if ((i + 1) % 3 == 0) System.out.println();
// }
// if (destinationIndices.size() % 3 != 0) System.out.println();

// System.out.print("\nEnter destination index (0-49): ");
// int dest = sc.nextInt();

//             if (!nodeData.isValidDestination(dest)) {
//                 System.out.println("Invalid destination! Must be a non-restaurant node within 0-69 range.");
//                 return;
//             }

//             DijkstraAlgorithm.dijkstra(graph, nodeData, source, dest);
//         } catch (Exception e) {
//             System.err.println("An error occurred: " + e.getMessage());
//         }
//     }
// }

package project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DistanceGraphApp {
    public static void main(String[] args) {
        final int NUM_NODES = 70;
        Graph graph = new Graph(NUM_NODES);
        NodeData nodeData = new NodeData(NUM_NODES);

        try {
            nodeData.loadNames("E:\\Project\\JAVA\\extra_work\\Final_Points.csv");
            graph.loadMatrix("E:\\Project\\JAVA\\extra_work\\updated_weights_matrix.csv");

            // Get node indices
            ArrayList<Integer> restaurantIndices = new ArrayList<>();
            ArrayList<Integer> destinationIndices = new ArrayList<>();
            for (int i = 0; i < NUM_NODES; i++) {
                String nick = nodeData.getNickname(i);
                if (nick != null) {
                    if (nick.startsWith("Res")) restaurantIndices.add(i);
                    else destinationIndices.add(i);
                }
            }

            // Setup JFrame
            JFrame frame = new JFrame("Path Finder - Restaurant to Location");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel heading = new JLabel("Dilivery route Optimization", SwingConstants.CENTER);
            heading.setFont(new Font("SansSerif", Font.BOLD, 20));
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            frame.add(heading, gbc);

            // Restaurant Dropdown
            JLabel resLabel = new JLabel("Select Restaurant:");
            gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
            frame.add(resLabel, gbc);

            JComboBox<String> restaurantBox = new JComboBox<>();
            for (int idx : restaurantIndices)
                restaurantBox.addItem(idx + ": " + nodeData.getName(idx));
            gbc.gridx = 1;
            frame.add(restaurantBox, gbc);

            // Location Dropdown
            JLabel locLabel = new JLabel("Select Destination:");
            gbc.gridx = 0; gbc.gridy = 2;
            frame.add(locLabel, gbc);

            JComboBox<String> locationBox = new JComboBox<>();
            for (int idx : destinationIndices)
                locationBox.addItem(idx + ": " + nodeData.getName(idx));
            gbc.gridx = 1;
            frame.add(locationBox, gbc);

            // Submit button
            JButton runButton = new JButton("Find Shortest Path");
            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
            frame.add(runButton, gbc);

            // Output display
            JTextArea resultArea = new JTextArea(5, 30);
            resultArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(resultArea);
            gbc.gridy = 4;
            frame.add(scrollPane, gbc);

            // Action on button click
            runButton.addActionListener(e -> {
                int sourceIndex = restaurantIndices.get(restaurantBox.getSelectedIndex());
                int destIndex = destinationIndices.get(locationBox.getSelectedIndex());

                if (!nodeData.isValidDestination(destIndex)) {
                    String msg = "Invalid destination selected.";
                    System.out.println(msg);
                    resultArea.setText(msg);
                    return;
                }

                System.out.println("Finding shortest path from " + nodeData.getName(sourceIndex) + " to " + nodeData.getName(destIndex) + "...");
                String result = DijkstraAlgorithm.dijkstra(graph, nodeData, sourceIndex, destIndex);
                resultArea.setText(result);
                System.out.println(result);
            });

            frame.setVisible(true);

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
