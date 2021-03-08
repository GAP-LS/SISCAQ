package com.suchorski.siscaq.models;

import java.sql.Date;

public class Status {
	
	private long id;
    private String description;
    private long days;
    private Date date;
    
	public Status(long id, String description, long days, Date date) {
		this.id = id;
		this.description = description;
		this.days = days;
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getDays() {
		return days;
	}

	public void setDays(long days) {
		this.days = days;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
}
