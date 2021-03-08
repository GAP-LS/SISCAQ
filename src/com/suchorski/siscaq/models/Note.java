package com.suchorski.siscaq.models;

import java.sql.Date;

public class Note {
	
	private long id;
	private String note;
	private Date creationDate;
	private Status status;
	private User user;
	
	public Note(long id, Status status, User user, Date creationDate, String note) {
		this.id = id;
		this.note = note;
		this.creationDate = creationDate;
		this.status = status;
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
