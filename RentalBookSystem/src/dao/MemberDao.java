package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import database.ConnectDatabase;
import dto.Member;

public class MemberDao {
	private Connection con;
	
	public MemberDao() {
		con = ConnectDatabase.makeConnection(); // �����ͺ��̽� ����
	}
	
	/* member ���� */
	public String insert(Member member) {
		try {
			String query = "insert into member (memberid, name, birth, phone, regdate) values (?,?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, member.getMemberid());
			stmt.setString(2, member.getName());
			stmt.setTimestamp(3, Timestamp.valueOf(member.getBirth()));
			stmt.setString(4, member.getPhone());
			stmt.setTimestamp(5, Timestamp.valueOf(member.getRegdate()));
			
			stmt.executeUpdate();
		} catch (Exception ex) {
			System.out.println("ȸ�� ��� ����");
		}
		
		return member.getMemberid();
	}
	
	/* member id �˻� */
	public Member selectById(String id) {
		try {
			String query = "select * from member where memberid = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, id);			
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Member member = new Member(
						rs.getString("memberid"),
						rs.getString("name"),
						rs.getTimestamp("birth").toLocalDateTime(),
						rs.getString("phone"),
						rs.getTimestamp("regdate").toLocalDateTime());
				return member;
			}
		} catch (Exception ex) {
			System.out.println("ȸ�� ID �˻� ����");
		}
		return null;
	}
	
	/* ��� member id ��ȯ */
	public ArrayList<String> selectAllMemberid() {
		ArrayList<String> result = new ArrayList<>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select memberid from member");
			
			while(rs.next()) result.add(rs.getString("memberid"));
			return result;
		} catch(Exception ex) {
			System.out.println("ȸ�� ID �ҷ����� ����");
		}
		return null;
	}
	
	/* member ��ȭ��ȣ �˻� --> �� ���� ȸ���� ���� ���� ���� �ڵ带 �߱��� �� ������ ��ȭ��ȣ Ȯ�� */
	public Member selectByPhone(String phone) {
		try {
			String query = "select * from member where phone = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, phone);			
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Member member = new Member(
						rs.getString("memberid"),
						rs.getString("name"),
						rs.getTimestamp("birth").toLocalDateTime(),
						rs.getString("phone"),
						rs.getTimestamp("regdate").toLocalDateTime());
				return member;
			}
		} catch (Exception ex) {
			System.out.println("ȸ�� Phone �˻� ����");
		}
		return null;
	}

}
