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
		con = ConnectDatabase.makeConnection(); // 데이터베이스 연결
	}
	
	/* 도서 삽입 */
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
			System.out.println("도서 입고 실패");
		}
	}
	
	/* 도서 id 검색하기 */
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
			System.out.println("도서 ID 검색 실패");
		}
		return null;
	}
	
	/* 모든 도서코드 반환 */
	public ArrayList<String> selectAllBookid() {
		ArrayList<String> result = new ArrayList<>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select bookid from book");
			
			while(rs.next()) result.add(rs.getString("bookid"));
			return result;
		} catch(Exception ex) {
			System.out.println("도서코드 불러오기 실패");
		}
		return null;
	}
	
	/* 대여 가능 권 수 수정 */
	public void updateLoanable(String id, boolean act) {
		try {
			Book book = selectById(id);
			if(book != null) {
				String query = "update book set loanable = ? where bookid = ?";
				PreparedStatement stmt = con.prepareStatement(query);
				
				/* 대출 시 : act == true | 반납 시 : act == false */
				if(act)	stmt.setInt(1, book.getLoanable() - 1);
				else stmt.setInt(1, book.getLoanable() + 1);
				
				stmt.setString(2, book.getBookid());
				stmt.executeUpdate();
			}
		} catch (Exception ex) {
			System.out.println("도서 대여 가능 권 수 수정 실패");
		}
	}
	
	/* 도서 정보 전체 불러오기 */
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
			System.out.println("데이터 불러오기 실패");
		}		
		
		return bookList;
	}
	
	/* 전체 도서정보 2차원 배열로 전환 후 반환 */
	public String[][] selectAllArray() {
		List<Book> bookList = selectAll();
		int size = bookList.size();
		
		String[][] results = new String[size][10];
		
		for(int i = 0; i < size; i++) {
			Book book = bookList.get(i);
			String[] contents = { book.getBookid(), book.getCategory(), book.getTitle(), book.getAuthor(),
					book.getPublisher(), String.valueOf(book.getPubdate()), String.valueOf(book.getPossession()), 
					String.valueOf(book.getLoanable()), String.valueOf(book.getRecdate()), ""};
			// 대여 가능 권 수가 0보다 큰 경우만 비고에 '대출' 노출
			if(book.getLoanable() > 0) contents[9] = "대출";
			
			for(int j = 0; j < contents.length; j++) {
				results[i][j] = contents[j];
			}
		}
		
		return results;
	}
	
	/* 검색 키워드로 도서 정보 추출하기 */
	public List<Book> selectBySearch(String search) {
		List<Book> bookList = new ArrayList<>();
		
		// 카테고리도 검색할 수 있게 수정 --- 22.12.21 (키워드 검색이라 카테고리 검색은 따로 구현하는 게 나을 듯)
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
			System.out.println("데이터 불러오기 실패");
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
			// 대여 가능 권 수가 0보다 큰 경우만 비고에 '대출' 노출
			if(book.getLoanable() > 0) contents[9] = "대출";
			
			for(int j = 0; j < contents.length; j++) {
				results[i][j] = contents[j];
			}
		}
		
		return results;
	}
	
	/* 카테고리별 도서 추출 */
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
			System.out.println("카테고리 별 도서 검색 실패");
		}
		return null;
	}

}
