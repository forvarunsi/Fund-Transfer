package com.money.transfer.accounts.service;

import com.money.transfer.accounts.dao.DAOFactory;
import com.money.transfer.accounts.exception.CustomException;
import com.money.transfer.accounts.model.AccountInfo;
import com.money.transfer.accounts.model.MoneyUtil;

import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountInfoService {
	
    private final DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);
    
    private static Logger log = Logger.getLogger(AccountInfoService.class);

    
    //Find all accounts
     
    @GET
    @Path("/all")
    public List<AccountInfo> getAllAccounts() throws CustomException {
        return daoFactory.getAccountInfoDAO().getAllAccounts();
    }

    //Find by account id
     
    @GET
    @Path("/{accountId}")
    public AccountInfo getAccount(@PathParam("accountId") long accountId) throws CustomException {
        return daoFactory.getAccountInfoDAO().getAccountById(accountId);
    }
    
    //Find balance by account Id
    
    @GET
    @Path("/{accountId}/balance")
    public BigDecimal getBalance(@PathParam("accountId") long accountId) throws CustomException {
        final AccountInfo account = daoFactory.getAccountInfoDAO().getAccountById(accountId);

        if(account == null){
            throw new WebApplicationException("Account not found", Response.Status.NOT_FOUND);
        }
        return account.getBalance();
    }

    //Deposit amount by account Id
     
    @PUT
    @Path("/{accountId}/deposit/{amount}")
    public AccountInfo deposit(@PathParam("accountId") long accountId,@PathParam("amount") BigDecimal amount) throws CustomException {

        if (amount.compareTo(MoneyUtil.zeroAmount) <=0){
            throw new WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST);
        }

        daoFactory.getAccountInfoDAO().updateAccountBalance(accountId,amount.setScale(4, RoundingMode.HALF_EVEN));
        return daoFactory.getAccountInfoDAO().getAccountById(accountId);
    }

    //Withdraw amount by account Id
    
    @PUT
    @Path("/{accountId}/withdraw/{amount}")
    public AccountInfo withdraw(@PathParam("accountId") long accountId,@PathParam("amount") BigDecimal amount) throws CustomException {

        if (amount.compareTo(MoneyUtil.zeroAmount) <=0){
            throw new WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST);
        }
        BigDecimal delta = amount.negate();
        if (log.isDebugEnabled())
            log.debug("Withdraw service: delta change to account  " + delta + " Account ID = " +accountId);
        daoFactory.getAccountInfoDAO().updateAccountBalance(accountId,delta.setScale(4, RoundingMode.HALF_EVEN));
        return daoFactory.getAccountInfoDAO().getAccountById(accountId);
    }

}
