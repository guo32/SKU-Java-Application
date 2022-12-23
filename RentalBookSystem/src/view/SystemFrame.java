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
		
	private JPanel searchPanel; // �˻�â
	private JTextField searchInput = new JTextField(40); // �˻��� �Է¶�
	private JButton searchButton = new JButton("�˻�"); // �˻� ��ư
		
	private JPanel mainContentPanel; // ���� ������
	
	private JPanel leftMenuPanel; // ���� �޴�
	private JPanel leftMenuButtonsPanel; // �޴� ���� ��ư �г�
	
	private JButton searchBookButton = new JButton("���� �˻�");
	private JButton returnBookButton = new JButton("���� �ݳ�");
	private JButton accessMemberButton = new JButton("ȸ�� ����");
	private JButton registMemberButton = new JButton("ȸ�� ���");
	private JButton requestBookButton = new JButton("���� ��û");
	
	private JLabel userInfo; // ����� ����(�̸�) ��
		
	public SystemFrame(Member member) {
		mainContentPanel = new JPanel();
				
		if(member == null) userInfo = new JLabel("�������� �����", JLabel.CENTER);
		else userInfo = new JLabel(member.getName() + "��", JLabel.CENTER);
		
		/* �˻�â */		
		searchButton.addActionListener(e -> {
			String q = searchInput.getText(); // �˻��� ����
			
			// �˻�� �Է��� ���
			if(!q.equals("") || q != null) changeMainContent(new BookListPanel(member, q));
		});
		searchPanel = new JPanel();
		searchPanel.add(new JLabel("�����˻�"));
		searchPanel.add(searchInput);
		searchPanel.add(searchButton);
		this.add(searchPanel, BorderLayout.NORTH);
		
		/* ���� ��� */
		BookListPanel book = new BookListPanel(member, null);
		mainContentPanel.add(book);
		this.add(mainContentPanel, BorderLayout.CENTER);
		
		/* ���� �޴� */
		leftMenuPanel = new JPanel(new GridLayout(2, 1));
		
		/* ���� �޴��� �ִ� ��ư�� ��� �г� */
		leftMenuButtonsPanel = new JPanel(new GridLayout(0, 1));
		
		/* ���� �˻�(���� �޴�) ��ư �̺�Ʈ ó�� */
		searchBookButton.addActionListener(e -> {
			changeMainContent(new BookListPanel(member, null)); // ��ü ���� ��� �гη� ����
		});
		
		/* ���� �ݳ� ��ư �̺�Ʈ ó�� */
		returnBookButton.addActionListener(e -> {
			changeMainContent(new BookReturnFormPanel());
		});
				
		/* ȸ�� ���� ��ư �̺�Ʈ ó�� */
		accessMemberButton.addActionListener(e -> {
			if(member == null) { // ������ ȸ���� ���� ���
				// MemberAccessFormPanel access = new MemberAccessFormPanel(); 
				changeMainContent(new MemberAccessFormPanel());
			} else { // ������ ȸ���� �ִ� ���
				new AlertDialog("ȸ�� ����", "������ �����Ǿ����ϴ�.");
				this.setVisible(false);
			}			
		});
		
		/* ȸ�� ��� ��ư �̺�Ʈ ó�� */
		registMemberButton.addActionListener(e -> {
			if(member == null) changeMainContent(new MemberRegistFormPanel());
			else new AlertDialog("ȸ�� ���", "���� ���� �� �õ��Ͻʽÿ�.");
		});
		
		/* ���� ��û ��ư �̺�Ʈ ó�� */
		requestBookButton.addActionListener(e -> {
			if(member == null) {
				new AlertDialog("���� ��û", "ȸ�� ������ �ʿ��մϴ�.");
				changeMainContent(new MemberAccessFormPanel()); // ȸ�� ���� ������ �̵�
			} else {
				changeMainContent(new BookRequestFormPanel(member));
			}
		});
		
		/* ��� ������ ���� �޴� ���� */
		if(member != null) {
			leftMenuButtonsPanel.add(searchBookButton);
			leftMenuButtonsPanel.add(returnBookButton);
			accessMemberButton.setText("���� ����");
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
		
		/* ������ ���� */
		this.setSize(600, 520);
		this.setTitle("���� �ý��� : �Ϲ�");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	/* ���� ������ ���� ���� */
	public void changeMainContent(Component component) {
		// ���� ���� �������� �ִ� ������Ʈ ����
		mainContentPanel.removeAll();
		// �� ������Ʈ �߰�
		mainContentPanel.add(component);
		
		mainContentPanel.revalidate();
		mainContentPanel.repaint();
	}
	
}
