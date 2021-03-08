package com.suchorski.siscaq.models;

public class Unity {
	
	private long id;
	private String initials;
	
	public Unity(long id, String initials) {
		this.id = id;
		this.initials = initials;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getInitials() {
		return initials;
	}
	
	public void setInitials(String initials) {
		this.initials = initials;
	}

}
