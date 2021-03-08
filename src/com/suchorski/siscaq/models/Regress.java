package com.suchorski.siscaq.models;

import java.sql.Date;

public class Regress {
	
	private long id;
	private Status status;
	private User user;
	private Date date;
	private String description;
	
	public Regress(long id, Status status, User user, Date date, String description) {
		this.id = id;
		this.status = status;
		this.user = user;
		this.date = date;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
