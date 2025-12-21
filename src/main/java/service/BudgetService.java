package service;


import repository.CSVRepository;

public class BudgetService {

    private CSVRepository repo = new CSVRepository();

    public void setBudget(String username, String category,
                          int month, int year, double limit) {
        if (limit <= 0) throw new IllegalArgumentException("Budget harus > 0");

        repo.append("budgets.csv",
                username + "," + category + "," + month + "," + year + "," + limit);
    }
}
