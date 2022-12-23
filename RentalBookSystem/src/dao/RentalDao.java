package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import database.ConnectDatabase;
import dto.Rental;

public class RentalDao {
	private Connection con;
	
	public RentalDao() {
		con = ConnectDatabase.makeConnection();
	}
	
	/* 대출 정보 삽입 */
	public Rental insert(Rental rental) {
		try {
			String query = "insert into rental (rentalid, bookid, memberid, rentaldate, returndate, status) values (?,?,?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, rental.getRentalid());
			stmt.setString(2, rental.getBookid());
			stmt.setString(3, rental.getMemberid());
			stmt.setTimestamp(4, Timestamp.valueOf(rental.getRentaldate()));
			stmt.setTimestamp(5, Timestamp.valueOf(rental.getReturndate()));
			stmt.setString(6, rental.getStatus());			
			stmt.executeUpdate();
			
			return rental;
		} catch (Exception ex) {
			System.out.println("도서 대출 실패");
		}
		return null;
	}
	
	/* 대출 id 검색 */
	public Rental selectById(String id) {
		try {
			String query = "select * from rental where rentalid = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, id);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Rental rental = new Rental(
						rs.getString("rentalid"),
						rs.getString("bookid"),
						rs.getString("memberid"),
						rs.getTimestamp("rentaldate").toLocalDateTime(),
						rs.getTimestamp("returndate").toLocalDateTime(),
						rs.getString("status"));
				return rental;
			}
		} catch (Exception ex) {
			System.out.println("대출 ID 검색 실패");
		}
		return null;
	}
	
	/* 대출 상태 변경 */
	public boolean updateStatus(String id, boolean act) {
		try {
			Rental rental = selectById(id);
			if(rental != null) {
				if(rental.getStatus().equals("Y")) return false;
				
				String query = "update rental set status = ? where rentalid = ?";
				PreparedStatement stmt = con.prepareStatement(query);
								
				if(!rental.getStatus().equals("Y") && act == true) stmt.setString(1, "Y");
				if(rental.getStatus().equals("N") && act == false && !rental.getReturndate().isAfter(LocalDateTime.now())) stmt.setString(1, "D");
				stmt.setString(2, rental.getRentalid());
				stmt.executeUpdate();
				
				return true;
			}
		} catch (Exception ex) {
			System.out.println("대출 상태 변경 실패");
		}
		return false;
	}
	
	/* 실행 시에 대출 중인 도서의 상태를 전체적으로 갱신 */
	public void updateAllStatus() {
		List<String> rentalIdList = new ArrayList<>();
		
		/* status가 'N'인 모든 rental id 불러오기 */
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select rentalid from rental where date(returndate) < date(now()) and status = 'N'");
			while(rs.next()) rentalIdList.add(rs.getString("rentalid"));
		} catch (Exception ex) {
			System.out.println("대출 ID 목록 불러오기 실패");
		}
		
		/* 미반납 --> 연체 상태 변경 */
		for(String id : rentalIdList) updateStatus(id, false);
	}
	
	/* 반납되지 않은 대출 정보 --> 리스트로 데이터를 받은 후에 배열로 변환하기로 
	 * 
	 * overdue == true --> 연체된 대출 정보
	 * overdue == false --> 미반납/연체 대출 정보
	 * */
	public String[][] selectBooksOnLoan(boolean overdue) {
		List<Rental> rentalList = new ArrayList<>();
		
		try {
			Statement stmt = con.createStatement();
			
			ResultSet rs = null;
			if(!overdue) rs = stmt.executeQuery("select * from rental where status != 'Y'");
			else if(overdue) rs = stmt.executeQuery("select * from rental where status = 'D'");
			
			while(rs.next()) {
				Rental rental = new Rental(
						rs.getString("rentalid"),
						rs.getString("bookid"),
						rs.getString("memberid"),
						rs.getTimestamp("rentaldate").toLocalDateTime(),
						rs.getTimestamp("returndate").toLocalDateTime(),
						rs.getString("status"));
				rentalList.add(rental);
			}
			
			// 배열로 변환
			int size = rentalList.size();
			String[][] booksOnLoan = new String[size][6];
			for(int i = 0; i < size; i++) {
				Rental rental = rentalList.get(i);
				String status = "";
				if(rental.getStatus().equals("N")) status = "미반납";
				else if(rental.getStatus().equals("D")) status = "연체";
				String[] contents = {rental.getRentalid(), 
						rental.getBookid(), rental.getMemberid(),
						rental.getRentaldate().toString(), 
						rental.getReturndate().toString(),
						status};
				// 생성한 contents(문자열 배열)의 요소를 결과 배열에 기입
				for(int j = 0; j < contents.length; j++) booksOnLoan[i][j] = contents[j];
			}
			
			return booksOnLoan;
		} catch (Exception ex) {
			System.out.println("미반납 목록 불러오기 실패");
		}
		return null;
	}
	
	/* 대출 도서 개수 추출을 위해
	 * 
	 * hashmap
	 * count 해서 도서코드, 개수 저장
	 * */
	public HashMap<String, Integer> countRentaledBook(ArrayList<String> bookIdList) {
		HashMap<String, Integer> result = new HashMap<>();
		
		for(String data : bookIdList) {
			String query = "select count(*) from rental where bookid = ?";
			try {
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(1, data);
				
				ResultSet rs = stmt.executeQuery();
				rs.next();
				result.put(data, rs.getInt(1));
			} catch(Exception ex) {
				System.out.println("대출 도서 개수 반환 실패");
			}
		}
		return result;
	}
	
	/* 회원 별 대출 횟수 추출을 위해
	 * 
	 * 전체 회원 코드를 사용해서
	 * 회원코드 count, 횟수 저장
	 * */
	public List<Entry<String, Integer>> countRentalMember(ArrayList<String> memberIdList, boolean overdue) {
		HashMap<String, Integer> countData = new HashMap<>();
		
		for(String data : memberIdList) {
			String query = "";
			if(!overdue) query = "select count(*) from rental where memberid = ?"; // falas --> 사용 내역
			else query = "select count(*) from rental where memberid = ? and status = 'D'"; // true --> 연체 내역
			try {
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(1, data);
				
				ResultSet rs = stmt.executeQuery();
				rs.next();
				countData.put(data, rs.getInt(1));
			} catch(Exception ex) {
				System.out.println("회원별 대출 횟수 반환 실패");
			}
		}		
		List<Entry<String, Integer>> result = new LinkedList<>(countData.entrySet());
		result.sort(Entry.comparingByValue()); // value로 오름차순 정렬
		Collections.reverse(result); // 내림차순으로 변환
		
		return result;
	}
	
	/* 회원 코드로 대출 내역 검색 */
	public List<Rental> selectByMemberId(String memberid) {
		List<Rental> rentalList = new ArrayList<>();
		try {
			String query = "select * from rental where memberid = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, memberid);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Rental rental = new Rental(
						rs.getString("rentalid"),
						rs.getString("bookid"),
						rs.getString("memberid"),
						rs.getTimestamp("rentaldate").toLocalDateTime(),
						rs.getTimestamp("returndate").toLocalDateTime(),
						rs.getString("status"));
				rentalList.add(rental);
			}
			
			return rentalList;
		} catch(Exception ex) {
			System.out.println("회원코드별 대출 내역 검색 실패");
		}
		return null;
	}
	
}
