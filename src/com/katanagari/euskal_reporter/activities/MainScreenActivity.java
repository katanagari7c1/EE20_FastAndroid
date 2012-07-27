package com.katanagari.euskal_reporter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.katanagari.euskal_reporter.R;

public class MainScreenActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }
}
