package com.clairtonluz.sigmatest.transactions;

import com.clairtonluz.sigmatest.core.exceptions.NoContentException;
import com.clairtonluz.sigmatest.core.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static com.clairtonluz.sigmatest.transactions.TransactionValidator.MAX_OLDER_IN_SEC;

class TransactionValidatorTest {

    private TransactionValidator transactionValidator;

    @BeforeEach
    void setUp() {
        transactionValidator = new TransactionValidator();
    }

    @Test
    void validar() {
        var transaction = new Transaction();
        transaction.setAmount(new BigDecimal("12.34"));
        transaction.setTimestamp(Instant.now());
        transactionValidator.validar(transaction);
    }

    @Test
    void validarTransactionInFuture() {
        var transaction = new Transaction();
        transaction.setAmount(new BigDecimal("12.34"));
        transaction.setTimestamp(Instant.now().plusSeconds(10));
        var exception = Assertions.assertThrows(UnprocessableEntityException.class, () ->
                transactionValidator.validar(transaction)
        );

        Assertions.assertEquals("The transaction date is in the future", exception.getMessage());
    }

    @Test
    void validarTransactionExceedLimit() {
        var transaction = new Transaction();
        transaction.setAmount(new BigDecimal("12.34"));
        transaction.setTimestamp(Instant.now().minusSeconds(MAX_OLDER_IN_SEC + 1));
        var exception = Assertions.assertThrows(NoContentException.class, () ->
                transactionValidator.validar(transaction)
        );

        Assertions.assertEquals(
                "The transaction is older than " + MAX_OLDER_IN_SEC + " seconds",
                exception.getMessage());
    }
}