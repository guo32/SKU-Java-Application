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
	
	/* ���� ���� ���� */
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
			System.out.println("���� ���� ����");
		}
		return null;
	}
	
	/* ���� id �˻� */
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
			System.out.println("���� ID �˻� ����");
		}
		return null;
	}
	
	/* ���� ���� ���� */
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
			System.out.println("���� ���� ���� ����");
		}
		return false;
	}
	
	/* ���� �ÿ� ���� ���� ������ ���¸� ��ü������ ���� */
	public void updateAllStatus() {
		List<String> rentalIdList = new ArrayList<>();
		
		/* status�� 'N'�� ��� rental id �ҷ����� */
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select rentalid from rental where date(returndate) < date(now()) and status = 'N'");
			while(rs.next()) rentalIdList.add(rs.getString("rentalid"));
		} catch (Exception ex) {
			System.out.println("���� ID ��� �ҷ����� ����");
		}
		
		/* �̹ݳ� --> ��ü ���� ���� */
		for(String id : rentalIdList) updateStatus(id, false);
	}
	
	/* �ݳ����� ���� ���� ���� --> ����Ʈ�� �����͸� ���� �Ŀ� �迭�� ��ȯ�ϱ�� 
	 * 
	 * overdue == true --> ��ü�� ���� ����
	 * overdue == false --> �̹ݳ�/��ü ���� ����
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
			
			// �迭�� ��ȯ
			int size = rentalList.size();
			String[][] booksOnLoan = new String[size][6];
			for(int i = 0; i < size; i++) {
				Rental rental = rentalList.get(i);
				String status = "";
				if(rental.getStatus().equals("N")) status = "�̹ݳ�";
				else if(rental.getStatus().equals("D")) status = "��ü";
				String[] contents = {rental.getRentalid(), 
						rental.getBookid(), rental.getMemberid(),
						rental.getRentaldate().toString(), 
						rental.getReturndate().toString(),
						status};
				// ������ contents(���ڿ� �迭)�� ��Ҹ� ��� �迭�� ����
				for(int j = 0; j < contents.length; j++) booksOnLoan[i][j] = contents[j];
			}
			
			return booksOnLoan;
		} catch (Exception ex) {
			System.out.println("�̹ݳ� ��� �ҷ����� ����");
		}
		return null;
	}
	
	/* ���� ���� ���� ������ ����
	 * 
	 * hashmap
	 * count �ؼ� �����ڵ�, ���� ����
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
				System.out.println("���� ���� ���� ��ȯ ����");
			}
		}
		return result;
	}
	
	/* ȸ�� �� ���� Ƚ�� ������ ����
	 * 
	 * ��ü ȸ�� �ڵ带 ����ؼ�
	 * ȸ���ڵ� count, Ƚ�� ����
	 * */
	public List<Entry<String, Integer>> countRentalMember(ArrayList<String> memberIdList, boolean overdue) {
		HashMap<String, Integer> countData = new HashMap<>();
		
		for(String data : memberIdList) {
			String query = "";
			if(!overdue) query = "select count(*) from rental where memberid = ?"; // falas --> ��� ����
			else query = "select count(*) from rental where memberid = ? and status = 'D'"; // true --> ��ü ����
			try {
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(1, data);
				
				ResultSet rs = stmt.executeQuery();
				rs.next();
				countData.put(data, rs.getInt(1));
			} catch(Exception ex) {
				System.out.println("ȸ���� ���� Ƚ�� ��ȯ ����");
			}
		}		
		List<Entry<String, Integer>> result = new LinkedList<>(countData.entrySet());
		result.sort(Entry.comparingByValue()); // value�� �������� ����
		Collections.reverse(result); // ������������ ��ȯ
		
		return result;
	}
	
	/* ȸ�� �ڵ�� ���� ���� �˻� */
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
			System.out.println("ȸ���ڵ庰 ���� ���� �˻� ����");
		}
		return null;
	}
	
}
