package com.clairtonluz.sigmatest.transactions;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class Transaction {
    BigDecimal amount;
    Instant timestamp;
}
