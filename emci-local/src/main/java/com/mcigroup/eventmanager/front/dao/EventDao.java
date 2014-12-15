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
import com.mcigroup.eventmanager.front.helper.ConnectionUtil;
import com.mcigroup.eventmanager.front.model.Event;
import com.mcigroup.eventmanager.front.model.EventMember;
import com.mcigroup.eventmanager.front.model.User;

public class EventDao {
	public Collection<Event> getEventByUser(User user) {
		Connection conn = ConnectionUtil.getConnection();
		List<Event> events = new ArrayList<Event>();
		try {
			try {
				String statement = "SELECT event.id, event.folderId, event.inboxNewFolderId, event.eventName, event.closedFolderId FROM event, eventmember where eventmember.event_id=event.id and eventmember.user_id = ? and eventmember.active=1";
				PreparedStatement stmt;

				stmt = conn.prepareStatement(statement);

				stmt.setInt(1, user.getId());
//				System.err.println("in getEventByUser with User id = " + user.getId());
				ResultSet resultSet = stmt.executeQuery();
				while (resultSet.next()) {
//					System.out.println("folderId = "
//							+ resultSet.getString("folderId"));
					events.add(new Event(resultSet.getInt("id"), resultSet.getString("folderId"),resultSet
							.getString("inboxNewFolderId"),resultSet
							.getString("eventName"), resultSet.getString("closedFolderId") ));
				}
			} finally {
				conn.close();
			}
		} catch (SQLException e1) {
//			System.err.println("connection error");
		}
		return events;
	}
	
	public Collection<EventMember> getEventMemberForUser(User user) {
		Connection conn = ConnectionUtil.getConnection();
		//List<User> userList = new ArrayList<User>();
		List<EventMember> userList = new ArrayList<EventMember>();
		try {
			try{
				String statement = "SELECT  event.id, event.folderId, event.inboxNewFolderId, event.eventName, event.closedFolderId, em.in_progress_folder_id, em.for_approval_folder_id, em.role FROM event event, eventmember em where em.event_id=event.id and em.user_id = ? and em.active=1";
				PreparedStatement stmt;

				stmt = conn.prepareStatement(statement);
				stmt.setInt(1, user.getId());
				ResultSet resultSet = stmt.executeQuery();
				while (resultSet.next()) {
//					System.out.println("userId = " + resultSet.getInt("id"));
					//userList.add(new User(resultSet.getInt("id"),resultSet.getString("userName"),resultSet.getString("userId")));
					userList.add(new EventMember(user, new Event(resultSet.getInt("id"),resultSet.getString("folderId"),resultSet.getString("inboxNewFolderId"),resultSet.getString("eventName"),resultSet.getString("closedFolderId")), resultSet.getString("in_progress_folder_id"), resultSet.getString("for_approval_folder_id"),resultSet.getString("role")));
				
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
