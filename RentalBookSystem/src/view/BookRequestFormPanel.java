package view;

import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.BookDao;
import dao.RequestDao;
import dto.Member;
import dto.Request;

/*
 * 도서 신청 폼을 담고있는 패널
 */
public class BookRequestFormPanel extends JPanel {
	
	// private BookDao bookDao;
	private RequestDao reqDao;
	
	private JPanel formScope; // form 영역
	
	private JTextField bookTitleInput, bookAuthorInput, bookPublisherInput;
	private JButton resetButton, submitButton; // 초기화 버튼, 신청(등록) 버튼
	
	/*
	 * [ 생성자 ]
	 * 
	 * 받은 member 정보 --> request 정보를 추가할 때 memberid가 필요함
	 * - 폼 구현
	 * - 초기화 버튼 액션 구현
	 * - 신청 버튼 액션 구현: 클릭 시 입력한 도서, 날짜, 상태 정보를 사용하여 request 객체 생성 후 db에 추가
	 */
	public BookRequestFormPanel(Member member) {
		Font font = new Font("맑은 고딕", Font.BOLD, 18);
		JLabel formTitle = new JLabel("도서 신청", JLabel.CENTER);
		formTitle.setFont(font);
		this.setLayout(new GridLayout(2, 1));
		this.add(formTitle);
		
		/* form 영역 작성 */
		formScope = new JPanel(new GridLayout(0, 2));
		formScope.add(new JLabel("도서명", JLabel.CENTER));
		formScope.add(bookTitleInput = new JTextField(20));
		formScope.add(new JLabel("저자", JLabel.CENTER));
		formScope.add(bookAuthorInput = new JTextField(20));
		formScope.add(new JLabel("출판사", JLabel.CENTER));
		formScope.add(bookPublisherInput = new JTextField(20));
		
		/* 내용 초기화 버튼 */
		resetButton = new JButton("초기화");
		resetButton.addActionListener(e -> {
			bookTitleInput.setText("");
			bookAuthorInput.setText("");
			bookPublisherInput.setText("");
		});
		/* 신청 버튼 */
		submitButton = new JButton("신청");
		submitButton.addActionListener(e -> {
			reqDao = new RequestDao();
			Request newRequest = new Request(
					bookTitleInput.getText(),
					bookAuthorInput.getText(),
					bookPublisherInput.getText(),
					member.getMemberid(),
					LocalDateTime.now(),
					"N"); // 신청 시 기본으로 상태는 'N' --> 입고 안 됨
			reqDao.insert(newRequest);
			
			new AlertDialog("도서 신청", "신청을 완료하였습니다.");
			// 신청 후 폼 내용 초기화
			bookTitleInput.setText("");
			bookAuthorInput.setText("");
			bookPublisherInput.setText("");
		});
		
		formScope.add(resetButton);
		formScope.add(submitButton);
		
		this.add(formScope);
	}

}
