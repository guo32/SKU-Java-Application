package view;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import dao.BookDao;
import dao.RentalDao;
import dto.Book;

/* << ���� ���� ��踦 ������ Panel >> 
 * 
 * - �ִ� ���� ����
 * - �̹ݳ� ����(��ü ����) --> rentalDao ������ �Լ� ���� �Ϸ�
 * - ��ü ���� --> rentalDao ������ �Լ� ���� �Ϸ�
 * 
 * ��ư�� ����� ��Ȳ�� �´� ��踦 ������ �� �ֵ��� ����
 * --> �� ��ư�� �г��� �����Ͽ� �����ų ��
 * --> ��ư�� ��ܿ� (GridLayout)
 * */
public class BookStatsPanel extends JPanel {
	
	private RentalDao rentalDao = new RentalDao();
	private BookDao bookDao = new BookDao();
	
	/* ��ư �κ� */
	private JPanel buttonAreaPanel;
	private JButton maxRentalButton = new JButton("�ִ� ���� ����");
	private JButton onLoanButton = new JButton("�̹ݳ� ����");
	private JButton overdueButton = new JButton("��ü ����");
	
	/* ������ �κ� --> table �ֱ� */
	private JPanel contentsAreaPanel;
	private JPanel maxLentaledBookPanel;
	// private JPanel onLoanPanel;
	// private JPanel overduePanel;
	
	/* table */
	private String[] header = {"��û�ڵ�", "�����ڵ�", "ȸ���ڵ�", "������", "�ݳ�������", "����"};
	private String[][] contents;
	
	private JTable table; // ��û ������ ������ ���̺�
	private JScrollPane scrollPanel;
	
	public BookStatsPanel() {
		buttonAreaPanel = new JPanel(new GridLayout(1, 3));
		buttonAreaPanel.add(maxRentalButton);
		buttonAreaPanel.add(onLoanButton);
		buttonAreaPanel.add(overdueButton);
		
		contentsAreaPanel = new JPanel();
		contentsAreaPanel.add(new JLabel("�޴��� �����ϼ���.", JLabel.CENTER));
		
		/* �ִ� ���� ���� ��ư �̺�Ʈ ó�� */
		maxRentalButton.addActionListener(e -> {			
			ArrayList<String> bookIdList = bookDao.selectAllBookid();
			// �����ڵ庰 �����ڵ�, ����Ǿ��� Ƚ��
			HashMap<String, Integer> countBookIdMap = rentalDao.countRentaledBook(bookIdList);
			
			// HashMap�� List�� ����
			List<Entry<String, Integer>> countBookIdList = new LinkedList<>(countBookIdMap.entrySet());
			countBookIdList.sort(Entry.comparingByValue());
			Collections.reverse(countBookIdList); // �������� ����
			
			// ���� Ƚ���� ���� ���Ҵ� ������ ���� ���
			Book maxLentaledBook = bookDao.selectById(countBookIdList.get(0).getKey());
			// String[] bookData = {maxLentaledBook.getBookid(), maxLentaledBook.getTitle(),
					// maxLentaledBook.getAuthor(), maxLentaledBook.getPublisher(), maxLentaledBook.getPubdate().toString()};
			maxLentaledBookPanel = new JPanel();
			JPanel maxLentaledBookInfoPanel = new JPanel(new GridLayout(0, 2));
			maxLentaledBookInfoPanel.add(new JLabel("�����ڵ�"));
			maxLentaledBookInfoPanel.add(new JLabel(maxLentaledBook.getBookid()));
			maxLentaledBookInfoPanel.add(new JLabel("������"));
			maxLentaledBookInfoPanel.add(new JLabel(maxLentaledBook.getTitle()));
			maxLentaledBookInfoPanel.add(new JLabel("����"));
			maxLentaledBookInfoPanel.add(new JLabel(maxLentaledBook.getAuthor()));
			maxLentaledBookInfoPanel.add(new JLabel("���ǻ�"));
			maxLentaledBookInfoPanel.add(new JLabel(maxLentaledBook.getPublisher()));
			maxLentaledBookInfoPanel.add(new JLabel("����Ƚ��"));
			maxLentaledBookInfoPanel.add(new JLabel(countBookIdList.get(0).getValue().toString() + "ȸ"));
			
			Font font = new Font("���� ���", Font.BOLD, 14);
			JLabel comment = new JLabel("[���� ���� ����Ǿ��� ����]");
			comment.setFont(font);
			
			maxLentaledBookPanel.add(comment);
			maxLentaledBookPanel.add(maxLentaledBookInfoPanel);
			maxLentaledBookPanel.setLayout(new BoxLayout(maxLentaledBookPanel, BoxLayout.Y_AXIS));
			
			// ���� Ƚ�� �������� table �����ؼ� �߰��ϱ�
			String[] header = {"����", "�����ڵ�", "������", "����", "���ǻ�", "����Ƚ��"}; // 6�� �׸�
			String[][] contents = new String[countBookIdList.size()][header.length];
			for(int i = 0; i < countBookIdList.size(); i++) {
				Book book = bookDao.selectById(countBookIdList.get(i).getKey());
				String[] content = {Integer.toString(i+1) + "��", 
						book.getBookid(), book.getTitle(), book.getAuthor(), 
						book.getPublisher(), Integer.toString(countBookIdList.get(i).getValue()) + "ȸ"};
				for(int j = 0; j < content.length; j++) contents[i][j] = content[j];
			}
			JTable table = new JTable(contents, header);
			JScrollPane scrollPanel = new JScrollPane(table);
			
			contentsAreaPanel.removeAll();
			contentsAreaPanel.add(maxLentaledBookPanel);
			contentsAreaPanel.add(scrollPanel);
			contentsAreaPanel.setLayout(new BoxLayout(contentsAreaPanel, BoxLayout.Y_AXIS));
			contentsAreaPanel.revalidate();
			contentsAreaPanel.repaint();
		});
		
		/* �̹ݳ� ���� ��ư �̺�Ʈ ó�� */
		onLoanButton.addActionListener(e -> {
			contents = rentalDao.selectBooksOnLoan(false); // false --> �̹ݳ� + ��ü ����
			table = new JTable(contents, header);
			scrollPanel = new JScrollPane(table);
			
			contentsAreaPanel.removeAll();
			contentsAreaPanel.add(scrollPanel);
			contentsAreaPanel.revalidate();
			contentsAreaPanel.repaint();
		});
		
		/* ��ü ���� ��ư �̺�Ʈ ó�� */
		overdueButton.addActionListener(e -> {
			contents = rentalDao.selectBooksOnLoan(true); // true --> ��ü ������ 
			table = new JTable(contents, header);
			scrollPanel = new JScrollPane(table);
			
			contentsAreaPanel.removeAll();
			contentsAreaPanel.add(scrollPanel);
			contentsAreaPanel.revalidate();
			contentsAreaPanel.repaint();
		});
		
		this.add(buttonAreaPanel);
		this.add(contentsAreaPanel);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));		
	}

}
