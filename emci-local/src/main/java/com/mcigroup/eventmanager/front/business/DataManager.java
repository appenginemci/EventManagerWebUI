package com.mcigroup.eventmanager.front.business;

import com.mcigroup.eventmanager.front.service.DriveAPIService;


public class DataManager {
	 
	
	
	
	
	public static<T> String getDriveList(String userEmail){
		
			return DriveAPIService.getFileList(userEmail);
		
	}
	
	public static<T> String getDriveListForManager(String userEmail){
		
		return DriveAPIService.getFileListForManager(userEmail);
	
}
	
	public static<T> String getConsumerType(String userEmail){
		
		return DriveAPIService.getConsumerType(userEmail);
	
}
	
}