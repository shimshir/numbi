package de.admir.service.impl;

import de.admir.dao.TransactionDao;
import de.admir.data.TransactionData;
import de.admir.exception.CrudOperationException;
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
	public Double getSumForParentId(Long parentId)
	{
		TransactionData rootTransaction = transactionDao.findTransactionById(parentId);
		if (rootTransaction == null)
			throw new CrudOperationException("Could not find transaction for id: " + parentId);
		return calculateChildrenSum(rootTransaction);
	}

	private Double calculateChildrenSum(TransactionData transactionNode)
	{
		List<TransactionData> children = transactionDao.findTransactionsByParentId(transactionNode.getId());
		return transactionNode.getAmount() + ((children == null || children.size() == 0) ? 0D :
				children
						.stream()
						.mapToDouble(this::calculateChildrenSum)
						.sum());
	}
}
