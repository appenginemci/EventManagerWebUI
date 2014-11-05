package com.mcigroup.eventmanager.front.model;

public class Event {
	private int id;
	private String eventId;
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
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public Event(int id, String eventId, String folderId) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.folderId = folderId;
	}
}
