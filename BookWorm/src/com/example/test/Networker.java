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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;


import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

public class Networker extends AsyncTask<String, Void, HttpResponse> {
	
	private NetworkerCallback callback;
	private StringBuilder builder = new StringBuilder();
	
	public Networker(NetworkerCallback callback) {
		this.callback = callback;
	}

	@Override
	protected HttpResponse doInBackground(String... urls) {
		String link = urls[0];
		HttpResponse response = null;
        HttpGet request = new HttpGet(link);
        AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        
        try {
        	builder.setLength(0);
            response = client.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			String line = null; 
    	    while((line = reader.readLine()) != null) {
    	    	builder.append(line).append('\n');
    	    }
    	    reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	client.close();
        }
        return null;
	}
	
	@Override
	protected void onPostExecute(HttpResponse result) {	
		super.onPostExecute(result);
		this.callback.onFinish(this);
	}
	
	public String getString() {
		return builder.toString();
	}
}
