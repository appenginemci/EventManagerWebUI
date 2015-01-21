package com.mcigroup.eventmanager.front.service;

import java.io.IOException;

import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.model.User;
import com.mcigroup.eventmanager.front.security.CredentialLoader;



public class DirectoryAPIService {
	
	private static Directory directory = getDirectory();
	private static Directory getDirectory(){
		Directory toReturn = directory;
		
		if(directory == null){ toReturn = CredentialLoader.getDirectoryService(); }
		
		return toReturn;
	}
	
	public static boolean isInDomainUser(String userEmail){
		
		boolean isDomainUser = false;
		try {
			User connected = directory.users().get(userEmail).execute();
			if (connected != null) {
				isDomainUser = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			isDomainUser = false;
		}
		
		return isDomainUser;
	}
}
