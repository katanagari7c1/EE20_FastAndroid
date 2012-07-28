package com.katanagari.euskal_reporter.classes;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailHelper extends Authenticator{
	private final String host = "smtp.gmail.com";
	private final String port = "465";
	private final String user = "account@gmail.com";
	private final String pass = "pass";

	private String subject = "";
	private String messageBody = "";
	
	public MailHelper() {
		subject = "nanana";
		messageBody = "OMGWTFBBQ";
	}
	
	public void send() throws Exception {
		Properties properties = this.setProperties();

		Session session = Session.getInstance(properties, this);
		
		MimeMessage message = new MimeMessage(session);
		message.setSubject(subject);
		message.setFrom(new InternetAddress("katanagari@gmail.com"));
		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("javier.armendariz@quomai.com"));
		message.setContent("Hi there", "text/plain");
		
		
		Transport.send(message, message.getAllRecipients());

	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, pass);
	}

	private Properties setProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.debug", "true");
		
		properties.put("mail.smtp.port", this.port); 
		properties.put("mail.smtp.socketFactory.port", this.port); 
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
		properties.put("mail.smtp.socketFactory.fallback", "false"); 
		
		return properties;
	}
	
}
