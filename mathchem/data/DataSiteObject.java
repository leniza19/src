package mathchem.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class DataSiteObject {

   private static DataSource dataSource;
   private static Object idLock = new Object();

   public static void setDataSource(DataSource dataSource) 
   {
      DataSiteObject.dataSource = dataSource;
   }

   protected static Connection getConnection() 
   {
      try {
         return dataSource.getConnection();
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }

   protected static void close(Statement statement, Connection connection) 
   {
      close(null, statement, connection);
   }

   protected static void close(ResultSet rs, Statement statement,
         Connection connection) 
   {
      try {
         if (rs != null)
            rs.close();
         if (statement != null)
            statement.close();
         if (connection != null)
            connection.close();
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }

   protected static Long getUniqueId() {
      ResultSet rs = null;
      PreparedStatement statement = null;
      Connection connection = null;
      try 
      {
         connection = getConnection();
         synchronized (idLock) 
         {
            statement = connection.prepareStatement("select next_value from sequence");
            rs = statement.executeQuery();
            rs.first();
            long id = rs.getLong(1);
            statement.close();
            statement = connection.prepareStatement("update sequence set next_value = ?");
            statement.setLong(1, id + 1);
            statement.executeUpdate();
            statement.close();
            return new Long(id);
         }
      }
      catch (SQLException e) 
      {
         throw new RuntimeException(e);
      }
      finally
      {
         close(rs, statement, connection);
      }
   }
}
