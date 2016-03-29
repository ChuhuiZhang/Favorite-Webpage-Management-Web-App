package model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import databean.FavoriteBean;

/**
 * @author Chuhui Zhang (chuhuiz@andrew.cmu.edu)
 * Course number: 08-672
 * Dec 5, 2015
 */

public class FavoriteDAO {

	private List<Connection> connectionPool = new ArrayList<Connection>();

	private String jdbcDriver;
	private String jdbcURL;

	public FavoriteDAO(String jdbcDriver, String jdbcURL)
			throws MyDAOException {
		this.jdbcDriver = jdbcDriver;
		this.jdbcURL = jdbcURL;

		if (!tableExists()) {
			createTable();
		}
	}

	private synchronized Connection getConnection() throws MyDAOException {
		if (connectionPool.size() > 0) {
			return connectionPool.remove(connectionPool.size() - 1);
		}

		try {
			Class.forName(jdbcDriver);
		} catch (ClassNotFoundException e) {
			throw new MyDAOException(e);
		}

		try {
			return DriverManager.getConnection(jdbcURL);
		} catch (SQLException e) {
			throw new MyDAOException(e);
		}
	}

	private synchronized void releaseConnection(Connection con) {
		connectionPool.add(con);
	}

	public synchronized FavoriteBean[] getUserFavorite(int id) throws MyDAOException {

		Connection con = null;
		try {
			con = getConnection();

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from chuhuiz_favorite where `user id` =" + id + ";");

			List<FavoriteBean> list = new ArrayList<FavoriteBean>();

			while (rs.next()) {
				FavoriteBean temp = new FavoriteBean();
				temp.setURL(rs.getString("url"));
				temp.setComment(rs.getString("comment"));
				temp.setCount(rs.getInt("click count"));
				temp.setfavoriteId(rs.getInt("favorite id"));
				temp.setUserId(rs.getInt("user id"));
				list.add(temp);
			}

			rs.close();
			stmt.close();
			releaseConnection(con);

			return list.toArray(new FavoriteBean[list.size()]);

		} catch (SQLException e) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) {
			}
			throw new MyDAOException(e);
		}
	}

	public synchronized void create(FavoriteBean newUser) throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();

			PreparedStatement pstmt = con.prepareStatement("insert into chuhuiz_favorite (`user id`, url, comment, `click count`) "
					+ "values(?,?,?,?)");

			pstmt.setInt(1, newUser.getUserId());
			pstmt.setString(2, newUser.getURL());
			pstmt.setString(3, newUser.getComment());
			pstmt.setInt(4, newUser.getCount());
			int count = pstmt.executeUpdate();
			if (count != 1) {
				throw new SQLException("Insert updated " + count + " rows");
			}
			System.out.println(count + "Count row!!");

			pstmt.close();
			releaseConnection(con);

		} catch (Exception e) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) {
			}
			throw new MyDAOException(e);
		}

	}


	public int size() throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select `favorite id` from chuhuiz_favorite");

			rs.next();
			int count = rs.getInt("`favorite id`");

			stmt.close();
			releaseConnection(con);

			return count;

		} catch (SQLException e) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) {
			}
			throw new MyDAOException(e);
		}
	}

	public int searchFavoriteId(String url) throws MyDAOException{
		int favoriteId = -1;
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql:///test");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from chuhuiz_favorite where `url` = \"" + url + "\";");

			if (rs.next()) {
				favoriteId = Integer.parseInt(rs.getString("favorite id"));
			}

			stmt.close();
			releaseConnection(con);
		} catch (SQLException e) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) {
			}
			throw new MyDAOException(e);
		}

		return favoriteId;
	}

	private boolean tableExists() throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();
			DatabaseMetaData metaData = con.getMetaData();
			ResultSet rs = metaData.getTables(null, null, "chuhuiz_favorite", null);

			boolean answer = rs.next();

			rs.close();
			releaseConnection(con);

			return answer;

		} catch (SQLException e) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) {
			}
			throw new MyDAOException(e);
		}
	}

	public void createTable() throws MyDAOException {
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql:///test");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("create table chuhuiz_favorite (`favorite id` int not null auto_increment primary key,"
					+ "`user id` int not null, url char(100) not null,"
					+ "comment char(100) not null, `click count` int default '0');");
			stmt.executeUpdate("insert chuhuiz_favorite (`user id`, url, comment, `click count`)"
					+ "values (\"1\", \"www.google.com\", \"My favorite search engine\", \"0\");");
			stmt.executeUpdate("insert chuhuiz_favorite (`user id`, url, comment, `click count`)"
					+ "values (\"1\", \"www.cmu.edu\", \"My favorite education website\", \"0\");");
			stmt.executeUpdate("insert chuhuiz_favorite (`user id`, url, comment, `click count`)"
					+ "values (\"1\", \"www.facebook.com\", \"My favorite social media\", \"0\");");
			stmt.executeUpdate("insert chuhuiz_favorite (`user id`, url, comment, `click count`)"
					+ "values (\"1\", \"www.cnn.com\", \"My favorite news website\", \"0\");");

			stmt.executeUpdate("insert chuhuiz_favorite (`user id`, url, comment, `click count`)"
					+ "values (\"2\", \"www.youtube.com\", \"My favorite video website\", \"0\");");
			stmt.executeUpdate("insert chuhuiz_favorite (`user id`, url, comment, `click count`)"
					+ "values (\"2\", \"www.pitt.edu\", \"My favorite education website\", \"0\");");
			stmt.executeUpdate("insert chuhuiz_favorite (`user id`, url, comment, `click count`)"
					+ "values (\"2\", \"www.twitter.com\", \"My favorite social media\", \"0\");");
			stmt.executeUpdate("insert chuhuiz_favorite (`user id`, url, comment, `click count`)"
					+ "values (\"2\", \"www.nytimes.com\", \"My favorite news website\", \"0\");");

			stmt.executeUpdate("insert chuhuiz_favorite (`user id`, url, comment, `click count`)"
					+ "values (\"3\", \"www.bing.com\", \"My favorite search engine\", \"0\");");
			stmt.executeUpdate("insert chuhuiz_favorite (`user id`, url, comment, `click count`)"
					+ "values (\"3\", \"www.upenn.edu\", \"My favorite education website\", \"0\");");
			stmt.executeUpdate("insert chuhuiz_favorite (`user id`, url, comment, `click count`)"
					+ "values (\"3\", \"www.linkedin.com\", \"My favorite social media\", \"0\");");
			stmt.executeUpdate("insert chuhuiz_favorite (`user id`, url, comment, `click count`)"
					+ "values (\"3\", \"www.wsj.com\", \"My favorite news website\", \"0\");");
			stmt.close();
			releaseConnection(con);

		} catch (SQLException e) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) {
			}
			throw new MyDAOException(e);
		}
	}

	public void clickCount(int favoriteId) throws MyDAOException {
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql:///test");
			PreparedStatement pstmt = con.prepareStatement("update chuhuiz_favorite set `click count` = `click count` + 1 where `favorite id` = ?;");
			pstmt.setInt(1, favoriteId);
			int count = pstmt.executeUpdate();
			if (count != 1) {
				throw new SQLException("Insert updated " + count + " rows");
			}
			System.out.println(count + "Count row!!");

			pstmt.close();
			releaseConnection(con);

		} catch (Exception e) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) {
			}
			throw new MyDAOException(e);
		}

	}

	public void deleteEntry(String favoriteId) throws MyDAOException {
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql:///test");
			PreparedStatement pstmt = con.prepareStatement("delete from chuhuiz_favorite where `favorite id` = ?;");
			pstmt.setString(1, favoriteId);
			int count = pstmt.executeUpdate();
			if (count != 1) {
				throw new SQLException("Insert updated " + count + " rows");
			}
			System.out.println(count + "Count row!!");

			pstmt.close();
			releaseConnection(con);

		} catch (Exception e) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) {
			}
			throw new MyDAOException(e);
		}
	}

}

