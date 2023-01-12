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
public class TransactionStatisticsRepositoryImpl implements TransactionStatisticsRepository {
    List<Transaction> transactions = Collections.synchronizedList(new LinkedList<>());
    TransactionStatistics statistics;

    public TransactionStatisticsRepositoryImpl() {
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
    public void add(Transaction transaction) {
        transactions.add(transaction);
        calculate();
    }

    @Override
    public TransactionStatistics getStatistics() {
        calculate();
        return statistics;
    }

    private void removeOldTransactions() {
        var timestampLimit = Instant.now().minusSeconds(60);
        transactions.removeIf(transaction -> transaction.getTimestamp().isBefore(timestampLimit));
    }

    private void calculate() {
        new Thread(() -> {
            removeOldTransactions();
            if (transactions.size() == 0) {
                resetStatistics();
                return;
            }

            var count = new BigDecimal(transactions.size());
            var sum = new BigDecimal(0);
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
        }).start();
    }
}
