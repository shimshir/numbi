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

import java.util.Arrays;

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
        MainTest.getTestTransactions().forEach(transactionDao::createTransaction);
        setUpIsDone = true;
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
