package com.clairtonluz.sigmatest.transactions;

import com.clairtonluz.sigmatest.core.exceptions.UnprocessableEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionValidator transactionValidator;
    private final TransactionStatisticsRepository transactionStatisticsRepository;

    @Override
    public void add(TransactionDTO transactionDTO) {
        try {
            var transaction = transactionDTO.toTransaction();
            transactionValidator.validar(transaction);
            transactionStatisticsRepository.add(transaction);
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new UnprocessableEntityException("The fields are not parsable");
        }
    }

    @Override
    public TransactionStatistics getStatistics() {
        return transactionStatisticsRepository.getStatistics();
    }
}
