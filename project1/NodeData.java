package project1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NodeData {
    private final String[] nicknames;
    private final String[] names;

    public NodeData(int size) {
        this.nicknames = new String[size];
        this.names = new String[size];
    }

    public void loadNames(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            int row = 0;
            String line;
            while ((line = reader.readLine()) != null && row < nicknames.length) {
                String[] values = line.split(",", -1);
                if (values.length >= 2) {
                    nicknames[row] = values[0].trim();
                    names[row] = values[1].trim();
                    row++;
                }
            }
        } catch (Throwable t) {
            try {
                reader.close();
            } catch (Throwable t2) {
                t.addSuppressed(t2);
            }
            throw t;
        }
        reader.close();
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
