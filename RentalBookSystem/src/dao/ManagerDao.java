package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import database.ConnectDatabase;
import dto.Manager;

public class ManagerDao {

	private Connection con;
	
	public ManagerDao() {
		con = ConnectDatabase.makeConnection(); // 데이터베이스 연결
	}
	
	/* manager 삽입 */
	public String insert(Manager manager) {
		try {
			String query = "insert into manager (managerid, password, name, regdate) values (?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, manager.getManagerid());
			stmt.setString(2, manager.getPassword());
			stmt.setString(3, manager.getName());
			stmt.setTimestamp(4, Timestamp.valueOf(manager.getRegdate()));			
			stmt.executeUpdate();
			
			return manager.getManagerid();
		} catch (Exception ex) {
			System.out.println("관리자 등록 실패");
		}
		return null;
	}
	
	/* id 검색 */
	public Manager selectById(String id) {
		try {
			String query = "select * from manager where managerid = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, id);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Manager manager = new Manager(
						rs.getString("managerid"),
						rs.getString("password"),
						rs.getString("name"),
						rs.getTimestamp("regdate").toLocalDateTime());
				return manager;
			}
		} catch (Exception ex) {
			System.out.println("관리자 ID 검색 실패");
		}
		return null;
	}
	
}
