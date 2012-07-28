package com.katanagari.euskal_reporter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.katanagari.euskal_reporter.R;
import com.katanagari.euskal_reporter.classes.mail.MailSender;
import com.katanagari.euskal_reporter.classes.mail.MailSenderCallback;
import com.katanagari.euskal_reporter.classes.model.Report;

public class MainScreenActivity extends Activity implements MailSenderCallback {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
		
        this.setSubmitButtonAction();
    }

//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//      getMenuInflater().inflate(R.menu.main_screen, menu);
//      return true;
//  }
    
	private void setSubmitButtonAction() {
		Button submitButton = (Button)findViewById(R.id.submit_button);
		
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSubmitButtonPressed();
			}
		});
	}
	
	
	private void onSubmitButtonPressed(){
		//Check if form has the minimum amount of data required
		//If so, send it using a mail helper object
		Report report = new Report("Something happened somewhere");
		new MailSender(this).sendMessage(report, "javier.armendariz@quomai.com");
	}

	@Override
	public void reportSentSuccessfully() {
		Toast.makeText(this, "Report sent! Yeeeha!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void reportSendFailed() {
		Toast.makeText(this, "Ow! the report could not be sent", Toast.LENGTH_LONG).show();
	}
}
