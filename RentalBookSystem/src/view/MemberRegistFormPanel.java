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
 * ȸ�� ��� ���� ���� �г�
 */
public class MemberRegistFormPanel extends JPanel {
	
	private MemberDao memberDao;
	
	private JPanel nameScope; // �̸� ����
	private JPanel birthScope; // ���� ����
	private JPanel phoneScope; // ��ȭ��ȣ ����
	
	private JTextField nameInput; // �̸� �Է¶�
	private JTextField yearInput, monthInput, dayInput;	// ���� �Է¶�
	private JTextField phoneFirstInput, phoneSecondInput, phoneThirdInput; // ��ȭ��ȣ �Է¶�
	
	private JButton submitButton = new JButton("���");
	
	public MemberRegistFormPanel() {
		memberDao = new MemberDao();
		
		this.setLayout(new GridLayout(5, 1));
		Font font = new Font("���� ���", Font.BOLD, 18);
		JLabel formTitle = new JLabel("ȸ�� ��� �� ���̵� �߱�", JLabel.CENTER);
		formTitle.setFont(font);
		this.add(formTitle);
		
		nameScope = new JPanel();
		nameScope.add(new JLabel("�� �� : "));
		nameScope.add(nameInput = new JTextField(18));
		
		birthScope = new JPanel();
		birthScope.add(new JLabel("�� �� : "));
		birthScope.add(yearInput = new JTextField(8));
		birthScope.add(monthInput = new JTextField(4));
		birthScope.add(dayInput = new JTextField(4));
		
		phoneScope = new JPanel();
		phoneScope.add(new JLabel("�� ȭ : "));
		phoneScope.add(phoneFirstInput = new JTextField(4));
		phoneScope.add(phoneSecondInput = new JTextField(6));
		phoneScope.add(phoneThirdInput = new JTextField(6));
		
		// Random rand = new Random();
		submitButton.addActionListener(e -> {
			/* ���̵� ���� */
			String id = "";
			/* 3���� ������ ����(A~Z)�� id�� ������ */
			do {
				id = "";
				for(int i = 0; i < 3; i++) id += (char)((int)(Math.random() * 26) + 65);				
				id += phoneThirdInput.getText(); // ��ȭ��ȣ ���ڸ� ������
			} while(memberDao.selectById(id) != null); // �����ϴ� ���̵��� �� ��߱�
			
			
			/* ������ LocalDateTime���� ���� */
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss");
			LocalDateTime birth = LocalDateTime.parse(yearInput.getText() + "-" +
						monthInput.getText() + "-" +
						dayInput.getText() + " 00:00:00", format);
			
			/* ��ȭ ���� ���� */
			String phone = phoneFirstInput.getText() + "-" + 
						phoneSecondInput.getText() + "-" + 
						phoneThirdInput.getText();
			
			/* ��ϵ� ��ȭ��ȣ�� �ƴ� ��� */
			if(memberDao.selectByPhone(phone) == null) {
				Member newMember = new Member(id, nameInput.getText(), birth, phone, LocalDateTime.now()); // �� ȸ�� ����
				String newId = memberDao.insert(newMember); // �� ȸ�� ���� �� �߱޹��� ���̵� ��ȯ����
				
				if(newId != null) {
					JButton okButton = new JButton("Ȯ��"); // Ȯ�� ��ư
					
					/* �˾�â ���� */
					JDialog registSuccess = new JDialog(new JFrame(), "���̵� �߱�");
					registSuccess.setSize(400, 100);
					registSuccess.add(new JLabel(newMember.getName() + "���� ���̵� ���������� �߱޵Ǿ����ϴ�."), BorderLayout.NORTH);
					registSuccess.add(new JLabel(newId), BorderLayout.CENTER);
					
					/* Ȯ�� ��ư�� Ŭ���ϸ� dialog�� ������ ù �������� �̵� */
					okButton.addActionListener(event -> {
						registSuccess.setVisible(false);
						BookListPanel bookListPanel = new BookListPanel(null, null);
						
						this.removeAll(); // ���� �г�(ȸ�� �����)�� ������ ��� ����
						this.add(bookListPanel); // ���� �гο� ���� ����� ���� �г� �߰�
						/* �ٽ� �׸��� */
						this.revalidate();
						this.repaint();
					});
					registSuccess.add(okButton, BorderLayout.SOUTH); // Ȯ�� ��ư �гο� �߰�
					
					registSuccess.setVisible(true);
				}
			} else {
				new AlertDialog("���̵� �߱� ����", "��ϵ� ��ȭ��ȣ�Դϴ�.");
				/* �� �ʱ�ȭ */
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
