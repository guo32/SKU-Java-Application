package view;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import dao.BookDao;
import dao.RentalDao;
import dto.Book;

/* << 도서 관련 통계를 보여줄 Panel >> 
 * 
 * - 최다 대출 도서
 * - 미반납 도서(연체 포함) --> rentalDao 내에서 함수 구현 완료
 * - 연체 도서 --> rentalDao 내에서 함수 구현 완료
 * 
 * 버튼을 만들어 상황에 맞는 통계를 보여줄 수 있도록 구현
 * --> 각 버튼과 패널을 생성하여 연결시킬 것
 * --> 버튼은 상단에 (GridLayout)
 * */
public class BookStatsPanel extends JPanel {
	
	private RentalDao rentalDao = new RentalDao();
	private BookDao bookDao = new BookDao();
	
	/* 버튼 부분 */
	private JPanel buttonAreaPanel;
	private JButton maxRentalButton = new JButton("최다 대출 도서");
	private JButton onLoanButton = new JButton("미반납 도서");
	private JButton overdueButton = new JButton("연체 도서");
	
	/* 컨텐츠 부분 --> table 넣기 */
	private JPanel contentsAreaPanel;
	private JPanel maxLentaledBookPanel;
	// private JPanel onLoanPanel;
	// private JPanel overduePanel;
	
	/* table */
	private String[] header = {"신청코드", "도서코드", "회원코드", "대출일", "반납예정일", "상태"};
	private String[][] contents;
	
	private JTable table; // 신청 정보를 저장할 테이블
	private JScrollPane scrollPanel;
	
	public BookStatsPanel() {
		buttonAreaPanel = new JPanel(new GridLayout(1, 3));
		buttonAreaPanel.add(maxRentalButton);
		buttonAreaPanel.add(onLoanButton);
		buttonAreaPanel.add(overdueButton);
		
		contentsAreaPanel = new JPanel();
		contentsAreaPanel.add(new JLabel("메뉴를 선택하세요.", JLabel.CENTER));
		
		/* 최다 대출 도서 버튼 이벤트 처리 */
		maxRentalButton.addActionListener(e -> {			
			ArrayList<String> bookIdList = bookDao.selectAllBookid();
			// 도서코드별 도서코드, 대출되었던 횟수
			HashMap<String, Integer> countBookIdMap = rentalDao.countRentaledBook(bookIdList);
			
			// HashMap을 List로 변경
			List<Entry<String, Integer>> countBookIdList = new LinkedList<>(countBookIdMap.entrySet());
			countBookIdList.sort(Entry.comparingByValue());
			Collections.reverse(countBookIdList); // 내림차순 변경
			
			// 대출 횟수가 가장 많았던 도서의 정보 출력
			Book maxLentaledBook = bookDao.selectById(countBookIdList.get(0).getKey());
			// String[] bookData = {maxLentaledBook.getBookid(), maxLentaledBook.getTitle(),
					// maxLentaledBook.getAuthor(), maxLentaledBook.getPublisher(), maxLentaledBook.getPubdate().toString()};
			maxLentaledBookPanel = new JPanel();
			JPanel maxLentaledBookInfoPanel = new JPanel(new GridLayout(0, 2));
			maxLentaledBookInfoPanel.add(new JLabel("도서코드"));
			maxLentaledBookInfoPanel.add(new JLabel(maxLentaledBook.getBookid()));
			maxLentaledBookInfoPanel.add(new JLabel("도서명"));
			maxLentaledBookInfoPanel.add(new JLabel(maxLentaledBook.getTitle()));
			maxLentaledBookInfoPanel.add(new JLabel("저자"));
			maxLentaledBookInfoPanel.add(new JLabel(maxLentaledBook.getAuthor()));
			maxLentaledBookInfoPanel.add(new JLabel("출판사"));
			maxLentaledBookInfoPanel.add(new JLabel(maxLentaledBook.getPublisher()));
			maxLentaledBookInfoPanel.add(new JLabel("대출횟수"));
			maxLentaledBookInfoPanel.add(new JLabel(countBookIdList.get(0).getValue().toString() + "회"));
			
			Font font = new Font("맑은 고딕", Font.BOLD, 14);
			JLabel comment = new JLabel("[가장 많이 대출되었던 도서]");
			comment.setFont(font);
			
			maxLentaledBookPanel.add(comment);
			maxLentaledBookPanel.add(maxLentaledBookInfoPanel);
			maxLentaledBookPanel.setLayout(new BoxLayout(maxLentaledBookPanel, BoxLayout.Y_AXIS));
			
			// 대출 횟수 순위별로 table 생성해서 추가하기
			String[] header = {"순위", "도서코드", "도서명", "저자", "출판사", "대출횟수"}; // 6개 항목
			String[][] contents = new String[countBookIdList.size()][header.length];
			for(int i = 0; i < countBookIdList.size(); i++) {
				Book book = bookDao.selectById(countBookIdList.get(i).getKey());
				String[] content = {Integer.toString(i+1) + "위", 
						book.getBookid(), book.getTitle(), book.getAuthor(), 
						book.getPublisher(), Integer.toString(countBookIdList.get(i).getValue()) + "회"};
				for(int j = 0; j < content.length; j++) contents[i][j] = content[j];
			}
			JTable table = new JTable(contents, header);
			JScrollPane scrollPanel = new JScrollPane(table);
			
			contentsAreaPanel.removeAll();
			contentsAreaPanel.add(maxLentaledBookPanel);
			contentsAreaPanel.add(scrollPanel);
			contentsAreaPanel.setLayout(new BoxLayout(contentsAreaPanel, BoxLayout.Y_AXIS));
			contentsAreaPanel.revalidate();
			contentsAreaPanel.repaint();
		});
		
		/* 미반납 도서 버튼 이벤트 처리 */
		onLoanButton.addActionListener(e -> {
			contents = rentalDao.selectBooksOnLoan(false); // false --> 미반납 + 연체 도서
			table = new JTable(contents, header);
			scrollPanel = new JScrollPane(table);
			
			contentsAreaPanel.removeAll();
			contentsAreaPanel.add(scrollPanel);
			contentsAreaPanel.revalidate();
			contentsAreaPanel.repaint();
		});
		
		/* 연체 도서 버튼 이벤트 처리 */
		overdueButton.addActionListener(e -> {
			contents = rentalDao.selectBooksOnLoan(true); // true --> 연체 도서만 
			table = new JTable(contents, header);
			scrollPanel = new JScrollPane(table);
			
			contentsAreaPanel.removeAll();
			contentsAreaPanel.add(scrollPanel);
			contentsAreaPanel.revalidate();
			contentsAreaPanel.repaint();
		});
		
		this.add(buttonAreaPanel);
		this.add(contentsAreaPanel);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));		
	}

}
