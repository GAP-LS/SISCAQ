package com.suchorski.siscaq.models;

public class Level {
	
	private long id;
	private long level;
	private String description;
	
	public Level(long id, long level, String description) {
		this.id = id;
		this.level = level;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLevel() {
		return level;
	}

	public void setLevel(long level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}

}
