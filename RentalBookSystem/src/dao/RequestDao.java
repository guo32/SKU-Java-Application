package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import database.ConnectDatabase;
import dto.Request;

public class RequestDao {
	private Connection con;	
	
	public RequestDao() {
		con = ConnectDatabase.makeConnection();
	}
	
	/* 신청 정보 삽입 */
	public void insert(Request request) {
		try {
			String query = "insert into request (requestid, booktitle, author, publisher, memberid, reqdate, status) values (?,?,?,?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, request.getRequestid());
			stmt.setString(2, request.getBooktitle());
			stmt.setString(3, request.getAuthor());
			stmt.setString(4, request.getPublisher());
			stmt.setString(5, request.getMemberid());
			stmt.setTimestamp(6, Timestamp.valueOf(request.getReqdate()));
			stmt.setString(7, request.getStatus());
			
			stmt.executeUpdate();
		} catch (Exception ex) {
			System.out.println("도서 신청 실패");
		}
	}
	
	/* 신청된 도서의 입고 상태 변경 */
	public void updateStatus(int id) {
		Request request = selectById(id);
		if(request != null) {
			try {
				String query = "update request set status = 'Y' where requestid = ?"; // 입고된 도서의 상태를 'Y'로 변경
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setInt(1, id);
				
				stmt.executeUpdate();
			} catch (Exception ex) {
				System.out.println("신청 상태 갱신 실패");
			}
		} else {
			System.out.println("존재하지 않는 신청 아이디");
		}
	}
	
	/* 신청 정보 전체 불러오기 */
	public List<Request> selectAll() {
		List<Request> requestList = new ArrayList<>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from request");
			while(rs.next()) {
				Request request = new Request(rs.getInt("requestid"),
						rs.getString("booktitle"),
						rs.getString("author"),
						rs.getString("publisher"),
						rs.getString("memberid"),
						rs.getTimestamp("reqdate").toLocalDateTime(),
						rs.getString("status"));
				requestList.add(request);
			}
		} catch (Exception ex) {
			System.out.println("데이터 불러오기 실패");
		}
		
		return requestList;
	}
	
	/* 전체 신청 정보 2차원 배열로 변환 */
	public String[][] selectAllArray() {
		List<Request> requestList = selectAll();
		int size = requestList.size();
		
		String[][] results = new String[size][8];
		
		for(int i = 0; i < size; i++) {
			Request request = requestList.get(i);
			String[] contents = {Integer.toString(request.getRequestid()), request.getBooktitle(), request.getAuthor(), request.getPublisher(), request.getMemberid(), request.getReqdate().toString(), request.getStatus(), ""};
			// 상태가 N인 경우 입고 활성화
			if(request.getStatus().equals("N")) contents[7] = "입고";
			
			for(int j = 0; j < contents.length; j++) {
				results[i][j] = contents[j];
			}
		}
		
		return results;
	}
	
	/* 신청 아이디 검색 */
	public Request selectById(int id) {
		try {
			String query = "select * from request where requestid = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Request request = new Request(rs.getInt("requestid"),
						rs.getString("booktitle"),
						rs.getString("author"),
						rs.getString("publisher"),
						rs.getString("memberid"),
						rs.getTimestamp("reqdate").toLocalDateTime(),
						rs.getString("status"));
				return request;
			}
		} catch (Exception ex) {
			System.out.println("신청 ID 검색 실패");
		}
		return null;
	}
	
	/* 회원 별 도서 신청 횟수 추출
	 * 
	 */
	public HashMap<String, Integer> countRequestBookMember(ArrayList<String> memberIdList) {
		HashMap<String, Integer> countData = new HashMap<>();
		
		for(String data : memberIdList) {
			String query = "select count(*) from request where memberid = ?";
			try {
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(1, data);
				
				ResultSet rs = stmt.executeQuery();
				rs.next();
				countData.put(data, rs.getInt(1));
			} catch(Exception ex) {
				System.out.println("회원별 도서 신청 횟수 반환 실패");
			}
		}
		
		return countData;
	}

}
