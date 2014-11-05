package com.mcigroup.eventmanager.front.security;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.mcigroup.eventmanager.front.dao.DomainCredentialsDAO;
import com.mcigroup.eventmanager.front.model.DomainCredentials;
import com.mcigroup.eventmanager.front.model.security.GoogleCredentialItem;

public class CredentialLoader {
	
		private static DomainCredentials domainCredentials = DomainCredentialsDAO.loadDomainCredentials();
		private static GoogleCredentialItem googleCredentialItem = generateGoogleCredentialItem(getAllServicesScopes());
		
		
		public static Drive getDriveService(){
			Drive driveService = null;
			if(googleCredentialItem != null){
				driveService = new Drive.Builder(googleCredentialItem.getHttpTransport(), googleCredentialItem.getJsonFactory(), googleCredentialItem.getGoogleCredential())
			      .setApplicationName("My Project").build();
			}
				  
		  return driveService;
		}

		private static GoogleCredentialItem generateGoogleCredentialItem(ArrayList<String> scopes){
			  HttpTransport httpTransport = new NetHttpTransport();
			  JacksonFactory jsonFactory = new JacksonFactory();
			  
			  GoogleCredential googleCredential = null;
			  GoogleCredentialItem googleCredentialItem = null;
			try {
//				googleCredential = new GoogleCredential.Builder()
//				      .setTransport(httpTransport)
//				      .setJsonFactory(jsonFactory)
//				      .setServiceAccountId(domainCredentials.getServiceAccountEmail())
//				      .setServiceAccountScopes(scopes)
//				      .setServiceAccountUser("nicolas.meunier@capgemini-sogeti.com")
//				      .setServiceAccountPrivateKeyFromP12File(new File(CredentialLoader.class.getResource("/" + domainCredentials.getCertificatePath()).toURI()))
//				      .build();
				googleCredential = new GoogleCredential.Builder()
			      .setTransport(httpTransport)
			      .setJsonFactory(jsonFactory)
			      .setServiceAccountId(domainCredentials.getServiceAccountEmail())
			      .setServiceAccountScopes(scopes)
			      .setServiceAccountUser("apps.engine@mci-group.com")
			      .setServiceAccountPrivateKeyFromP12File(new File(CredentialLoader.class.getResource("/" + domainCredentials.getCertificatePath()).toURI()))
			      .build();
				
				googleCredentialItem = new GoogleCredentialItem();
				googleCredentialItem.setGoogleCredential(googleCredential);
				
			} catch (GeneralSecurityException e) {
				System.out.println("Error1: " + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Error2: " + e.getMessage());
				e.printStackTrace();
			} catch (URISyntaxException e) {
				System.out.println("Error3: " + e.getMessage());
				e.printStackTrace();
			}
			
			return googleCredentialItem;
		}
		
		
		private static ArrayList<String> getAllServicesScopes(){
			ArrayList<String> scopes = new ArrayList<String>();
			//scopes.addAll(getDirectoryScopes());
			scopes.addAll(getDriveScopes());
			
			return scopes;
		}
		
		/**
		 * 
		 * @return Google Drive API scopes required
		 */
		private static ArrayList<String> getDriveScopes(){
			ArrayList<String> scopes = new ArrayList<String>();
			scopes.add(DriveScopes.DRIVE_READONLY);
			
			return scopes;
		}	
		
}