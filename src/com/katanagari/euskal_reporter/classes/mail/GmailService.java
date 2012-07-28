package com.katanagari.euskal_reporter.classes.mail;

import java.util.Properties;

public class GmailService {
	private final String host = "smtp.gmail.com";
	private final String port = "465";
	
	public Properties getServiceProperties(){
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.debug", "true");
		
		props.put("mail.smtp.port", this.port); 
		props.put("mail.smtp.socketFactory.port", this.port); 
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
		props.put("mail.smtp.socketFactory.fallback", "false"); 		
		return props;
	}
}
