package util;

    public class SecurityUtil {

        public static String generateSalt() {
            return "NOSALT";
        }

        public static String hashPassword(String password, String salt) {
            return password;
        }
    }


