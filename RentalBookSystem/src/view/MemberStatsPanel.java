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

/* << 회원 관련 통계를 보여줄 Panel >>
 * 
 * - 대출(시스템 사용)이 많은 사용자
 * - 현재 연체 중인 도서가 많은 사용자
 * 
 * */
public class MemberStatsPanel extends JPanel {
	
	private RentalDao rentalDao = new RentalDao();
	private MemberDao memberDao = new MemberDao(); // 전체 회원 ID 정보 획득에 사용
	private RequestDao requestDao = new RequestDao();
	
	/* 버튼 부분 */
	private JPanel buttonAreaPanel;
	private JButton maxRentalMemberButton = new JButton("최다 이용 회원");
	private JButton maxOverdueMemberButton = new JButton("최다 연체 회원");
	
	/* 컨텐츠 부분 */
	private JPanel contentsAreaPanel;
	
	public MemberStatsPanel() {
		buttonAreaPanel = new JPanel(new GridLayout(1, 2));
		buttonAreaPanel.add(maxRentalMemberButton);
		buttonAreaPanel.add(maxOverdueMemberButton);
		
		contentsAreaPanel = new JPanel();
		contentsAreaPanel.add(new JLabel("메뉴를 선택하세요.", JLabel.CENTER));
		
		/* 최다 이용 회원 버튼 이벤트 처리 */
		maxRentalMemberButton.addActionListener(e -> {
			// 회원 별 대출 횟수
			List<Entry<String, Integer>> memberList =  rentalDao.countRentalMember(memberDao.selectAllMemberid(), false);
			// 회원 별 도서 신청 횟수
			HashMap<String, Integer> useSystemMap = requestDao.countRequestBookMember(memberDao.selectAllMemberid());
			
			// 대출 횟수 + 도서 신청 횟수
			for(Entry<String, Integer> data : memberList) {
				int use = useSystemMap.get(data.getKey());
				useSystemMap.put(data.getKey(), data.getValue() + use);
			}
			List<Entry<String, Integer>> useSystemList = new LinkedList<>(useSystemMap.entrySet());
			// 정렬
			useSystemList.sort(Entry.comparingByValue());
			Collections.reverse(useSystemList);
			
			String[] header = {"순위", "회원코드", "이름", "생일", "전화번호", "가입일", "이용횟수"}; // 7개 항목
			String[][] contents = new String[useSystemList.size()][header.length];
			for(int i = 0; i < useSystemList.size(); i++) {
				Member member = memberDao.selectById(useSystemList.get(i).getKey());
				String[] content = {Integer.toString(i+1) + "위", member.getMemberid(), member.getName(), member.getBirth().toString(),
						member.getPhone(), member.getRegdate().toString(), useSystemList.get(i).getValue().toString() + "회"};
				for(int j = 0; j < content.length; j++) contents[i][j] = content[j];
			}
			JTable table = new JTable(contents, header);
			JScrollPane scrollPanel = new JScrollPane(table);
			
			contentsAreaPanel.removeAll();
			contentsAreaPanel.add(scrollPanel);
			contentsAreaPanel.revalidate();
			contentsAreaPanel.repaint();
		});
		
		/* 최다 연체 회원(연체 중) 버튼 이벤트 처리 */
		maxOverdueMemberButton.addActionListener(e -> {
			List<Entry<String, Integer>> memberList =  rentalDao.countRentalMember(memberDao.selectAllMemberid(), true);
			String[] header = {"순위", "회원코드", "이름", "생일", "전화번호", "가입일", "연체 중인 도서 수"}; // 7개 항목
			String[][] contents = new String[memberList.size()][header.length];
			for(int i = 0; i < memberList.size(); i++) {
				Member member = memberDao.selectById(memberList.get(i).getKey());
				String[] content = {Integer.toString(i+1) + "위", member.getMemberid(), member.getName(), member.getBirth().toString(),
						member.getPhone(), member.getRegdate().toString(), memberList.get(i).getValue().toString() + "권"};
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
