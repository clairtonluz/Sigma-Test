package com.clairtonluz.sigmatest.transactions;

import com.clairtonluz.sigmatest.core.exceptions.NoContentException;
import com.clairtonluz.sigmatest.core.exceptions.UnprocessableEntityException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TransactionValidator {
    public static final int MAX_OLDER_IN_SEC = 90;
    public void validar(Transaction transaction) {
        if (Instant.now().isBefore(transaction.getTimestamp())) {
            throw new UnprocessableEntityException("The transaction date is in the future");
        }

        Instant limitTimestamp = Instant.now().minusSeconds(MAX_OLDER_IN_SEC);
        if (transaction.getTimestamp().isBefore(limitTimestamp)) {
            throw new NoContentException("The transaction is older than " + MAX_OLDER_IN_SEC + " seconds");
        }
    }
}
