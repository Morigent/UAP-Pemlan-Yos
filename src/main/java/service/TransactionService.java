package service;

import model.Transaction;
import repository.CSVRepository;
import util.DateUtil;


import java.util.*;
import java.util.stream.Collectors;

public class TransactionService {

    private CSVRepository repo = new CSVRepository();

    public void addTransaction(Transaction t) {
        if (t.getAmount() <= 0) throw new IllegalArgumentException("Nominal harus > 0");

        repo.append("transactions.csv",
                String.join(",",
                        UUID.randomUUID().toString(),
                        t.getUsername(),
                        t.getType(),
                        t.getCategory(),
                        String.valueOf(t.getAmount()),
                        t.getDate().format(DateUtil.FORMATTER),
                        t.getCategory()));
    }

    public List<Transaction> getTransactions(String username, int month, int year) {
        return repo.read("transactions.csv").stream()
                .filter(r -> r[1].equals(username))
                .map(r -> new Transaction(
                        r[0], r[1], r[2], r[3],
                        Double.parseDouble(r[4]),
                        DateUtil.parse(r[5]), r[6]
                ))
                .filter(t -> t.getDate().getMonthValue() == month &&
                        t.getDate().getYear() == year)
                .collect(Collectors.toList());
    }
}
