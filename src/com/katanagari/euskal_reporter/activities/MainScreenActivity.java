package com.katanagari.euskal_reporter.activities;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.katanagari.euskal_reporter.R;
import com.katanagari.euskal_reporter.classes.listener.CategorySpinnerListener;
import com.katanagari.euskal_reporter.classes.mail.MailSender;
import com.katanagari.euskal_reporter.classes.mail.MailSenderCallback;
import com.katanagari.euskal_reporter.classes.model.Report;
import com.katanagari.euskal_reporter.classes.utils.ImageFileFactory;
import com.katanagari.euskal_reporter.classes.utils.UriToPath;

public class MainScreenActivity extends Activity implements MailSenderCallback {
	private static final int REQUEST_TAKE_PHOTO = 0;
	private static final int REQUEST_PICK_PHOTO = 1;
	
	private Report report;
	private ProgressDialog progressDialog;
	private String takenPhotoPath;
	

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
		
		if (resultCode == RESULT_OK){
			TextView photoPathView = (TextView)findViewById(R.id.selectedPhotoPath);
			findViewById(R.id.addImageButtonArea).setVisibility(View.GONE);
			this.setRemoveImageButtonListener();
			
			//TODO: Show a delete button that will restore the buttons
			if(requestCode == REQUEST_PICK_PHOTO){
				this.takenPhotoPath = new UriToPath().convertUriToPath(this, data.getData());
			}
			
			photoPathView.setText(this.takenPhotoPath);
			findViewById(R.id.addedImageButton).setVisibility(View.VISIBLE);
			
			this.report.setPhotoPath(this.takenPhotoPath);
		}
	}
    


	/**
	 * Button actions
	 */
	
	private void setRemoveImageButtonListener() {
		Button removeImageButton = (Button)findViewById(R.id.removeFileButton);
		removeImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onRemovePictureButtonPressed();
			}
		});

	}
	
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

		this.insertOutputFileUriToIntent(intent);
		
		startActivityForResult(intent, REQUEST_TAKE_PHOTO);			
	}

	private void insertOutputFileUriToIntent(Intent intent) {
		File imageFile = ImageFileFactory.getFile();
		this.takenPhotoPath = imageFile.getAbsolutePath();
		Uri outputFile = Uri.fromFile(imageFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFile);
		intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1);
	}
	
	private void onPickPhotoButtonPressed() {
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, REQUEST_PICK_PHOTO);
	}
	
	private void onRemovePictureButtonPressed() {
		this.takenPhotoPath = "";
		View imagePathView = findViewById(R.id.addedImageButton);
		
		this.animateImageButtonToExit(imagePathView);
		
		findViewById(R.id.addImageButtonArea).setVisibility(View.VISIBLE);
	}

	private void animateImageButtonToExit(View imagePathView) {
		Animation animation = new TranslateAnimation(0, -1000, 0, 0);
		animation.setDuration(200);
		imagePathView.startAnimation(animation);
		imagePathView.setVisibility(View.GONE);
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
