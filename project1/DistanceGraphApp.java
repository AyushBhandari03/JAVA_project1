// DistanceGraphApp.java
// ---------------------
// Main application class with GUI.
// - Loads node data and distance matrix from CSV files.
// - Filters restaurant and destination nodes.
// - Provides a Swing-based GUI with dropdowns and a result area.
// - On button click, uses DijkstraAlgorithm to find the shortest path.
// - Displays the result in a JTextArea.

// package project1;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;
// import java.util.*;

// public class DistanceGraphApp {
//     public static void main(String[] args) {
//         final int N = 70;

//         Graph g = new Graph(N);
//         NodeData nd = new NodeData(N);

//         try {
//             nd.loadNames("E:\\Project\\JAVA\\extra_work\\csv_files\\Final_Points.csv");
//             g.loadMatrix("E:\\Project\\JAVA\\extra_work\\csv_files\\updated_weights_matrix.csv");

//             ArrayList<Integer> resIdx = new ArrayList<>();
//             ArrayList<Integer> locIdx = new ArrayList<>();

//             for (int i = 0; i < N; i++) {
//                 String nick = nd.getNickname(i);
//                 if (nick != null) {
//                     if (nick.startsWith("Res")) resIdx.add(i);
//                     else locIdx.add(i);
//                 }
//             }

//             JFrame f = new JFrame("Path Finder - Restaurant to Location");
//             f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//             f.setSize(600, 400);
//             f.setLayout(new GridBagLayout());
//             GridBagConstraints gbc = new GridBagConstraints();
//             gbc.insets = new Insets(10, 10, 10, 10);
//             gbc.fill = GridBagConstraints.HORIZONTAL;

//             JLabel title = new JLabel("Delivery Route Optimization", SwingConstants.CENTER);
//             title.setFont(new Font("SansSerif", Font.BOLD, 20));
//             gbc.gridx = 0;
//             gbc.gridy = 0;
//             gbc.gridwidth = 2;
//             f.add(title, gbc);

//             JLabel resLbl = new JLabel("Select Restaurant:");
//             gbc.gridx = 0;
//             gbc.gridy = 1;
//             gbc.gridwidth = 1;
//             f.add(resLbl, gbc);

//             JComboBox<String> resBox = new JComboBox<>();
//             for (int idx : resIdx) resBox.addItem(idx + ": " + nd.getName(idx));
//             gbc.gridx = 1;
//             f.add(resBox, gbc);

//             JLabel locLbl = new JLabel("Select Destination:");
//             gbc.gridx = 0;
//             gbc.gridy = 2;
//             f.add(locLbl, gbc);

//             JComboBox<String> locBox = new JComboBox<>();
//             for (int idx : locIdx) locBox.addItem(idx + ": " + nd.getName(idx));
//             gbc.gridx = 1;
//             f.add(locBox, gbc);

//             JButton btn = new JButton("Find Shortest Path");
//             gbc.gridx = 0;
//             gbc.gridy = 3;
//             gbc.gridwidth = 2;
//             f.add(btn, gbc);

//             JTextArea out = new JTextArea(5, 60);
//             out.setEditable(false);
//             JScrollPane scroll = new JScrollPane(out);
//             gbc.gridy = 4;
//             f.add(scroll, gbc);

//             btn.addActionListener(e -> {
//                 int src = resIdx.get(resBox.getSelectedIndex());
//                 int dest = locIdx.get(locBox.getSelectedIndex());

//                 if (!nd.isValidDestination(dest)) {
//                     out.setText("Invalid destination selected.");
//                     return;
//                 }

//                 String result = DijkstraAlgorithm.dijkstra(g, nd, src, dest);
//                 System.out.println("Finding shortest path from " + nd.getName(src) + " to " + nd.getName(dest) + "...");
//                 System.out.println(result+"\n");
//                 out.setText(result);

//             });

//             f.setVisible(true);

//         } catch (Exception e) {
//             System.err.println("An error occurred: " + e.getMessage());
//         }
//     }
// }







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
//             nodeData.loadNames("E:\\Project\\JAVA\\extra_work\\csv_files\\Final_Points.csv");
//             graph.loadMatrix("E:\\Project\\JAVA\\extra_work\\csv_files\\updated_weights_matrix.csv");

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

//             String result = DijkstraAlgorithm.dijkstra(graph, nodeData, source, dest);
//             System.out.println("\n" + result);

//         } catch (Exception e) {
//             System.err.println("An error occurred: " + e.getMessage());
//         }
//     }
// }




// package project1;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;
// import java.awt.image.*;
// import java.io.File;
// import java.io.IOException;

// import javax.imageio.ImageIO;
// import java.util.*;

