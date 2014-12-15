package com.mcigroup.eventmanager.front.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.FileList;
import com.mcigroup.eventmanager.front.dao.EventDao;
import com.mcigroup.eventmanager.front.dao.UserDao;
import com.mcigroup.eventmanager.front.helper.ConnectionUtil;
import com.mcigroup.eventmanager.front.helper.Tools;
import com.mcigroup.eventmanager.front.model.ConsumerTypeEnum;
import com.mcigroup.eventmanager.front.model.Event;
import com.mcigroup.eventmanager.front.model.EventMember;
import com.mcigroup.eventmanager.front.model.User;
import com.mcigroup.eventmanager.front.model.UserRoleEnum;
import com.mcigroup.eventmanager.front.security.CredentialLoader;

public class DriveAPIService {

	private static Drive drive = getDrive();

	private static Drive getDrive() {
		Drive toReturn = drive;

		if (drive == null) {
			toReturn = CredentialLoader.getDriveService();
		}

		return toReturn;
	}

	
	
	
	public static HashMap<String, Object> getFilesForEventAndUser(EventMember em, HashMap<String,Object> numberOfNewFiles, HashMap<String,Object> numberOfClosedFiles) throws IOException {
		HashMap<String, Object> userHashMap = new HashMap<>();
//			
					userHashMap.put("name", em.getEvent().getEventName());
//					System.err.println("check in progress folder id " + em.getInProgressFolderId() + " for user " + em.getUser().getUserName());
					userHashMap.put("in_progress", getNbFilesInFolder(em.getInProgressFolderId()));
//					System.err.println("check for approval folder id " + em.getForApprovalFolderId() + " for user " + em.getUser().getUserName());
					userHashMap.put("validation_ask", getNbFilesInFolder(em.getForApprovalFolderId()));
					userHashMap.put("incoming", numberOfNewFiles);
					userHashMap.put("closed", numberOfClosedFiles);
					userHashMap.put("user", em.getUser().getUserName());
		return userHashMap;
	}
	
