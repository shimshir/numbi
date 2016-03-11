package de.admir.dao.impl;

import de.admir.exception.CrudOperationException;
import de.admir.dao.TransactionDao;
import de.admir.data.TransactionData;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Author:  Admir Memic
 * Date:    11.03.2016
 * E-Mail:  admir.memic@dmc.de
 */
@Repository("transactionDao")
public class TransactionDaoImpl implements TransactionDao
{
	private List<TransactionData> transactionsTableStub = new ArrayList<>();

	@Override
	public TransactionData createTransaction(TransactionData transactionData)
	{
		if (transactionsTableStub
				.stream()
				.filter(t -> t.getId().equals(transactionData.getId()))
				.findFirst()
				.isPresent())
			throw new CrudOperationException(
					String.format("Transaction id: %s exists already, please choose another id", transactionData.getId()));
		transactionsTableStub.add(transactionData);
		return transactionData;
	}

	@Override
	public TransactionData findTransactionById(Long id)
	{
		return transactionsTableStub
				.stream()
				.filter(t -> id.equals(t.getId()))
				.findFirst().orElse(null);
	}

	@Override
	public List<TransactionData> findTransactionsByParentId(Long parentId)
	{
		return transactionsTableStub
				.stream()
				.filter(t -> parentId.equals(t.getParentId()))
				.collect(Collectors.toList());
	}

	@Override
	public List<TransactionData> findTransactionsByType(String type)
	{
		return transactionsTableStub
				.stream()
				.filter(t -> type.equals(t.getType()))
				.collect(Collectors.toList());
	}
}