// public class DistanceGraphApp {
//     public static void main(String[] args) {
//         final int N = 70;

//         Graph g = new Graph(N);
//         NodeData nd = new NodeData(N);

//         try {
//             nd.loadNames("E:\\Project\\JAVA\\extra_work\\csv_files\\Final_Points.csv");
//             g.loadMatrix("E:\\Project\\JAVA\\extra_work\\csv_files\\updated_weights_matrix.csv");

//             ArrayList<Integer> resIdx = new ArrayList<>();
//             ArrayList<Integer> locIdx = new ArrayList<>();

//             for (int i = 0; i < N; i++) {
//                 String nick = nd.getNickname(i);
//                 if (nick != null) {
//                     if (nick.startsWith("Res")) resIdx.add(i);
//                     else locIdx.add(i);
//                 }
//             }

//             // Main frame
//             JFrame f = new JFrame("Path Finder - Restaurant to Location");
//             f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//             f.setSize(800, 700);

//             // Main content panel (inside scroll)
//             JPanel contentPanel = new JPanel();
//             contentPanel.setLayout(new GridBagLayout());
//             GridBagConstraints gbc = new GridBagConstraints();
//             gbc.insets = new Insets(10, 10, 10, 10);
//             gbc.fill = GridBagConstraints.HORIZONTAL;

//             // Title
//             JLabel title = new JLabel("Delivery Route Optimization", SwingConstants.CENTER);
//             title.setFont(new Font("SansSerif", Font.BOLD, 20));
//             gbc.gridx = 0;
//             gbc.gridy = 0;
//             gbc.gridwidth = 2;
//             contentPanel.add(title, gbc);

//             // Restaurant selection
//             JLabel resLbl = new JLabel("Select Restaurant:");
//             gbc.gridy = 1;
//             gbc.gridx = 0;
//             gbc.gridwidth = 1;
//             contentPanel.add(resLbl, gbc);

//             JComboBox<String> resBox = new JComboBox<>();
//             for (int idx : resIdx) resBox.addItem(idx + ": " + nd.getName(idx));
//             gbc.gridx = 1;
//             contentPanel.add(resBox, gbc);

//             // Destination selection
//             JLabel locLbl = new JLabel("Select Destination:");
//             gbc.gridy = 2;
//             gbc.gridx = 0;
//             contentPanel.add(locLbl, gbc);

//             JComboBox<String> locBox = new JComboBox<>();
//             for (int idx : locIdx) locBox.addItem(idx + ": " + nd.getName(idx));
//             gbc.gridx = 1;
//             contentPanel.add(locBox, gbc);

//             // Button
//             JButton btn = new JButton("Find Shortest Path");
//             gbc.gridy = 3;
//             gbc.gridx = 0;
//             gbc.gridwidth = 2;
//             contentPanel.add(btn, gbc);

//             // Output area
//             JTextArea out = new JTextArea(5, 60);
//             out.setEditable(false);
//             JScrollPane textScroll = new JScrollPane(out);
//             gbc.gridy = 4;
//             contentPanel.add(textScroll, gbc);

//             // Map image panel
//             JLabel mapLabel = new JLabel("Map Viewer (Zoom and Scroll)");
//             mapLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
//             gbc.gridy = 5;
//             contentPanel.add(mapLabel, gbc);

//             MapPanel mapPanel = new MapPanel("E:\\Project\\JAVA\\extra_work\\extras\\MAP.png");
//             JScrollPane mapScrollPane = new JScrollPane(mapPanel);
//             mapScrollPane.setPreferredSize(new Dimension(700, 500));
//             gbc.gridy = 6;
//             contentPanel.add(mapScrollPane, gbc);

//             // Wrap everything in scrollable pane
//             JScrollPane mainScrollPane = new JScrollPane(contentPanel);
//             f.setContentPane(mainScrollPane);

//             // Button Action
//             btn.addActionListener(e -> {
//                 int src = resIdx.get(resBox.getSelectedIndex());
//                 int dest = locIdx.get(locBox.getSelectedIndex());

//                 if (!nd.isValidDestination(dest)) {
//                     out.setText("Invalid destination selected.");
//                     return;
//                 }

//                 String result = DijkstraAlgorithm.dijkstra(g, nd, src, dest);
//                 out.setText(result);
//             });

//             f.setVisible(true);

//         } catch (Exception e) {
//             System.err.println("An error occurred: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }

//     static class MapPanel extends JPanel {
//     private BufferedImage image;
//     private double scale = 1.0;
//     private double minScale = 1.0;

//     public MapPanel(String imagePath) {
//         try {
//             image = ImageIO.read(new File(imagePath));
//             setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

