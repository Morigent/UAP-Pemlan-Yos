package service;


import repository.CSVRepository;
import util.SecurityUtil;

public class AuthService {

    private CSVRepository repo = new CSVRepository();

    public boolean login(String username, String password) {
        for (String[] row : repo.read("users.csv")) {
            if (row[0].equals(username)) {
                String hash = SecurityUtil.hashPassword(password, row[2]);
                return hash.equals(row[1]);
            }
        }
        return false;
    }
}
