package dto;

import java.time.LocalDateTime;

public class Book {
	
	private String bookid; // ���� ���̵�
	private String category; // ���� ī�װ�
	private String title; // ���� ����
	private String author; // �۰�
	private String publisher; // ���ǻ�
	private LocalDateTime pubdate; // ������
	private int possession; // ���� �� ��
	private int loanable; // ���� ���� �� ��
	private LocalDateTime recdate; // �԰���
	
	/* ���� ������ */
	public Book(String bookid, String category, String title, 
			String author, String publisher, LocalDateTime pubdate,
			int possession, int loanable, LocalDateTime recdate) {
		this.bookid = bookid;
		this.category = category;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.pubdate = pubdate;
		this.possession = possession;
		this.loanable = loanable;
		this.recdate = recdate;
	}

	public String getBookid() {
		return bookid;
	}

	public void setBookid(String bookid) {
		this.bookid = bookid;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public LocalDateTime getPubdate() {
		return pubdate;
	}

	public void setPubdate(LocalDateTime pubdate) {
		this.pubdate = pubdate;
	}

	public int getPossession() {
		return possession;
	}

	public void setPossession(int possession) {
		this.possession = possession;
	}

	public int getLoanable() {
		return loanable;
	}

	public void setLoanable(int loanable) {
		this.loanable = loanable;
	}

	public LocalDateTime getRecdate() {
		return recdate;
	}

	public void setRecdate(LocalDateTime recdate) {
		this.recdate = recdate;
	}	

}
