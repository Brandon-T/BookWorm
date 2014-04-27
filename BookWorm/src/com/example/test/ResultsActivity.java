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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.layout.ExpandableListAdapter;
import com.example.layout.ExpandableRow;
import com.example.layout.ExpandableRowType;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

public class ResultsActivity extends Activity {
	private static final Pattern infoPattern = Pattern.compile("(?:true|false)(?:\"|')>(.*?)<.*?>\\s*(.*?)\\s*<.*?>(.*?)<.*?>(.*?)<.*?>(.*?)<.*?>(.*?)<.*?><.*?>(.*?)<.*?>.*?href=(?:\"|')(.*?)(?:\"|')");	
	private static final Pattern availabilityPattern = Pattern.compile("<td(?:.*?)item_detail_(?:.*?)_(?:.*?)>(?s)(.*?)<\\/.*?>");
	private HashMap<ExpandableRow, List<ExpandableRow>> adapterList = null;
	private ExpandableListAdapter adapter = null;
	private boolean displayAvailability = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		
		String htmlPage = this.getIntent().getStringExtra("page");
		this.adapterList = new HashMap<ExpandableRow, List<ExpandableRow>>();
		ExpandableListView view = (ExpandableListView) findViewById(R.id.ResultsList);
		view.setAdapter(this.adapter = new ExpandableListAdapter(this, adapterList));
		
