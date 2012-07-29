package com.katanagari.euskal_reporter.application;

import android.app.Application;

import com.katanagari.euskal_reporter.classes.mail.MailSender;
import com.katanagari.euskal_reporter.classes.mail.MailSenderCallback;

public class ReporterApplication extends Application {
	private MailSender mailReportSender = null;
	private MailSenderCallback currentCallback = null;
	
	public MailSender getMailSender(){
		if (this.mailReportSender == null) {
			this.mailReportSender = new MailSender(currentCallback, getResources());
		}
		
		return this.mailReportSender;
	}
	
	public void unregisterMailSenderCallback() {
		this.currentCallback = null;
	}
	
	public void registerMailSenderCallback(MailSenderCallback callback) {
		this.currentCallback = callback;
	}
	
	public void mailSenderHasFinished() {
		this.mailReportSender = null;
	}
}
