package com.clairtonluz.sigmatest.transactions;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionStatisticsScheduler {
    private final TransactionRepository transactionRepository;

    @Scheduled(fixedRate = 1000)
    public void refreshStatistics() {
        transactionRepository.calculate();
    }

}
