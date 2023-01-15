package com.clairtonluz.sigmatest.transactions;

public interface TransactionRepository {
    void add(Transaction transaction);

    TransactionStatistics getStatistics();

    void calculate();

    void deleteAll();
}
