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
 * ���� ��û ���� ����ִ� �г�
 */
public class BookRequestFormPanel extends JPanel {
	
	// private BookDao bookDao;
	private RequestDao reqDao;
	
	private JPanel formScope; // form ����
	
	private JTextField bookTitleInput, bookAuthorInput, bookPublisherInput;
	private JButton resetButton, submitButton; // �ʱ�ȭ ��ư, ��û(���) ��ư
	
	/*
	 * [ ������ ]
	 * 
	 * ���� member ���� --> request ������ �߰��� �� memberid�� �ʿ���
	 * - �� ����
	 * - �ʱ�ȭ ��ư �׼� ����
	 * - ��û ��ư �׼� ����: Ŭ�� �� �Է��� ����, ��¥, ���� ������ ����Ͽ� request ��ü ���� �� db�� �߰�
	 */
	public BookRequestFormPanel(Member member) {
		Font font = new Font("���� ���", Font.BOLD, 18);
		JLabel formTitle = new JLabel("���� ��û", JLabel.CENTER);
		formTitle.setFont(font);
		this.setLayout(new GridLayout(2, 1));
		this.add(formTitle);
		
		/* form ���� �ۼ� */
		formScope = new JPanel(new GridLayout(0, 2));
		formScope.add(new JLabel("������", JLabel.CENTER));
		formScope.add(bookTitleInput = new JTextField(20));
		formScope.add(new JLabel("����", JLabel.CENTER));
		formScope.add(bookAuthorInput = new JTextField(20));
		formScope.add(new JLabel("���ǻ�", JLabel.CENTER));
		formScope.add(bookPublisherInput = new JTextField(20));
		
		/* ���� �ʱ�ȭ ��ư */
		resetButton = new JButton("�ʱ�ȭ");
		resetButton.addActionListener(e -> {
			bookTitleInput.setText("");
			bookAuthorInput.setText("");
			bookPublisherInput.setText("");
		});
		/* ��û ��ư */
		submitButton = new JButton("��û");
		submitButton.addActionListener(e -> {
			reqDao = new RequestDao();
			Request newRequest = new Request(
					bookTitleInput.getText(),
					bookAuthorInput.getText(),
					bookPublisherInput.getText(),
					member.getMemberid(),
					LocalDateTime.now(),
					"N"); // ��û �� �⺻���� ���´� 'N' --> �԰� �� ��
			reqDao.insert(newRequest);
			
			new AlertDialog("���� ��û", "��û�� �Ϸ��Ͽ����ϴ�.");
			// ��û �� �� ���� �ʱ�ȭ
			bookTitleInput.setText("");
			bookAuthorInput.setText("");
			bookPublisherInput.setText("");
		});
		
		formScope.add(resetButton);
		formScope.add(submitButton);
		
		this.add(formScope);
	}

}
