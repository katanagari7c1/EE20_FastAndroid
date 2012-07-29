package com.katanagari.euskal_reporter.classes.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SenderAccount extends Authenticator{
	private final String username = "ee20.reporter.android@gmail.com";
	private final String password = "fastAndroid";

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
	
	public String getUserAddress(){
		return this.username;
	}
}
