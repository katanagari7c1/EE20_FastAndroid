package com.katanagari.euskal_reporter.classes.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

public class ImageFileFactory {
	public static File getFile() {
		String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ee_reports/";
		File newDir = new File(directory);
		newDir.mkdirs();
		
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String filePath = directory + timestamp + ".jpg";
		File newFile = new File(filePath);
		try {
			newFile.createNewFile();
		}
		catch (IOException e){
			Log.e("ImageFileFactory", "Could not create file: " + newFile.getAbsolutePath());
		}
		
		return newFile;
	}
}
