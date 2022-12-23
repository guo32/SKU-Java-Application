package database;
import java.sql.*;

/*
 * 데이터베이스 연결
 */
public class ConnectDatabase {
	
	public static Connection makeConnection() {
		String url = "jdbc:mysql://localhost:3306/rentalbookdb";
		String user = "root";
		String password = "1234";
		Connection con = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
		} catch(ClassNotFoundException ex) {
			System.out.println("드라이버를 찾을 수 없습니다.");
		} catch(SQLException ex) {
			System.out.println("데이터베이스 연결에 실패했습니다.");
		}
		
		return con;
	}

}
