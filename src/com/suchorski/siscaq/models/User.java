package com.suchorski.siscaq.models;

import java.sql.Date;

public class User {
	
	private long id;
	private String cpf;
	private String displayName;
	private Date creationDate;
	private Level level;
	
	public User(long id, String cpf, String displayName, Date creationDate, Level level) {
		this.id = id;
		this.cpf = cpf;
		this.displayName = displayName;
		this.creationDate = creationDate;
		this.level = level;
	}
	
	public User(long id, String displayName) {
		this(id, "", displayName, null, null);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
	
}