//             // Mouse wheel zoom
//             addMouseWheelListener(e -> {
//                 double prevScale = scale;
//                 if (e.getPreciseWheelRotation() < 0) {
//                     scale *= 1.1;
//                 } else {
//                     scale /= 1.1;
//                     scale = Math.max(scale, minScale); // Prevent zoom out beyond horizontal fit
//                 }
//                 if (scale != prevScale) {
//                     revalidate();
//                     repaint();
//                 }
//             });

//             // Recalculate minScale when resized
//             addComponentListener(new ComponentAdapter() {
//                 @Override
//                 public void componentResized(ComponentEvent e) {
//                     calculateMinScale();
//                 }

//                 @Override
//                 public void componentShown(ComponentEvent e) {
//                     calculateMinScale();
//                 }
//             });

//         } catch (IOException ex) {
//             System.err.println("Could not load map image: " + ex.getMessage());
//         }
//     }

//     private void calculateMinScale() {
//         if (image == null || getParent() == null) return;

//         Dimension viewSize = getParent().getSize();
//         double widthRatio = viewSize.getWidth() / image.getWidth();  // ✅ Only horizontal
//         minScale = widthRatio;
//         scale = Math.max(scale, minScale);
//         revalidate();
//         repaint();
//     }

//     @Override
//     public Dimension getPreferredSize() {
//         if (image != null) {
//             return new Dimension((int) (image.getWidth() * scale), (int) (image.getHeight() * scale));
//         }
//         return super.getPreferredSize();
//     }

//     @Override
//     protected void paintComponent(Graphics g) {
//         super.paintComponent(g);
//         if (image != null) {
//             Graphics2D g2 = (Graphics2D) g;
//             g2.scale(scale, scale);
//             g2.drawImage(image, 0, 0, this);
//         }
//     }
// } 
// }




package project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.util.List;

