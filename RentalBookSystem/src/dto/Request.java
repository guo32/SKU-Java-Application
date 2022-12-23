package dto;

import java.time.LocalDateTime;

public class Request {
	
	private int requestid;
	private String booktitle;
	private String author;
	private String publisher;
	private String memberid;
	private LocalDateTime reqdate;
	private String status;
	
	public Request(String booktitle, String author,
			String publisher, String memberid, LocalDateTime reqdate,
			String status) {
		this.booktitle = booktitle;
		this.author = author;
		this.publisher = publisher;
		this.memberid = memberid;
		this.reqdate = reqdate;
		this.status = status;
	}
	
	public Request(int requestid, String booktitle, String author,
			String publisher, String memberid, LocalDateTime reqdate,
			String status) {
		this.requestid = requestid;
		this.booktitle = booktitle;
		this.author = author;
		this.publisher = publisher;
		this.memberid = memberid;
		this.reqdate = reqdate;
		this.status = status;
	}

	public int getRequestid() {
		return requestid;
	}

	public void setRequestid(int requestid) {
		this.requestid = requestid;
	}

	public String getBooktitle() {
		return booktitle;
	}

	public void setBooktitle(String booktitle) {
		this.booktitle = booktitle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}

	public LocalDateTime getReqdate() {
		return reqdate;
	}

	public void setReqdate(LocalDateTime reqdate) {
		this.reqdate = reqdate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
