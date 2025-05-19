package project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DistanceGraphApp {
    public static void main(String[] args) {
        final int N = 70;

        Graph g = new Graph(N);
        NodeData nd = new NodeData(N);

        try {
            nd.loadNames("E:\\Project\\JAVA\\extra_work\\csv_files\\Final_Points.csv");
            g.loadMatrix("E:\\Project\\JAVA\\extra_work\\csv_files\\updated_weights_matrix.csv");

            ArrayList<Integer> resIdx = new ArrayList<>();
            ArrayList<Integer> locIdx = new ArrayList<>();

            for (int i = 0; i < N; i++) {
                String nick = nd.getNickname(i);
                if (nick != null) {
                    if (nick.startsWith("Res")) resIdx.add(i);
                    else locIdx.add(i);
                }
            }

            JFrame f = new JFrame("Path Finder - Restaurant to Location");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(600, 400);
            f.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel title = new JLabel("Delivery Route Optimization", SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 20));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            f.add(title, gbc);

            JLabel resLbl = new JLabel("Select Restaurant:");
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            f.add(resLbl, gbc);

            JComboBox<String> resBox = new JComboBox<>();
            for (int idx : resIdx) resBox.addItem(idx + ": " + nd.getName(idx));
            gbc.gridx = 1;
            f.add(resBox, gbc);

            JLabel locLbl = new JLabel("Select Destination:");
            gbc.gridx = 0;
            gbc.gridy = 2;
            f.add(locLbl, gbc);

            JComboBox<String> locBox = new JComboBox<>();
            for (int idx : locIdx) locBox.addItem(idx + ": " + nd.getName(idx));
            gbc.gridx = 1;
            f.add(locBox, gbc);

            JButton btn = new JButton("Find Shortest Path");
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            f.add(btn, gbc);

            JTextArea out = new JTextArea(5, 60);
            out.setEditable(false);
            JScrollPane scroll = new JScrollPane(out);
            gbc.gridy = 4;
            f.add(scroll, gbc);

            btn.addActionListener(e -> {
                int src = resIdx.get(resBox.getSelectedIndex());
                int dest = locIdx.get(locBox.getSelectedIndex());

                if (!nd.isValidDestination(dest)) {
                    out.setText("Invalid destination selected.");
                    return;
                }

                String result = DijkstraAlgorithm.dijkstra(g, nd, src, dest);
                System.out.println("Finding shortest path from " + nd.getName(src) + " to " + nd.getName(dest) + "...");
                System.out.println(result+"\n");
                out.setText(result);

            });

            f.setVisible(true);

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}







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

