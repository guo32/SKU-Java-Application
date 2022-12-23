package dto;

import java.time.LocalDateTime;

public class Rental {
	
	private String rentalid;
	private String bookid;
	private String memberid;
	private LocalDateTime rentaldate;
	private LocalDateTime returndate;
	private String status;
	
	public Rental(String rentalid, String bookid, String memberid,
			LocalDateTime rentaldate, LocalDateTime returndate,
			String status) {
		this.rentalid = rentalid;
		this.bookid = bookid;
		this.memberid = memberid;
		this.rentaldate = rentaldate;
		this.returndate = returndate;
		this.status = status;
	}

	public String getRentalid() {
		return rentalid;
	}

	public void setRentalid(String rentalid) {
		this.rentalid = rentalid;
	}

	public String getBookid() {
		return bookid;
	}

	public void setBookid(String bookid) {
		this.bookid = bookid;
	}

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}

	public LocalDateTime getRentaldate() {
		return rentaldate;
	}

	public void setRentaldate(LocalDateTime rentaldate) {
		this.rentaldate = rentaldate;
	}

	public LocalDateTime getReturndate() {
		return returndate;
	}

	public void setReturndate(LocalDateTime returndate) {
		this.returndate = returndate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
