package view;

import java.awt.GridLayout;
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

import dao.MemberDao;
import dao.RentalDao;
import dao.RequestDao;
import dto.Member;

/* << ȸ�� ���� ��踦 ������ Panel >>
 * 
 * - ����(�ý��� ���)�� ���� �����
 * - ���� ��ü ���� ������ ���� �����
 * 
 * */
public class MemberStatsPanel extends JPanel {
	
	private RentalDao rentalDao = new RentalDao();
	private MemberDao memberDao = new MemberDao(); // ��ü ȸ�� ID ���� ȹ�濡 ���
	private RequestDao requestDao = new RequestDao();
	
	/* ��ư �κ� */
	private JPanel buttonAreaPanel;
	private JButton maxRentalMemberButton = new JButton("�ִ� �̿� ȸ��");
	private JButton maxOverdueMemberButton = new JButton("�ִ� ��ü ȸ��");
	
	/* ������ �κ� */
	private JPanel contentsAreaPanel;
	
	public MemberStatsPanel() {
		buttonAreaPanel = new JPanel(new GridLayout(1, 2));
		buttonAreaPanel.add(maxRentalMemberButton);
		buttonAreaPanel.add(maxOverdueMemberButton);
		
		contentsAreaPanel = new JPanel();
		contentsAreaPanel.add(new JLabel("�޴��� �����ϼ���.", JLabel.CENTER));
		
		/* �ִ� �̿� ȸ�� ��ư �̺�Ʈ ó�� */
		maxRentalMemberButton.addActionListener(e -> {
			// ȸ�� �� ���� Ƚ��
			List<Entry<String, Integer>> memberList =  rentalDao.countRentalMember(memberDao.selectAllMemberid(), false);
			// ȸ�� �� ���� ��û Ƚ��
			HashMap<String, Integer> useSystemMap = requestDao.countRequestBookMember(memberDao.selectAllMemberid());
			
			// ���� Ƚ�� + ���� ��û Ƚ��
			for(Entry<String, Integer> data : memberList) {
				int use = useSystemMap.get(data.getKey());
				useSystemMap.put(data.getKey(), data.getValue() + use);
			}
			List<Entry<String, Integer>> useSystemList = new LinkedList<>(useSystemMap.entrySet());
			// ����
			useSystemList.sort(Entry.comparingByValue());
			Collections.reverse(useSystemList);
			
			String[] header = {"����", "ȸ���ڵ�", "�̸�", "����", "��ȭ��ȣ", "������", "�̿�Ƚ��"}; // 7�� �׸�
			String[][] contents = new String[useSystemList.size()][header.length];
			for(int i = 0; i < useSystemList.size(); i++) {
				Member member = memberDao.selectById(useSystemList.get(i).getKey());
				String[] content = {Integer.toString(i+1) + "��", member.getMemberid(), member.getName(), member.getBirth().toString(),
						member.getPhone(), member.getRegdate().toString(), useSystemList.get(i).getValue().toString() + "ȸ"};
				for(int j = 0; j < content.length; j++) contents[i][j] = content[j];
			}
			JTable table = new JTable(contents, header);
			JScrollPane scrollPanel = new JScrollPane(table);
			
			contentsAreaPanel.removeAll();
			contentsAreaPanel.add(scrollPanel);
			contentsAreaPanel.revalidate();
			contentsAreaPanel.repaint();
		});
		
		/* �ִ� ��ü ȸ��(��ü ��) ��ư �̺�Ʈ ó�� */
		maxOverdueMemberButton.addActionListener(e -> {
			List<Entry<String, Integer>> memberList =  rentalDao.countRentalMember(memberDao.selectAllMemberid(), true);
			String[] header = {"����", "ȸ���ڵ�", "�̸�", "����", "��ȭ��ȣ", "������", "��ü ���� ���� ��"}; // 7�� �׸�
			String[][] contents = new String[memberList.size()][header.length];
			for(int i = 0; i < memberList.size(); i++) {
				Member member = memberDao.selectById(memberList.get(i).getKey());
				String[] content = {Integer.toString(i+1) + "��", member.getMemberid(), member.getName(), member.getBirth().toString(),
						member.getPhone(), member.getRegdate().toString(), memberList.get(i).getValue().toString() + "��"};
				for(int j = 0; j < content.length; j++) contents[i][j] = content[j];
			}
			JTable table = new JTable(contents, header);
			JScrollPane scrollPanel = new JScrollPane(table);
			
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
