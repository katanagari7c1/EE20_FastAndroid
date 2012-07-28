package com.katanagari.euskal_reporter.classes.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SenderAccount extends Authenticator{
	private final String username = "x@gmail.com";
	private final String password = "x";

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
	
	public String getUserAddress(){
		return this.username;
	}
}
