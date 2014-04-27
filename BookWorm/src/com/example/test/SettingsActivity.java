/**  © 2014, Brandon T. All Rights Reserved.
 *
 *  This file is part of the BookWorm project.
 *  You may use this file only for your personal, and non-commercial use.
 *  You may not modify or use the contents of this file for any purpose (other
 *  than as specified above) without the express written consent of the author.
 *  You may not reproduce, republish, post, transmit, publicly display,
 *  publicly perform, or distribute in print or electronically any of the contents
 *  of this file without express consent of rightful owner.
 *  This notice must be retained in all files and may not be removed.
 *  This License is subject to change at any time without notice/warning.
 *
 *						Author : Brandon T.
 *						Contact: Brandon.T-@Live.com
 */

package com.example.test;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class SettingsActivity extends Activity {
	
	public static final String FIRST_BOOT = "firstboot";
	public static final String PREFERENCES = "settings";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		final Spinner campusSpinner = (Spinner)findViewById(R.id.campus_spinner);	
		final Spinner semesterSpinner = (Spinner)findViewById(R.id.semester_spinner);	
		SharedPreferences settings = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
		
		int campus_spinner_item = settings.getInt("campus_spinner", -1);
		String semester_spinner_item = settings.getString("semester_spinner", null);
		
		
		if (campus_spinner_item != -1 && semester_spinner_item != null) {
			campusSpinner.setSelection(campus_spinner_item);
			setSpinnerSelection(semesterSpinner, semester_spinner_item);
		}
				
		findViewById(R.id.save_settings).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean firstboot = getIntent().getBooleanExtra(FIRST_BOOT, false);
				
				if (firstboot) {
					getSharedPreferences("settings", MODE_PRIVATE).edit().putBoolean(FIRST_BOOT, false).commit();
				}		
				
				SharedPreferences settings = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
				
				settings.edit().putInt("campus_spinner", campusSpinner.getSelectedItemPosition()).commit();
				settings.edit().putString("semester_spinner", semesterSpinner.getSelectedItem().toString()).commit();
				
				Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	private boolean setSpinnerSelection(Spinner spinner, String item) {
		SpinnerAdapter adapter = spinner.getAdapter();
		
		for (int i = 0; i < adapter.getCount(); ++i) {
			if (item.equals(adapter.getItem(i))) {
				spinner.setSelection(i);
				return true;
			}
		}
		return false;
	}
}
