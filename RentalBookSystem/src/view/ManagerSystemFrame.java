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

	private JPanel mainContentPanel; // ���� ������
	
	private JPanel leftMenuPanel; // ���� �޴�
	private JPanel leftMenuButtonsPanel; // �޴� ���� ��ư �г�
		
	private JButton registBookButton = new JButton("���� �԰�");
	private JButton manageBookButton = new JButton("���� ���");
	private JButton manageUserButton = new JButton("ȸ�� ���");
	private JButton logoutManageButton = new JButton("���� ����");
	
	private JLabel managerInfo;
	
	public ManagerSystemFrame(Manager manager) {
		/* ������ ���� */
		managerInfo = new JLabel(manager.getName() + " ������", JLabel.CENTER);
		
		/* ���� �޴� */
		leftMenuPanel = new JPanel(new GridLayout(0, 1));
		leftMenuPanel.add(managerInfo);
		
		leftMenuButtonsPanel = new JPanel(new GridLayout(0, 1));
		leftMenuButtonsPanel.add(registBookButton);
		leftMenuButtonsPanel.add(manageBookButton);
		leftMenuButtonsPanel.add(manageUserButton);
		leftMenuButtonsPanel.add(logoutManageButton);
		
		leftMenuPanel.add(leftMenuButtonsPanel);
		
		/* ���� �԰� ��ư �̺�Ʈ ó�� */
		registBookButton.addActionListener(e -> {
			changeMainContent(new RequestListPanel());
		});
		
		/* ���� ��� ��ư �̺�Ʈ ó�� */
		manageBookButton.addActionListener(e -> {
			changeMainContent(new BookStatsPanel());
		});
		
		/* ȸ�� ��� ��ư �̺�Ʈ ó�� */
		manageUserButton.addActionListener(e -> {
			changeMainContent(new MemberStatsPanel());
		});
		
		/* ���� ���� ��ư �̺�Ʈ ó�� */
		logoutManageButton.addActionListener(e -> {
			this.setVisible(false); // ������ frame �ݱ�
		});
		
		/* ���� ������ */
		mainContentPanel = new JPanel();
		mainContentPanel.add(new JLabel("������ �޴��� Ŭ���ϼ���", JLabel.CENTER), BorderLayout.CENTER);
		this.add(mainContentPanel, BorderLayout.CENTER);
		
		this.add(leftMenuPanel, BorderLayout.LINE_START);
		
		/* ������ ���� */
		this.setSize(600, 520);
		this.setTitle("���� �ý��� : ������");
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
