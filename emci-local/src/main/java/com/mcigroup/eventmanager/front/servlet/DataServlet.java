package com.mcigroup.eventmanager.front.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.mcigroup.eventmanager.front.business.DataManager;
import com.mcigroup.eventmanager.front.model.ConsumerTypeEnum;



public class DataServlet  extends HttpServlet {

	private static final long serialVersionUID = -1488008862968967881L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{

		String toReturn = null;
//		UserSession userSession = null;
//		String userEmail = null;

//		UserService userService = UserServiceFactory.getUserService();
		//String type = req.getParameter("type");

//		if (req.getUserPrincipal() != null) {
//			if (req.getUserPrincipal() != null) {
//				if(userService.getCurrentUser() != null){
//
//					userEmail = userService.getCurrentUser().getEmail();
//					userSession = UserSessionCacheDAO.load(userEmail);
//
//					if(userSession != null){

						//if(type != null && !type.isEmpty()){
						UserService userService = UserServiceFactory.getUserService();
						if(userService.getCurrentUser() != null){
							String userEmail = userService.getCurrentUser().getEmail();
							String consumerType = DataManager.getConsumerType(userEmail);
							if(ConsumerTypeEnum.MANAGER.getConsumerType().equals(consumerType)) {
								toReturn = DataManager.getDriveListForManager(userEmail);
								resp.setContentType("application/json");
								PrintWriter out = resp.getWriter();
								out.print(toReturn);
								out.flush();
							} else {
								toReturn = DataManager.getDriveList(userEmail);
								resp.setContentType("application/json");
								PrintWriter out = resp.getWriter();
								out.print(toReturn);
								out.flush();
							}
//						}
//					}
//				}
//			}
		}
	}
}
