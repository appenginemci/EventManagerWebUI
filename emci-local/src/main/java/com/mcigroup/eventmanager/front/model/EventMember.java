package com.mcigroup.eventmanager.front.model;

public class EventMember {
private User user;
private Event event;
private String forApprovalFolderId;
private String inProgressFolderId;




public User getUser() {
	return user;
}



public void setUser(User user) {
	this.user = user;
}



public Event getEvent() {
	return event;
}



public void setEvent(Event event) {
	this.event = event;
}



public String getForApprovalFolderId() {
	return forApprovalFolderId;
}



public void setForApprovalFolderId(String forApprovalFolderId) {
	this.forApprovalFolderId = forApprovalFolderId;
}



public String getInProgressFolderId() {
	return inProgressFolderId;
}



public void setInProgressFolderId(String inProgressFolderId) {
	this.inProgressFolderId = inProgressFolderId;
}



public EventMember(User user, Event event, String inProgressFolderId ,String forApprovalFolderId ) {
	super();
	this.user = user;
	this.event = event;
	this.inProgressFolderId = inProgressFolderId;
	this.forApprovalFolderId = forApprovalFolderId;
}

}
