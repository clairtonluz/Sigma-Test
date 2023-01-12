package com.clairtonluz.sigmatest.transactions;

public interface TransactionStatisticsRepository {
    void add(Transaction transaction);

    TransactionStatistics getStatistics();

    void calculate();
}
