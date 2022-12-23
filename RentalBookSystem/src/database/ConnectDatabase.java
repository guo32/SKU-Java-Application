package database;
import java.sql.*;

/*
 * �����ͺ��̽� ����
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
			System.out.println("����̹��� ã�� �� �����ϴ�.");
		} catch(SQLException ex) {
			System.out.println("�����ͺ��̽� ���ῡ �����߽��ϴ�.");
		}
		
		return con;
	}

}
