package repository;

import java.io.*;
import java.util.*;

public class CSVRepository {

    // Method read dengan parameter file
    public List<String[]> read(String filePath) {
        List<String[]> data = new ArrayList<>();
        File file = new File(filePath);

        // Jika file tidak ada, return list kosong
        if (!file.exists()) {
            System.out.println("File tidak ditemukan: " + filePath);
            return data;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    // Handle empty values
                    String[] row = line.split(",", -1); // -1 untuk keep empty values
                    data.add(row);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    public void append(String filePath, String line) {
        ensureFileExists(filePath);
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(line);
        } catch (IOException e) {
            System.err.println("Error appending to file " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void writeAll(String filePath, List<String[]> data) {
        ensureFileExists(filePath);
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                pw.println(String.join(",", row));
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteById(String filePath, String id) {
        List<String[]> allData = read(filePath);
        if (allData.isEmpty()) return;

        List<String[]> newData = new ArrayList<>();
        int idColumnIndex = 0; // Asumsikan ID ada di kolom pertama

        for (String[] row : allData) {
            if (row.length > 0 && !row[idColumnIndex].equals(id)) {
                newData.add(row);
            }
        }

        writeAll(filePath, newData);
    }

    public void update(String filePath, String id, String[] newData) {
        List<String[]> allData = read(filePath);
        if (allData.isEmpty()) return;

        List<String[]> updatedData = new ArrayList<>();
        int idColumnIndex = 0; // Asumsikan ID ada di kolom pertama

        for (String[] row : allData) {
            if (row.length > 0 && row[idColumnIndex].equals(id)) {
                updatedData.add(newData);
            } else {
                updatedData.add(row);
            }
        }

        writeAll(filePath, updatedData);
    }

    private void ensureFileExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                // Buat directory jika belum ada
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }

                // Buat file dan handle hasilnya
                boolean fileCreated = file.createNewFile();
                if (fileCreated) {
                    System.out.println("File created: " + filePath);
                } else {
                    System.out.println("File already exists or cannot be created: " + filePath);
                }
            } catch (IOException e) {
                System.err.println("Error creating file " + filePath + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Method helper untuk mencari data
    public List<String[]> findByColumn(String filePath, int columnIndex, String value) {
        List<String[]> allData = read(filePath);
        List<String[]> result = new ArrayList<>();

        for (String[] row : allData) {
            if (row.length > columnIndex && row[columnIndex].equals(value)) {
                result.add(row);
            }
        }

        return result;
    }

    // Method khusus untuk users.csv
    public List<String[]> readUsers() {
        return read("users.csv");
    }

    public void appendUser(String username, String password) {
        append("users.csv", username + "," + password);
    }

    // Method khusus untuk transactions.csv
    public List<String[]> readTransactions() {
        return read("transactions.csv");
    }

    public void appendTransaction(String transactionData) {
        append("transactions.csv", transactionData);
    }

    // Method khusus untuk budgets.csv
    public List<String[]> readBudgets() {
        return read("budgets.csv");
    }

    public void appendBudget(String budgetData) {
        append("budgets.csv", budgetData);
    }
}