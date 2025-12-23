package repository;

import java.io.*;
import java.util.*;

public class CSVRepository {
    // Method baru: Find with multiple criteria
    public List<String[]> find(String filePath, Map<String, String> criteria) {
        List<String[]> allData = read(filePath);
        List<String[]> result = new ArrayList<>();

        for (String[] row : allData) {
            boolean matches = true;

            for (Map.Entry<String, String> entry : criteria.entrySet()) {
                int columnIndex = Integer.parseInt(entry.getKey());
                if (row.length <= columnIndex || !row[columnIndex].equals(entry.getValue())) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                result.add(row);
            }
        }

        return result;
    }

    // Method baru: Count rows
    public int count(String filePath) {
        return read(filePath).size();
    }

    // Method baru: Get distinct values
    public List<String> getDistinctValues(String filePath, int columnIndex) {
        List<String[]> allData = read(filePath);
        Set<String> distinctValues = new HashSet<>();

        for (String[] row : allData) {
            if (row.length > columnIndex) {
                distinctValues.add(row[columnIndex]);
            }
        }

        return new ArrayList<>(distinctValues);
    }

    // Method baru: Backup file
    public boolean backup(String filePath) {
        try {
            File original = new File(filePath);
            if (!original.exists()) return false;

            String backupPath = filePath + ".backup";
            File backup = new File(backupPath);

            try (FileInputStream fis = new FileInputStream(original);
                 FileOutputStream fos = new FileOutputStream(backup)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            }

            return true;
        } catch (IOException e) {
            System.err.println("Backup error: " + e.getMessage());
            return false;
        }
    }

    // Baca file CSV dan kembalikan list baris sebagai array kolom
    public List<String[]> read(String filePath) {
        List<String[]> data = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            // Jika file belum ada, kembalikan list kosong
            return data;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] row = line.split(",", -1);
                    data.add(row);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    // Tambah satu baris ke file (append)
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

    // Tulis ulang seluruh file dengan data baru
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

    // Hapus baris berdasarkan id di kolom pertama
    public boolean deleteById(String filePath, String id) {
        List<String[]> allData = read(filePath);
        List<String[]> newData = new ArrayList<>();
        boolean found = false;

        for (String[] row : allData) {
            if (row.length > 0 && row[0].equals(id)) {
                found = true;
                System.out.println("Deleting: " + Arrays.toString(row));
            } else {
                newData.add(row);
            }
        }

        if (found) {
            writeAll(filePath, newData);
            return true;
        }

        return false;
    }

    // Update baris yang memiliki id di kolom pertama
    public void update(String filePath, String id, String[] newData) {
        List<String[]> allData = read(filePath);
        if (allData.isEmpty()) return;

        List<String[]> updated = new ArrayList<>();
        for (String[] row : allData) {
            if (row.length > 0 && row[0].equals(id)) {
                updated.add(newData);
            } else {
                updated.add(row);
            }
        }

        writeAll(filePath, updated);
    }

    // Pastikan file dan folder ada
    private void ensureFileExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) parent.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating file " + filePath + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Helper khusus users
    public List<String[]> readUsers() {
        return read("users.csv");
    }

    public void appendUser(String username, String password) {
        append("users.csv", username + "," + password);
    }

    // Helpers khusus transactions/budgets (konvensi pemanggilan di service)
    public List<String[]> readTransactions() {
        return read("transactions.csv");
    }

    public void appendTransaction(String transactionData) {
        append("transactions.csv", transactionData);
    }

    public List<String[]> readBudgets() {
        return read("budgets.csv");
    }

    public void appendBudget(String line) {
        append("budgets.csv", line);
    }

    // Find by column
    public List<String[]> findByColumn(String filePath, int columnIndex, String value) {
        List<String[]> all = read(filePath);
        List<String[]> result = new ArrayList<>();
        for (String[] row : all) {
            if (row.length > columnIndex && row[columnIndex].equals(value)) {
                result.add(row);
            }
        }
        return result;
    }
}