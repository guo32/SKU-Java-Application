package view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/* �� ��Ȳ�� �´� dialog ��� */
public class AlertDialog extends JDialog {

	/*
	 * dialog title�� ���뿡 �� �� ���� String �����͸� ����
	 */
	public AlertDialog(String title, String content) {
		JButton submitButton = new JButton("Ȯ��");
		submitButton.addActionListener(e -> {
			this.setVisible(false); // dialog â �ݱ�
		});
		
		this.setTitle(title);
		this.setSize(300, 150);
		
		this.add(new JLabel(content, JLabel.CENTER), BorderLayout.CENTER);
		this.add(submitButton, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
}
