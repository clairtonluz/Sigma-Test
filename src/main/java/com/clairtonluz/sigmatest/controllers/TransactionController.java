package com.clairtonluz.sigmatest.controllers;

import com.clairtonluz.sigmatest.core.config.GlobalErrorHandler;
import com.clairtonluz.sigmatest.transactions.TransactionDTO;
import com.clairtonluz.sigmatest.transactions.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("transactions")
@Tag(name = "Transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Add a transaction",
            responses = {
                    @ApiResponse(responseCode = "201", description = "in case of success"),
                    @ApiResponse(responseCode = "204", description = "if the transaction is older than 60 seconds",
                            content = @Content(schema = @Schema(implementation = GlobalErrorHandler.ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "if the JSON is invalid",
                            content = @Content(schema = @Schema(implementation = GlobalErrorHandler.ErrorResponse.class))),
                    @ApiResponse(responseCode = "422",
                            description = "if any of the fields are not parsable or the transaction date is in the future",
                            content = @Content(schema = @Schema(implementation = GlobalErrorHandler.ErrorResponse.class)))
            })
    @PostMapping
    public ResponseEntity<Void> addTransactions(@RequestBody TransactionDTO transactionDTO) {
        transactionService.add(transactionDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Delete all transactions",
            responses = {
                    @ApiResponse(responseCode = "204", description = "All transactions was deleted"),
            })
    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addTransactions() {
        transactionService.deleteAll();
    }
}
