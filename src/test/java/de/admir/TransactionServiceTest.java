package de.admir;

import de.admir.dao.TransactionDao;
import de.admir.data.TransactionData;
import de.admir.service.TransactionService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Author:  Admir Memic
 * Date:    13.03.2016
 * E-Mail:  admir.memic@dmc.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Main.class)
public class TransactionServiceTest {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionDao transactionDao;

    private static boolean setUpIsDone = false;

    @Before
    public void setUp() {
        if (setUpIsDone)
            return;
        createTestTransactions();
        setUpIsDone = true;
    }

    private void createTestTransactions() {
        List<TransactionData> transactions = new ArrayList<>();
        transactions.add(new TransactionData(0L, 1000D, "root", null));
        transactions.add(new TransactionData(1L, 1010D, "car", 0L));
        transactions.add(new TransactionData(2L, 1020D, "food", 0L));
        transactions.add(new TransactionData(3L, 1030D, "part", 1L));
        transactions.add(new TransactionData(4L, 1040D, "part", 1L));
        transactions.add(new TransactionData(5L, 1050D, "part", 1L));
        transactions.add(new TransactionData(6L, 1060D, "fruit", 2L));
        transactions.add(new TransactionData(7L, 1070D, "meat", 2L));
        transactions.add(new TransactionData(8L, 1080D, "electric", 4L));
        transactions.add(new TransactionData(9L, 1090D, "electric", 4L));
        transactions.add(new TransactionData(10L, 1100D, "fish", 7L));
        transactions.add(new TransactionData(11L, 1110D, "meat", 7L));
        transactions.add(new TransactionData(12L, 1120D, "fish", 10L));
        transactions.forEach(transaction -> transactionDao.createTransaction(transaction));
    }

    @Test
    public void testCreateTransaction() {
        TransactionData expectedTransaction = new TransactionData();
        expectedTransaction.setAmount(1.23D);
        expectedTransaction.setType("music");
        final Long id = 42L;
        transactionService.putTransaction(id, expectedTransaction);
        TransactionData actualTransaction = transactionService.getTransaction(id);
        assertEquals(expectedTransaction.getId(), actualTransaction.getId());
        assertEquals(expectedTransaction.getType(), actualTransaction.getType());
        assertEquals(expectedTransaction.getAmount(), actualTransaction.getAmount(), 0.0001);
    }

    @Test
    public void testGetTransactionsByType() {
        Long[] rootIds = {0L};
        Long[] carIds = {1L};
        Long[] foodIds = {2L};
        Long[] partIds = {3L, 4L, 5L};
        Long[] fruitIds = {6L};
        Long[] meatIds = {7L, 11L};
        Long[] fishIds = {10L, 12L};
        Long[] electricIds = {8L, 9L};

        Arrays.asList(rootIds).forEach(rootId ->
                assertTrue(transactionService.getTransactionsByType("root").contains(rootId)));
        Arrays.asList(carIds).forEach(carId ->
                assertTrue(transactionService.getTransactionsByType("car").contains(carId)));
        Arrays.asList(foodIds).forEach(foodId ->
                assertTrue(transactionService.getTransactionsByType("food").contains(foodId)));
        Arrays.asList(partIds).forEach(partId ->
                assertTrue(transactionService.getTransactionsByType("part").contains(partId)));
        Arrays.asList(fruitIds).forEach(fruitId ->
                assertTrue(transactionService.getTransactionsByType("fruit").contains(fruitId)));
        Arrays.asList(meatIds).forEach(meatId ->
                assertTrue(transactionService.getTransactionsByType("meat").contains(meatId)));
        Arrays.asList(fishIds).forEach(fishId ->
                assertTrue(transactionService.getTransactionsByType("fish").contains(fishId)));
        Arrays.asList(electricIds).forEach(electricId ->
                assertTrue(transactionService.getTransactionsByType("electric").contains(electricId)));
    }

    @Test
    public void testGetSumByParentId() {
        assertEquals(13780D, transactionService.getSumByParentId(0L), 0.0001);
        assertEquals(6300D, transactionService.getSumByParentId(1L), 0.0001);
        assertEquals(6480D, transactionService.getSumByParentId(2L), 0.0001);
        assertEquals(1030D, transactionService.getSumByParentId(3L), 0.0001);
        assertEquals(3210D, transactionService.getSumByParentId(4L), 0.0001);
        assertEquals(1050D, transactionService.getSumByParentId(5L), 0.0001);
        assertEquals(1060D, transactionService.getSumByParentId(6L), 0.0001);
        assertEquals(4400D, transactionService.getSumByParentId(7L), 0.0001);
        assertEquals(1080D, transactionService.getSumByParentId(8L), 0.0001);
        assertEquals(1090D, transactionService.getSumByParentId(9L), 0.0001);
        assertEquals(2220D, transactionService.getSumByParentId(10L), 0.0001);
        assertEquals(1110D, transactionService.getSumByParentId(11L), 0.0001);
        assertEquals(1120D, transactionService.getSumByParentId(12L), 0.0001);
    }
}
