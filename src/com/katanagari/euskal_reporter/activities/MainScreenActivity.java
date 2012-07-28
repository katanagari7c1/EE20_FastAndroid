package com.katanagari.euskal_reporter.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.katanagari.euskal_reporter.R;
import com.katanagari.euskal_reporter.classes.listener.CategorySpinnerListener;
import com.katanagari.euskal_reporter.classes.mail.MailSender;
import com.katanagari.euskal_reporter.classes.mail.MailSenderCallback;
import com.katanagari.euskal_reporter.classes.model.Report;

public class MainScreenActivity extends Activity implements MailSenderCallback {
	
	private Report report;
	private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
		
        this.report = new Report();
        
		this.createProgressDialog();
        this.initializeCategorySpinnerData();
        this.initializeFormWidgetListeners();
        
        this.setSubmitButtonAction();
        this.initializePhotoButtons();
    }

    /**
     * Initialization helpers
     */
	private void createProgressDialog() {
		this.progressDialog = new ProgressDialog(this);
		this.progressDialog.setTitle("lalala");
		this.progressDialog.setMessage("Please wait a sec");
		this.progressDialog.setIndeterminate(true);
		this.progressDialog.setCancelable(false);
	}

	private void initializeFormWidgetListeners() {
		((Spinner)findViewById(R.id.reportCategoryField)).setOnItemSelectedListener(new CategorySpinnerListener(this.report));
	}

	private void initializeCategorySpinnerData() {
		Spinner categories = (Spinner)findViewById(R.id.reportCategoryField);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.report_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);
	}

//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//      getMenuInflater().inflate(R.menu.main_screen, menu);
//      return true;
//  }
	/**
	 * Photo picker result
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK){
			Uri targetUri = data.getData();
		}
	}
    
	/**
	 * Button actions
	 */
	
	private void initializePhotoButtons() {
		Button addPhotoButton = (Button)findViewById(R.id.takePhotoButton);
		
		if( deviceHasACamera() ) {
			addPhotoButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onAddPhotoButtonPressed();
				}
			});	
		}
		else{
			addPhotoButton.setVisibility(View.GONE);
		}
		
		
		Button pickPhotoButton = (Button)findViewById(R.id.pickPhotoButton);
		pickPhotoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onPickPhotoButtonPressed();
			}
		});		
	}
	
	private void setSubmitButtonAction() {
		Button submitButton = (Button)findViewById(R.id.submit_button);
		
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSubmitButtonPressed();
			}
		});
	}
	
	private void onAddPhotoButtonPressed() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, 0);			
	}
	
	private void onPickPhotoButtonPressed() {
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, 0);
	}

	private boolean deviceHasACamera() {
		return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}
	
	private void onSubmitButtonPressed(){
		this.progressDialog.show();
		//Check if form has the minimum amount of data required
		//If so, send it using a mail helper object
		EditText description = (EditText)findViewById(R.id.descriptionField);
		this.report.setDescription(description.getText().toString());
		
		new MailSender(this,this.getResources()).sendMessage(report, "javier.armendariz@quomai.com");
	}
	
	/**
	 * Report sending callback
	 */

	@Override
	public void reportSentSuccessfully() {
		this.progressDialog.cancel();
		Toast.makeText(this, "Report sent! Yeeeha!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void reportSendFailed() {
		this.progressDialog.cancel();
		Toast.makeText(this, "Ow! the report could not be sent", Toast.LENGTH_LONG).show();
	}
}
