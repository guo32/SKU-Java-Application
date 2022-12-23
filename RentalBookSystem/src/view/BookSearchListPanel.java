package view;

import javax.swing.*;

import dao.BookDao;

/* << ���� �˻� ��� >> --> BookListPanel �����ϸ鼭 ��� ���ϴ� ����
 * 
 * JTable�� �����ؼ� �гο� �߰�
 * */
public class BookSearchListPanel extends JPanel {
	
	private BookDao bookDao = new BookDao();
	
	private String[] header = {"�����ڵ�", "�з�", "������", "����", "���ǻ�", "������", "�԰�Ǽ�", "���Ⱑ��", "���"};
	
	private JTable table; // ���� ������ ������ ���̺�
	private JScrollPane scrollPanel;
	
	public BookSearchListPanel(String search) {
		String[][] contents = bookDao.selectBySearchArray(search); // �˻� ���ǿ� �´� ���� �����͸� 2���� �迭�� ����
		table = new JTable(contents, header); // ���� �����Ϳ� header�� �����Ͽ� ���̺� ����
		scrollPanel = new JScrollPane(table);
		this.add(scrollPanel);
	}
	

}
