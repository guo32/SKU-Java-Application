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
	
	private String[] header = {"no.", "도서명", "저자", "출판사", "신청자", "신청일", "상태", "비고"};
	private String[][] contents;
	
	private JTable table; // 신청 정보를 저장할 테이블
	private JScrollPane scrollPanel;
	
	public RequestListPanel() {
		Font font = new Font("맑은 고딕", Font.BOLD, 19); // 폰트
		contents = requestDao.selectAllArray();
		table = new JTable(contents, header);
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				int column = table.getSelectedColumn();
				
				// 클릭된 셀의 데이터(문자열)
				String clickedData = table.getModel().getValueAt(row, column).toString();
				
				if(clickedData.equals("입고")) {
					// 클릭된 행의 신청 아이디
					int clickedId = Integer.parseInt(table.getModel().getValueAt(row, 0).toString());
					Request request = requestDao.selectById(clickedId);
					
					/* 도서 입고 dialog */
					JDialog bookRegistDialog = new JDialog(new JFrame(), "도서 입고");
					
					/* 도서 입고 폼 */
					// 입력란
					JTextField titleInput, authorInput, publisherInput, pubdateInput, possessionInput;
					// 콤보 박스(카테고리)
					JComboBox categoryCombo = new JComboBox();
					String[] categoryData = {"철학", "종교", "사회과학", "자연과학", "기술과학", "예술", "언어", "문학", "역사"};
					for(String data : categoryData)	categoryCombo.addItem(data);
					// 버튼
					JButton cancelButton = new JButton("취소");
					JButton submitButton = new JButton("입고");
					// 입고 폼 패널
					JPanel bookRegistFormPanel = new JPanel(new GridLayout(0, 2));
					
					// 폼 구성
					bookRegistFormPanel.add(new JLabel("카테고리"));
					bookRegistFormPanel.add(categoryCombo); // 콤보
					bookRegistFormPanel.add(new JLabel("도서명"));
					bookRegistFormPanel.add(titleInput = new JTextField(request.getBooktitle()));
					bookRegistFormPanel.add(new JLabel("저자"));
					bookRegistFormPanel.add(authorInput = new JTextField(request.getAuthor()));
					bookRegistFormPanel.add(new JLabel("출판사"));
					bookRegistFormPanel.add(publisherInput = new JTextField(request.getPublisher()));
					bookRegistFormPanel.add(new JLabel("출판일"));
					bookRegistFormPanel.add(pubdateInput = new JTextField(20));
					bookRegistFormPanel.add(new JLabel("수량"));
					bookRegistFormPanel.add(possessionInput = new JTextField(20));
					cancelButton.setBackground(Color.red);
					bookRegistFormPanel.add(cancelButton);
					submitButton.setBackground(Color.cyan);
					bookRegistFormPanel.add(submitButton);
										
					JLabel formTitle = new JLabel("도서 입고", JLabel.CENTER);
					formTitle.setFont(font);
					
					bookRegistDialog.add(formTitle, BorderLayout.NORTH);
					bookRegistDialog.add(bookRegistFormPanel, BorderLayout.CENTER);
					
					/* 취소 버튼 이벤트 처리 */
					cancelButton.addActionListener(event -> {
						bookRegistDialog.setVisible(false);
					});
					
					/* 입고 버튼 이벤트 처리 */
					submitButton.addActionListener(event -> {
						/* 아이디 생성 */
						String id = "";
						/* 3개의 랜덤한 문자(A~Z)를 id에 덧붙임 */
						do {
							id = "";
							for(int i = 0; i < 3; i++) id += (char)((int)(Math.random() * 26) + 65);				
							for(int i = 0; i < 4; i++) id += Integer.toString((int)(Math.random() * 10));
						} while(bookDao.selectById(id) != null); // 존재하는 아이디일 때 재발급
						
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
						new AlertDialog("도서 입고", "도서가 입고되었습니다.");
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
