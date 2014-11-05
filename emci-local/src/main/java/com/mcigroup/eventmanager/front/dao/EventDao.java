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

public class EventDao {
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
	public Collection<Event> getEventByUser(User user) {
		Connection conn = getConnection();
		List<Event> events = new ArrayList<Event>();
		try {
			try {
				String statement = "SELECT event.eventId, event.id, event.folderId FROM event, eventmember where eventmember.event_id=event.id and eventmember.user_id = ?";
				PreparedStatement stmt;

				stmt = conn.prepareStatement(statement);

				stmt.setInt(1, user.getId());
				System.err.println("in getEventByUser with User id = " + user.getId());
				ResultSet resultSet = stmt.executeQuery();
				while (resultSet.next()) {
					System.out.println("folderId = "
							+ resultSet.getString("folderId"));
					events.add(new Event(resultSet.getInt("id"), resultSet
							.getString("eventId"), resultSet
							.getString("folderId")));
				}
			} finally {
				conn.close();
			}
		} catch (SQLException e1) {
			System.err.println("connection error");
		}
		return events;
	}
}
