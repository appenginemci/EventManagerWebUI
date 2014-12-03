package com.mcigroup.eventmanager.front.model;

public class Event {
	private int id;
	private String folderId;
	private String newFolderId;
	private String closedFolderId;
	public String getClosedFolderId() {
		return closedFolderId;
	}
	public void setClosedFolderId(String closedFolderId) {
		this.closedFolderId = closedFolderId;
	}
	private String eventName;
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getNewFolderId() {
		return newFolderId;
	}
	public void setNewFolderId(String newFolderId) {
		this.newFolderId = newFolderId;
	}
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
	public Event(int id, String folderId, String newFolderId, String eventName, String closedFolderId) {
		super();
		this.id = id;
		this.folderId = folderId;
		this.newFolderId = newFolderId;
		this.eventName = eventName;
		this.closedFolderId = closedFolderId;
	}
}
