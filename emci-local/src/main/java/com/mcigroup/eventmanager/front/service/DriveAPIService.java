package com.mcigroup.eventmanager.front.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.appengine.api.utils.SystemProperty;
import com.mcigroup.eventmanager.front.dao.EventDao;
import com.mcigroup.eventmanager.front.dao.UserDao;
import com.mcigroup.eventmanager.front.helper.Tools;
import com.mcigroup.eventmanager.front.model.ConsumerTypeEnum;
import com.mcigroup.eventmanager.front.model.Event;
import com.mcigroup.eventmanager.front.model.User;
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

	
	//public static int getNbFilesForEventAndUser(File event, String userName, String type) throws IOException {
	public static HashMap<String,Object> getNbFilesForEventAndUser(File event, String userName, String type) throws IOException {
		HashMap<String, Object> fileLinkAndCount = new HashMap<String, Object>();
		int numberOfFiles = 0;
		String folderLink = "";
		List<File> result = new ArrayList<File>();
		Files.List request;
		request = drive.files().list().setQ("'" + event.getId() + "' in parents and mimeType = 'application/vnd.google-apps.folder' and title='20-Inbox'");
		FileList files = request.execute();
		result = files.getItems();
		System.err.println("Nb of 20-Inbox folder = " + result.size());
		if (result.size() == 1) {
			request = drive.files().list().setQ("'" + result.get(0).getId() + "' in parents and mimeType = 'application/vnd.google-apps.folder' and title='" + type + "'");
			files = request.execute();
			result = files.getItems();
			System.err.println("Nb of 20-In progress folder = " + result.size());
			if (result.size() == 1) {
				File InProgressFolder = result.get(0);
				
				request = drive.files().list().setQ("'" + InProgressFolder.getId() + "' in parents and mimeType = 'application/vnd.google-apps.folder' and title = '" + userName + "'");
				FileList userFolders = request.execute();
				result = userFolders.getItems();
				System.err.println("Nb of " + userName + " folder = " + result.size());
				for(File folder : result) {
					System.err.println("Event = " + event.getTitle() + " user = " + userName + " type = " + type + " folder =  " + folder.getTitle());
					request = drive.files().list().setQ("'" + folder.getId() + "' in parents");
					folderLink = "https://drive.google.com/a/mci-group.com/?usp=folder#folders/" + folder.getId();
					System.err.println("link1 = " + folder.getAlternateLink());
					System.err.println("link2 = " + folder.getDefaultOpenWithLink());
					System.err.println("link3 = " + folder.getSelfLink());
					System.err.println("link4 = " + folder.getWebViewLink());
					FileList userFiles = request.execute();
					numberOfFiles += userFiles.getItems().size();
				}
			}
			System.err.println("number of file for event = " + event.getTitle() + " user = " + userName + " type = " + type + " = " + numberOfFiles);
		}
		fileLinkAndCount.put("number", numberOfFiles);
		fileLinkAndCount.put("folderLink", folderLink);
		//return numberOfFiles;
		return fileLinkAndCount;
	}

//	public static int getNbNewFilesForEvent(File event) throws IOException {
	public static HashMap<String, Object> getNbNewFilesForEvent(File event) throws IOException {
		HashMap<String, Object> fileLinkAndCount = new HashMap<String, Object>();
		int numberOfFiles = 0;
		String folderLink = "";
		List<File> result = new ArrayList<File>();
		Files.List request;
		request = drive.files().list().setQ("'" + event.getId() + "' in parents and mimeType = 'application/vnd.google-apps.folder' and title='20-Inbox'");
		FileList files = request.execute();
		result = files.getItems();
		if (result.size() == 1) {
			request = drive.files().list().setQ("'" + result.get(0).getId() + "' in parents and mimeType = 'application/vnd.google-apps.folder' and title='10-New'");
			files = request.execute();
			result = files.getItems();
			if (result.size() == 1) {
				File newFolder = result.get(0);
				folderLink = "https://drive.google.com/a/mci-group.com/?usp=folder#folders/" + newFolder.getId();
				request = drive.files().list().setQ("'" + newFolder.getId() + "' in parents and trashed = false");
				FileList userFolders = request.execute();
				for (File file : userFolders.getItems()) {
					System.err.println("New file title = " + file.getTitle());
				}
				numberOfFiles += userFolders.getItems().size();
				
			}
			//System.out.println("number of file for event = " + event.getTitle() + " user = " + userEmail + " type = " + type + " = " + numberOfFiles);
		}
		fileLinkAndCount.put("number", numberOfFiles);
		fileLinkAndCount.put("folderLink", folderLink);
//		return numberOfFiles;
		return fileLinkAndCount;
	}
	
	public static HashMap<String, Object> getFilesForEventAndUser(File event, String userName) {
		HashMap<String, Object> userHashMap = new HashMap<>();
//		HashMap<String, Object> eventHashMap = new HashMap<>();
		try {
//			int numberInProgressFiles = getNbFilesForEventAndUser(event, userName, "20-In progress");
//			int numberValidationFiles = getNbFilesForEventAndUser(event, userName, "50-For approval");
//			int numberNewFiles = getNbNewFilesForEvent(event);
			userHashMap.put("name", event.getTitle());
//			userHashMap.put("in_progress", numberInProgressFiles);
			userHashMap.put("in_progress", getNbFilesForEventAndUser(event, userName, "20-In progress"));
//			userHashMap.put("validation_ask", numberValidationFiles);
			userHashMap.put("validation_ask", getNbFilesForEventAndUser(event, userName, "50-For approval"));
//			userHashMap.put("incoming", numberNewFiles);
			userHashMap.put("incoming", getNbNewFilesForEvent(event));
			userHashMap.put("user", userName);
//			eventHashMap.put("event", userHashMap);
		} catch (IOException e) {
			System.err.println("error while trying to retrieve files for user " + userName + " and event " + event.getTitle());
		}
		
//		return eventHashMap;
		return userHashMap;
	}
	
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
				Collection<Event> events = eventDao.getEventByUser(user);
				for(Event event : events) {
					File eventFolder;
					
						eventFolder = drive.files().get(event.getFolderId()).execute();
					
					userEvents.add(getFilesForEventAndUser(eventFolder, user.getUserName()));
				}
				}
		} catch (IOException e) {
			System.err.println("Error while trying to retrieve the eventFolder");
		}
		return Tools.gson.toJson(userEvents);

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
					File eventFolder = drive.files().get(event.getFolderId())
							.execute();

					Collection<User> usersForEvent = userDao
							.getUsersForEvent(event);
					for (User userEvent : usersForEvent) {
						System.err.println("");
						userEvents.add(getFilesForEventAndUser(eventFolder,
								userEvent.getUserName()));
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
		Connection conn = getConnection();
		System.err.println("getConsumerType");
	      try {
	        
	          String statement = "SELECT ct.consumertype, me.id FROM consumertype ct, consumeruser cu, member me where ct.id = cu.consumertype_id and me.id=cu.user_id and me.userId = ?";
	          PreparedStatement stmt = conn.prepareStatement(statement);
	          stmt.setString(1, userEmail);
	          ResultSet resultSet = stmt.executeQuery();
	          int userId = 0;
	          while (resultSet.next()) {
	        	  consumerType = resultSet.getString("consumertype");
	        	  System.err.println("consumerType = " + consumerType);
	        	  userId = resultSet.getInt("id");
	        	  System.err.println("userId = " + userId);
	          }
	          
	        conn.close();
	    } catch (SQLException e) {
	      e.printStackTrace();
	    }
		return consumerType;
	}
}
