package com.katanagari.euskal_reporter.classes.mail;

public interface MailSenderCallback {
	public void reportSentSuccessfully();
	public void reportSendFailed();
}
