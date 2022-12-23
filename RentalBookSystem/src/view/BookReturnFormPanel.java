package view;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.BookDao;
import dao.RentalDao;
import dto.Book;
import dto.Rental;

/*
 * ���� �ݳ� ���� ��� �ִ� �г�
 * */
public class BookReturnFormPanel extends JPanel {
	
	private RentalDao rentalDao = new RentalDao();
	private BookDao bookDao = new BookDao();
	
	private JTextField idInput = new JTextField(20);
	private JButton submitButton = new JButton("�ݳ�");
	
	/*
	 * [ ������ ]
	 * 
	 * - �� ����
	 * - �ݳ� ��ư ������ ���� ���̵� �˻�
	 * - �˻� ��� ���� ������ �����Ѵٸ� �ݳ� ó��
	 * 
	 * */
	public BookReturnFormPanel() {
		Font font = new Font("���� ���", Font.BOLD, 19);
		JLabel formTitle = new JLabel("���� �ݳ�", JLabel.CENTER);
		formTitle.setFont(font);
		
		this.setLayout(new GridLayout(4, 1));
		this.add(formTitle);
		this.add(new JLabel("���� ���� �� �߱޹��� �ڵ带 �Է����ּ���.", JLabel.CENTER));
		this.add(idInput);
		
		submitButton.addActionListener(e -> {
			String id = idInput.getText();
			Rental rental = rentalDao.selectById(id);
			if(rental != null) {
				bookDao.updateLoanable(rental.getBookid(), false); // ���� ���� ���� �� �� ����
				// ���� ���� ���� �� boolean �� ��ȯ
				if(!rentalDao.updateStatus(rental.getRentalid(), true)) new AlertDialog("���� �ݳ�", "��ȿ���� �ʽ��ϴ�.");
				else new AlertDialog("���� �ݳ�", "���������� ó���Ǿ����ϴ�.");
			} else new AlertDialog("���� �ݳ�", "��ȿ���� �ʽ��ϴ�.");
		});
		
		this.add(submitButton);
	}

}