		this.parseBookPage(htmlPage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.results, menu);
		return true;
	}

	private void buildResultPage(final String[] data) {	
		if (displayAvailability) {
			new Networker(new NetworkerCallback() {
	
				@Override
				public void onFinish(Networker networker) {
					createBookList(data, parseAvailabilityPage(networker.getString()));
				}
				
			}).execute(data[9]);
		} else {
			createBookList(data, parseAvailabilityPage(null));
		}
	}
	
	private void createBookList(String[] data, String[] availabilityData) {
		ExpandableRow group = new ExpandableRow(data[0]);
		group.setTextColour(Color.WHITE);
		group.setMargins(1, 1, 1, 3);
		group.setBackgroundColour(getResources().getColor(R.color.table_header_color));
		group.setHeight(75);
		
		ArrayList<ExpandableRow> children = new ArrayList<ExpandableRow>();
		ExpandableRow child = new ExpandableRow(createBook(data, availabilityData));
		child.setMultiLine(true);
		child.setStyle(ExpandableRowType.HTML);
		children.add(child);
		ResultsActivity.this.adapterList.put(group, children);
		ResultsActivity.this.adapter.notifyDataSetChanged();
	}
	
	private String[] parseBookPage(String htmlPage) {
		Matcher matcher = infoPattern.matcher(htmlPage);
		
		while(matcher.find()) {
			String name = matcher.group(1) + " " + matcher.group(2);
			String isbn = getGroupData(matcher.group(3), "ISBN");
			String author = getGroupData(matcher.group(4), "Author");
			String publisher = getGroupData(matcher.group(4), "Publisher");
			String edition = getGroupData(matcher.group(5), "Edition");
			String cover = getGroupData(matcher.group(5), "Cover");
			String onhand = getGroupData(matcher.group(6), "On Hand");
			String onorder = getGroupData(matcher.group(6), "On Order");
			String price = getGroupData(matcher.group(7), "Price");
			String url = "http://gbcbookstore.bookware3000.ca" + matcher.group(8);
			
			this.buildResultPage(new String[]{name, isbn, author, publisher, edition, cover, onhand, onorder, price, url});
		}
		return null;
	}
	
	private String[] parseAvailabilityPage(String htmlPage) {
		Matcher matcher = availabilityPattern.matcher(htmlPage);
		ArrayList<String> temp = new ArrayList<String>();
		while(matcher.find()) {
			temp.add(matcher.group(1));
		}
		
		ArrayList<String> results = new ArrayList<String>();
		
		for (int i = 1, j = 0; i < 10; ++i, ++j) {
			String index = temp.get(i).replaceAll("<.*?>", "").replaceAll("Campus", "").trim();
			String data[] = index.split("\r|\n|\r\n");
			if (data.length > 1) {
				results.add(j, data[0].replaceAll("(.*?):(\\s*)", "").trim());
				results.add(++j, data[1].replaceAll("(.*?):(\\s*)", "").trim());
			} else {
				results.add(index.replaceAll("(.*?):(\\s*)", "").trim());
			}
		}

		return results.isEmpty() ? null : results.toArray(new String[results.size()]);
	}
	
	private String getGroupData(String htmlSection, String category) {
		String data[] = htmlSection.split("(&.*?;)+");
		for (int i = 0; i < data.length; ++i) {
			if (data[i].contains(category)) {
				return data[i].split(":(\\s*)")[1].trim();
			}
		}
		return "N/A";
	}
	
	private Spanned createBook(String[] data, String[] availabilityData) {
		boolean bookAvailable = false;
		StringBuilder builder = new StringBuilder();
		
		if (displayAvailability && availabilityData != null) {
			for (int i = 0; i < availabilityData.length; i += 4) {
				int onHand = Integer.valueOf(availabilityData[i + 2]);
				int onOrder = Integer.valueOf(availabilityData[i + 3]);
				if (onHand != 0 || onOrder != 0) {
					bookAvailable = true;
				}
			}
		}
		
		builder.append("<b><font size='4' color='blue'>Information</font></b><br />");
		
		builder.append("<p><font size='2'>");
		
		builder.append("<b>ISBN:</b>");
		builder.append(HTMLParser.padLeft("", 16).replaceAll("\\s", "&nbsp;")).append(data[1]).append("<br />");
		
		builder.append("<b>Author:</b>");
		builder.append(HTMLParser.padLeft("", 13).replaceAll("\\s", "&nbsp;")).append(data[2]).append("<br />");
		
		builder.append("<b>Publisher:</b>");
		builder.append(HTMLParser.padLeft("", 8).replaceAll("\\s", "&nbsp;")).append(data[3]).append("<br />");

		builder.append("<b>Edition:</b>");
		builder.append(HTMLParser.padLeft("", 13).replaceAll("\\s", "&nbsp;")).append(data[4]).append("<br />");
		
		builder.append("<b>Cover:</b>");
		builder.append(HTMLParser.padLeft("", 15).replaceAll("\\s", "&nbsp;")).append(data[5]).append("<br />");
		
		if (!displayAvailability || !bookAvailable) {
			builder.append("<b>On Hand:</b>");
			builder.append(HTMLParser.padLeft("", 10).replaceAll("\\s", "&nbsp;")).append(data[6]).append("<br />");
			
			builder.append("<b>On Order:</b>");
			builder.append(HTMLParser.padLeft("", 10).replaceAll("\\s", "&nbsp;")).append(data[7]).append("<br />");
			bookAvailable = false;
		}
		
		builder.append("<b>Price:</b>");
		builder.append(HTMLParser.padLeft("", 16).replaceAll("\\s", "&nbsp;")).append(data[8]).append("<br />");
		
		if (displayAvailability && availabilityData != null) {
			builder.append("<br /><b><font size='4' color='blue'>Availability</font></b><br />");
			
			for (int i = 0; i < availabilityData.length; ) {
				int onHand = Integer.valueOf(availabilityData[i + 2]);
				int onOrder = Integer.valueOf(availabilityData[i + 3]);
				
				if (onHand != 0 || onOrder != 0) {
					bookAvailable = true;
					builder.append("<b>Campus:</b>");
					builder.append(HTMLParser.padLeft("", 11).replaceAll("\\s", "&nbsp;")).append(availabilityData[i + 0]).append("<br />");
					
					builder.append("<b>Phone:</b>");
					builder.append(HTMLParser.padLeft("", 14).replaceAll("\\s", "&nbsp;")).append(availabilityData[i + 1]).append("<br />");
					
					builder.append("<b>On Hand:</b>");
					builder.append(HTMLParser.padLeft("", 10).replaceAll("\\s", "&nbsp;")).append(availabilityData[i + 2]).append("<br />");
					
					builder.append("<b>On Order:</b>");
					builder.append(HTMLParser.padLeft("", 10).replaceAll("\\s", "&nbsp;")).append(availabilityData[i + 3]).append("<br />");
					builder.append("<br />");
				}
				
				i += 4;
			}
			
			if (!bookAvailable) {
				builder.append("This book is not available at any campus.");
			}
		}
		builder.append("</font></p>");
		
		return Html.fromHtml(builder.toString());
	}
}
