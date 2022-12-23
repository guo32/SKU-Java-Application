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
		con = ConnectDatabase.makeConnection(); // 데이터베이스 연결
	}
	
	/* member 삽입 */
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
			System.out.println("회원 등록 실패");
		}
		
		return member.getMemberid();
	}
	
	/* member id 검색 */
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
			System.out.println("회원 ID 검색 실패");
		}
		return null;
	}
	
	/* 모든 member id 반환 */
	public ArrayList<String> selectAllMemberid() {
		ArrayList<String> result = new ArrayList<>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select memberid from member");
			
			while(rs.next()) result.add(rs.getString("memberid"));
			return result;
		} catch(Exception ex) {
			System.out.println("회원 ID 불러오기 실패");
		}
		return null;
	}
	
	/* member 전화번호 검색 --> 한 명의 회원이 여러 개의 인증 코드를 발급할 수 없도록 전화번호 확인 */
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
			System.out.println("회원 Phone 검색 실패");
		}
		return null;
	}

}
