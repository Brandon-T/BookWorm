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

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;

import com.example.layout.ListViewTable;
import com.example.layout.TableAdapter;
import com.example.layout.TableCell;
import com.example.layout.TableRow;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity implements NetworkerCallback, OnItemClickListener {
	private static ListViewTable table = null;
	private static int campus_spinner_item = -1;
	private static int campus_spinner_term = -1;
	private static SharedPreferences settings = null;
	private static final Pattern OPTIONS_VALUES_REGEX = Pattern.compile(">([A-Z0-9]+)\\s-\\s([A-Za-z-\\s]+)\\(([A-Za-z0-9-\\s]+)\\)<");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ListView view = (ListView) findViewById(R.id.list_view);
		view.setTextFilterEnabled(true);
		view.setOnItemClickListener(this);
		
		table = new ListViewTable(view, 3);
		
		TableCell[] headers = {
			new TableCell("Program"),
			new TableCell("Course #"),
			new TableCell("Book-Name")
		};
		
		for (int i = 0; i < headers.length; ++i) {
			headers[i].setWeight(1);
			headers[i].setGravity(Gravity.CENTER);
			headers[i].setMargins(1, 1, 1, 3);
			headers[i].setBackgroundColour(getResources().getColor(R.color.table_header_color));
			headers[i].setTextColour(Color.WHITE);
			headers[i].setWidth(headers[i].getTextWidth(30));
		}
		
		table.add(new TableRow(headers));
		table.alignColumns();
		
		settings = getSharedPreferences(SettingsActivity.PREFERENCES, MODE_PRIVATE);
		campus_spinner_item = settings.getInt("campus_spinner", -1);
		String semester_spinner_item = settings.getString("semester_spinner", null);
		
		if (campus_spinner_item != -1 && semester_spinner_item != null) {
			String campus_ids[] = getResources().getStringArray(R.array.campus_spinner_id);
			
			StringBuilder url = new StringBuilder();
			url.append("http://gbcbookstore.bookware3000.ca/eSolution/course.php?campus=");
			url.append(campus_ids[campus_spinner_item]).append("&term=");
			url.append(Calendar.getInstance().get(Calendar.YEAR));
			campus_spinner_term = Integer.parseInt(semester_spinner_item);
			campus_spinner_term = (int) Math.floor(campus_spinner_term / Math.round(Integer.parseInt(semester_spinner_item) / 2.0f));
			url.append(String.format("%02d", campus_spinner_term));
			
			try {
				new Networker(this).execute(url.toString());
			} catch(Exception e) {
				
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
		searchView.setSearchableInfo(searchableInfo);
		searchView.setIconifiedByDefault(true);
		
		searchView.setOnQueryTextListener(new OnQueryTextListener() {	
			@Override
			public boolean onQueryTextSubmit(String query) {
				searchView.setVisibility(View.VISIBLE);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				ListView view = (ListView) findViewById(R.id.list_view);
				if (newText == null || newText.isEmpty()) {
					view.clearTextFilter();
				} else {
					view.setFilterText(newText);
				}
				return true;
			}
		});
		
		searchView.setOnCloseListener(new OnCloseListener() {		
			@Override
			public boolean onClose() {
				((ListView) findViewById(R.id.list_view)).clearTextFilter();
				return false;
			}
		});
		
		searchView.setOnFocusChangeListener(new OnFocusChangeListener() {		
			@SuppressLint("NewApi")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
		            ((SearchView) v).onActionViewCollapsed();
		            ((SearchView)v).setQuery("", false);
		        }
			}
		});
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_help: {
				Intent intent = new Intent(MainActivity.this, HelpActivity.class);
				startActivity(intent);
				finish();
			}
			break;
				
			case R.id.action_settings: {
				Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(intent);
				finish();
			}
			break;
				
			default:
				return false;
		}
		
		return true;
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			((ListView)findViewById(R.id.list_view)).setFilterText(query);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onFinish(Networker networker) {
		
		String htmlPage = new HTMLParser().trimOptions(networker.getString());
		final Matcher matcher = OPTIONS_VALUES_REGEX.matcher(htmlPage);
		
		new UIThread(this, new UIThreadCallback() {

			@Override
			public void run() {
				TableCell pcell = new TableCell(matcher.group(3).trim());
				pcell.setGravity(Gravity.CENTER);
				pcell.setWidth(pcell.getTextWidth(30));
				
				TableCell ccell = new TableCell(matcher.group(1).trim());
				ccell.setGravity(Gravity.LEFT);
				ccell.setWidth(ccell.getTextWidth(30));
				
				TableCell bcell = new TableCell(matcher.group(2).trim());
				bcell.setGravity(Gravity.LEFT);
				bcell.setWidth(bcell.getTextWidth(30));
				table.add(new TableRow(new TableCell[]{pcell, ccell, bcell}));
			}

			@Override
			public boolean condition() {
				return matcher.find();
			}

			@Override
			public void onThreadFinish() {
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						TableRow headers = table.getRow(0);
						for (int i = 0; i < headers.getCount(); ++i) {
							headers.getCell(i).setWeight(0);
						}
						
						table.alignColumns();
						table.notifyDataSetChanged();
					}		
				});
			}
			
		}, htmlPage.split("</option>").length).execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ListView v = (ListView)parent;
		TableAdapter adapter = (TableAdapter)v.getAdapter();
		TableRow row = adapter.getItem(position);
		
		String Program = row.getCell(0).getValue().toString();
		String Course = row.getCell(1).getValue().toString();
		
		String campus_ids[] = getResources().getStringArray(R.array.campus_spinner_id);

		StringBuilder data = new StringBuilder();
		data.append(campus_ids[campus_spinner_item]).append(',');
		data.append(Calendar.getInstance().get(Calendar.YEAR));
		data.append(String.format("%02d", campus_spinner_term)).append(',');
		data.append(Program).append(',');
		data.append(Course);		
		String link = data.toString();
		
		String url = "http://gbcbookstore.bookware3000.ca/eSolution/course_search.php?list=" + 
					 link + "&course_basket=" + link + "&course[0]=" + link;
		
		new Networker(new NetworkerCallback() {
			
			@Override
			public void onFinish(Networker networker) {
				String htmlPage = networker.getString();
				String available = HTMLParser.betweenEx(htmlPage, "</div><p>", "</p>");
				if (available != null && available.contains("No")) {
					displayDialog("Result..", "There are currently no books required for this course.");
				} else {
					
					htmlPage = HTMLParser.between(htmlPage, "course_header", "</form>");
					if (htmlPage != null) {						
						Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
						intent.putExtra("page", htmlPage);
						startActivity(intent);
					} else {
						displayDialog("Error!", "Unable to parse results.");
					}
				}
			}
		}).execute(url);
	}
	
	private void displayDialog(String title, String text) {
		AlertDialog.Builder messageBox  = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
		messageBox.setMessage(text);
		messageBox.setTitle(title);
		messageBox.setCancelable(true);
		
		messageBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		messageBox.create().show();
	}
}
