import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MyDbTest {
	
	public static final String URL = "jdbc:mysql://localhost:3306/test";
	public static final String USER = "root";
	public static final String PWD = "1Q2w3e4r5t6y7";
	public static final String SQL = "insert into people(id, name, age) values (?,?,?);";
	public static final String SQL2= "update people set age = 0 where id > ?";
	public static final String SQL3= "update people set age = ? where id = ?";
	public static final String SQL4= "select * from people";
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PWD);
	}
	
	
	public static PreparedStatement getStatement(Connection conn, String sql) throws SQLException {
		return conn.prepareStatement(sql);
	}
	
	
	public static List<People> query(String sql, Object[] params) {
		
		List<People> rlt = null;
		
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnection();
			ps = getStatement(conn, sql);
			
			int i = 1;
			for (Object object : params) {
				ps.setObject(i, object);
				i++;
			}
			
			rs = ps.executeQuery();
			rlt = initPeople(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
	            if (ps != null) {
	            	ps.close();
	            }
	            if (conn != null) {
	            	conn.close();
	            }
	        } catch (SQLException ex) {
	        	ex.printStackTrace();
	        }
		}
		
		return rlt;
	}
	
	
	public static int[] updateBatch(String sql, List<Object[]> params) {
		int[] rlt = null;
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			ps = getStatement(conn, sql);
			
			
			for (Object[] param : params) {
				int i = 1;
				for (Object ojb : param) {
					ps.setObject(i, ojb);
					i++;
				}
				ps.addBatch();
			}
			
			rlt = ps.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
	            if (ps != null) {
	            	ps.close();
	            }
	            if (conn != null) {
	            	conn.close();
	            }
	        } catch (SQLException ex) {
	        	ex.printStackTrace();
	        }
		}
		
		return rlt;
	}
	
	
	public static int update(String sql, Object[] params) {
		
		int rlt = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnection();
			ps = getStatement(conn, sql);
			
			int i = 1;
			for (Object object : params) {
				ps.setObject(i, object);
				i++;
				
			}
			
			rlt = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
	            if (ps != null) {
	            	ps.close();
	            }
	            if (conn != null) {
	            	conn.close();
	            }
	        } catch (SQLException ex) {
	        	ex.printStackTrace();
	        }
		}
		
		return rlt;
	}
	
	
	public static List<People> initPeople(ResultSet rs) throws SQLException {
		List<People> rltList = new ArrayList<People>();
		
		while (rs.next()) {
			People rlt = new People();
			
			rlt.setId(rs.getInt("id"));
			rlt.setName(rs.getString("name"));
			rlt.setAge(rs.getInt("age"));
			
			rltList.add(rlt);
		}
		
		return rltList;
	}
	
	
	public static class People {
		private Integer id;
		private String name;
		private Integer age;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		public String toString() {
			return "[id = " + id + "], [name = " + name + "], [age = " + age + "]\n";
		}
	}
	
	
	public static void main(String[] args) {
		//int rlt = update(SQL, new Object[]{5, "孙业萍", 62});
		
		//int rlt = update(SQL2, new Object[]{3});
		
		//System.out.println(rlt);
		
		
		/*List<Object[]> params = new ArrayList<Object[]>();
		
		params.add(new Object[]{63, 4});
		params.add(new Object[]{62, 5});
		
		int[] rlt = updateBatch(SQL3, params);
		
		System.out.println(rlt[0] + ":" +  rlt[1]);*/
		
		ResultSet rs = null;
		
		List<People> peoples = MyDbTest.query(SQL4, new Object[]{});
		
		System.out.println(peoples);
	}
}
