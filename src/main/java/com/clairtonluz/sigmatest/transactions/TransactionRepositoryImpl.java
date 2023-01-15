package com.clairtonluz.sigmatest.transactions;

import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Repository
@ApplicationScope
public class TransactionRepositoryImpl implements TransactionRepository {
    List<Transaction> transactions = Collections.synchronizedList(new LinkedList<>());
    TransactionStatistics statistics;

    public TransactionRepositoryImpl() {
        resetStatistics();
    }

    private void resetStatistics() {
        var zero = new BigDecimal(0);
        var zeroString = zero.setScale(2, RoundingMode.HALF_UP).toString();
        this.statistics = new TransactionStatistics(
                zeroString,
                zeroString,
                zeroString,
                zeroString,
                0L);
    }

    @Override
    public synchronized void add(Transaction transaction) {
        transactions.add(transaction);
        new Thread(this::calculate).start();
    }

    @Override
    public TransactionStatistics getStatistics() {
        return statistics;
    }

    private void removeOldTransactions() {
        var timestampLimit = Instant.now().minusSeconds(60);
        transactions.removeIf(transaction -> transaction.getTimestamp().isBefore(timestampLimit));
    }

    @Override
    public synchronized void calculate() {
        removeOldTransactions();
        if (transactions.size() == 0) {
            resetStatistics();
            return;
        }

        var count = new BigDecimal(transactions.size());
        BigDecimal sum = new BigDecimal(0);
        BigDecimal max = new BigDecimal(0);
        BigDecimal min = null;


        for (var transaction : transactions) {
            var amount = transaction.getAmount();
            sum = sum.add(amount);
            max = max.max(amount);
            min = min == null ? amount : min.min(amount);
        }

        var avg = sum.divide(count, RoundingMode.HALF_UP);

        if (min == null) min = new BigDecimal(0);

        this.statistics = new TransactionStatistics(
                sum.setScale(2, RoundingMode.HALF_UP).toString(),
                avg.setScale(2, RoundingMode.HALF_UP).toString(),
                max.setScale(2, RoundingMode.HALF_UP).toString(),
                min.setScale(2, RoundingMode.HALF_UP).toString(),
                count.longValue());
    }

    @Override
    public void deleteAll() {
        transactions = Collections.synchronizedList(new LinkedList<>());
        calculate();
    }
}
