package mathchem.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ArticleItemDAO extends DataSiteObject {
	
	private ArticleItem read(ResultSet rs) throws SQLException {
		   Long id = new Long(rs.getLong("id"));
		   String title = rs.getString("title");
		   String url = rs.getString("url");
		   ArticleItem articleItem = new ArticleItem();
		   articleItem.setId(id);
		   articleItem.setTitle(title);
		   articleItem.setUrl(url);
		   return articleItem;
		}
	
	public List<ArticleItem> findAll() {
		   LinkedList<ArticleItem> articleItems = new LinkedList<ArticleItem>();
		   ResultSet rs = null;
		   PreparedStatement statement = null;
		   Connection connection = null;
		   try {
		      connection = getConnection();
		      String sql = "select * from article_item order by id";
		      statement = connection.prepareStatement(sql);
		      rs = statement.executeQuery();
		      while (rs.next()) {
		    	  ArticleItem articleItem = read(rs);
		    	  articleItems.add(articleItem);
		      }
		      return articleItems;
		   }
		   catch (SQLException e) {
		      throw new RuntimeException(e);
		   } 
		   finally {
		      close(rs, statement, connection);
		   }
		}
	
	public ArticleItem find(Long id) {
		   ResultSet rs = null;
		   PreparedStatement statement = null;
		   Connection connection = null;
		   try {
		      connection = getConnection();
		      String sql = "select * from article_item where id=?";
		      statement = connection.prepareStatement(sql);
		      statement.setLong(1, id.longValue());
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

}
