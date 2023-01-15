package com.clairtonluz.sigmatest.controllers;

import com.clairtonluz.sigmatest.transactions.Transaction;
import com.clairtonluz.sigmatest.transactions.TransactionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;

import static com.clairtonluz.sigmatest.transactions.TransactionValidator.MAX_OLDER_IN_SEC;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerTest {
    @Value(value = "http://localhost:${local.server.port}/transactions")
    private String apiUrl;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void addTransactionsSuccess() {
        var amount = new BigDecimal("12.213");
        var timestamp = Instant.now();
        var transactionDto = new TransactionDTO(amount.toString(), timestamp.toString());
        var response = this.restTemplate.postForEntity(apiUrl, transactionDto, Transaction.class);

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
    }

    @Test
    void addTransactionsInTheFuture() {
        var amount = new BigDecimal("12.213");
        var timestamp = Instant.now().plusSeconds(10);
        var transactionDto = new TransactionDTO(amount.toString(), timestamp.toString());
        var response = this.restTemplate.postForEntity(apiUrl, transactionDto, Transaction.class);

        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatusCode().value());
    }

    @Test
    void addTransactionsInvalidJson() {
        var transactionDto = new TransactionDTO("12.213", "2018-07-17T09:59:51.312Z");
        var jsonString = String.format("{ invalid \"amount\": \"%s\", \"timestamp\": \"%s\"}", transactionDto.getAmount(), transactionDto.getTimestamp());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var requestEntity = new RequestEntity<>(jsonString, headers, HttpMethod.POST, URI.create(apiUrl));
        var response = this.restTemplate.exchange(requestEntity, Object.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    @Test
    void addTransactionsInvalidAmount() {
        var transactionDto = new TransactionDTO("12.21.3", "2018-07-17T09:59:51.312Z");
        var response = this.restTemplate.postForEntity(apiUrl, transactionDto, Transaction.class);

        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatusCode().value());
    }

    @Test
    void addTransactionsInvalidTimestamp() {
        var transactionDto = new TransactionDTO("12.213", "2018-07-17 09:59:51");
        var response = this.restTemplate.postForEntity(apiUrl, transactionDto, Transaction.class);

        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatusCode().value());
    }

    @Test
    void addTransactionsExceedLimitTimestamp() {
        var transactionDto = new TransactionDTO("12.213", Instant.now().minusSeconds(MAX_OLDER_IN_SEC + 1).toString());
        var response = this.restTemplate.postForEntity(apiUrl, transactionDto, Transaction.class);

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
    }


    @Test
    void deleteAll() {
        RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.DELETE, URI.create(apiUrl));
        var response = this.restTemplate.exchange(requestEntity, String.class);

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
    }
}