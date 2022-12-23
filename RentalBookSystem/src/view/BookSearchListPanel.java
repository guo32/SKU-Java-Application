package view;

import javax.swing.*;

import dao.BookDao;

/* << 도서 검색 목록 >> --> BookListPanel 수정하면서 사용 안하는 파일
 * 
 * JTable에 저장해서 패널에 추가
 * */
public class BookSearchListPanel extends JPanel {
	
	private BookDao bookDao = new BookDao();
	
	private String[] header = {"도서코드", "분류", "도서명", "저자", "출판사", "출판일", "입고권수", "대출가능", "비고"};
	
	private JTable table; // 도서 정보를 저장할 테이블
	private JScrollPane scrollPanel;
	
	public BookSearchListPanel(String search) {
		String[][] contents = bookDao.selectBySearchArray(search); // 검색 조건에 맞는 도서 데이터를 2차원 배열로 저장
		table = new JTable(contents, header); // 도서 데이터와 header를 조합하여 테이블 생성
		scrollPanel = new JScrollPane(table);
		this.add(scrollPanel);
	}
	

}
