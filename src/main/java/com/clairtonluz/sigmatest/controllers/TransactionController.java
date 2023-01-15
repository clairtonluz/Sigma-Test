package com.clairtonluz.sigmatest.controllers;

import com.clairtonluz.sigmatest.transactions.TransactionDTO;
import com.clairtonluz.sigmatest.transactions.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Object> addTransactions(@RequestBody TransactionDTO transactionDTO) {
        transactionService.add(transactionDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addTransactions() {
        transactionService.deleteAll();
    }
}
