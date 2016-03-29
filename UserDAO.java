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

import databean.UserBean;

/**
 * @author Chuhui Zhang (chuhuiz@andrew.cmu.edu)
 * Course number: 08-672
 * Dec 5, 2015
 */
public class UserDAO {
	private List<Connection> connectionPool = new ArrayList<Connection>();

	private String jdbcDriver;
	private String jdbcURL;

	public UserDAO(String jdbcDriver, String jdbcURL)
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

	public UserBean read(String userEmail) throws MyDAOException {

		Connection con = null;
		try {
			con = getConnection();

			PreparedStatement pstmt = con.prepareStatement("select * from `chuhuiz_user` where `e-mail address` = ?;");
			pstmt.setString(1, userEmail);
			ResultSet rs = pstmt.executeQuery();

			UserBean user;
			if (!rs.next()) {
				user = null;
			} else {
				user = new UserBean();
				user.setEmail(rs.getString("e-mail address"));
				user.setPassword(rs.getString("user's password"));
				user.setFirstName(rs.getString("user's first name"));
				user.setLastName(rs.getString("user's last name"));
				user.setUserId(rs.getInt("user id"));
			}

			rs.close();
			pstmt.close();
			releaseConnection(con);
			return user;

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


	public void create(UserBean newUser) throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement("insert into chuhuiz_user (`e-mail address`, `user's first name`, `user's last name`, `user's password`) "
					+ "values(?,?,?,?) ");

			pstmt.setString(1, newUser.getEmail());
			pstmt.setString(2, newUser.getFirstName());
			pstmt.setString(3, newUser.getLastName());
			pstmt.setString(4, newUser.getPassword());

			int count = pstmt.executeUpdate();
			if (count != 1) {
				throw new SQLException("Insert updated " + count + " rows");
			}

			pstmt.close();
			releaseConnection(con);

		} catch (Exception e) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) { /* ignore */
			}
			throw new MyDAOException(e);
		}
	}

	private boolean tableExists() throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();
			DatabaseMetaData metaData = con.getMetaData();
			ResultSet rs = metaData.getTables(null, null, "chuhuiz_user", null);

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
			stmt.executeUpdate("create table `chuhuiz_user` (`user id` int not null auto_increment primary key,"
					+ "`e-mail address` char(100) not null, `user's first name` char(100) not null, `user's last name` char(100) not null,"
					+ "`user's password` char(100) not null)");
			stmt.executeUpdate("insert into chuhuiz_user (`e-mail address`, `user's first name`, `user's last name`, `user's password`)"
					+"values (\"chuhui0812@andrew.cmu.edu\", \"Chuhui\", \"Zhang\", \"123\");");
			stmt.executeUpdate("insert into chuhuiz_user (`e-mail address`, `user's first name`, `user's last name`, `user's password`)"
					+"values (\"chuhui0812@gmail.com\", \"John\", \"Smith\", \"1234\")");
			stmt.executeUpdate("insert into chuhuiz_user (`e-mail address`, `user's first name`, `user's last name`, `user's password`)"
					+"values (\"chz0720@gmail.com\", \"Bryan\", \"Park\", \"12345\")");

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

	public int searchId(String email) throws MyDAOException {
		Connection con = null;
		int userId = -1;
		try {
			con = DriverManager.getConnection("jdbc:mysql:///test");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from chuhuiz_user where `e-mail address` = \"" + email + "\";");

			if (rs.next()) {
				userId = rs.getInt("user id");
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
		return userId;
	}

	public UserBean[] getList() throws MyDAOException {
		Connection con = null;
		ArrayList<UserBean> userList = new ArrayList<UserBean>();
		try {
			con = DriverManager.getConnection("jdbc:mysql:///test");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from chuhuiz_user;");

			while (rs.next()) {
				UserBean user = new UserBean();
				user.setEmail(rs.getString("e-mail address"));
				user.setPassword(rs.getString("user's password"));
				user.setFirstName(rs.getString("user's first name"));
				user.setLastName(rs.getString("user's last name"));
				user.setUserId(rs.getInt("user id"));
				userList.add(user);
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

		return userList.toArray(new UserBean[userList.size()]);
	}

	public void changePassword(int userId, String newPassword) throws MyDAOException{
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql:///test");
			PreparedStatement pstmt = con.prepareStatement("update chuhuiz_user set `user's password` =? where `user id`=?;");
			pstmt.setString(1, newPassword);
			pstmt.setInt(2, userId);
			int count = pstmt.executeUpdate();
			if (count != 1) {
				throw new SQLException("Insert updated " + count + " rows");
			}
			System.out.println(count + "Count row!!");

			pstmt.close();
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
}

