package mathchem.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserDAO extends DataSiteObject {
	private User read(ResultSet rs) throws SQLException {
		   Long id = new Long(rs.getLong("id"));
		   String username = rs.getString("username");
		   String password = rs.getString("password");
		   Long permission = new Long(rs.getLong("permission"));
		   User user = new User();
		   user.setId(id);
		   user.setUsername(username);
		   user.setPassword(password);		  
		   user.setPermission(permission);
		   return user;
		}
	
	public List<User> findAll() {
		LinkedList<User> userItems = new LinkedList<User>();
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			
			String sql = "SELECT id, username, password, permission "+
			" FROM webuser order by id";				
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				User userItem = read(rs);
				userItems.add(userItem);
			}
			return userItems;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(rs, statement, connection);
		}
	}
	
	public User findById (String id) {		
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			
			String sql = "SELECT id, username, password, permission "+
			" FROM webuser where id="+id;				
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			if (!rs.next()) {
				return null;
			}
			return read(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(rs, statement, connection);
		}
	}
	
	public User findByUsername(String username) {
		   ResultSet rs = null;
		   PreparedStatement statement = null;
		   Connection connection = null;
		   try {
		      connection = getConnection();
		      String sql = "SELECT id, username, password,permission FROM webuser" +
		      		" where username=?";
		      statement = connection.prepareStatement(sql);
		      statement.setString(1, username);
		      rs = statement.executeQuery();
		      if (!rs.next()) {
		         return null;
		      }
		      return read(rs);
		   }
		   catch (SQLException e) {
		      throw new RuntimeException(e);
		   }
		   finally {
		      close(rs, statement, connection);
		   }
		}
	
	public Long getNewId() {
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String sql = "select max(id)+1 " +
					"from webuser";

			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			if (!rs.next()) {
				return null;
			}
			return rs.getLong(1);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(rs, statement, connection);
		}		
	}
}
