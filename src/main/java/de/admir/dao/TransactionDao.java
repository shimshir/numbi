package de.admir.dao;

import de.admir.data.TransactionData;

import java.util.List;


/**
 * Author:  Admir Memic
 * Date:    11.03.2016
 * E-Mail:  admir.memic@dmc.de
 */
public interface TransactionDao
{
	TransactionData createTransaction(TransactionData transactionData);
	TransactionData findTransactionById(Long id);
	List<TransactionData> findTransactionsByType(String type);
}
