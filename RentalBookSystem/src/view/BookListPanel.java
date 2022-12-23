package view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import dao.BookDao;
import dao.RentalDao;
import dto.Book;
import dto.Member;
import dto.Rental;

/* << ���� ��� >>
 * 
 * - JTable�� �����ؼ� �гο� �߰�
 * - search ���� �ִ� ��� �˻��� ��� ���
 * - JTable �� ���� Ŭ�� �� �̺�Ʈ ó�� --> ���� ���� 
 * - ������ ȸ�� + ���� ���� ���� �� ��õ ���� ���
 * */
public class BookListPanel extends JPanel {
	private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	private BookDao bookDao = new BookDao();
	private RentalDao rentalDao = new RentalDao();
	
	private String[] header = {"�����ڵ�", "�з�", "������", "����", "���ǻ�", "������", "�԰�Ǽ�", "���Ⱑ��", "�԰���", "���"};
	//private String[][] contents = bookDao.selectAllArray();
	private String[][] contents;
	
	private JTable table; // ���� ������ ������ ���̺�
	private JScrollPane scrollPanel;
	
	private Book recentBook = null; // ȸ������ ��õ�� �ֽ� ����
	
	/* ������ */
	public BookListPanel(Member member, String search) {
		/*
		 * �˻� Ű���尡 ���� ��� : ���� ��ü
		 * �˻� Ű���尡 �ִ� ��� : �˻��� ������
		 */
		if(search == null || search.equals("")) contents = bookDao.selectAllArray();
		else contents = bookDao.selectBySearchArray(search);
		
		/* ȸ�� ���� ���� 
		 * 
		 * ���� ���� ����Ʈ
		 * */
		List<Rental> rentalList = new ArrayList<>();
		String[] category = {"ö��", "����", "��ȸ����", "�ڿ�����", "�������", "����", "���", "����", "����"};
		HashMap<String, Integer> categoryCount = new HashMap<>();
		// HashMap �ʱ�ȭ : �� ī�װ� �� ���� ������ ������ 0���� �ʱ�ȭ
		for(String data : category) categoryCount.put(data, 0);
		
		if(member != null) {
			rentalList = rentalDao.selectByMemberId(member.getMemberid()); // ������ ȸ�� ���̵�� ���� ���� �˻�
			// ���� ������ �����ϴ� ��� --> �ֽ� ���� ��õ
			if(!rentalList.isEmpty()) {
				// HashMap�� ī�װ��� ���� ���� Ƚ�� ����
				for(Rental rental : rentalList) {
					Book book = bookDao.selectById(rental.getBookid()); // ���� ������ ���� �ڵ�� å ���� ã��
					categoryCount.put(book.getCategory(), categoryCount.get(book.getCategory())+1); // �ش� ī�װ� count �� 1����(������Ʈ)
				}
				List<Entry<String, Integer>> rentalCountList = new LinkedList<>(categoryCount.entrySet()); // HashMap�� List�� ����
				rentalCountList.sort(Entry.comparingByValue()); // value�� �������� ����
				String categoryResult = rentalCountList.get(rentalCountList.size() - 1).getKey(); // ������ ���� key(ī�װ�) �� ����
				List<Book> bookList = bookDao.selectByCategory(categoryResult); // ī�װ��� ���� �˻�
				
				// ���� �ֱٿ� �԰�� ���� ã��
				recentBook = bookList.get(0);
				for(Book book : bookList) {
					// ���� �˻� ���� ������ recentBook�� �԰� ��¥���� ������ ��¥�� ���
					if(book.getRecdate().isAfter(recentBook.getRecdate())) recentBook = book;
				}
				
				Font font = new Font("���� ���", Font.BOLD, 12);
				JLabel comment = new JLabel("[" + member.getName() + "���� ���� ã���ô� ī�װ��� �ֽ� ����]");
				comment.setFont(font);
				
				JPanel recentBookPanel = new JPanel(); // ���� ��õ(�ֱ� ����)�� ������ ���� �г�
				recentBookPanel.add(comment);
				
				// ��õ ���� ����
				JPanel recentBookInfoPanel = new JPanel(new GridLayout(0, 2));
				recentBookInfoPanel.add(new JLabel("�����ڵ�"));
				recentBookInfoPanel.add(new JLabel(recentBook.getBookid()));
				recentBookInfoPanel.add(new JLabel("������"));
				recentBookInfoPanel.add(new JLabel(recentBook.getTitle()));
				recentBookInfoPanel.add(new JLabel("����"));
				recentBookInfoPanel.add(new JLabel(recentBook.getAuthor()));
				recentBookInfoPanel.add(new JLabel("���ǻ�"));
				recentBookInfoPanel.add(new JLabel(recentBook.getPublisher()));
				recentBookInfoPanel.add(new JLabel("���Ⱑ��"));
				recentBookInfoPanel.add(new JLabel(Integer.toString(recentBook.getLoanable()) + "��"));
				
				recentBookInfoPanel.add(new JLabel(""));
				JButton recentBookRentalButton = new JButton("����");
				recentBookInfoPanel.add(recentBookRentalButton); // ���� ��ư
				// ��õ ���� ���� ��ư �̺�Ʈ ó��
				recentBookRentalButton.addActionListener(e -> {
					// ���� dialog
					JDialog checkDialog = new JDialog(new JFrame(), "���� ����");
					checkDialog.setSize(300, 150);
					checkDialog.add(new JLabel("[" + recentBook.getTitle() + "]�� �����Ͻðڽ��ϱ�?", JLabel.CENTER), BorderLayout.CENTER);
					
					JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
					
					// ���� ��� ��ư
					JButton cancelButton = new JButton("���");
					cancelButton.setBackground(Color.RED);
					cancelButton.addActionListener(event -> {
						checkDialog.setVisible(false);
					});
					
					// ���� ��ư --> Ŭ�� �� ���� ���� ����
					JButton okButton = new JButton("����");
					okButton.addActionListener(event -> {
						/* ���̵� ���� */
						String id;
						/* 3���� ������ ����(A~Z)�� id�� ������ */
						do {
							id = "R"; // Rental�� ���̵��� ���� �����ϱ� ����
							for(int i = 0; i < 3; i++) id += (char)((int)(Math.random() * 26) + 65);				
							for(int i = 0; i < 4; i++) id += (char)((int)(Math.random() * 10) + 48);
						} while(rentalDao.selectById(id) != null); // �����ϴ� ���̵��� �� ��߱�
						
						Rental newRental = new Rental(id, recentBook.getBookid(), 
								member.getMemberid(), LocalDateTime.now(), 
								LocalDateTime.now().plusDays(7), "N");
						/* ���� ���� ���� */
						rentalDao.insert(newRental);
						/* ���� ���� �� �� ���� */
						bookDao.updateLoanable(recentBook.getBookid(), true);	
						
						checkDialog.setVisible(false);
						
						/* ���� ���� dialog */
						JDialog rentalInfoDialog = new JDialog(new JFrame(), "���� ����");
						rentalInfoDialog.setSize(300, 300);
						rentalInfoDialog.add(new JLabel("���� ����", JLabel.CENTER), BorderLayout.NORTH);
						
						JPanel rentalInfoPanel = new JPanel(new GridLayout(4, 1));
						rentalInfoPanel.add(new JLabel("���� ���̵� : " + id));
						rentalInfoPanel.add(new JLabel("������ : " + recentBook.getTitle()));
						rentalInfoPanel.add(new JLabel("������ : " + newRental.getRentaldate()));
						rentalInfoPanel.add(new JLabel("�ݳ� ������ : " + newRental.getReturndate()));
						rentalInfoDialog.add(rentalInfoPanel, BorderLayout.CENTER);
						
						JButton checkOkButton = new JButton("Ȯ��");
						checkOkButton.addActionListener(buttonEvent -> {
							rentalInfoDialog.setVisible(false);								
						});
						rentalInfoDialog.add(checkOkButton, BorderLayout.SOUTH);
						rentalInfoDialog.setVisible(true);
					});						
					
					buttonPanel.add(cancelButton);
					buttonPanel.add(okButton);
					
					checkDialog.add(buttonPanel, BorderLayout.SOUTH);
					checkDialog.setVisible(true);
				});
				
				recentBookPanel.add(recentBookInfoPanel);
				recentBookPanel.setLayout(new BoxLayout(recentBookPanel, BoxLayout.Y_AXIS));
				this.add(recentBookPanel);
			}			
		}		
		
		table = new JTable(contents, header);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(member != null) {
					int row = table.getSelectedRow();
					int column = table.getSelectedColumn();
					
					// Ŭ���� ���� ������(���ڿ�)
					String clickedData = table.getModel().getValueAt(row, column).toString();
					
					// Ŭ���� ���� ������ ������ �� --> ���� ��ġ�� Ȯ������ ���
					if(clickedData.equals("����")) {
						// �ش� ���� ���� ������ ���ڿ� �迭�� ����
						String[] clickedRow = new String[table.getModel().getColumnCount()];
						for(int i = 0; i < table.getModel().getColumnCount(); i++)
							clickedRow[i] = table.getModel().getValueAt(row, i).toString();
						
						// ������ ������ Book ��ü�� ��ȯ
						Book clickedBook = new Book(
									clickedRow[0], // �����ڵ�
									clickedRow[1], // �з�
									clickedRow[2], // ������
									clickedRow[3], // ����
									clickedRow[4], // ���ǻ�
									LocalDateTime.parse(clickedRow[5], format), // ������
									Integer.valueOf(clickedRow[6]), // �԰�Ǽ�
									Integer.valueOf(clickedRow[7]), // ���Ⱑ��
									LocalDateTime.parse(clickedRow[8], format) // �԰���
								);
						// ���� dialog
						JDialog checkDialog = new JDialog(new JFrame(), "���� ����");
						checkDialog.setSize(300, 150);
						checkDialog.add(new JLabel("[" + clickedBook.getTitle() + "]�� �����Ͻðڽ��ϱ�?", JLabel.CENTER), BorderLayout.CENTER);
						
						JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
						
						// ���� ��� ��ư
						JButton cancelButton = new JButton("���");
						cancelButton.setBackground(Color.RED);
						cancelButton.addActionListener(event -> {
							checkDialog.setVisible(false);
						});
						
						// ���� ��ư --> Ŭ�� �� ���� ���� ����
						JButton okButton = new JButton("����");
						okButton.addActionListener(event -> {
							/* ���̵� ���� */
							String id;
							/* 3���� ������ ����(A~Z)�� id�� ������ */
							do {
								id = "R"; // Rental�� ���̵��� ���� �����ϱ� ����
								for(int i = 0; i < 3; i++) id += (char)((int)(Math.random() * 26) + 65);				
								for(int i = 0; i < 4; i++) id += (char)((int)(Math.random() * 10) + 48);
							} while(rentalDao.selectById(id) != null); // �����ϴ� ���̵��� �� ��߱�
							
							Rental newRental = new Rental(id, clickedBook.getBookid(), 
									member.getMemberid(), LocalDateTime.now(), 
									LocalDateTime.now().plusDays(7), "N");
							/* ���� ���� ���� */
							rentalDao.insert(newRental);
							/* ���� ���� �� �� ���� */
							bookDao.updateLoanable(clickedBook.getBookid(), true);	
							
							checkDialog.setVisible(false);
							
							/* ���� ���� dialog */
							JDialog rentalInfoDialog = new JDialog(new JFrame(), "���� ����");
							rentalInfoDialog.setSize(300, 300);
							rentalInfoDialog.add(new JLabel("���� ����", JLabel.CENTER), BorderLayout.NORTH);
							
							JPanel rentalInfoPanel = new JPanel(new GridLayout(4, 1));
							rentalInfoPanel.add(new JLabel("���� ���̵� : " + id));
							rentalInfoPanel.add(new JLabel("������ : " + clickedBook.getTitle()));
							rentalInfoPanel.add(new JLabel("������ : " + newRental.getRentaldate()));
							rentalInfoPanel.add(new JLabel("�ݳ� ������ : " + newRental.getReturndate()));
							rentalInfoDialog.add(rentalInfoPanel, BorderLayout.CENTER);
							
							JButton checkOkButton = new JButton("Ȯ��");
							checkOkButton.addActionListener(buttonEvent -> {
								rentalInfoDialog.setVisible(false);								
							});
							rentalInfoDialog.add(checkOkButton, BorderLayout.SOUTH);
							rentalInfoDialog.setVisible(true);							
						});						
						
						buttonPanel.add(cancelButton);
						buttonPanel.add(okButton);
						
						checkDialog.add(buttonPanel, BorderLayout.SOUTH);
						checkDialog.setVisible(true);						
					}
					
					// test
					System.out.println("���õ� �� :" + clickedData);
				} else { // ������ ȸ���� �ƴ� ��� --> member == null
					new AlertDialog("���� ����", "ȸ�� ������ �ʿ��մϴ�.");
				}
			}
		});
		
		scrollPanel = new JScrollPane(table);
		this.add(scrollPanel);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}	

} 