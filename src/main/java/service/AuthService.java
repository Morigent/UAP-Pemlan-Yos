package service;

import model.User;
import repository.CSVRepository;
import util.SecurityUtil;

public class AuthService {
    private CSVRepository repo = new CSVRepository();

    public boolean register(String username, String password) {
        // Cek apakah username sudah ada
        for (String[] row : repo.read("users.csv")) {
            if (row[0].equals(username)) {
                return false; // Username sudah terdaftar
            }
        }

        // Buat user baru
        String salt = SecurityUtil.generateSalt();
        String hash = SecurityUtil.hashPassword(password, salt);

        repo.append("users.csv", username + "," + hash + "," + salt);
        return true;
    }

    public boolean login(String username, String password) {
        for (String[] row : repo.read("users.csv")) {
            if (row[0].equals(username)) {
                String storedHash = row[1];
                String salt = row[2];
                String inputHash = SecurityUtil.hashPassword(password, salt);
                return storedHash.equals(inputHash);
            }
        }
        return false;
    }
}