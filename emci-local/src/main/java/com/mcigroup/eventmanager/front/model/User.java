package com.mcigroup.eventmanager.front.model;

public class User {
private int id;
private String userName;
private String userEmail;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getUserEmail() {
	return userEmail;
}
public void setUserEmail(String userEmail) {
	this.userEmail = userEmail;
}
public User(int id, String userName, String userEmail) {
	super();
	this.id = id;
	this.userName = userName;
	this.userEmail = userEmail;
}

}
