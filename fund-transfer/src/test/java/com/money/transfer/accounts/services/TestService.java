package com.money.transfer.accounts.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.money.transfer.accounts.dao.DAOFactory;
import com.money.transfer.accounts.service.AccountInfoService;
import com.money.transfer.accounts.service.ServiceExceptionMapper;
import com.money.transfer.accounts.service.TransferFundService;

import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;


public abstract class TestService {
    protected static Server server = null;
    protected static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

    protected static HttpClient client ;
    protected static DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);
    protected ObjectMapper mapper = new ObjectMapper();
    protected URIBuilder builder = new URIBuilder().setScheme("http").setHost("localhost:8084");


    @BeforeClass
    public static void setup() throws Exception {
        h2DaoFactory.populateTestData();
        startServer();
        connManager.setDefaultMaxPerRoute(100);
        connManager.setMaxTotal(200);
        client= HttpClients.custom()
                .setConnectionManager(connManager)
                .setConnectionManagerShared(true)
                .build();

    }

    @AfterClass
    public static void closeClient() throws Exception {
        //server.stop();
        HttpClientUtils.closeQuietly(client);
    }


    private static void startServer() throws Exception {
        if (server == null) {
            server = new Server(8084);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
            servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                            AccountInfoService.class.getCanonicalName() + "," +
                            ServiceExceptionMapper.class.getCanonicalName() + "," +
                            TransferFundService.class.getCanonicalName());
            server.start();
        }
    }
}
