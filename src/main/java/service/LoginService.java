package service;

import repository.CSVRepository;

public class LoginService {
    private CSVRepository repo = new CSVRepository();

    public boolean register(String username, String password) {
        // Cek apakah username sudah ada
        for (String[] row : repo.readUsers()) {
            if (row.length >= 1 && row[0].equals(username)) {
                return false; // Username sudah terdaftar
            }
        }

        // Simpan password langsung (tanpa hash)
        repo.appendUser(username, password);
        return true;
    }

    public boolean login(String username, String password) {
        for (String[] row : repo.readUsers()) {
            if (row.length >= 2 && row[0].equals(username)) {
                // Bandingkan password langsung
                return row[1].equals(password);
            }
        }
        return false;
    }

    // Method untuk cek keberadaan user
    public boolean userExists(String username) {
        for (String[] row : repo.readUsers()) {
            if (row.length >= 1 && row[0].equals(username)) {
                return true;
            }
        }
        return false;
    }

    // Method untuk update password
    public boolean updatePassword(String username, String newPassword) {
        try {
            // Baca semua user
            java.util.List<String[]> allUsers = repo.readUsers();
            java.util.List<String[]> newUsers = new java.util.ArrayList<>();
            boolean found = false;

            for (String[] row : allUsers) {
                if (row.length >= 1 && row[0].equals(username)) {
                    // Update password
                    newUsers.add(new String[]{username, newPassword});
                    found = true;
                } else {
                    newUsers.add(row);
                }
            }

            if (found) {
                // Tulis kembali ke file
                repo.writeAll("users.csv", newUsers);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}