package com.katanagari.euskal_reporter.classes.utils;

import android.app.Activity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

public class UriToPath {

	public String convertUriToPath(Activity activity, Uri uri) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			return convertOnGingerbreadOnwards(uri, activity);
		}
		else{
			return convertOnGingerbreadBackwards(uri, activity);
		}
	}
	
	private String convertOnGingerbreadOnwards(Uri uri, Activity activity) {
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader cursorLoader = new CursorLoader(activity.getApplicationContext(), uri, proj, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		
		return cursor.getString(column_index);
	}
	
	private String convertOnGingerbreadBackwards(Uri uri, Activity activity) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		
		return cursor.getString(column_index);
	}
}
