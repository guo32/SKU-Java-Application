package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import dao.MemberDao;
import dto.Member;

/*
 * 회원 등록 폼을 담은 패널
 */
public class MemberRegistFormPanel extends JPanel {
	
	private MemberDao memberDao;
	
	private JPanel nameScope; // 이름 영역
	private JPanel birthScope; // 생일 영역
	private JPanel phoneScope; // 전화번호 영역
	
	private JTextField nameInput; // 이름 입력란
	private JTextField yearInput, monthInput, dayInput;	// 생일 입력란
	private JTextField phoneFirstInput, phoneSecondInput, phoneThirdInput; // 전화번호 입력란
	
	private JButton submitButton = new JButton("등록");
	
	public MemberRegistFormPanel() {
		memberDao = new MemberDao();
		
		this.setLayout(new GridLayout(5, 1));
		Font font = new Font("맑은 고딕", Font.BOLD, 18);
		JLabel formTitle = new JLabel("회원 등록 및 아이디 발급", JLabel.CENTER);
		formTitle.setFont(font);
		this.add(formTitle);
		
		nameScope = new JPanel();
		nameScope.add(new JLabel("이 름 : "));
		nameScope.add(nameInput = new JTextField(18));
		
		birthScope = new JPanel();
		birthScope.add(new JLabel("생 일 : "));
		birthScope.add(yearInput = new JTextField(8));
		birthScope.add(monthInput = new JTextField(4));
		birthScope.add(dayInput = new JTextField(4));
		
		phoneScope = new JPanel();
		phoneScope.add(new JLabel("전 화 : "));
		phoneScope.add(phoneFirstInput = new JTextField(4));
		phoneScope.add(phoneSecondInput = new JTextField(6));
		phoneScope.add(phoneThirdInput = new JTextField(6));
		
		// Random rand = new Random();
		submitButton.addActionListener(e -> {
			/* 아이디 생성 */
			String id = "";
			/* 3개의 랜덤한 문자(A~Z)를 id에 덧붙임 */
			do {
				id = "";
				for(int i = 0; i < 3; i++) id += (char)((int)(Math.random() * 26) + 65);				
				id += phoneThirdInput.getText(); // 전화번호 뒷자리 덧붙임
			} while(memberDao.selectById(id) != null); // 존재하는 아이디일 때 재발급
			
			
			/* 생일을 LocalDateTime으로 변형 */
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss");
			LocalDateTime birth = LocalDateTime.parse(yearInput.getText() + "-" +
						monthInput.getText() + "-" +
						dayInput.getText() + " 00:00:00", format);
			
			/* 전화 형식 변형 */
			String phone = phoneFirstInput.getText() + "-" + 
						phoneSecondInput.getText() + "-" + 
						phoneThirdInput.getText();
			
			/* 등록된 전화번호가 아닌 경우 */
			if(memberDao.selectByPhone(phone) == null) {
				Member newMember = new Member(id, nameInput.getText(), birth, phone, LocalDateTime.now()); // 새 회원 생성
				String newId = memberDao.insert(newMember); // 새 회원 삽입 후 발급받은 아이디 반환받음
				
				if(newId != null) {
					JButton okButton = new JButton("확인"); // 확인 버튼
					
					/* 팝업창 띄우기 */
					JDialog registSuccess = new JDialog(new JFrame(), "아이디 발급");
					registSuccess.setSize(400, 100);
					registSuccess.add(new JLabel(newMember.getName() + "님의 아이디가 성공적으로 발급되었습니다."), BorderLayout.NORTH);
					registSuccess.add(new JLabel(newId), BorderLayout.CENTER);
					
					/* 확인 버튼을 클릭하면 dialog가 닫히고 첫 페이지로 이동 */
					okButton.addActionListener(event -> {
						registSuccess.setVisible(false);
						BookListPanel bookListPanel = new BookListPanel(null, null);
						
						this.removeAll(); // 현재 패널(회원 등록폼)의 내용을 모두 지움
						this.add(bookListPanel); // 현재 패널에 도서 목록을 담은 패널 추가
						/* 다시 그리기 */
						this.revalidate();
						this.repaint();
					});
					registSuccess.add(okButton, BorderLayout.SOUTH); // 확인 버튼 패널에 추가
					
					registSuccess.setVisible(true);
				}
			} else {
				new AlertDialog("아이디 발급 실패", "등록된 전화번호입니다.");
				/* 폼 초기화 */
				nameInput.setText("");
				yearInput.setText(""); 
				monthInput.setText("");
				dayInput.setText("");
				phoneFirstInput.setText("");
				phoneSecondInput.setText("");
				phoneThirdInput.setText("");
			}			
		});
		
		this.add(nameScope);
		this.add(birthScope);
		this.add(phoneScope);
		this.add(submitButton);
	}

}
