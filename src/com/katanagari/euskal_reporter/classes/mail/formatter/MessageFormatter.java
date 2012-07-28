package com.katanagari.euskal_reporter.classes.mail.formatter;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import android.content.res.Resources;

import com.katanagari.euskal_reporter.R;
import com.katanagari.euskal_reporter.classes.model.Report;

public class MessageFormatter {
	private Resources resources;
	
	public MessageFormatter(Resources resources) {
		this.resources = resources;
	}
	
	public void formatReportIntoMimeMessage(Report report, MimeMessage message) throws MessagingException {		

			String subject = resources.getString(R.string.subject, report.getCategory());
			String messageBody = resources.getString(R.string.message_body_head, report.getCategory());
			
			if (report.getDescription().length() > 0) {
				messageBody += resources.getString(R.string.message_body_description, report.getDescription());
			}
			
			//TODO: Photos missing!
			
			message.setSubject(subject);
			message.setContent(messageBody, "text/plain");
	}
}
