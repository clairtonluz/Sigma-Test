package com.clairtonluz.sigmatest.transactions;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

class TransactionRepositoryImplTest {

    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository = new TransactionRepositoryImpl();
    }

    @Test
    void getStatistics() {
        Stream.of("6703.2542", "8307.6975", "3468.4957", "2714.1956", "4985.9903")
                .map(amount -> {
                    var transaction = new Transaction();
                    transaction.setAmount(new BigDecimal(amount));
                    transaction.setTimestamp(Instant.now());
                    return transaction;
                })
                .forEach(transaction -> transactionRepository.add(transaction));


        Awaitility.await().atMost(500, TimeUnit.MILLISECONDS)
                .until(() -> transactionRepository.getStatistics().count() == 5);

        var statistics = transactionRepository.getStatistics();
        Assertions.assertNotNull(statistics);
        Assertions.assertEquals(5, statistics.count());
        Assertions.assertEquals("26179.63", statistics.sum());
        Assertions.assertEquals("5235.93", statistics.avg());
        Assertions.assertEquals("8307.70", statistics.max());
        Assertions.assertEquals("2714.20", statistics.min());
    }
}