public class DistanceGraphApp {
    public static void main(String[] args) {
        final int N = 70;

        Graph g = new Graph(N);
        NodeData nd = new NodeData(N);

        try {
            nd.loadNames("E:\\Project\\JAVA\\extra_work\\csv_files\\Final_Points.csv");
            g.loadMatrix("E:\\Project\\JAVA\\extra_work\\csv_files\\updated_weights_matrix.csv");

            Map<Integer, Point> imageCoordinates = new HashMap<>();
            try (Scanner scanner = new Scanner(new File("E:\\Project\\JAVA\\extra_work\\csv_files\\Final_Points_UPDATED.csv"))) {
                scanner.nextLine(); // skip header
                int idx = 0;
                while (scanner.hasNextLine()) {
                    String[] parts = scanner.nextLine().split(",");
                    if (parts.length >= 6) {
                        int x = Integer.parseInt(parts[4].trim());
                        int y = Integer.parseInt(parts[5].trim());
                        imageCoordinates.put(idx, new Point(x, y));
                    }
                    idx++;
                }
            }

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
            f.setSize(800, 700);

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel title = new JLabel("Delivery Route Optimization", SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 20));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            contentPanel.add(title, gbc);

            JLabel resLbl = new JLabel("Select Restaurant:");
            gbc.gridy = 1;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            contentPanel.add(resLbl, gbc);

            JComboBox<String> resBox = new JComboBox<>();
            for (int idx : resIdx) resBox.addItem(idx + ": " + nd.getName(idx));
            gbc.gridx = 1;
            contentPanel.add(resBox, gbc);

            JLabel locLbl = new JLabel("Select Destination:");
            gbc.gridy = 2;
            gbc.gridx = 0;
            contentPanel.add(locLbl, gbc);

            JComboBox<String> locBox = new JComboBox<>();
            for (int idx : locIdx) locBox.addItem(idx + ": " + nd.getName(idx));
            gbc.gridx = 1;
            contentPanel.add(locBox, gbc);

            JButton btn = new JButton("Find Shortest Path");
            gbc.gridy = 3;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            contentPanel.add(btn, gbc);

            JTextArea out = new JTextArea(5, 60);
            out.setEditable(false);
            JScrollPane textScroll = new JScrollPane(out);
            gbc.gridy = 4;
            contentPanel.add(textScroll, gbc);

            JLabel mapLabel = new JLabel("PATH POINTS");
            mapLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            gbc.gridy = 5;
            contentPanel.add(mapLabel, gbc);

            MapPanel mapPanel = new MapPanel("E:\\Project\\JAVA\\extra_work\\extras\\map_without _points.png");
            JScrollPane mapScrollPane = new JScrollPane(mapPanel);
            mapScrollPane.setPreferredSize(new Dimension(700, 500));
            gbc.gridy = 6;
            contentPanel.add(mapScrollPane, gbc);

            JScrollPane mainScrollPane = new JScrollPane(contentPanel);
            f.setContentPane(mainScrollPane);

            btn.addActionListener(e -> {
                int src = resIdx.get(resBox.getSelectedIndex());
                int dest = locIdx.get(locBox.getSelectedIndex());

                if (!nd.isValidDestination(dest)) {
                    out.setText("Invalid destination selected.");
                    return;
                }

                // Show readable path info
                String result = DijkstraAlgorithm.dijkstra(g, nd, src, dest);
                out.setText(result);

                // Highlight the actual path on the map
                List<Integer> pathIndices = DijkstraAlgorithm.getPathIndices(g, src, dest);
                ArrayList<Point> pathPoints = new ArrayList<>();
                for (int nodeId : pathIndices) {
                    Point p = imageCoordinates.get(nodeId);
                    if (p != null) pathPoints.add(p);
                }
                mapPanel.setHighlightedPoints(pathPoints);
            });

            f.setVisible(true);

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static class MapPanel extends JPanel {
        private BufferedImage image;
        private double scale = 1.0;
        private double minScale = 1.0;
        private java.util.List<Point> highlightedPoints = new ArrayList<>();

        public MapPanel(String imagePath) {
            try {
                image = ImageIO.read(new File(imagePath));
                setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

                addMouseWheelListener(e -> {
                    if (image == null) return;

                    double oldScale = scale;
                    int mouseX = e.getX();
                    int mouseY = e.getY();

                    if (e.getPreciseWheelRotation() < 0) {
                        scale *= 1.1;
                    } else {
                        scale /= 1.1;
                        scale = Math.max(scale, minScale);
                    }

                    // If scale didn't change, skip updating
                    if (oldScale == scale) return;

                    // Get the JScrollPane (for scrolling adjustments)
                    Container parent = getParent();
                    if (parent instanceof JViewport viewport) {
                        Point viewPos = viewport.getViewPosition();

                        double scaleChange = scale / oldScale;

                        int newX = (int) ((mouseX + viewPos.x) * scaleChange - mouseX);
                        int newY = (int) ((mouseY + viewPos.y) * scaleChange - mouseY);

                        viewport.setViewPosition(new Point(newX, newY));
                    }

                    revalidate();
                    repaint();
                });

                addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        calculateMinScale();
                    }

                    @Override
                    public void componentShown(ComponentEvent e) {
                        calculateMinScale();
                    }
                });

            } catch (IOException ex) {
                System.err.println("Could not load map image: " + ex.getMessage());
            }
        }

        public void setHighlightedPoints(ArrayList<Point> points) {
            this.highlightedPoints = points;
            repaint();
        }

        private void calculateMinScale() {
            if (image == null || getParent() == null) return;

            Dimension viewSize = getParent().getSize();
            double widthRatio = viewSize.getWidth() / image.getWidth();
            minScale = widthRatio;
            scale = Math.max(scale, minScale);
            revalidate();
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            if (image != null) {
                return new Dimension((int) (image.getWidth() * scale), (int) (image.getHeight() * scale));
            }
            return super.getPreferredSize();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                // Draw the map image with scaling
                Graphics2D g2 = (Graphics2D) g.create();
                g2.scale(scale, scale);
                g2.drawImage(image, 0, 0, this);
                g2.dispose();

                // Draw fixed-size markers (not scaled with image)
                Graphics2D markerG = (Graphics2D) g.create();
                markerG.setFont(new Font("SansSerif", Font.BOLD, 12));
                int markerSize = 16;
                for (int i = 0; i < highlightedPoints.size(); i++) {
                    Point p = highlightedPoints.get(i);
                    int screenX = (int) (p.x * scale);
                    int screenY = (int) (p.y * scale);
                    // Color based on start, middle, or end
                    if (i == 0) {
                        markerG.setColor(Color.GREEN); // Start point
                    } else if (i == highlightedPoints.size() - 1) {
                        markerG.setColor(Color.BLUE); // Destination
                    } else {
                        markerG.setColor(Color.RED); // Path steps
                    }
                    // Draw marker circle
                    markerG.fillOval(screenX - markerSize / 2, screenY - markerSize / 2, markerSize, markerSize);
                    // Draw step number inside marker
                    markerG.setColor(Color.WHITE);
                    String label = String.valueOf(i + 1);
                    FontMetrics fm = markerG.getFontMetrics();
                    int textWidth = fm.stringWidth(label);
                    int textHeight = fm.getAscent();
                    markerG.drawString(label, screenX - textWidth / 2, screenY + textHeight / 3);
                }
                markerG.dispose();
            }
        }
    }
}
