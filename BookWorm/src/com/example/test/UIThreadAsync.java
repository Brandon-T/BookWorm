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
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;



public class UIThreadAsync extends AsyncTask<Void, Integer, Void> {
	private UIThreadCallback callback = null;
	private ProgressDialog dialog = null;
	private int totalWork = 1, count = 0;
	private int maxValue = 100, incAmount = 1;
	private Context context = null;
	
	public UIThreadAsync(Context context, UIThreadCallback callback, int totalWork) {
		this.context = context;
		this.callback = callback;
		this.totalWork = totalWork > 0 ? totalWork : 1;
	}
	
	@Override
	protected Void doInBackground(Void... args) {
		while(this.callback.condition()) {
			this.callback.run();
			float percentage = ((float)++count / (float)totalWork) * 100.0f;
			this.publishProgress((int) percentage);
		}
		
		return null;
	}
	
	@Override protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		dialog.setProgress(values[0]);
		dialog.incrementProgressBy(incAmount);
		if (dialog.getProgress() >= 100) {
			dialog.setMessage("Setting Data...");
		}
	};
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		dialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_DARK);
		dialog.setCancelable(false);
		dialog.setMessage("Loading...");
		dialog.setProgress(0);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setMax(maxValue);
		dialog.show();
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		this.callback.onThreadFinish();
		
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
	}
	
}
