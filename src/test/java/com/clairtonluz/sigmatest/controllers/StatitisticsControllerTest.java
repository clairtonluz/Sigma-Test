package com.clairtonluz.sigmatest.controllers;

import com.clairtonluz.sigmatest.transactions.Transaction;
import com.clairtonluz.sigmatest.transactions.TransactionDTO;
import com.clairtonluz.sigmatest.transactions.TransactionStatistics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.stream.Stream;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatitisticsControllerTest {
    @Value(value = "http://localhost:${local.server.port}/transactions")
    private String apiTransactionUrl;
    @Value(value = "http://localhost:${local.server.port}/statistics")
    private String apiStatisticsUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getStatistics() {

        Stream.of("6703.2542", "8307.6975", "3468.4957", "2714.1956", "4985.9903")
                .map(BigDecimal::new)
                .forEach(amount -> {
                    var timestamp = Instant.now();
                    var transactionDto = new TransactionDTO(amount.toString(), timestamp.toString());
                    var response = this.restTemplate.postForEntity(apiTransactionUrl, transactionDto, Transaction.class);

                    Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
                });

        var response = this.restTemplate.getForEntity(apiStatisticsUrl, TransactionStatistics.class);
        var body = response.getBody();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertNotNull(body);
        Assertions.assertEquals("26179.63", body.sum());
        Assertions.assertEquals("5235.93", body.avg());
        Assertions.assertEquals("8307.70", body.max());
        Assertions.assertEquals("2714.20", body.min());
        Assertions.assertEquals(5, body.count());

    }
}