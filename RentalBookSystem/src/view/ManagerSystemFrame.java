package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dto.Manager;

public class ManagerSystemFrame extends JFrame {

	private JPanel mainContentPanel; // 메인 컨텐츠
	
	private JPanel leftMenuPanel; // 왼쪽 메뉴
	private JPanel leftMenuButtonsPanel; // 메뉴 조작 버튼 패널
		
	private JButton registBookButton = new JButton("도서 입고");
	private JButton manageBookButton = new JButton("도서 통계");
	private JButton manageUserButton = new JButton("회원 통계");
	private JButton logoutManageButton = new JButton("인증 해제");
	
	private JLabel managerInfo;
	
	public ManagerSystemFrame(Manager manager) {
		/* 관리자 정보 */
		managerInfo = new JLabel(manager.getName() + " 관리자", JLabel.CENTER);
		
		/* 왼쪽 메뉴 */
		leftMenuPanel = new JPanel(new GridLayout(0, 1));
		leftMenuPanel.add(managerInfo);
		
		leftMenuButtonsPanel = new JPanel(new GridLayout(0, 1));
		leftMenuButtonsPanel.add(registBookButton);
		leftMenuButtonsPanel.add(manageBookButton);
		leftMenuButtonsPanel.add(manageUserButton);
		leftMenuButtonsPanel.add(logoutManageButton);
		
		leftMenuPanel.add(leftMenuButtonsPanel);
		
		/* 도서 입고 버튼 이벤트 처리 */
		registBookButton.addActionListener(e -> {
			changeMainContent(new RequestListPanel());
		});
		
		/* 도서 통계 버튼 이벤트 처리 */
		manageBookButton.addActionListener(e -> {
			changeMainContent(new BookStatsPanel());
		});
		
		/* 회원 통계 버튼 이벤트 처리 */
		manageUserButton.addActionListener(e -> {
			changeMainContent(new MemberStatsPanel());
		});
		
		/* 인증 해제 버튼 이벤트 처리 */
		logoutManageButton.addActionListener(e -> {
			this.setVisible(false); // 관리자 frame 닫기
		});
		
		/* 메인 컨텐츠 */
		mainContentPanel = new JPanel();
		mainContentPanel.add(new JLabel("좌측의 메뉴를 클릭하세요", JLabel.CENTER), BorderLayout.CENTER);
		this.add(mainContentPanel, BorderLayout.CENTER);
		
		this.add(leftMenuPanel, BorderLayout.LINE_START);
		
		/* 프레임 설정 */
		this.setSize(600, 520);
		this.setTitle("도서 시스템 : 관리자");
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
