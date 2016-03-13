package de.admir.service;


import de.admir.data.TransactionData;

import java.util.List;


/**
 * Author:  Admir Memic
 * Date:    11.03.2016
 * E-Mail:  admir.memic@dmc.de
 */
public interface TransactionService {
    TransactionData putTransaction(Long id, TransactionData transactionData);

    TransactionData getTransaction(Long id);

    List<Long> getTransactionsByType(String type);

    Double getSumByParentId(Long parentId);
}
