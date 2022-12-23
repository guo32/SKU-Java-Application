package dto;

import java.time.LocalDateTime;

public class Member {

	private String memberid;
	private String name;
	private LocalDateTime birth;
	private String phone;
	private LocalDateTime regdate;
	
	public Member(String memberid, String name, LocalDateTime birth, 
			String phone, LocalDateTime regdate) {
		this.memberid = memberid;
		this.name = name;
		this.birth = birth;
		this.phone = phone;
		this.regdate = regdate;
	}

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getBirth() {
		return birth;
	}

	public void setBirth(LocalDateTime birth) {
		this.birth = birth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDateTime getRegdate() {
		return regdate;
	}

	public void setRegdate(LocalDateTime regdate) {
		this.regdate = regdate;
	}
	
}
