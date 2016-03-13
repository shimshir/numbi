package de.admir;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import de.admir.dao.TransactionDao;
import de.admir.data.TransactionData;
import de.admir.exception.CrudOperationException;

import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Author:  Admir Memic
 * Date:    13.03.16
 * E-Mail:  admir.memic@dmc.de
 */

public class MainTest {
    private static final String HOST = "http://localhost:8080";
    private static final String API_ENDPOINT = "/transactionservice";
    private static final String REQUEST_URL = HOST + API_ENDPOINT;
    private static ApplicationContext ctx;

    @BeforeClass
    public static void setUp() {
        ctx = SpringApplication.run(Main.class);
        TransactionDao transactionDao = (TransactionDao) ctx.getBean("transactionDao");
        getTestTransactions().forEach(transactionDao::createTransaction);
    }

    @AfterClass
    public static void tearDown() {
        SpringApplication.exit(ctx);
    }

    @Test
    public void testPutTransaction() throws UnirestException {
        HttpResponse<JsonNode> response = putTransaction(new TransactionData(100L, 1000D, "clothes", null));
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("{\"status\":\"ok\"}", response.getBody().toString());
    }

    @Test
    public void testPutDuplicateTransaction() throws UnirestException, ClassNotFoundException {
        TransactionData transactionData = new TransactionData(101L, 1000D, "clothes", null);
        putTransaction(transactionData);
        HttpResponse<JsonNode> response = putTransaction(transactionData);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(Class.forName(response.getBody().getObject().getString("exception")), CrudOperationException.class);
    }

    @Test
    public void testGetTransaction() throws UnirestException {
        TransactionData transactionData = new TransactionData(102L, 1000D, "clothes", 100L);
        putTransaction(transactionData);
        HttpResponse<JsonNode> response = getTransaction(102L);
        JSONObject responseJSON = response.getBody().getObject();
        assertEquals(transactionData.getAmount(), responseJSON.getDouble("amount"), 0.0001);
        assertEquals(transactionData.getType(), responseJSON.getString("type"));
        assertEquals(transactionData.getParentId().longValue(), responseJSON.getLong("parent_id"));
    }

    @Test
    public void testGetTransactionsByType() throws UnirestException {
        Long[] rootIds = {0L};
        Long[] carIds = {1L};
        Long[] foodIds = {2L};
        Long[] partIds = {3L, 4L, 5L};
        Long[] fruitIds = {6L};
        Long[] meatIds = {7L, 11L};
        Long[] fishIds = {10L, 12L};
        Long[] electricIds = {8L, 9L};

        Arrays.asList(rootIds).forEach(rootId ->
                assertTrue(convertToListOfLongs(getTransactionsByType("root").getBody().getArray()).contains(rootId)));
        Arrays.asList(carIds).forEach(carId ->
                assertTrue(convertToListOfLongs(getTransactionsByType("car").getBody().getArray()).contains(carId)));
        Arrays.asList(foodIds).forEach(foodId ->
                assertTrue(convertToListOfLongs(getTransactionsByType("food").getBody().getArray()).contains(foodId)));
        Arrays.asList(partIds).forEach(partId ->
                assertTrue(convertToListOfLongs(getTransactionsByType("part").getBody().getArray()).contains(partId)));
        Arrays.asList(fruitIds).forEach(fruitId ->
                assertTrue(convertToListOfLongs(getTransactionsByType("fruit").getBody().getArray()).contains(fruitId)));
        Arrays.asList(meatIds).forEach(meatId ->
                assertTrue(convertToListOfLongs(getTransactionsByType("meat").getBody().getArray()).contains(meatId)));
        Arrays.asList(fishIds).forEach(fishId ->
                assertTrue(convertToListOfLongs(getTransactionsByType("fish").getBody().getArray()).contains(fishId)));
        Arrays.asList(electricIds).forEach(electricId ->
                assertTrue(convertToListOfLongs(getTransactionsByType("electric").getBody().getArray()).contains(electricId)));
    }

    @Test
    public void testGetSumByParentId() {
        assertEquals(13780D, getSumByParentId(0L).getBody().getObject().getDouble("sum"), 0.0001);
        assertEquals(6300D, getSumByParentId(1L).getBody().getObject().getDouble("sum"), 0.0001);
        assertEquals(6480D, getSumByParentId(2L).getBody().getObject().getDouble("sum"), 0.0001);
        assertEquals(1030D, getSumByParentId(3L).getBody().getObject().getDouble("sum"), 0.0001);
        assertEquals(3210D, getSumByParentId(4L).getBody().getObject().getDouble("sum"), 0.0001);
        assertEquals(1050D, getSumByParentId(5L).getBody().getObject().getDouble("sum"), 0.0001);
        assertEquals(1060D, getSumByParentId(6L).getBody().getObject().getDouble("sum"), 0.0001);
        assertEquals(4400D, getSumByParentId(7L).getBody().getObject().getDouble("sum"), 0.0001);
        assertEquals(1080D, getSumByParentId(8L).getBody().getObject().getDouble("sum"), 0.0001);
        assertEquals(1090D, getSumByParentId(9L).getBody().getObject().getDouble("sum"), 0.0001);
        assertEquals(2220D, getSumByParentId(10L).getBody().getObject().getDouble("sum"), 0.0001);
        assertEquals(1110D, getSumByParentId(11L).getBody().getObject().getDouble("sum"), 0.0001);
        assertEquals(1120D, getSumByParentId(12L).getBody().getObject().getDouble("sum"), 0.0001);
    }

    private static HttpResponse<JsonNode> putTransaction(TransactionData transaction) throws UnirestException {
        JSONObject transactionJSONObject = new JSONObject()
                .put("amount", transaction.getAmount())
                .put("type", transaction.getType())
                .put("parent_id", transaction.getParentId());
        JsonNode transactionBody = new JsonNode(transactionJSONObject.toString());
        return Unirest.put(REQUEST_URL + "/transaction/" + transaction.getId())
                .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                .body(transactionBody)
                .asJson();
    }

    private static HttpResponse<JsonNode> getTransaction(Long id) throws UnirestException {
        return Unirest.get(REQUEST_URL + "/transaction/" + id).asJson();
    }

    private static HttpResponse<JsonNode> getTransactionsByType(String type) {
        try {
            return Unirest.get(REQUEST_URL + "/types/" + type).asJson();
        } catch (UnirestException e) {
            return null;
        }
    }

    private static HttpResponse<JsonNode> getSumByParentId(Long parentId) {
        try {
            return Unirest.get(REQUEST_URL + "/sum/" + parentId).asJson();
        } catch (UnirestException e) {
            return null;
        }
    }

    private static List<Long> convertToListOfLongs(JSONArray jsonArray) {
        ArrayList<Long> list = new ArrayList<>();
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                list.add((long) (int) jsonArray.get(i));
            }
        }
        return list;
    }

    static List<TransactionData> getTestTransactions() {
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
        return transactions;
    }
}
