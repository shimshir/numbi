package de.admir;

import de.admir.dao.TransactionDao;
import de.admir.dao.impl.TransactionDaoImpl;
import de.admir.data.TransactionData;
import de.admir.exception.CrudOperationException;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Author:  Admir Memic
 * Date:    13.03.2016
 * E-Mail:  admir.memic@dmc.de
 */
public class TransactionDaoTest {

    private TransactionDao transactionDao = new TransactionDaoImpl();

    @Test
    public void testCreateTransaction() {
        final Long transactionId = 1L;
        TransactionData expectedTransaction = new TransactionData(transactionId, 500D, "tech", null);
        transactionDao.createTransaction(expectedTransaction);
        TransactionData actualTransaction = transactionDao.findTransactionById(transactionId);
        assertEquals(expectedTransaction.getId(), actualTransaction.getId());
        assertEquals(expectedTransaction.getAmount(), actualTransaction.getAmount());
        assertEquals(expectedTransaction.getType(), actualTransaction.getType());
    }

    @Test(expected = CrudOperationException.class)
    public void testDuplicateCreateTransaction() {
        Long transactionId = 1L;
        TransactionData transactionData = new TransactionData(transactionId, 500D, "tech", null);
        transactionDao.createTransaction(transactionData);
        transactionDao.createTransaction(transactionData);
    }

    @Test
    public void testFindTransactionsByParentId() {
        List<TransactionData> transactions = new ArrayList<>();
        transactions.add(new TransactionData(0L, 100D, "tech", null));
        transactions.add(new TransactionData(1L, 200D, "tech", 0L));
        transactions.add(new TransactionData(2L, 300D, "tech", 0L));
        transactions.add(new TransactionData(3L, 400D, "tech", 0L));
        transactions.add(new TransactionData(4L, 500D, "tech", 1L));
        transactions.add(new TransactionData(5L, 600D, "tech", 1L));
        transactions.add(new TransactionData(6L, 700D, "tech", 0L));
        transactions.forEach(transaction -> transactionDao.createTransaction(transaction));

        assertEquals(4, transactionDao.findTransactionsByParentId(0L).size());
        assertEquals(2, transactionDao.findTransactionsByParentId(1L).size());
    }

    @Test
    public void testFindTransactionsByType() {
        List<TransactionData> transactions = new ArrayList<>();
        transactions.add(new TransactionData(0L, 100D, "tech", null));
        transactions.add(new TransactionData(1L, 200D, "tech", 0L));
        transactions.add(new TransactionData(2L, 300D, "cars", 0L));
        transactions.add(new TransactionData(3L, 400D, "cars", 0L));
        transactions.add(new TransactionData(4L, 500D, "cars", 1L));
        transactions.add(new TransactionData(5L, 600D, "food", 1L));
        transactions.add(new TransactionData(6L, 700D, "toys", 0L));
        transactions.forEach(transaction -> transactionDao.createTransaction(transaction));

        assertEquals(2, transactionDao.findTransactionsByType("tech").size());
        assertEquals(3, transactionDao.findTransactionsByType("cars").size());
        assertEquals(1, transactionDao.findTransactionsByType("food").size());
        assertEquals(1, transactionDao.findTransactionsByType("toys").size());
    }
}
