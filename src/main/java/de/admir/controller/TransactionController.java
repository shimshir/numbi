package de.admir.controller;

import de.admir.data.TransactionData;
import de.admir.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Author:  Admir Memic
 * Date:    11.03.2016
 * E-Mail:  admir.memic@dmc.de
 */
@RestController
@RequestMapping("/transactionservice")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(path = "/transaction/{transactionId}", method = RequestMethod.PUT)
    public ResponseEntity<?> putTransaction(@PathVariable Long transactionId, @RequestBody TransactionData transactionData) {
        TransactionData newTransaction = transactionService.putTransaction(transactionId, transactionData);
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        return ResponseEntity.ok(response);
    }

    @RequestMapping(path = "/transaction/{transactionId}", method = RequestMethod.GET)
    public ResponseEntity<?> getTransaction(@PathVariable Long transactionId) {
        TransactionData transaction = transactionService.getTransaction(transactionId);
        return transaction != null ?
                ResponseEntity.ok(transaction) :
                new ResponseEntity<>("Transaction not found", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/types/{type}", method = RequestMethod.GET)
    public ResponseEntity<?> getTypes(@PathVariable String type) {
        List<Long> transactionIds = transactionService.getTransactionsByType(type);
        return ResponseEntity.ok(transactionIds);
    }

    @RequestMapping(path = "/sum/{transactionId}", method = RequestMethod.GET)
    public ResponseEntity<?> getSum(@PathVariable Long transactionId) {
        Double sum = transactionService.getSumByParentId(transactionId);
        Map<String, Double> response = new HashMap<>();
        response.put("sum", sum);
        return ResponseEntity.ok(response);
    }
}
