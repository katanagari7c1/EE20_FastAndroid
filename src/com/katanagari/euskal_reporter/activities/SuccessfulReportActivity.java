package com.katanagari.euskal_reporter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.katanagari.euskal_reporter.R;

public class SuccessfulReportActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_successful_screen);

		((Button) findViewById(R.id.post_another_button))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						goBack();
					}
				});
	}
	
	private void goBack() {
		finish();
	}
}
