package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import database.ConnectDatabase;
import dto.Book;

public class BookDao {
	private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss");
	private Connection con;
	
	public BookDao() {
		con = ConnectDatabase.makeConnection(); // �����ͺ��̽� ����
	}
	
	/* ���� ���� */
	public void insert(Book book) {
		try {
			String query = "insert into book (bookid, category, title, author, publisher, pubdate, possession, loanable, recdate) values (?,?,?,?,?,?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, book.getBookid());
			stmt.setString(2, book.getCategory());
			stmt.setString(3, book.getTitle());
			stmt.setString(4, book.getAuthor());
			stmt.setString(5, book.getPublisher());
			stmt.setTimestamp(6, Timestamp.valueOf(book.getPubdate()));
			stmt.setInt(7, book.getPossession());
			stmt.setInt(8, book.getLoanable());
			stmt.setTimestamp(9, Timestamp.valueOf(book.getRecdate()));
			
			stmt.executeUpdate();
		} catch (Exception ex) {
			System.out.println("���� �԰� ����");
		}
	}
	
	/* ���� id �˻��ϱ� */
	public Book selectById(String id) {
		try {
			String query = "select * from book where bookid = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, id);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Book book = new Book(rs.getString("bookid"),
						rs.getString("category"),
						rs.getString("title"),
						rs.getString("author"),
						rs.getString("publisher"),
						LocalDateTime.parse(rs.getString("pubdate"), format),
						rs.getInt("possession"),
						rs.getInt("loanable"),
						LocalDateTime.parse(rs.getString("recdate"), format));
				return book;
			}
		} catch (Exception ex) {
			System.out.println("���� ID �˻� ����");
		}
		return null;
	}
	
	/* ��� �����ڵ� ��ȯ */
	public ArrayList<String> selectAllBookid() {
		ArrayList<String> result = new ArrayList<>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select bookid from book");
			
			while(rs.next()) result.add(rs.getString("bookid"));
			return result;
		} catch(Exception ex) {
			System.out.println("�����ڵ� �ҷ����� ����");
		}
		return null;
	}
	
	/* �뿩 ���� �� �� ���� */
	public void updateLoanable(String id, boolean act) {
		try {
			Book book = selectById(id);
			if(book != null) {
				String query = "update book set loanable = ? where bookid = ?";
				PreparedStatement stmt = con.prepareStatement(query);
				
				/* ���� �� : act == true | �ݳ� �� : act == false */
				if(act)	stmt.setInt(1, book.getLoanable() - 1);
				else stmt.setInt(1, book.getLoanable() + 1);
				
				stmt.setString(2, book.getBookid());
				stmt.executeUpdate();
			}
		} catch (Exception ex) {
			System.out.println("���� �뿩 ���� �� �� ���� ����");
		}
	}
	
	/* ���� ���� ��ü �ҷ����� */
	public List<Book> selectAll() {
		List<Book> bookList = new ArrayList<>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from book");
			while(rs.next()) {
				Book book = new Book(rs.getString("bookid"),
						rs.getString("category"),
						rs.getString("title"),
						rs.getString("author"),
						rs.getString("publisher"),
						LocalDateTime.parse(rs.getString("pubdate"), format),
						rs.getInt("possession"),
						rs.getInt("loanable"),
						LocalDateTime.parse(rs.getString("recdate"), format));
				bookList.add(book);
			}
		} catch (Exception ex) {
			System.out.println("������ �ҷ����� ����");
		}		
		
		return bookList;
	}
	
	/* ��ü �������� 2���� �迭�� ��ȯ �� ��ȯ */
	public String[][] selectAllArray() {
		List<Book> bookList = selectAll();
		int size = bookList.size();
		
		String[][] results = new String[size][10];
		
		for(int i = 0; i < size; i++) {
			Book book = bookList.get(i);
			String[] contents = { book.getBookid(), book.getCategory(), book.getTitle(), book.getAuthor(),
					book.getPublisher(), String.valueOf(book.getPubdate()), String.valueOf(book.getPossession()), 
					String.valueOf(book.getLoanable()), String.valueOf(book.getRecdate()), ""};
			// �뿩 ���� �� ���� 0���� ū ��츸 ��� '����' ����
			if(book.getLoanable() > 0) contents[9] = "����";
			
			for(int j = 0; j < contents.length; j++) {
				results[i][j] = contents[j];
			}
		}
		
		return results;
	}
	
	/* �˻� Ű����� ���� ���� �����ϱ� */
	public List<Book> selectBySearch(String search) {
		List<Book> bookList = new ArrayList<>();
		
		// ī�װ��� �˻��� �� �ְ� ���� --- 22.12.21 (Ű���� �˻��̶� ī�װ� �˻��� ���� �����ϴ� �� ���� ��)
		Connection con = ConnectDatabase.makeConnection();
		String query = "select * from book where category like ? or title like ? or author like ? or publisher like ?";
		
		try {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, "%" + search + "%");
			stmt.setString(2, "%" + search + "%");
			stmt.setString(3, "%" + search + "%");
			stmt.setString(4, "%" + search + "%");

			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Book book = new Book(rs.getString("bookid"),
						rs.getString("category"),
						rs.getString("title"),
						rs.getString("author"),
						rs.getString("publisher"),
						LocalDateTime.parse(rs.getString("pubdate"), format),
						rs.getInt("possession"),
						rs.getInt("loanable"),
						LocalDateTime.parse(rs.getString("recdate"), format));
				bookList.add(book);
			}
		} catch (Exception ex) {
			System.out.println("������ �ҷ����� ����");
		}		
		
		return bookList;
	}
	
	public String[][] selectBySearchArray(String search) {
		List<Book> bookList = selectBySearch(search);
		int size = bookList.size();
		
		String[][] results = new String[size][10];
		
		for(int i = 0; i < size; i++) {
			Book book = bookList.get(i);
			String[] contents = { book.getBookid(), book.getCategory(), book.getTitle(), book.getAuthor(),
					book.getPublisher(), String.valueOf(book.getPubdate()), String.valueOf(book.getPossession()), 
					String.valueOf(book.getLoanable()), String.valueOf(book.getRecdate()), ""};
			// �뿩 ���� �� ���� 0���� ū ��츸 ��� '����' ����
			if(book.getLoanable() > 0) contents[9] = "����";
			
			for(int j = 0; j < contents.length; j++) {
				results[i][j] = contents[j];
			}
		}
		
		return results;
	}
	
	/* ī�װ��� ���� ���� */
	public List<Book> selectByCategory(String category) {
		ArrayList<Book> bookList = new ArrayList<>();
		
		try {
			String query = "select * from book where category = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, category);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Book book = new Book(rs.getString("bookid"),
						rs.getString("category"),
						rs.getString("title"),
						rs.getString("author"),
						rs.getString("publisher"),
						LocalDateTime.parse(rs.getString("pubdate"), format),
						rs.getInt("possession"),
						rs.getInt("loanable"),
						LocalDateTime.parse(rs.getString("recdate"), format));
				bookList.add(book);
			}
			return bookList;
		} catch(Exception ex) {
			System.out.println("ī�װ� �� ���� �˻� ����");
		}
		return null;
	}

}
