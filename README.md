# 🛵 QuickEats – Food Delivery Route Optimization

QuickEats is a Java-based food delivery route optimization application designed to help restaurants efficiently manage their delivery routes. The system calculates the shortest path between a source restaurant and a customer’s address using Dijkstra’s algorithm and displays the result visually on an interactive map.

With its intuitive Swing-based graphical interface, QuickEats simplifies food delivery operations — improving route accuracy, reducing delivery times, and ensuring timely service to customers.

✨ Features
📍 Shortest path calculation using Dijkstra’s algorithm on a graph built from real-world map data.
🧠 Efficient pathfinding across all restaurants and customer locations loaded from CSV files.
🖥️ Interactive Swing GUI allowing restaurant and destination selection, triggered pathfinding, and route display.
🧭 Zoomable & pannable map so you can clearly visualize the entire path and every intermediate node.
🧩 Dynamic highlighting of the computed path points on a customized map.
✅ Error handling for invalid or unreachable destinations.

📂 Project Structure
DistanceGraphApp — The main application class responsible for launching the Swing GUI, reading files, and handling events.
DijkstraAlgorithm — Core logic for computing shortest paths using Dijkstra's algorithm.
Graph — Handles the graph data as an adjacency matrix and loads the distance data from Matrix.csv.
NodeData — Loads and provides node names, nicknames, and destination validation logic from Final_Points.csv.

🧠 Algorithm Overview
QuickEats uses Dijkstra’s algorithm to:
Compute the minimum distance between two points.
Return both the cumulative distance and the ordered list of nodes representing the path.
Handle cases where the destination cannot be reached (e.g., missing edges or disconnected nodes).

🗂️ CSV Input Data
Your csv_files directory must include:
Points.csv — Map pixel coordinates for visualizing node positions.
Final_Points_without_head.csv — Two columns per row containing Nickname and Full Name.
Matrix.csv — Distance matrix where each row corresponds to a node and each column contains the distance to the other nodes.
Empty cells or errors in the distance matrix are treated as Double.POSITIVE_INFINITY to ensure they don’t contribute to any valid path.

🏃 Getting Started
✅ Prerequisites
Java 8 or newer installed
Swing and AWT come with the standard Java Development Kit
Ensure that all CSV files (Points.csv, Final_Points_without_head.csv, Matrix.csv) exist in the csv_files directory

⚙️ Steps to Run
Clone this repository:
git clone https://github.com/your-username/quickeats-route-optimizer.git
cd quickeats-route-optimizer
Place the CSV files inside the csv_files directory. Adjust file paths in DistanceGraphApp.java if needed.
Compile the source files:
javac project1/*.java
Run the main application:
java project1.DistanceGraphApp
Once the UI appears:
Select a restaurant.
Select a destination.
Click Find Shortest Path to compute and visualize the optimal route!

🎮 Usage Example
Once you run the app:
Choose your source restaurant (e.g. Res1, Res2, etc.).
Choose the destination address from the list.
The app will:
Print the shortest path summary to the text area.
Highlight the route on the map:
🟢 Green node for the source restaurant.
🔵 Blue node for the destination.
🔴 Red nodes for intermediate points.
Display node indices as you hover over them.

🧭 Future Improvements
Implement A* algorithm for faster performance on larger datasets.
Add multiple path options for comparison.
Display estimated delivery time based on average speed.
Integrate a database for live updates of node data.
Improve map rendering for smoother interactions at all zoom levels.

📜 License
This project is licensed under the MIT License — feel free to use, modify, and distribute this code in any of your own work!

🤝 Contributing
Contributions are welcome! Feel free to fork this repo, submit issues, or send pull requests if you’d like to add new features or improve the existing implementation.

📧 Contact
For questions, suggestions, or bug reports, please reach out at your_email@example.com.

💡 QuickEats aims to make food delivery faster and smarter — one optimized route at a time!
