package com.katanagari.euskal_reporter.classes.mail.formatter;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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
			Multipart multipart = new MimeMultipart();
			
			if (report.getDescription().length() > 0) {
				messageBody += resources.getString(R.string.message_body_description, report.getDescription());
			}
			
			if (report.getPhotoPath().length() > 0) {
				messageBody += resources.getString(R.string.message_body_photo);
				this.insertPhotoIntoEmailAsMultipart(report, multipart);
			}	
			
			message.setSubject(subject);
			this.insertContentIntoEmailAsMultipart(messageBody, multipart);
			
			message.setContent(multipart);
	}

	private void insertPhotoIntoEmailAsMultipart(Report report,
			Multipart multipart) throws MessagingException {
		BodyPart messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(report.getPhotoPath());
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(report.getPhotoPath());
		
		multipart.addBodyPart(messageBodyPart);
	}

	private void insertContentIntoEmailAsMultipart(String messageBody,
			Multipart multipart) throws MessagingException {
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(messageBody);
		multipart.addBodyPart(messageBodyPart);
	}
}
