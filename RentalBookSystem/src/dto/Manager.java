package dto;

import java.time.LocalDateTime;

public class Manager {
	
	private String managerid; // ������ ���̵�
	private String password; // ������ ��й�ȣ
	private String name; // ������ �̸�
	private LocalDateTime regdate; // ��� ��¥
	
	public Manager(String managerid, String password, String name, LocalDateTime regdate) {
		this.managerid = managerid;
		this.password = password;
		this.name = name;
		this.regdate = regdate;
	}

	public String getManagerid() {
		return managerid;
	}

	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getRegdate() {
		return regdate;
	}

	public void setRegdate(LocalDateTime regdate) {
		this.regdate = regdate;
	}

}
