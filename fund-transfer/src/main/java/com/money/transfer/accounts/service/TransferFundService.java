package com.money.transfer.accounts.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.money.transfer.accounts.dao.DAOFactory;
import com.money.transfer.accounts.exception.CustomException;
import com.money.transfer.accounts.model.MoneyUtil;
import com.money.transfer.accounts.model.UserTransaction;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransferFundService {

	private final DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);
	
	//Transfer fund between two accounts.
	
	@POST
	public Response transferFund(UserTransaction transaction) throws CustomException {

		String currency = transaction.getCurrencyCode();
		if (MoneyUtil.INSTANCE.validateCcyCode(currency)) {
			int updateCount = daoFactory.getAccountInfoDAO().transferAccountBalance(transaction);
			if (updateCount == 2) {
				return Response.status(Response.Status.OK).build();
			} else {
				// transaction failed
				throw new WebApplicationException("Transaction failed", Response.Status.BAD_REQUEST);
			}

		} else {
			throw new WebApplicationException("Currency Code Invalid ", Response.Status.BAD_REQUEST);
		}

	}

}
