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

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

public class UIThread {
	private UIThreadCallback callback = null;
	private ProgressDialog dialog = null;
	private int totalWork = 1, count = 0;
	private int maxValue = 100, incAmount = 1;
	private Context context = null;
	
	public UIThread(Context context, UIThreadCallback callback, int totalWork) {
		this.context = context;
		this.callback = callback;
		this.totalWork = totalWork > 0 ? totalWork : 1;
	}
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (dialog.getProgress() >= 99) {
				dialog.setMessage("Setting Data..");
			} else {
				dialog.setProgress(msg.what);
				dialog.incrementProgressBy(incAmount);
			}
			super.handleMessage(msg);
		}
	};
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void execute() {
		dialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_DARK);
		dialog.setCancelable(false);
		dialog.setMessage("Loading..");
		dialog.setProgress(0);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setMax(maxValue);
		dialog.show();
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while(UIThread.this.callback.condition()) {
					UIThread.this.callback.run();
					float percentage = ((float)++count / (float)totalWork) * 100.0f;
					handler.sendEmptyMessage((int) percentage);
				}
				
				UIThread.this.callback.onThreadFinish();
				
				if (UIThread.this.dialog.isShowing()) {
					UIThread.this.dialog.dismiss();
				}
			}
		};
		
		new Thread(runnable).start();
	}
}
