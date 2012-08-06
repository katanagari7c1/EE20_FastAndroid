package com.katanagari.euskal_reporter.activities;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
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
import com.katanagari.euskal_reporter.application.ReporterApplication;
import com.katanagari.euskal_reporter.classes.listener.CategorySpinnerListener;
import com.katanagari.euskal_reporter.classes.mail.MailSender;
import com.katanagari.euskal_reporter.classes.mail.MailSenderCallback;
import com.katanagari.euskal_reporter.classes.model.Report;
import com.katanagari.euskal_reporter.classes.utils.ImageFileFactory;
import com.katanagari.euskal_reporter.classes.utils.UriToPath;

public class MainScreenActivity extends Activity implements MailSenderCallback {
	private static final int REQUEST_TAKE_PHOTO = 0;
	private static final int REQUEST_PICK_PHOTO = 1;
	
	private static final String BUNDLE_STATE_REPORT = "bundle.state.report";
	private static final String BUNDLE_STATE_DIALOG_ACTIVE = "bundle.state.dialog.active";

	
	private Report report;
	private ProgressDialog progressDialog;
	
	private EditText emailDialogTextField = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
		
        this.loadReportFromInstanceStateIfAvailable(savedInstanceState);
        
        ((ReporterApplication)getApplication()).registerMailSenderCallback(this);
        
		this.createProgressDialog(savedInstanceState);
        this.initializeCategorySpinnerData();
        this.initializeFormWidgetListeners();
        
        this.setSubmitButtonAction();
        this.initializePhotoButtons();
    }
    
    @Override
    protected void onDestroy() {
    	((ReporterApplication)getApplication()).unregisterMailSenderCallback();
    	this.progressDialog.cancel();
    	
    	super.onDestroy();
    }

	private void loadReportFromInstanceStateIfAvailable(
			Bundle savedInstanceState) {
		if(savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_STATE_REPORT)){
        	this.report = (Report)savedInstanceState.getSerializable(BUNDLE_STATE_REPORT);
        }
        else {
        	this.report = new Report();	
        }
	}
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	outState.putSerializable(BUNDLE_STATE_REPORT, this.report);
    	outState.putBoolean(BUNDLE_STATE_DIALOG_ACTIVE, this.progressDialog.isShowing());
    	super.onSaveInstanceState(outState);
    }
    
    /**
     * Initialization helpers
     */
	private void createProgressDialog(Bundle savedInstanceState) {
		this.progressDialog = new ProgressDialog(this);
		this.progressDialog.setTitle(R.string.submit_dialog_title);
		this.progressDialog.setMessage(getString(R.string.submit_dialog_message));
		this.progressDialog.setIndeterminate(true);
		this.progressDialog.setCancelable(false);
		
		if (savedInstanceState != null && savedInstanceState.getBoolean(BUNDLE_STATE_DIALOG_ACTIVE)) {
			this.progressDialog.show();
		}
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
			if(requestCode == REQUEST_PICK_PHOTO){
				this.report.setPhotoPath(new UriToPath().convertUriToPath(this, data.getData()));
			}

			this.refreshImageButtonsState();
		}
	}

	private void refreshImageButtonsState() {
		if(this.report.getPhotoPath().length() > 0){
			TextView photoPathView = (TextView)findViewById(R.id.selectedPhotoPath);
			findViewById(R.id.addImageButtonArea).setVisibility(View.GONE);
			this.setRemoveImageButtonListener();
			
			photoPathView.setText(this.report.getPhotoPath());
			findViewById(R.id.addedImageButton).setVisibility(View.VISIBLE);
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
		
		this.refreshImageButtonsState();
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
		this.report.setPhotoPath(imageFile.getAbsolutePath());
		Uri outputFile = Uri.fromFile(imageFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFile);
		intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1);
	}
	
	private void onPickPhotoButtonPressed() {
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, REQUEST_PICK_PHOTO);
	}
	
	private void onRemovePictureButtonPressed() {
		this.report.setPhotoPath("");
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
		LayoutInflater inflater = getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.send_dialog_layout, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		this.emailDialogTextField = (EditText)dialogView.findViewById(R.id.send_dialog_textfield);
		
		builder.setView(dialogView)
		.setTitle(R.string.send_to_email_title)
		.setPositiveButton(getString(R.string.send_to_email_ok_button), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (emailDialogTextField != null) {
					submitReportToAddress(emailDialogTextField.getText().toString());	
				}
			}

		})
		.setNegativeButton(getString(R.string.send_to_email_cancel_button), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.setCancelable(false);
		
		builder.create().show();
	}
	
	private void submitReportToAddress(String address) {
		this.progressDialog.show();
		//Check if form has the minimum amount of data required
		//If so, send it using a mail helper object
		EditText description = (EditText)findViewById(R.id.descriptionField);
		this.report.setDescription(description.getText().toString());
		
		MailSender sender = ((ReporterApplication)getApplication()).getMailSender();
		
		if (sender.getStatus() != AsyncTask.Status.RUNNING) {
			sender.sendMessage(this.report, address);	
		}
	}
	
	/**
	 * Report sending callback
	 */

	@Override
	public void reportSentSuccessfully() {
		this.progressDialog.cancel();
		((ReporterApplication)getApplication()).mailSenderHasFinished();
		
		Intent intent = new Intent(this, SuccessfulReportActivity.class);
		startActivity(intent);
	}

	@Override
	public void reportSendFailed() {
		this.progressDialog.cancel();
		Toast.makeText(this, R.string.report_failed, Toast.LENGTH_LONG).show();
		((ReporterApplication)getApplication()).mailSenderHasFinished();
	}
}
