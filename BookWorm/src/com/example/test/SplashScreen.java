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

import com.example.test.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class SplashScreen extends Activity {
	
	private SystemUiHider mSystemUiHider;
	private static final boolean AUTO_HIDE = true;
	private static final boolean TOGGLE_ON_CLICK = false;
	private static final int AUTO_HIDE_DELAY_MILLIS = 1000;
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	
	Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);
		View view = getWindow().getDecorView();
		
		mSystemUiHider = SystemUiHider.getInstance(this, view, HIDER_FLAGS);
		mSystemUiHider.setup();
		
		mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {		
			@Override
			public void onVisibilityChange(boolean visible) {
				if (visible && AUTO_HIDE) {
                    delayedHide(AUTO_HIDE_DELAY_MILLIS);
                }
			}
		});
		
		view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });
		
		mHideHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Class<?> cls = MainActivity.class;
				boolean firstboot = getSharedPreferences(SettingsActivity.PREFERENCES, MODE_PRIVATE).getBoolean(SettingsActivity.FIRST_BOOT, true);
				
			    if (firstboot) {
			    	cls = SettingsActivity.class;
			    }
			    
				Intent intent = new Intent(SplashScreen.this, cls);
				intent.putExtra(SettingsActivity.FIRST_BOOT, firstboot);
				startActivity(intent);
				finish();
			}
		}, 3000);
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(25);
    }
    
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
