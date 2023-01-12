package com.clairtonluz.sigmatest.transactions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    String amount;
    String timestamp;

    Transaction toTransaction() {
        var transaction = new Transaction();
        transaction.setAmount(new BigDecimal(amount));
        transaction.setTimestamp(Instant.parse(timestamp));
        return transaction;
    }
}
