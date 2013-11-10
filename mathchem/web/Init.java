package mathchem.web;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import mathchem.data.DataAccessObject;
import mathchem.data.DataSiteObject;

public class Init implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
    
    private void contextInitialized2(ServletContext servletContext)
    throws Exception {
       InitialContext enc = new InitialContext();
       Context compContext = (Context) enc.lookup("java:comp/env");
       DataSource dataSource = (DataSource) compContext.lookup("chemdatasource");
       DataAccessObject.setDataSource(dataSource);
       DataSource dataSiteSource = (DataSource) compContext.lookup("sitedatasource");
       DataSiteObject.setDataSource(dataSiteSource);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        try {
           contextInitialized2(servletContext);
        }
        catch (Exception e)
        {
//           logger.error("Initialization failed.", e);
        	System.out.println("Initialization failed");
           throw new RuntimeException(e);
        }
        System.out.println("Initialization succeeded");
//        logger.debug("Initialization succeeded.");
    }


}
