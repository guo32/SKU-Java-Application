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
 * 도서 반납 폼을 담고 있는 패널
 * */
public class BookReturnFormPanel extends JPanel {
	
	private RentalDao rentalDao = new RentalDao();
	private BookDao bookDao = new BookDao();
	
	private JTextField idInput = new JTextField(20);
	private JButton submitButton = new JButton("반납");
	
	/*
	 * [ 생성자 ]
	 * 
	 * - 폼 구현
	 * - 반납 버튼 누르면 대출 아이디 검색
	 * - 검색 결과 대출 정보가 존재한다면 반납 처리
	 * 
	 * */
	public BookReturnFormPanel() {
		Font font = new Font("맑은 고딕", Font.BOLD, 19);
		JLabel formTitle = new JLabel("도서 반납", JLabel.CENTER);
		formTitle.setFont(font);
		
		this.setLayout(new GridLayout(4, 1));
		this.add(formTitle);
		this.add(new JLabel("도서 대출 시 발급받은 코드를 입력해주세요.", JLabel.CENTER));
		this.add(idInput);
		
		submitButton.addActionListener(e -> {
			String id = idInput.getText();
			Rental rental = rentalDao.selectById(id);
			if(rental != null) {
				bookDao.updateLoanable(rental.getBookid(), false); // 도서 대출 가능 권 수 수정
				// 대출 상태 변경 후 boolean 값 반환
				if(!rentalDao.updateStatus(rental.getRentalid(), true)) new AlertDialog("도서 반납", "유효하지 않습니다.");
				else new AlertDialog("도서 반납", "정상적으로 처리되었습니다.");
			} else new AlertDialog("도서 반납", "유효하지 않습니다.");
		});
		
		this.add(submitButton);
	}

}
