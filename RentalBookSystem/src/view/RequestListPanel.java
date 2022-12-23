package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import dao.BookDao;
import dao.RequestDao;
import dto.Book;
import dto.Request;

public class RequestListPanel extends JPanel {

	private RequestDao requestDao = new RequestDao();
	private BookDao bookDao = new BookDao();
	
	private String[] header = {"no.", "������", "����", "���ǻ�", "��û��", "��û��", "����", "���"};
	private String[][] contents;
	
	private JTable table; // ��û ������ ������ ���̺�
	private JScrollPane scrollPanel;
	
	public RequestListPanel() {
		Font font = new Font("���� ���", Font.BOLD, 19); // ��Ʈ
		contents = requestDao.selectAllArray();
		table = new JTable(contents, header);
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				int column = table.getSelectedColumn();
				
				// Ŭ���� ���� ������(���ڿ�)
				String clickedData = table.getModel().getValueAt(row, column).toString();
				
				if(clickedData.equals("�԰�")) {
					// Ŭ���� ���� ��û ���̵�
					int clickedId = Integer.parseInt(table.getModel().getValueAt(row, 0).toString());
					Request request = requestDao.selectById(clickedId);
					
					/* ���� �԰� dialog */
					JDialog bookRegistDialog = new JDialog(new JFrame(), "���� �԰�");
					
					/* ���� �԰� �� */
					// �Է¶�
					JTextField titleInput, authorInput, publisherInput, pubdateInput, possessionInput;
					// �޺� �ڽ�(ī�װ�)
					JComboBox categoryCombo = new JComboBox();
					String[] categoryData = {"ö��", "����", "��ȸ����", "�ڿ�����", "�������", "����", "���", "����", "����"};
					for(String data : categoryData)	categoryCombo.addItem(data);
					// ��ư
					JButton cancelButton = new JButton("���");
					JButton submitButton = new JButton("�԰�");
					// �԰� �� �г�
					JPanel bookRegistFormPanel = new JPanel(new GridLayout(0, 2));
					
					// �� ����
					bookRegistFormPanel.add(new JLabel("ī�װ�"));
					bookRegistFormPanel.add(categoryCombo); // �޺�
					bookRegistFormPanel.add(new JLabel("������"));
					bookRegistFormPanel.add(titleInput = new JTextField(request.getBooktitle()));
					bookRegistFormPanel.add(new JLabel("����"));
					bookRegistFormPanel.add(authorInput = new JTextField(request.getAuthor()));
					bookRegistFormPanel.add(new JLabel("���ǻ�"));
					bookRegistFormPanel.add(publisherInput = new JTextField(request.getPublisher()));
					bookRegistFormPanel.add(new JLabel("������"));
					bookRegistFormPanel.add(pubdateInput = new JTextField(20));
					bookRegistFormPanel.add(new JLabel("����"));
					bookRegistFormPanel.add(possessionInput = new JTextField(20));
					cancelButton.setBackground(Color.red);
					bookRegistFormPanel.add(cancelButton);
					submitButton.setBackground(Color.cyan);
					bookRegistFormPanel.add(submitButton);
										
					JLabel formTitle = new JLabel("���� �԰�", JLabel.CENTER);
					formTitle.setFont(font);
					
					bookRegistDialog.add(formTitle, BorderLayout.NORTH);
					bookRegistDialog.add(bookRegistFormPanel, BorderLayout.CENTER);
					
					/* ��� ��ư �̺�Ʈ ó�� */
					cancelButton.addActionListener(event -> {
						bookRegistDialog.setVisible(false);
					});
					
					/* �԰� ��ư �̺�Ʈ ó�� */
					submitButton.addActionListener(event -> {
						/* ���̵� ���� */
						String id = "";
						/* 3���� ������ ����(A~Z)�� id�� ������ */
						do {
							id = "";
							for(int i = 0; i < 3; i++) id += (char)((int)(Math.random() * 26) + 65);				
							for(int i = 0; i < 4; i++) id += Integer.toString((int)(Math.random() * 10));
						} while(bookDao.selectById(id) != null); // �����ϴ� ���̵��� �� ��߱�
						
						DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss");
						Book newBook = new Book(id,
								categoryCombo.getSelectedItem().toString(),
								titleInput.getText(),
								authorInput.getText(),
								publisherInput.getText(),
								LocalDateTime.parse(pubdateInput.getText() + " 00:00:01", format),
								Integer.parseInt(possessionInput.getText()),
								Integer.parseInt(possessionInput.getText()),
								LocalDateTime.now());
						bookDao.insert(newBook);
						requestDao.updateStatus(clickedId);
						new AlertDialog("���� �԰�", "������ �԰�Ǿ����ϴ�.");
						bookRegistDialog.setVisible(false);
					});
					
					bookRegistDialog.setSize(350, 250);
					bookRegistDialog.setVisible(true);
				}
			}
		});
		
		scrollPanel = new JScrollPane(table);
		this.add(scrollPanel);
	}
	
}
