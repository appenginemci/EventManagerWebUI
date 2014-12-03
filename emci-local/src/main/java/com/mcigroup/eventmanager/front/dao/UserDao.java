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

public class UserDao {
	public User getUserByEmail(String userEmail) {
		Connection conn = ConnectionUtil.getConnection();
		try {
			try{
			String statement = "SELECT me.id,me.userName,me.userId FROM member me where me.userId = ?";
			PreparedStatement stmt;

			stmt = conn.prepareStatement(statement);

			stmt.setString(1, userEmail);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
//				System.out.println("userId = " + resultSet.getInt("id"));
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
	
	
	public Collection<EventMember> getEventMemberForEvent(Event event) {
		Connection conn = ConnectionUtil.getConnection();
		//List<User> userList = new ArrayList<User>();
		List<EventMember> userList = new ArrayList<EventMember>();
		try {
			try{
				String statement = "SELECT me.id,me.userId,me.userName, em.in_progress_folder_id, em.for_approval_folder_id FROM member me, eventmember em where em.user_id=me.id and em.event_id = ?";
				PreparedStatement stmt;

				stmt = conn.prepareStatement(statement);
				stmt.setInt(1, event.getId());
				ResultSet resultSet = stmt.executeQuery();
				while (resultSet.next()) {
//					System.out.println("userId = " + resultSet.getInt("id"));
					//userList.add(new User(resultSet.getInt("id"),resultSet.getString("userName"),resultSet.getString("userId")));
					userList.add(new EventMember(new User(resultSet.getInt("id"),resultSet.getString("userName"),resultSet.getString("userId")), event, resultSet.getString("in_progress_folder_id"), resultSet.getString("for_approval_folder_id")));
				
				}
			}finally {
				conn.close();
			}
		} catch (SQLException e1) {
			System.err.println("connection error");
		}
		return userList;
	}
	
//	public Collection<EventMember> getEventMemberForManager(User user) {
//		Connection conn = ConnectionUtil.getConnection();
//		//List<User> userList = new ArrayList<User>();
//		List<EventMember> userList = new ArrayList<EventMember>();
//		try {
//			try{
//				
//				String statement = "select member.id as user_id, member.userName, member.userId, event.id as event_id, event.eventName, event.folderId, event.inboxNewFolderId, event.closedFolderId, eventmember.in_progress_folder_id, eventmember.for_approval_folder_id from eventmember, event, member where event.id=eventmember.event_id and member.id=eventmember.user_id and eventmember.event_id in (select distinct event_id from eventmember where user_id=?) order by event_id, user_id";
//				//String statement = "SELECT me.*, em.*, event.* FROM member me, eventmember em, event event where em.user_id=me.id and em.event_id = ?";
//				PreparedStatement stmt;
//
//				stmt = conn.prepareStatement(statement);
//				stmt.setInt(1, user.getId());
//				ResultSet resultSet = stmt.executeQuery();
//				while (resultSet.next()) {
//					System.out.println("userId = " + resultSet.getInt("user_id"));
//					//userList.add(new User(resultSet.getInt("id"),resultSet.getString("userName"),resultSet.getString("userId")));
//					userList.add(new EventMember(new User(resultSet.getInt("user_id"), resultSet.getString("userName"), resultSet.getString("userId")), new Event(resultSet.getInt("event_id"), resultSet.getString("folderId"), resultSet.getString("inboxNewFolderId"), resultSet.getString("eventName"), resultSet.getString("closedFolderId")), resultSet.getString("in_progress_folder_id"), resultSet.getString("for_approval_folder_id")));
//				
//				}
//			}finally {
//				conn.close();
//			}
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//			System.err.println("connection error");
//		}
//		return userList;
//	}
	
	
}
