package de.admir.service.impl;

import de.admir.dao.TransactionDao;
import de.admir.data.TransactionData;
import de.admir.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Author:  Admir Memic
 * Date:    11.03.2016
 * E-Mail:  admir.memic@dmc.de
 */
@Service("transactionService")
public class TransactionServiceImpl implements TransactionService
{
	@Autowired
	private TransactionDao transactionDao;

	@Override
	public TransactionData putTransaction(Long id, TransactionData transactionData)
	{
		transactionData.setId(id);
		return transactionDao.createTransaction(transactionData);
	}

	@Override
	public TransactionData getTransaction(Long id)
	{
		return transactionDao.findTransactionById(id);
	}

	@Override
	public List<Long> getTransactionsForType(String type)
	{
		return transactionDao.findTransactionsByType(type)
				.stream()
				.map(TransactionData::getId)
				.collect(Collectors.toList());
	}

	@Override
	public Double getSumForParentId(Long id)
	{
		// TODO: Remove stub implementation
		return 0D;
	}
}
