package com.katanagari.euskal_reporter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.katanagari.euskal_reporter.R;
import com.katanagari.euskal_reporter.classes.MailHelper;

public class MainScreenActivity extends Activity {

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
		MailHelper helper = new MailHelper();
		try {
			helper.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
