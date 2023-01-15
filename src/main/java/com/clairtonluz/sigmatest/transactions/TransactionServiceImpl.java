package com.clairtonluz.sigmatest.transactions;

import com.clairtonluz.sigmatest.core.exceptions.UnprocessableEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionValidator transactionValidator;
    private final TransactionRepository transactionRepository;

    @Override
    public void add(TransactionDTO transactionDTO) {
        try {
            var transaction = transactionDTO.toTransaction();
            transactionValidator.validar(transaction);
            transactionRepository.add(transaction);
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new UnprocessableEntityException("The fields are not parsable");
        }
    }

    @Override
    public TransactionStatistics getStatistics() {
        return transactionRepository.getStatistics();
    }

    @Override
    public void deleteAll() {
        transactionRepository.deleteAll();
    }
}
