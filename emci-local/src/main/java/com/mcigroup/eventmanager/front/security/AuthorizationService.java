package com.mcigroup.eventmanager.front.security;


import com.mcigroup.eventmanager.front.model.UserSession;

public class AuthorizationService {
	
//	private static String groupEmailAddress = "ppic-acl@capgemini-sogeti.com";
	
	public static UserSession getAuthorization(String userEmail){
		
		UserSession userSession = null;
		
//		if(DirectoryAPIService.isExistingGroup(groupEmailAddress)){
//			if(DirectoryAPIService.isGroupMember(groupEmailAddress, userEmail)){
				userSession = new UserSession();
				userSession.setEmail(userEmail);
//			}else{
//				System.err.println(userEmail + " is not member of " + groupEmailAddress);
//			}
//		}else{
//			System.err.println(groupEmailAddress + " is not existing");
//		}
		
		return userSession;
	}

}
