package mathchem.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class NewsItemDAO extends DataSiteObject {

	int newsOnPageNumber = 5;

	private NewsItem read(ResultSet rs) throws SQLException {
		Long id = new Long(rs.getLong("id"));
		String title = rs.getString("title");
		String content = rs.getString("content");
		String newsDate = rs.getString("news_date");

		NewsItem newsItem = new NewsItem();
		newsItem.setId(id);
		newsItem.setTitle(title);
		newsItem.setContent(content);
		newsItem.setNewsDate(newsDate);
		return newsItem;
	}

	public List<NewsItem> findAll(String rownum) {
		LinkedList<NewsItem> newsItems = new LinkedList<NewsItem>();
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String tempSql = "row_number() over(order by news_date desc,id desc)+"
					+ String.valueOf(newsOnPageNumber - 1) + ")/"
					+ String.valueOf(newsOnPageNumber);
			String sql = "select * from (" + "select (" + tempSql
					+ " as rownum,id, title, content, news_date "
					+ "FROM news_item order by news_date desc) t "
					+ "where rownum=" + rownum;
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				NewsItem newsItem = read(rs);
				newsItems.add(newsItem);
			}
			return newsItems;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(rs, statement, connection);
		}
	}

	public NewsItem find(String id) {
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String sql = "select * from news_item where id=" + id;
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

	public Long getPageNumber() { // возвращает количество групп
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String sql = "select (count(*)-1)/" + newsOnPageNumber
					+ "+1 from news_item";

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

	public void update(NewsItem newsItem) {
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String sql = "update news_item set " + "title=?, content=?, "
					+ "news_date ='" + newsItem.getNewsDate() + "' where id=?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, newsItem.getTitle());
			statement.setString(2, newsItem.getContent());
			statement.setLong(3, newsItem.getId().longValue());
			statement.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement, connection);
		}
	}

	public Long getNewId() {
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String sql = "select max(id)+1 " +
					"from news_item";

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
	
	public void insert(NewsItem newsItem) {
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String sql = "insert into news_item(id, title, content, news_date) values "
					+ " (?,?,?,'" + newsItem.getNewsDate() + "')";
			System.out.println(sql);
			
			statement = connection.prepareStatement(sql);
			statement.setLong(1, newsItem.getId().longValue());
			statement.setString(2, newsItem.getTitle());
			statement.setString(3, newsItem.getContent());			
			statement.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement, connection);
		}
	}
	
	public void delete(Long id) {
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String sql = "delete from news_item where id= "
					+ id.toString();
			
			statement = connection.prepareStatement(sql);						
			statement.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(statement, connection);
		}
	}
}
