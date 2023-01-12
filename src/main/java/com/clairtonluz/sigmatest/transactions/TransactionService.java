package com.clairtonluz.sigmatest.transactions;

public interface TransactionService {

    void add(TransactionDTO transactionDTO);

    TransactionStatistics getStatistics();
}
