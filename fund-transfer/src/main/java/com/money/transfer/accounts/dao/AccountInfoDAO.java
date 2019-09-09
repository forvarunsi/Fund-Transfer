package com.money.transfer.accounts.dao;

import com.money.transfer.accounts.model.AccountInfo;
import com.money.transfer.accounts.model.UserTransaction;
import com.money.transfer.accounts.exception.CustomException;

import java.math.BigDecimal;
import java.util.List;


public interface AccountInfoDAO {

    List<AccountInfo> getAllAccounts() throws CustomException;
    AccountInfo getAccountById(long accountId) throws CustomException;
    long createAccount(AccountInfo account) throws CustomException;
    int deleteAccountById(long accountId) throws CustomException;
    int updateAccountBalance(long accountId, BigDecimal deltaAmount) throws CustomException;
    int transferAccountBalance(UserTransaction userTransaction) throws CustomException;
}
