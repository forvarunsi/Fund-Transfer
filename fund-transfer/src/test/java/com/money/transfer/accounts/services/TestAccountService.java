package com.money.transfer.accounts.services;

import com.money.transfer.accounts.model.AccountInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

/**
 * Integration testing for RestAPI
 * Test data are initialised from src/test/resources/demo.sql
 * <p>
 * INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('test2',100.0000,'USD'); --ID =1
 * INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('test1',200.0000,'USD'); --ID =2
 * INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('test2',500.0000,'EUR'); --ID =3
 * INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('test1',500.0000,'EUR'); --ID =4
 */

public class TestAccountService extends TestService {


    /*
    TC A1 Positive Category = AccountService
    Scenario: test get user account by user name
              return 200 OK
     */
    @Test
    public void testGetAccountByUserName() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/1").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == 200);
        //check the content
        String jsonString = EntityUtils.toString(response.getEntity());
        AccountInfo account = mapper.readValue(jsonString, AccountInfo.class);
        assertTrue(account.getUserName().equals("varun"));
    }

    /*
    TC A2 Positive Category = AccountService
    Scenario: test get all user accounts
              return 200 OK
    */
    @Test
    public void testGetAllAccounts() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/all").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
        //check the content
        String jsonString = EntityUtils.toString(response.getEntity());
        AccountInfo[] accounts = mapper.readValue(jsonString, AccountInfo[].class);
        assertTrue(accounts.length > 0);
    }

    /*
    TC A3 Positive Category = AccountService
    Scenario: test get account balance given account ID
              return 200 OK
    */
    @Test
    public void testGetAccountBalance() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/1/balance").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
        //check the content, assert user test2 have balance 100
        String balance = EntityUtils.toString(response.getEntity());
        BigDecimal res = new BigDecimal(balance).setScale(4, RoundingMode.HALF_EVEN);
        BigDecimal db = new BigDecimal(100).setScale(4, RoundingMode.HALF_EVEN);
        assertTrue(res.equals(db));
    }



}
