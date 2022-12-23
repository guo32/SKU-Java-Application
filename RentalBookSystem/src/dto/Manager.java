package dto;

import java.time.LocalDateTime;

public class Manager {
	
	private String managerid; // 관리자 아이디
	private String password; // 관리자 비밀번호
	private String name; // 관리자 이름
	private LocalDateTime regdate; // 등록 날짜
	
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
