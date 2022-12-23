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
 * ȸ�� ���� �� + ������ ���� �� ���
 */
public class MemberAccessFormPanel extends JPanel {
	
	private MemberDao memberDao;
	
	private JPanel memberForm;
	private JTextField idInput = new JTextField(20);
	private JButton submitButton = new JButton("����");
	
	/* ������ ���� ���� */
	private ManagerDao managerDao = new ManagerDao();
	private JPanel managerForm;
	private JLabel managerAccess = new JLabel("������ ����");
	
	private JTextField managerIdInput, managerPasswordInput;
	private JButton managerSubmitButton = new JButton("����");
	private JButton managerRegisterButton = new JButton("������ ���");
		
	public MemberAccessFormPanel() {		
		Font font = new Font("���� ���", Font.BOLD, 19);
		/* �Ϲ� ����� ���� �� */
		JLabel formTitle = new JLabel("ȸ�� ����", JLabel.CENTER);
		formTitle.setFont(font);
		
		memberForm = new JPanel();
		memberForm.setLayout(new GridLayout(0, 1));
		memberForm.add(formTitle);
		memberForm.add(new JLabel("ȸ�� ��� �� �߱޹��� ���̵� �����Ͽ� �ֽʽÿ�.", JLabel.CENTER));
		memberForm.add(idInput);
		
		/* ���� ��ư �̺�Ʈ ó�� */
		submitButton.addActionListener(e -> {
			memberDao = new MemberDao();
			/* �Էµ� �������� ȸ�� ã�� */
			Member member = memberDao.selectById(idInput.getText());
			if(member != null) {
				// System.out.println("ȸ�� ���� ����"); // test				
				SystemFrame s = new SystemFrame(member); // ������ ȸ�� ������ ������ ����
				
				/* ���� dialog ��� */
				new AlertDialog("ȸ�� ����", "ȸ�� ������ �����߽��ϴ�.");
				idInput.setText("");
			} else {
				// System.out.println("ȸ�� ���� ����"); // test
				/* ���� dialog ��� */
				new AlertDialog("ȸ�� ����", "ȸ�� ������ �����߽��ϴ�.");
				
				/* form �ʱ�ȭ */
				idInput.setText("");
			}
		});
		memberForm.add(submitButton);
		managerAccess.setForeground(Color.BLUE);
		memberForm.add(managerAccess);
		this.add(memberForm);		
		
		/* ������ ��� �̺�Ʈ ó�� */
		managerAccess.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JPanel managerAccessForm = new JPanel();
				/* ������ ���� �� */
				JLabel formTitle = new JLabel("������ ����", JLabel.CENTER);
				formTitle.setFont(font);
				
				managerAccessForm.add(formTitle, BorderLayout.NORTH);
				managerForm = new JPanel(new GridLayout(0, 2));
				managerForm.add(new JLabel("���̵�"));
				managerForm.add(managerIdInput = new JTextField(16));
				managerForm.add(new JLabel("��й�ȣ"));
				managerForm.add(managerPasswordInput = new JTextField(16));
				managerForm.add(managerRegisterButton);
				managerForm.add(managerSubmitButton);
				managerAccessForm.add(managerForm, BorderLayout.CENTER);
				
				/* ������ ��� ��ư �̺�Ʈ ó�� */
				managerRegisterButton.addActionListener(event -> {
					JDialog managerRegisterAccessDialog = new JDialog(new JFrame(), "������ ��� ����");
					JPanel managerRegisterAccessPanel = new JPanel(new GridLayout(0, 1));
					JTextField managerRegisterAccessInput;
					JButton managerRegisterAccessButton = new JButton("����");
					
					/* �Ϲ� ����ڰ� �����ڷ� ��ϵǴ� �� ������ ���� �ڵ�(���� String data)�� �� �� �� ���� */
					JLabel accessFormTitle = new JLabel("������ ��� ����", JLabel.CENTER);
					accessFormTitle.setFont(font);
					managerRegisterAccessPanel.add(accessFormTitle);
					managerRegisterAccessPanel.add(new JLabel("������ ����� ���� ���� �ڵ尡 �ʿ��մϴ�.", JLabel.CENTER));
					managerRegisterAccessPanel.add(managerRegisterAccessInput = new JTextField(20));
					managerRegisterAccessPanel.add(managerRegisterAccessButton);
					
					/* ������ ��� ���� ��ư �̺�Ʈ ó�� */
					managerRegisterAccessButton.addActionListener(accessEvent -> {
						if(managerRegisterAccessInput.getText().equals("MNRA0000")) {
							JDialog managerRegisterDialog = new JDialog(new JFrame(), "������ ���");
							managerRegisterDialog.setSize(350, 180);
							
							JLabel registerFormTitle = new JLabel("������ ���", JLabel.CENTER);
							registerFormTitle.setFont(font);
							
							JPanel registerFormPanel = new JPanel(new GridLayout(0, 2));
							JTextField newManagerIdInput, newManagerPWInput, newManagerNameInput;
							JButton dataResetButton = new JButton("�ʱ�ȭ"), managerRegisterSubmitButton = new JButton("���");
							
							/* ������ ��� �� : ������ ���� */
							registerFormPanel.add(new JLabel("������ ���̵�"));
							registerFormPanel.add(newManagerIdInput = new JTextField(16));
							registerFormPanel.add(new JLabel("������ ��й�ȣ"));
							registerFormPanel.add(newManagerPWInput = new JTextField(16));
							registerFormPanel.add(new JLabel("������ �̸�"));
							registerFormPanel.add(newManagerNameInput = new JTextField(16));
							registerFormPanel.add(dataResetButton);
							registerFormPanel.add(managerRegisterSubmitButton);
							
							/* �� �ʱ�ȭ ��ư �̺�Ʈ ó�� */
							dataResetButton.addActionListener(resetEvent -> {
								newManagerIdInput.setText("");
								newManagerPWInput.setText("");
								newManagerNameInput.setText("");
							});
							
							/* ������ ���� ��� ��ư �̺�Ʈ ó�� */
							managerRegisterSubmitButton.addActionListener(regEvent -> {
								String id = newManagerIdInput.getText();
								String password = newManagerPWInput.getText();
								String name = newManagerNameInput.getText();
								
								/* ���̵� �ߺ� �˻� */
								Manager manager = managerDao.selectById(id);
								if(manager != null) {
									/* ���̵� �ߺ� �˸� */
									new AlertDialog("������ ���", "�����ϴ� ���̵��Դϴ�.");
								} else {
									Manager newManager = new Manager(id, password, name, LocalDateTime.now());
									managerDao.insert(newManager);
									
									new AlertDialog("������ ���", newManager.getName() + "(" + newManager.getManagerid() + ")�� ���� ��ϵǾ����ϴ�.");
									managerRegisterDialog.setVisible(false); // ������ ��� �� �����
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
				
				JDialog managerAccessDialog = new JDialog(new JFrame(), "������ ����"); // ������ ���� Dialog
				
				/* ���� ��ư �̺�Ʈ ó�� : �α��� ó�� �� ������ Frame ���� */
				managerSubmitButton.addActionListener(event -> {
					Manager manager = managerDao.selectById(managerIdInput.getText());
					if(manager != null) {
						if(manager.getPassword().equals(managerPasswordInput.getText())) {
							/* ������ Frame �߰� �� �ۼ� */
							ManagerSystemFrame ms = new ManagerSystemFrame(manager);
							/* �ӽ� Ȯ�ο� */
							new AlertDialog("������ ����", "������ ������ �����Ͽ����ϴ�.");
							/* ������ ���� �� �����*/
							managerAccessDialog.setVisible(false);
						} else new AlertDialog("������ ����", "������ ������ �����Ͽ����ϴ�.");
					} else new AlertDialog("������ ����", "��ϵ��� ���� ������ ���̵��Դϴ�.");
				});					
				
				managerAccessDialog.add(managerAccessForm);
				managerAccessDialog.setSize(350, 180);
				managerAccessDialog.setVisible(true);
			}
		});
		
	}
	
}
