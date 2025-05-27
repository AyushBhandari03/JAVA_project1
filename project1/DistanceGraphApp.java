// DistanceGraphApp.java
// This application provides a GUI for optimizing food delivery routes 
// from restaurants to customer locations using Dijkstra's algorithm. 
// It loads node names, distances (as a graph), and map coordinates from CSV files.
// Users can select a restaurant and destination to find the shortest path.
// The GUI includes dropdowns for selection, a map view with zooming, and 
// visual highlights of the calculated path.
// Pathfinding logic is integrated with map visualization for a better user experience.
 
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
            nd.loadNames("csv_files/Final_Points_without_head.csv");
            g.loadMatrix("csv_files/Matrix.csv");

            Map<Integer, Point> imageCoordinates = new HashMap<>();
            try (Scanner scanner = new Scanner(new File("E:\\Project\\JAVA\\extra_work\\csv_files\\Points.csv"))) {
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

            JPanel contentPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(new Color(50, 100, 255));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
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
            btn.setBackground(new Color(25, 25, 180));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(new Color(60, 100, 150), 2));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            gbc.gridy = 3;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            contentPanel.add(btn, gbc);

            JTextArea out = new JTextArea(5, 60);
            out.setEditable(false);
            JScrollPane textScroll = new JScrollPane(out);
            gbc.gridy = 4;
            contentPanel.add(textScroll, gbc);

            JLabel mapLabel = new JLabel("PATH POINTS",SwingConstants.CENTER);
            mapLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            gbc.gridy = 5;
            contentPanel.add(mapLabel, gbc);

            MapPanel mapPanel = new MapPanel("E:\\Project\\JAVA\\extra_work\\important\\MAP.png");
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

                String result = DijkstraAlgorithm.dijkstra(g, nd, src, dest);
                out.setText(result);

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

                    if (oldScale == scale) return;

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
                Graphics2D g2 = (Graphics2D) g.create();
                g2.scale(scale, scale);
                g2.drawImage(image, 0, 0, this);
                g2.dispose();

                Graphics2D markerG = (Graphics2D) g.create();
                markerG.setFont(new Font("SansSerif", Font.BOLD, 12));
                int markerSize = 16;
                for (int i = 0; i < highlightedPoints.size(); i++) {
                    Point p = highlightedPoints.get(i);
                    int screenX = (int) (p.x * scale);
                    int screenY = (int) (p.y * scale);
                    if (i == 0) {
                        markerG.setColor(Color.GREEN);
                    } else if (i == highlightedPoints.size() - 1) {
                        markerG.setColor(Color.BLUE);
                    } else {
                        markerG.setColor(Color.RED);
                    }
                    markerG.fillOval(screenX - markerSize / 2, screenY - markerSize / 2, markerSize, markerSize);
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
