package repository;

import java.io.*;
import java.util.*;

public class CSVRepository {

    public List<String[]> read(String file) {
        List<String[]> data = new ArrayList<>();
        File f = new File(file);
        if (!f.exists()) return data;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    data.add(line.split(","));
                }
            }
        }  catch (IOException e) {
            System.err.println("Error reading file: " + file);
        }
        return data;
    }


    public void append(String file, String line) {
        ensureFileExists(file);
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(line + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + file);
        }
    }
    public void writeAll(String file, List<String[]> data) {
        ensureFileExists(file);
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (String[] row : data) {
                pw.println(String.join(",", row));
            }
        } catch (IOException e) {
            System.err.println("Error writing all to file: " + file);
        }
    }
    public void deleteById(String file, String id) {
        List<String[]> allData = read(file);
        List<String[]> newData = new ArrayList<>();

        for (String[] row : allData) {
            if (row.length > 0 && !row[0].equals(id)) {
                newData.add(row);
            }
        }

        writeAll(file, newData);
    }
    private void ensureFileExists(String file) {
        try {
            File f = new File(file);
            if (!f.exists()) {
                f.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error creating file: " + file);
        }
    }
}
