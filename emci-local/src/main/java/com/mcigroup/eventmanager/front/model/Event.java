package com.mcigroup.eventmanager.front.model;

public class Event {
	private int id;
	private String folderId;
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Event(int id, String folderId) {
		super();
		this.id = id;
		this.folderId = folderId;
	}
}
