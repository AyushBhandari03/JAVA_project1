package project1;
// Manages the loading and access of node nicknames and names
import java.io.*;

public class NodeData {
    private final String[] nicknames;
    private final String[] names;

    public NodeData(int size) {
        this.nicknames = new String[size];
        this.names = new String[size];
    }

    public void loadNames(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null && i < nicknames.length) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 2) {
                    nicknames[i] = parts[0].trim();
                    names[i] = parts[1].trim();
                    i++;
                }
            }
        }
    }

    public String getNickname(int index) {
        return nicknames[index];
    }

    public String getName(int index) {
        return names[index];
    }

    public boolean isValidDestination(int index) {
        return index >= 0 && index < nicknames.length && nicknames[index] != null && !nicknames[index].startsWith("Res");
    }
}
