package com.money.transfer.accounts;

import com.money.transfer.accounts.dao.DAOFactory;
import com.money.transfer.accounts.service.AccountInfoService;
import com.money.transfer.accounts.service.ServiceExceptionMapper;
import com.money.transfer.accounts.service.TransferFundService;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;


public class Application {

	private static Logger log = Logger.getLogger(Application.class);

	public static void main(String[] args) throws Exception {
		// Initialize H2 database with demo data
		log.info("Initialisation Start....");
		DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);
		h2DaoFactory.populateTestData();
		log.info("Initialisation Complete....");
		// Host service on jetty
		startService();
	}
    
	private static void startService() throws Exception {
		Server server = new Server(9090);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
		servletHolder.setInitParameter("jersey.config.server.provider.classnames",
				AccountInfoService.class.getCanonicalName() + ","
						+ ServiceExceptionMapper.class.getCanonicalName() + ","
						+ TransferFundService.class.getCanonicalName());
		try {
			server.start();
			server.join();
		} finally {
			server.destroy();
		}
	}

}
