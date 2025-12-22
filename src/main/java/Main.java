
//import Controller;
import Controller.*;
import GUI.LandingPage;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static LoginController authController = new LoginController();
    private static DashboardController dashboardController = new DashboardController();
    private static TransactionController transactionController = new TransactionController();
    private static BudgetController budgetController = new BudgetController();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        LandingPage landingPage = new LandingPage();
    }

    private static void showLoginMenu() {
        LandingPage landingPage = new LandingPage();

        /*switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                System.exit(0);
                break;
        }*/
    }

    private static void login() {
        try {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            boolean success = authController.login(username, password);
            if (success) {
                System.out.println("Login berhasil!");
            } else {
                System.out.println("Username atau password salah!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void register() {
        try {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.print("Konfirmasi Password: ");
            String confirmPassword = scanner.nextLine();

            boolean success = authController.register(username, password, confirmPassword);
            if (success) {
                System.out.println("Registrasi berhasil! Silakan login.");
            } else {
                System.out.println("Username sudah terdaftar!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== DASHBOARD ===");
        System.out.println("User: " + authController.getCurrentUser());
        System.out.println("1. Lihat Dashboard");
        System.out.println("2. Tambah Transaksi");
        System.out.println("3. Riwayat Transaksi");
        System.out.println("4. Kelola Budget");
        System.out.println("5. Logout");
        System.out.print("Pilihan: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                showDashboard();
                break;
            case 2:
                addTransaction();
                break;
            case 3:
                showTransactionHistory();
                break;
            case 4:
                manageBudget();
                break;
            case 5:
                authController.logout();
                break;
        }
    }

    private static void showDashboard() {
        try {
            double saldo = dashboardController.getSaldoBulanIni();
            double pemasukan = dashboardController.getTotalPemasukanBulanIni();
            double pengeluaran = dashboardController.getTotalPengeluaranBulanIni();

            System.out.println("\n=== SUMMARY BULAN INI ===");
            System.out.printf("Saldo: Rp %,.2f%n", saldo);
            System.out.printf("Pemasukan: Rp %,.2f%n", pemasukan);
            System.out.printf("Pengeluaran: Rp %,.2f%n", pengeluaran);

            // Tampilkan peringatan budget
            var warnings = budgetController.getBudgetWarnings();
            if (!warnings.isEmpty()) {
                System.out.println("\n⚠️ PERINGATAN BUDGET:");
                warnings.forEach((kategori, persentase) -> {
                    System.out.printf("  %s: %.1f%% dari budget%n", kategori, persentase);
                });
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addTransaction() {
        try {
            System.out.println("\n=== TAMBAH TRANSAKSI ===");
            System.out.print("Tipe (INCOME/EXPENSE): ");
            String type = scanner.nextLine().toUpperCase();
            System.out.print("Kategori: ");
            String category = scanner.nextLine();
            System.out.print("Jumlah: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Deskripsi: ");
            String description = scanner.nextLine();

            transactionController.addTransaction(type, category, amount,
                    LocalDate.now(), description);
            System.out.println("Transaksi berhasil ditambahkan!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showTransactionHistory() {
        System.out.println("\n=== RIWAYAT TRANSAKSI ===");
        System.out.println("1. Bulan Ini");
        System.out.println("2. Bulan Lalu");
        System.out.println("3. Custom Range");
        System.out.print("Pilihan: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        try {
            switch (choice) {
                case 1:
                    var transactions = transactionController.getTransactionsThisMonth();
                    displayTransactions(transactions);
                    break;
                // ... implementasi lainnya
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayTransactions(java.util.List<model.Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("Tidak ada transaksi.");
            return;
        }

        System.out.printf("%-20s %-10s %-15s %-15s %-20s%n",
                "Tanggal", "Tipe", "Kategori", "Jumlah", "Deskripsi");
        System.out.println("-".repeat(80));

        for (var t : transactions) {
            System.out.printf("%-20s %-10s %-15s %-15.2f %-20s%n",
                    t.getDate(), t.getType(), t.getCategory(),
                    t.getAmount(), t.getDescription());
        }
    }

    private static void manageBudget() {
        System.out.println("\n=== KELOLA BUDGET ===");
        System.out.println("1. Set Budget");
        System.out.println("2. Lihat Budget");
        System.out.println("3. Hapus Budget");
        System.out.print("Pilihan: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        try {
            switch (choice) {
                case 1:
                    System.out.print("Kategori: ");
                    String category = scanner.nextLine();
                    System.out.print("Bulan (1-12): ");
                    int month = scanner.nextInt();
                    System.out.print("Tahun: ");
                    int year = scanner.nextInt();
                    System.out.print("Limit Budget: ");
                    double limit = scanner.nextDouble();
                    scanner.nextLine();

                    budgetController.setBudget(category, month, year, limit);
                    System.out.println("Budget berhasil diset!");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}