	public static HashMap<String, Object> getNbFilesInFolder(String folderId) {
			HashMap<String, Object> fileLinkAndCount = new HashMap<String, Object>();
			int numberOfFiles = 0;
			String folderLink = "";
			System.err.println("CHECK FOLDER ID = " + folderId);
			if(!StringUtils.isEmpty(folderId) && !"null".equals(folderId.toLowerCase())){
				try {
					folderLink = "https://drive.google.com/a/mci-group.com/?usp=folder#folders/" + folderId;
					Files.List request = drive.files().list().setQ("mimeType != 'application/vnd.google-apps.folder' and " + "'" + folderId + "' in parents and trashed = false");
					FileList userFolders = request.execute();
					numberOfFiles += userFolders.getItems().size();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			fileLinkAndCount.put("number", numberOfFiles);
			fileLinkAndCount.put("folderLink", folderLink);
			return fileLinkAndCount;
	}
	
	
	public static String getFileList(String userEmail) {
		List<HashMap<String, Object>> userEvents = new ArrayList<HashMap<String, Object>>();
		UserDao userDao = new UserDao();
		EventDao eventDao = new EventDao();
		HashMap<String,Object> consumerType = new HashMap<String, Object>(1);
		consumerType.put("consumerType", ConsumerTypeEnum.USER.getConsumerType());
		userEvents.add(consumerType);
		try {
			User user = userDao.getUserByEmail(userEmail);
			if (user != null) {
				
			Collection<EventMember> eventMembers = eventDao.getEventMemberForUser(user);
			for(EventMember eventMember : eventMembers) {
				
//				System.err.println("New folder id = " + eventMember.getEvent().getNewFolderId());
				HashMap<String, Object> numberOfNewFiles = getNbFilesInFolder(eventMember.getEvent().getNewFolderId());
				HashMap<String, Object> numberOfClosedFiles = getNbFilesInFolder(eventMember.getEvent().getClosedFolderId());
				userEvents.add(getFilesForEventAndUser(eventMember, numberOfNewFiles, numberOfClosedFiles));
//				System.err.println("User : " + eventMember.getUser().getUserName() + " checked");
			}
			}
		} catch (IOException e) {
			System.err.println("Error while trying to retrieve the eventFolder");
		}
		return Tools.gson.toJson(userEvents);

	}
	
	public static String getFileListForUser(String userEmail) {
		List<HashMap<String, Object>> userEvents = new ArrayList<HashMap<String, Object>>();
		UserDao userDao = new UserDao();
		EventDao eventDao = new EventDao();
//		HashMap<String,Object> consumerType = new HashMap<String, Object>(1);
//		consumerType.put("consumerType", ConsumerTypeEnum.USER.getConsumerType());
//		userEvents.add(consumerType);
//		try {
			User user = userDao.getUserByEmail(userEmail);
			if (user != null) {
				
			Collection<EventMember> eventMembers = eventDao.getEventMemberForUser(user);
			for(EventMember eventMember : eventMembers) {
				if(UserRoleEnum.EVENTHEAD.getUserRole().equalsIgnoreCase(eventMember.getRole()) || UserRoleEnum.POOLHEAD.getUserRole().equalsIgnoreCase(eventMember.getRole())) {
					System.err.println("Member is MANAGER");
					userEvents.addAll(getFileListManager(eventMember));
				} else {
					System.err.println("Member is USER");
					userEvents.add(getFileListUser(eventMember));
				}
//				
			}
			}
//		} catch (IOException e) {
//			System.err.println("Error while trying to retrieve the eventFolder");
//		}
		return Tools.gson.toJson(userEvents);

	}
	
	public static HashMap<String, Object> getFileListUser(EventMember eventMember) {
		System.err.println("New folder id = " + eventMember.getEvent().getNewFolderId());
		HashMap<String, Object> numberOfNewFiles = getNbFilesInFolder(eventMember.getEvent().getNewFolderId());
		HashMap<String, Object> numberOfClosedFiles = getNbFilesInFolder(eventMember.getEvent().getClosedFolderId());
		HashMap<String, Object> toReturn = new HashMap<String, Object>();
		try {
			toReturn = getFilesForEventAndUser(eventMember, numberOfNewFiles, numberOfClosedFiles);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toReturn;
//		System.err.println("User : " + eventMember.getUser().getUserName() + " checked");
	}
	
	public static List<HashMap<String, Object>> getFileListManager(EventMember eventMember) {
		UserDao userDao = new UserDao();
		List<HashMap<String, Object>> toReturn = new ArrayList<HashMap<String, Object>>();
		Collection<EventMember> usersForEvent = userDao
									.getEventMemberForEvent(eventMember.getEvent());
		HashMap<String, Object> numberOfNewFiles = getNbFilesInFolder(eventMember.getEvent().getNewFolderId());
		HashMap<String, Object> numberOfClosedFiles = getNbFilesInFolder(eventMember.getEvent().getClosedFolderId());
		for (EventMember userEvent : usersForEvent) {
			try {
				toReturn.add(getFilesForEventAndUser(userEvent, numberOfNewFiles, numberOfClosedFiles));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.err.println("User : " + userEvent.getUser().getUserName() + " checked");
		}
		return toReturn;
				
	}
	
	public static String getFileListForManager(String userEmail) {
		List<HashMap<String, Object>> userEvents = new ArrayList<HashMap<String, Object>>();
		UserDao userDao = new UserDao();
		EventDao eventDao = new EventDao();
		try {

			User user = userDao.getUserByEmail(userEmail);
			if (user != null) {
				Collection<Event> events = eventDao.getEventByUser(user);
				for (Event event : events) {
						System.err.println("Event : id = " + event.getId()
						+ " -- folderId = " + event.getFolderId());
							Collection<EventMember> usersForEvent = userDao
									.getEventMemberForEvent(event);
					HashMap<String, Object> numberOfNewFiles = getNbFilesInFolder(event.getNewFolderId());
					HashMap<String, Object> numberOfClosedFiles = getNbFilesInFolder(event.getClosedFolderId());
					for (EventMember userEvent : usersForEvent) {
						userEvents.add(getFilesForEventAndUser(userEvent, numberOfNewFiles, numberOfClosedFiles));
						System.err.println("User : " + userEvent.getUser().getUserName() + " checked");
					}
				}
			}
		} catch (IOException e) {
			System.err
					.println("Error while trying to retrieve the eventFolder");
		}
		return Tools.gson.toJson(userEvents);
	}
	
	public static String getConsumerType(String userEmail) {
		String consumerType = "";
		Connection conn = ConnectionUtil.getConnection();
		System.err.println("getConsumerType");
	      try {
	        
	          String statement = "SELECT ct.consumertype, me.id FROM consumertype ct, consumeruser cu, member me where ct.id = cu.consumertype_id and me.id=cu.user_id and me.userId = ?";
	          PreparedStatement stmt = conn.prepareStatement(statement);
	          stmt.setString(1, userEmail);
	          ResultSet resultSet = stmt.executeQuery();
//	          int userId = 0;
	          while (resultSet.next()) {
	        	  consumerType = resultSet.getString("consumertype");
//	        	  System.err.println("consumerType = " + consumerType);
//	        	  userId = resultSet.getInt("id");
//	        	  System.err.println("userId = " + userId);
	          }
	          
	        conn.close();
	    } catch (SQLException e) {
	      e.printStackTrace();
	    }
		return consumerType;
	}
}
