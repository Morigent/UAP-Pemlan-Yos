package repository;

import java.io.*;
import java.util.*;

public class CSVRepository {

    public List<String[]> read(String file) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void append(String file, String line) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
