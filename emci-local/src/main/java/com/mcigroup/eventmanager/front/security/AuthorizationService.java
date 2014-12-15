package com.mcigroup.eventmanager.front.security;


import com.mcigroup.eventmanager.front.model.UserSession;
import com.mcigroup.eventmanager.front.service.DirectoryAPIService;

public class AuthorizationService {
	
	
	public static UserSession getAuthorization(String userEmail){
		
		UserSession userSession = null;
		if(DirectoryAPIService.isInDomainUser(userEmail)) {
				userSession = new UserSession();
				userSession.setEmail(userEmail);
		} else {
			System.err.println(userEmail + " is not mci-group domain");
		}
		
		return userSession;
	}

}
