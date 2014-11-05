package com.mcigroup.eventmanager.front.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.appengine.api.utils.SystemProperty;
import com.mcigroup.eventmanager.front.model.Event;
import com.mcigroup.eventmanager.front.model.User;

public class UserDao {
	private static Connection getConnection() {
		String url = null;
		Connection conn = null;
		try {
			if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
				// Load the class that provides the new "jdbc:google:mysql://"
				// prefix.
				System.err.println("get SQL cloud connection");
				Class.forName("com.mysql.jdbc.GoogleDriver");
				url = "jdbc:google:mysql://bright-folder-720:eventmgr/eventmanager";
			} else {
				// Local MySQL instance to use during development.
				System.err.println("Try to establish local connection with DB");
				Class.forName("com.mysql.jdbc.Driver");
				url = "jdbc:mysql://127.0.0.1:3306/eventmanager";

			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
		try {
			conn = DriverManager.getConnection(url, "evtmgradmin", "sogeTTi$00");
			return conn;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} 
		return conn;
	}
	
	public User getUserByEmail(String userEmail) {
		Connection conn = getConnection();
		try {
			try{
			String statement = "SELECT me.id,me.userName,me.userId FROM member me where me.userId = ?";
			PreparedStatement stmt;

			stmt = conn.prepareStatement(statement);

			stmt.setString(1, userEmail);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				System.out.println("userId = " + resultSet.getInt("id"));
				return new User(resultSet.getInt("id"),resultSet.getString("userName"),userEmail);
				
			}
			}finally {
				conn.close();
			}
	} catch (SQLException e1) {
		System.err.println("connection error");
	}
		return null;
	}
	
	public Collection<User> getUsersForEvent(Event event) {
		Connection conn = getConnection();
		List<User> userList = new ArrayList<User>();
		try {
			try{
				String statement = "SELECT me.id,me.userId,me.userName FROM member me, eventmember em where em.user_id=me.id and em.event_id = ?";
				PreparedStatement stmt;

				stmt = conn.prepareStatement(statement);
				stmt.setInt(1, event.getId());
				ResultSet resultSet = stmt.executeQuery();
				while (resultSet.next()) {
					System.out.println("userId = " + resultSet.getInt("id"));
					userList.add(new User(resultSet.getInt("id"),resultSet.getString("userName"),resultSet.getString("userId")));
				
				}
			}finally {
				conn.close();
			}
		} catch (SQLException e1) {
			System.err.println("connection error");
		}
		return userList;
	}
}
