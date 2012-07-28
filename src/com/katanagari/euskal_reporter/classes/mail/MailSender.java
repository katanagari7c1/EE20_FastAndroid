package com.katanagari.euskal_reporter.classes.mail;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.katanagari.euskal_reporter.classes.mail.formatter.MessageFormatter;
import com.katanagari.euskal_reporter.classes.model.Report;


public class MailSender extends AsyncTask<MimeMessage, Void, Boolean> {
	private GmailService mailService;
	private SenderAccount sender;
	private MailSenderCallback callback;
	private Resources resources;
		
	public MailSender(MailSenderCallback callback, Resources resources){
		this.mailService = new GmailService();
		this.sender = new SenderAccount();
		this.callback = callback;
		this.resources = resources;
	}
	
	public void sendMessage(Report report, String recipient) {
		Session session = Session.getInstance(mailService.getServiceProperties(), sender);
		
		MimeMessage mail = new MimeMessage(session);

		this.fillMailWithReport(mail, report, recipient);
		
		this.execute(mail);
	}

	private void fillMailWithReport(MimeMessage mail, Report report, String recipient) {
		try{
			mail.setFrom(new InternetAddress(this.sender.getUserAddress()));
			mail.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipient));			

			new MessageFormatter(this.resources).formatReportIntoMimeMessage(report, mail);
		}
		catch(MessagingException e){
			Log.d(this.getClass().getName(), "Could not fill the mail object:" + e.getMessage());
		}
	}

	@Override
	protected Boolean doInBackground(MimeMessage... mails) {
		try {			
			Transport.send(mails[0], mails[0].getAllRecipients());
		} catch (Exception e) {
			Log.e(this.getClass().getName(), "Mail could not be sent: " + e.getStackTrace());
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if (result == Boolean.TRUE){
			callback.reportSentSuccessfully();
		}
		else {
			callback.reportSendFailed();
		}
		
		super.onPostExecute(result);
	}
}
