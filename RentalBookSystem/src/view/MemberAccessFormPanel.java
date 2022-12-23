package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.ManagerDao;
import dao.MemberDao;
import dto.Manager;
import dto.Member;

/*
 * 회원 인증 폼 + 관리자 인증 및 등록
 */
public class MemberAccessFormPanel extends JPanel {
	
	private MemberDao memberDao;
	
	private JPanel memberForm;
	private JTextField idInput = new JTextField(20);
	private JButton submitButton = new JButton("인증");
	
	/* 관리자 인증 관련 */
	private ManagerDao managerDao = new ManagerDao();
	private JPanel managerForm;
	private JLabel managerAccess = new JLabel("관리자 인증");
	
	private JTextField managerIdInput, managerPasswordInput;
	private JButton managerSubmitButton = new JButton("인증");
	private JButton managerRegisterButton = new JButton("관리자 등록");
		
	public MemberAccessFormPanel() {		
		Font font = new Font("맑은 고딕", Font.BOLD, 19);
		/* 일반 사용자 인증 폼 */
		JLabel formTitle = new JLabel("회원 인증", JLabel.CENTER);
		formTitle.setFont(font);
		
		memberForm = new JPanel();
		memberForm.setLayout(new GridLayout(0, 1));
		memberForm.add(formTitle);
		memberForm.add(new JLabel("회원 등록 시 발급받은 아이디를 기입하여 주십시오.", JLabel.CENTER));
		memberForm.add(idInput);
		
		/* 인증 버튼 이벤트 처리 */
		submitButton.addActionListener(e -> {
			memberDao = new MemberDao();
			/* 입력된 내용으로 회원 찾기 */
			Member member = memberDao.selectById(idInput.getText());
			if(member != null) {
				// System.out.println("회원 인증 성공"); // test				
				SystemFrame s = new SystemFrame(member); // 인증된 회원 정보로 프레임 생성
				
				/* 성공 dialog 출력 */
				new AlertDialog("회원 인증", "회원 인증에 성공했습니다.");
				idInput.setText("");
			} else {
				// System.out.println("회원 인증 실패"); // test
				/* 실패 dialog 출력 */
				new AlertDialog("회원 인증", "회원 인증에 실패했습니다.");
				
				/* form 초기화 */
				idInput.setText("");
			}
		});
		memberForm.add(submitButton);
		managerAccess.setForeground(Color.BLUE);
		memberForm.add(managerAccess);
		this.add(memberForm);		
		
		/* 관리자 등록 이벤트 처리 */
		managerAccess.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JPanel managerAccessForm = new JPanel();
				/* 관리자 인증 폼 */
				JLabel formTitle = new JLabel("관리자 인증", JLabel.CENTER);
				formTitle.setFont(font);
				
				managerAccessForm.add(formTitle, BorderLayout.NORTH);
				managerForm = new JPanel(new GridLayout(0, 2));
				managerForm.add(new JLabel("아이디"));
				managerForm.add(managerIdInput = new JTextField(16));
				managerForm.add(new JLabel("비밀번호"));
				managerForm.add(managerPasswordInput = new JTextField(16));
				managerForm.add(managerRegisterButton);
				managerForm.add(managerSubmitButton);
				managerAccessForm.add(managerForm, BorderLayout.CENTER);
				
				/* 관리자 등록 버튼 이벤트 처리 */
				managerRegisterButton.addActionListener(event -> {
					JDialog managerRegisterAccessDialog = new JDialog(new JFrame(), "관리자 등록 인증");
					JPanel managerRegisterAccessPanel = new JPanel(new GridLayout(0, 1));
					JTextField managerRegisterAccessInput;
					JButton managerRegisterAccessButton = new JButton("인증");
					
					/* 일반 사용자가 관리자로 등록되는 일 없도록 고유 코드(임의 String data)로 한 번 더 인증 */
					JLabel accessFormTitle = new JLabel("관리자 등록 인증", JLabel.CENTER);
					accessFormTitle.setFont(font);
					managerRegisterAccessPanel.add(accessFormTitle);
					managerRegisterAccessPanel.add(new JLabel("관리자 등록을 위해 인증 코드가 필요합니다.", JLabel.CENTER));
					managerRegisterAccessPanel.add(managerRegisterAccessInput = new JTextField(20));
					managerRegisterAccessPanel.add(managerRegisterAccessButton);
					
					/* 관리자 등록 인증 버튼 이벤트 처리 */
					managerRegisterAccessButton.addActionListener(accessEvent -> {
						if(managerRegisterAccessInput.getText().equals("MNRA0000")) {
							JDialog managerRegisterDialog = new JDialog(new JFrame(), "관리자 등록");
							managerRegisterDialog.setSize(350, 180);
							
							JLabel registerFormTitle = new JLabel("관리자 등록", JLabel.CENTER);
							registerFormTitle.setFont(font);
							
							JPanel registerFormPanel = new JPanel(new GridLayout(0, 2));
							JTextField newManagerIdInput, newManagerPWInput, newManagerNameInput;
							JButton dataResetButton = new JButton("초기화"), managerRegisterSubmitButton = new JButton("등록");
							
							/* 관리자 등록 폼 : 데이터 기입 */
							registerFormPanel.add(new JLabel("관리자 아이디"));
							registerFormPanel.add(newManagerIdInput = new JTextField(16));
							registerFormPanel.add(new JLabel("관리자 비밀번호"));
							registerFormPanel.add(newManagerPWInput = new JTextField(16));
							registerFormPanel.add(new JLabel("관리자 이름"));
							registerFormPanel.add(newManagerNameInput = new JTextField(16));
							registerFormPanel.add(dataResetButton);
							registerFormPanel.add(managerRegisterSubmitButton);
							
							/* 폼 초기화 버튼 이벤트 처리 */
							dataResetButton.addActionListener(resetEvent -> {
								newManagerIdInput.setText("");
								newManagerPWInput.setText("");
								newManagerNameInput.setText("");
							});
							
							/* 관리자 정보 등록 버튼 이벤트 처리 */
							managerRegisterSubmitButton.addActionListener(regEvent -> {
								String id = newManagerIdInput.getText();
								String password = newManagerPWInput.getText();
								String name = newManagerNameInput.getText();
								
								/* 아이디 중복 검사 */
								Manager manager = managerDao.selectById(id);
								if(manager != null) {
									/* 아이디 중복 알림 */
									new AlertDialog("관리자 등록", "존재하는 아이디입니다.");
								} else {
									Manager newManager = new Manager(id, password, name, LocalDateTime.now());
									managerDao.insert(newManager);
									
									new AlertDialog("관리자 등록", newManager.getName() + "(" + newManager.getManagerid() + ")님 정상 등록되었습니다.");
									managerRegisterDialog.setVisible(false); // 관리자 등록 폼 지우기
								}							
								
							});
							
							managerRegisterDialog.add(registerFormTitle, BorderLayout.NORTH);
							managerRegisterDialog.add(registerFormPanel, BorderLayout.CENTER);
							managerRegisterAccessDialog.setVisible(false);
							managerRegisterDialog.setVisible(true);
						}
					});
					
					managerRegisterAccessDialog.add(managerRegisterAccessPanel);
					managerRegisterAccessDialog.setSize(300, 150);
					managerRegisterAccessDialog.setVisible(true);
				});
				
				JDialog managerAccessDialog = new JDialog(new JFrame(), "관리자 인증"); // 관리자 인증 Dialog
				
				/* 인증 버튼 이벤트 처리 : 로그인 처리 후 관리자 Frame 접근 */
				managerSubmitButton.addActionListener(event -> {
					Manager manager = managerDao.selectById(managerIdInput.getText());
					if(manager != null) {
						if(manager.getPassword().equals(managerPasswordInput.getText())) {
							/* 관리자 Frame 추가 후 작성 */
							ManagerSystemFrame ms = new ManagerSystemFrame(manager);
							/* 임시 확인용 */
							new AlertDialog("관리자 인증", "관리자 인증에 성공하였습니다.");
							/* 관리자 인증 폼 지우기*/
							managerAccessDialog.setVisible(false);
						} else new AlertDialog("관리자 인증", "관리자 인증에 실패하였습니다.");
					} else new AlertDialog("관리자 인증", "등록되지 않은 관리자 아이디입니다.");
				});					
				
				managerAccessDialog.add(managerAccessForm);
				managerAccessDialog.setSize(350, 180);
				managerAccessDialog.setVisible(true);
			}
		});
		
	}
	
}
