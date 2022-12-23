package view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import dao.BookDao;
import dao.RentalDao;
import dto.Book;
import dto.Member;
import dto.Rental;

/* << 도서 목록 >>
 * 
 * - JTable에 저장해서 패널에 추가
 * - search 값이 있는 경우 검색된 결과 출력
 * - JTable 내 대출 클릭 시 이벤트 처리 --> 도서 대출 
 * - 인증된 회원 + 대출 내역 존재 시 추천 도서 출력
 * */
public class BookListPanel extends JPanel {
	private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	private BookDao bookDao = new BookDao();
	private RentalDao rentalDao = new RentalDao();
	
	private String[] header = {"도서코드", "분류", "도서명", "저자", "출판사", "출판일", "입고권수", "대출가능", "입고일", "비고"};
	//private String[][] contents = bookDao.selectAllArray();
	private String[][] contents;
	
	private JTable table; // 도서 정보를 저장할 테이블
	private JScrollPane scrollPanel;
	
	private Book recentBook = null; // 회원에게 추천할 최신 도서
	
	/* 생성자 */
	public BookListPanel(Member member, String search) {
		/*
		 * 검색 키워드가 없는 경우 : 도서 전체
		 * 검색 키워드가 있는 경우 : 검색된 도서만
		 */
		if(search == null || search.equals("")) contents = bookDao.selectAllArray();
		else contents = bookDao.selectBySearchArray(search);
		
		/* 회원 인증 상태 
		 * 
		 * 대출 정보 리스트
		 * */
		List<Rental> rentalList = new ArrayList<>();
		String[] category = {"철학", "종교", "사회과학", "자연과학", "기술과학", "예술", "언어", "문학", "역사"};
		HashMap<String, Integer> categoryCount = new HashMap<>();
		// HashMap 초기화 : 각 카테고리 별 대출 도서의 개수를 0으로 초기화
		for(String data : category) categoryCount.put(data, 0);
		
		if(member != null) {
			rentalList = rentalDao.selectByMemberId(member.getMemberid()); // 인증된 회원 아이디로 대출 내역 검색
			// 대출 내역이 존재하는 경우 --> 최신 도서 추천
			if(!rentalList.isEmpty()) {
				// HashMap에 카테고리별 도서 대출 횟수 저장
				for(Rental rental : rentalList) {
					Book book = bookDao.selectById(rental.getBookid()); // 대출 내역의 도서 코드로 책 정보 찾기
					categoryCount.put(book.getCategory(), categoryCount.get(book.getCategory())+1); // 해당 카테고리 count 값 1증가(업데이트)
				}
				List<Entry<String, Integer>> rentalCountList = new LinkedList<>(categoryCount.entrySet()); // HashMap을 List로 변경
				rentalCountList.sort(Entry.comparingByValue()); // value로 오름차순 정렬
				String categoryResult = rentalCountList.get(rentalCountList.size() - 1).getKey(); // 마지막 값의 key(카테고리) 값 저장
				List<Book> bookList = bookDao.selectByCategory(categoryResult); // 카테고리로 도서 검색
				
				// 가장 최근에 입고된 도서 찾기
				recentBook = bookList.get(0);
				for(Book book : bookList) {
					// 현재 검사 중인 도서가 recentBook의 입고 날짜보다 이후의 날짜일 경우
					if(book.getRecdate().isAfter(recentBook.getRecdate())) recentBook = book;
				}
				
				Font font = new Font("맑은 고딕", Font.BOLD, 12);
				JLabel comment = new JLabel("[" + member.getName() + "님이 자주 찾으시는 카테고리의 최신 도서]");
				comment.setFont(font);
				
				JPanel recentBookPanel = new JPanel(); // 도서 추천(최근 도서)의 내용을 담을 패널
				recentBookPanel.add(comment);
				
				// 추천 도서 정보
				JPanel recentBookInfoPanel = new JPanel(new GridLayout(0, 2));
				recentBookInfoPanel.add(new JLabel("도서코드"));
				recentBookInfoPanel.add(new JLabel(recentBook.getBookid()));
				recentBookInfoPanel.add(new JLabel("도서명"));
				recentBookInfoPanel.add(new JLabel(recentBook.getTitle()));
				recentBookInfoPanel.add(new JLabel("저자"));
				recentBookInfoPanel.add(new JLabel(recentBook.getAuthor()));
				recentBookInfoPanel.add(new JLabel("출판사"));
				recentBookInfoPanel.add(new JLabel(recentBook.getPublisher()));
				recentBookInfoPanel.add(new JLabel("대출가능"));
				recentBookInfoPanel.add(new JLabel(Integer.toString(recentBook.getLoanable()) + "권"));
				
				recentBookInfoPanel.add(new JLabel(""));
				JButton recentBookRentalButton = new JButton("대출");
				recentBookInfoPanel.add(recentBookRentalButton); // 대출 버튼
				// 추천 도서 대출 버튼 이벤트 처리
				recentBookRentalButton.addActionListener(e -> {
					// 대출 dialog
					JDialog checkDialog = new JDialog(new JFrame(), "도서 대출");
					checkDialog.setSize(300, 150);
					checkDialog.add(new JLabel("[" + recentBook.getTitle() + "]을 대출하시겠습니까?", JLabel.CENTER), BorderLayout.CENTER);
					
					JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
					
					// 대출 취소 버튼
					JButton cancelButton = new JButton("취소");
					cancelButton.setBackground(Color.RED);
					cancelButton.addActionListener(event -> {
						checkDialog.setVisible(false);
					});
					
					// 대출 버튼 --> 클릭 시 대출 정보 저장
					JButton okButton = new JButton("대출");
					okButton.addActionListener(event -> {
						/* 아이디 생성 */
						String id;
						/* 3개의 랜덤한 문자(A~Z)를 id에 덧붙임 */
						do {
							id = "R"; // Rental의 아이디인 것을 구분하기 위해
							for(int i = 0; i < 3; i++) id += (char)((int)(Math.random() * 26) + 65);				
							for(int i = 0; i < 4; i++) id += (char)((int)(Math.random() * 10) + 48);
						} while(rentalDao.selectById(id) != null); // 존재하는 아이디일 때 재발급
						
						Rental newRental = new Rental(id, recentBook.getBookid(), 
								member.getMemberid(), LocalDateTime.now(), 
								LocalDateTime.now().plusDays(7), "N");
						/* 대출 정보 삽입 */
						rentalDao.insert(newRental);
						/* 대출 가능 권 수 변경 */
						bookDao.updateLoanable(recentBook.getBookid(), true);	
						
						checkDialog.setVisible(false);
						
						/* 대출 정보 dialog */
						JDialog rentalInfoDialog = new JDialog(new JFrame(), "도서 대출");
						rentalInfoDialog.setSize(300, 300);
						rentalInfoDialog.add(new JLabel("대출 정보", JLabel.CENTER), BorderLayout.NORTH);
						
						JPanel rentalInfoPanel = new JPanel(new GridLayout(4, 1));
						rentalInfoPanel.add(new JLabel("대출 아이디 : " + id));
						rentalInfoPanel.add(new JLabel("도서명 : " + recentBook.getTitle()));
						rentalInfoPanel.add(new JLabel("대출일 : " + newRental.getRentaldate()));
						rentalInfoPanel.add(new JLabel("반납 예정일 : " + newRental.getReturndate()));
						rentalInfoDialog.add(rentalInfoPanel, BorderLayout.CENTER);
						
						JButton checkOkButton = new JButton("확인");
						checkOkButton.addActionListener(buttonEvent -> {
							rentalInfoDialog.setVisible(false);								
						});
						rentalInfoDialog.add(checkOkButton, BorderLayout.SOUTH);
						rentalInfoDialog.setVisible(true);
					});						
					
					buttonPanel.add(cancelButton);
					buttonPanel.add(okButton);
					
					checkDialog.add(buttonPanel, BorderLayout.SOUTH);
					checkDialog.setVisible(true);
				});
				
				recentBookPanel.add(recentBookInfoPanel);
				recentBookPanel.setLayout(new BoxLayout(recentBookPanel, BoxLayout.Y_AXIS));
				this.add(recentBookPanel);
			}			
		}		
		
		table = new JTable(contents, header);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(member != null) {
					int row = table.getSelectedRow();
					int column = table.getSelectedColumn();
					
					// 클릭된 셀의 데이터(문자열)
					String clickedData = table.getModel().getValueAt(row, column).toString();
					
					// 클릭된 셀의 내용이 대출일 때 --> 열의 위치로 확인할지 고민
					if(clickedData.equals("대출")) {
						// 해당 셀의 행의 정보를 문자열 배열로 저장
						String[] clickedRow = new String[table.getModel().getColumnCount()];
						for(int i = 0; i < table.getModel().getColumnCount(); i++)
							clickedRow[i] = table.getModel().getValueAt(row, i).toString();
						
						// 저장한 정보를 Book 객체로 변환
						Book clickedBook = new Book(
									clickedRow[0], // 도서코드
									clickedRow[1], // 분류
									clickedRow[2], // 도서명
									clickedRow[3], // 저자
									clickedRow[4], // 출판사
									LocalDateTime.parse(clickedRow[5], format), // 출판일
									Integer.valueOf(clickedRow[6]), // 입고권수
									Integer.valueOf(clickedRow[7]), // 대출가능
									LocalDateTime.parse(clickedRow[8], format) // 입고일
								);
						// 대출 dialog
						JDialog checkDialog = new JDialog(new JFrame(), "도서 대출");
						checkDialog.setSize(300, 150);
						checkDialog.add(new JLabel("[" + clickedBook.getTitle() + "]을 대출하시겠습니까?", JLabel.CENTER), BorderLayout.CENTER);
						
						JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
						
						// 대출 취소 버튼
						JButton cancelButton = new JButton("취소");
						cancelButton.setBackground(Color.RED);
						cancelButton.addActionListener(event -> {
							checkDialog.setVisible(false);
						});
						
						// 대출 버튼 --> 클릭 시 대출 정보 저장
						JButton okButton = new JButton("대출");
						okButton.addActionListener(event -> {
							/* 아이디 생성 */
							String id;
							/* 3개의 랜덤한 문자(A~Z)를 id에 덧붙임 */
							do {
								id = "R"; // Rental의 아이디인 것을 구분하기 위해
								for(int i = 0; i < 3; i++) id += (char)((int)(Math.random() * 26) + 65);				
								for(int i = 0; i < 4; i++) id += (char)((int)(Math.random() * 10) + 48);
							} while(rentalDao.selectById(id) != null); // 존재하는 아이디일 때 재발급
							
							Rental newRental = new Rental(id, clickedBook.getBookid(), 
									member.getMemberid(), LocalDateTime.now(), 
									LocalDateTime.now().plusDays(7), "N");
							/* 대출 정보 삽입 */
							rentalDao.insert(newRental);
							/* 대출 가능 권 수 변경 */
							bookDao.updateLoanable(clickedBook.getBookid(), true);	
							
							checkDialog.setVisible(false);
							
							/* 대출 정보 dialog */
							JDialog rentalInfoDialog = new JDialog(new JFrame(), "도서 대출");
							rentalInfoDialog.setSize(300, 300);
							rentalInfoDialog.add(new JLabel("대출 정보", JLabel.CENTER), BorderLayout.NORTH);
							
							JPanel rentalInfoPanel = new JPanel(new GridLayout(4, 1));
							rentalInfoPanel.add(new JLabel("대출 아이디 : " + id));
							rentalInfoPanel.add(new JLabel("도서명 : " + clickedBook.getTitle()));
							rentalInfoPanel.add(new JLabel("대출일 : " + newRental.getRentaldate()));
							rentalInfoPanel.add(new JLabel("반납 예정일 : " + newRental.getReturndate()));
							rentalInfoDialog.add(rentalInfoPanel, BorderLayout.CENTER);
							
							JButton checkOkButton = new JButton("확인");
							checkOkButton.addActionListener(buttonEvent -> {
								rentalInfoDialog.setVisible(false);								
							});
							rentalInfoDialog.add(checkOkButton, BorderLayout.SOUTH);
							rentalInfoDialog.setVisible(true);							
						});						
						
						buttonPanel.add(cancelButton);
						buttonPanel.add(okButton);
						
						checkDialog.add(buttonPanel, BorderLayout.SOUTH);
						checkDialog.setVisible(true);						
					}
					
					// test
					System.out.println("선택된 셀 :" + clickedData);
				} else { // 인증된 회원이 아닌 경우 --> member == null
					new AlertDialog("도서 대출", "회원 인증이 필요합니다.");
				}
			}
		});
		
		scrollPanel = new JScrollPane(table);
		this.add(scrollPanel);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}	

} 