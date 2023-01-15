package com.clairtonluz.sigmatest.transactions;

import com.clairtonluz.sigmatest.core.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.stream.Stream;

class TransactionServiceImplTest {

    @Mock
    private TransactionValidator transactionValidator;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void add() {
        var transactionDTO = new TransactionDTO();
        transactionDTO.setAmount("12.123");
        transactionDTO.setTimestamp(Instant.now().toString());

        transactionService.add(transactionDTO);
        Mockito.verify(transactionValidator, Mockito.times(1))
                .validar(Mockito.any(Transaction.class));
        Mockito.verify(transactionRepository, Mockito.times(1))
                .add(Mockito.any(Transaction.class));
    }

    @Test
    void addInvalidAmount() {
        var transactionDTO = new TransactionDTO();
        transactionDTO.setAmount("12.12.3");
        transactionDTO.setTimestamp(Instant.now().toString());

        var exception = Assertions.assertThrows(
                UnprocessableEntityException.class,
                () -> transactionService.add(transactionDTO));

        Assertions.assertEquals("The fields are not parsable", exception.getMessage());
    }

    @Test
    void addInvalidTimestamp() {
        var transactionDTO = new TransactionDTO();
        transactionDTO.setAmount("12.123");
        transactionDTO.setTimestamp(Instant.now().toString() + "invalid");

        var exception = Assertions.assertThrows(
                UnprocessableEntityException.class,
                () -> transactionService.add(transactionDTO));

        Assertions.assertEquals("The fields are not parsable", exception.getMessage());
    }

    @Test
    void getStatistics() throws InterruptedException {
        Stream.of("6703.2542", "8307.6975", "3468.4957", "2714.1956", "4985.9903")
                .map(amount -> {
                    var transaction = new TransactionDTO();
                    transaction.setAmount(amount);
                    transaction.setTimestamp(Instant.now().toString());
                    return transaction;
                })
                .forEach(transaction -> transactionService.add(transaction));

        Mockito.verify(transactionRepository, Mockito.times(5))
                .add(Mockito.any(Transaction.class));
        Mockito.verify(transactionValidator, Mockito.times(5))
                .validar(Mockito.any(Transaction.class));

        var expected = new TransactionStatistics("", "", "", "", 5L);
        Mockito.when(transactionRepository.getStatistics())
                .thenReturn(expected);

        var statistics = transactionService.getStatistics();

        Assertions.assertNotNull(statistics);
        Assertions.assertEquals(expected.count(), statistics.count());
        Assertions.assertEquals(expected.sum(), statistics.sum());
        Assertions.assertEquals(expected.avg(), statistics.avg());
        Assertions.assertEquals(expected.max(), statistics.max());
        Assertions.assertEquals(expected.min(), statistics.min());
    }

    @Test
    void deleteAll() {
        transactionService.deleteAll();
        Mockito.verify(transactionRepository, Mockito.times(1))
                .deleteAll();
    }
}