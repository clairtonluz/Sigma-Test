package com.clairtonluz.sigmatest.controllers;

import com.clairtonluz.sigmatest.transactions.TransactionService;
import com.clairtonluz.sigmatest.transactions.TransactionStatistics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("statistics")
@Tag(name = "Statistics")
public class StatitisticsController {

    private final TransactionService transactionService;

    @Operation(summary = "Returns statistics for the last 60 seconds")
    @GetMapping
    public ResponseEntity<TransactionStatistics> getStatistics() {
        TransactionStatistics statistics = transactionService.getStatistics();
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}
