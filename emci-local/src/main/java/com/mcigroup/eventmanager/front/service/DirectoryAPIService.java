package com.mcigroup.eventmanager.front.service;

import java.io.IOException;

import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.model.Group;
import com.mcigroup.eventmanager.front.security.CredentialLoader;



public class DirectoryAPIService {
	
	private static Directory directory = getDirectory();

	private static Directory getDirectory(){
		Directory toReturn = directory;
		
		if(directory == null){ toReturn = CredentialLoader.getDirectoryService(); }
		
		return toReturn;
	}
	
	public static boolean isExistingGroup(String groupEmailAddress){
		boolean isExistingGroup = false;
		
		try {
//			System.err.println("groups");
//			for(Group group : directory.groups().list().execute().getGroups()) {
//				System.err.println("group title = " + group.getName() + " -- Group email = " + group.getEmail());
//			}
			isExistingGroup = directory.groups().get(groupEmailAddress).execute() != null;
		} catch (IOException e) {
			isExistingGroup = false;
		}
		
		return isExistingGroup;
	}

	public static boolean isGroupMember(String groupEmail, String userEmail){
		
		boolean isGroupMember = false;
			
		try {
			directory.members().get(groupEmail, userEmail).execute();
			isGroupMember = true;
		} catch (IOException e) {
			isGroupMember = false;
		}
		
		return isGroupMember;
	}
}
