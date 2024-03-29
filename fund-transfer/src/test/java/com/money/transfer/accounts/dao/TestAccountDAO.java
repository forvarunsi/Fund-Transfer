package com.money.transfer.accounts.dao;

import com.money.transfer.accounts.dao.DAOFactory;
import com.money.transfer.accounts.exception.CustomException;
import com.money.transfer.accounts.model.AccountInfo;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class TestAccountDAO {

	private static final DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

	@BeforeClass
	public static void setup() {
		// prepare test database and test data. Test data are initialised from
		// src/test/resources/demo.sql
		h2DaoFactory.populateTestData();
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testGetAllAccounts() throws CustomException {
		List<AccountInfo> allAccounts = h2DaoFactory.getAccountInfoDAO().getAllAccounts();
		assertTrue(allAccounts.size() > 1);
	}

	@Test
	public void testGetAccountById() throws CustomException {
		AccountInfo account = h2DaoFactory.getAccountInfoDAO().getAccountById(1L);
		assertTrue(account.getUserName().equals("varun"));
	}

	@Test
	public void testGetNonExistingAccById() throws CustomException {
		AccountInfo account = h2DaoFactory.getAccountInfoDAO().getAccountById(100L);
		assertTrue(account == null);
	}

	@Test
	public void testCreateAccount() throws CustomException {
		BigDecimal balance = new BigDecimal(10).setScale(4, RoundingMode.HALF_EVEN);
		AccountInfo a = new AccountInfo("test2", balance, "CNY");
		long aid = h2DaoFactory.getAccountInfoDAO().createAccount(a);
		AccountInfo afterCreation = h2DaoFactory.getAccountInfoDAO().getAccountById(aid);
		assertTrue(afterCreation.getUserName().equals("test2"));
		assertTrue(afterCreation.getCurrencyCode().equals("CNY"));
		assertTrue(afterCreation.getBalance().equals(balance));
	}

	@Test
	public void testDeleteAccount() throws CustomException {
		int rowCount = h2DaoFactory.getAccountInfoDAO().deleteAccountById(2L);
		// assert one row(user) deleted
		assertTrue(rowCount == 1);
		// assert user no longer there
		assertTrue(h2DaoFactory.getAccountInfoDAO().getAccountById(2L) == null);
	}

	@Test
	public void testDeleteNonExistingAccount() throws CustomException {
		int rowCount = h2DaoFactory.getAccountInfoDAO().deleteAccountById(500L);
		// assert no row(user) deleted
		assertTrue(rowCount == 0);

	}

	@Test
	public void testUpdateAccountBalanceSufficientFund() throws CustomException {

		BigDecimal deltaDeposit = new BigDecimal(50).setScale(4, RoundingMode.HALF_EVEN);
		BigDecimal afterDeposit = new BigDecimal(150).setScale(4, RoundingMode.HALF_EVEN);
		int rowsUpdated = h2DaoFactory.getAccountInfoDAO().updateAccountBalance(1L, deltaDeposit);
		assertTrue(rowsUpdated == 1);
		assertTrue(h2DaoFactory.getAccountInfoDAO().getAccountById(1L).getBalance().equals(afterDeposit));
		BigDecimal deltaWithDraw = new BigDecimal(-50).setScale(4, RoundingMode.HALF_EVEN);
		BigDecimal afterWithDraw = new BigDecimal(100).setScale(4, RoundingMode.HALF_EVEN);
		int rowsUpdatedW = h2DaoFactory.getAccountInfoDAO().updateAccountBalance(1L, deltaWithDraw);
		assertTrue(rowsUpdatedW == 1);
		assertTrue(h2DaoFactory.getAccountInfoDAO().getAccountById(1L).getBalance().equals(afterWithDraw));

	}

	@Test(expected = CustomException.class)
	public void testUpdateAccountBalanceNotEnoughFund() throws CustomException {
		BigDecimal deltaWithDraw = new BigDecimal(-50000).setScale(4, RoundingMode.HALF_EVEN);
		int rowsUpdatedW = h2DaoFactory.getAccountInfoDAO().updateAccountBalance(1L, deltaWithDraw);
		assertTrue(rowsUpdatedW == 0);

	}

}