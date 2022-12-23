package view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/* 각 상황에 맞는 dialog 출력 */
public class AlertDialog extends JDialog {

	/*
	 * dialog title과 내용에 들어갈 두 개의 String 데이터를 받음
	 */
	public AlertDialog(String title, String content) {
		JButton submitButton = new JButton("확인");
		submitButton.addActionListener(e -> {
			this.setVisible(false); // dialog 창 닫기
		});
		
		this.setTitle(title);
		this.setSize(300, 150);
		
		this.add(new JLabel(content, JLabel.CENTER), BorderLayout.CENTER);
		this.add(submitButton, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
}
