package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dto.Member;

public class SystemFrame extends JFrame {
		
	private JPanel searchPanel; // 검색창
	private JTextField searchInput = new JTextField(40); // 검색어 입력란
	private JButton searchButton = new JButton("검색"); // 검색 버튼
		
	private JPanel mainContentPanel; // 메인 컨텐츠
	
	private JPanel leftMenuPanel; // 왼쪽 메뉴
	private JPanel leftMenuButtonsPanel; // 메뉴 조작 버튼 패널
	
	private JButton searchBookButton = new JButton("도서 검색");
	private JButton returnBookButton = new JButton("도서 반납");
	private JButton accessMemberButton = new JButton("회원 인증");
	private JButton registMemberButton = new JButton("회원 등록");
	private JButton requestBookButton = new JButton("도서 신청");
	
	private JLabel userInfo; // 사용자 정보(이름) 라벨
		
	public SystemFrame(Member member) {
		mainContentPanel = new JPanel();
				
		if(member == null) userInfo = new JLabel("미인증된 사용자", JLabel.CENTER);
		else userInfo = new JLabel(member.getName() + "님", JLabel.CENTER);
		
		/* 검색창 */		
		searchButton.addActionListener(e -> {
			String q = searchInput.getText(); // 검색어 추출
			
			// 검색어를 입력한 경우
			if(!q.equals("") || q != null) changeMainContent(new BookListPanel(member, q));
		});
		searchPanel = new JPanel();
		searchPanel.add(new JLabel("도서검색"));
		searchPanel.add(searchInput);
		searchPanel.add(searchButton);
		this.add(searchPanel, BorderLayout.NORTH);
		
		/* 도서 목록 */
		BookListPanel book = new BookListPanel(member, null);
		mainContentPanel.add(book);
		this.add(mainContentPanel, BorderLayout.CENTER);
		
		/* 왼쪽 메뉴 */
		leftMenuPanel = new JPanel(new GridLayout(2, 1));
		
		/* 왼쪽 메뉴에 있는 버튼을 담는 패널 */
		leftMenuButtonsPanel = new JPanel(new GridLayout(0, 1));
		
		/* 도서 검색(왼쪽 메뉴) 버튼 이벤트 처리 */
		searchBookButton.addActionListener(e -> {
			changeMainContent(new BookListPanel(member, null)); // 전체 도서 목록 패널로 변경
		});
		
		/* 도서 반납 버튼 이벤트 처리 */
		returnBookButton.addActionListener(e -> {
			changeMainContent(new BookReturnFormPanel());
		});
				
		/* 회원 인증 버튼 이벤트 처리 */
		accessMemberButton.addActionListener(e -> {
			if(member == null) { // 인증된 회원이 없는 경우
				// MemberAccessFormPanel access = new MemberAccessFormPanel(); 
				changeMainContent(new MemberAccessFormPanel());
			} else { // 인증된 회원이 있는 경우
				new AlertDialog("회원 인증", "인증이 해제되었습니다.");
				this.setVisible(false);
			}			
		});
		
		/* 회원 등록 버튼 이벤트 처리 */
		registMemberButton.addActionListener(e -> {
			if(member == null) changeMainContent(new MemberRegistFormPanel());
			else new AlertDialog("회원 등록", "인증 해제 후 시도하십시오.");
		});
		
		/* 도서 신청 버튼 이벤트 처리 */
		requestBookButton.addActionListener(e -> {
			if(member == null) {
				new AlertDialog("도서 신청", "회원 인증이 필요합니다.");
				changeMainContent(new MemberAccessFormPanel()); // 회원 인증 폼으로 이동
			} else {
				changeMainContent(new BookRequestFormPanel(member));
			}
		});
		
		/* 멤버 유무에 따른 메뉴 변경 */
		if(member != null) {
			leftMenuButtonsPanel.add(searchBookButton);
			leftMenuButtonsPanel.add(returnBookButton);
			accessMemberButton.setText("인증 해제");
			leftMenuButtonsPanel.add(accessMemberButton);
			leftMenuButtonsPanel.add(requestBookButton);
		} else {
			leftMenuButtonsPanel.add(searchBookButton);
			leftMenuButtonsPanel.add(returnBookButton);
			leftMenuButtonsPanel.add(accessMemberButton);
			leftMenuButtonsPanel.add(registMemberButton);
			leftMenuButtonsPanel.add(requestBookButton);
		}		
		
		leftMenuPanel.add(userInfo);
		leftMenuPanel.add(leftMenuButtonsPanel);
		
		this.add(leftMenuPanel, BorderLayout.LINE_START);
		
		/* 프레임 설정 */
		this.setSize(600, 520);
		this.setTitle("도서 시스템 : 일반");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	/* 메인 컨텐츠 내용 변경 */
	public void changeMainContent(Component component) {
		// 현재 메인 컨텐츠에 있는 컴포넌트 제거
		mainContentPanel.removeAll();
		// 새 컴포넌트 추가
		mainContentPanel.add(component);
		
		mainContentPanel.revalidate();
		mainContentPanel.repaint();
	}
	
}
