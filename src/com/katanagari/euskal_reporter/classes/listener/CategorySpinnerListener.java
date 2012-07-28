package com.katanagari.euskal_reporter.classes.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.katanagari.euskal_reporter.classes.model.Report;

public class CategorySpinnerListener implements OnItemSelectedListener{
	private Report report;
	
	public CategorySpinnerListener(Report report) {
		this.report = report;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		this.report.setCategory((String) parent.getItemAtPosition(pos));
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
}
