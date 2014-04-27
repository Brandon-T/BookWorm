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

package com.example.layout;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	private Context context = null;
	private HashMap<ExpandableRow, List<ExpandableRow>> dataList = null;

	public ExpandableListAdapter(Context context, HashMap<ExpandableRow, List<ExpandableRow>> data) {
		this.context = context;
		this.dataList = data;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this.dataList.get(this.getGroup(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosititon) {
		return childPosititon;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.dataList.get(this.getGroup(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.dataList.keySet().toArray()[groupPosition];
	}

	@Override
	public int getGroupCount() {	
		return this.dataList.keySet().size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View view, ViewGroup parent) {
		return new ViewHolder(this.context, (ExpandableRow)this.getChild(groupPosition, childPosition));
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		return new ViewHolder(this.context, (ExpandableRow)this.getGroup(groupPosition));
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private class ViewHolder extends LinearLayout {

		public ViewHolder(Context context, ExpandableRow row) {
			super(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(row.getWidth(), row.getHeight());
			params.setMargins(row.getMargins()[0], row.getMargins()[1], row.getMargins()[2], row.getMargins()[3]);	
			this.setBackgroundColor(row.getBackgroundColour());
			
			if (row.getType() == ExpandableRowType.TEXT) {
				TextView view = new TextView(context);
				view.setLines(1);
				view.setTextColor(row.getTextColour());
				view.setBackgroundColor(row.getBackgroundColour());
				view.setText(String.valueOf(row.getValue()));
				view.setTypeface(view.getTypeface(), row.getTextStyle());
				view.setSingleLine(!row.isMultiLine());
				this.addView(view, params);
			} else if (row.getType() == ExpandableRowType.IMAGE) {
				ImageView view = new ImageView(context);
				view.setBackgroundColor(Color.WHITE);
				view.setImageResource((Integer)row.getValue());
				this.addView(view, params);
			} else if (row.getType() == ExpandableRowType.VIEW) {
				this.addView((View)row.getValue(), params);
			} else if (row.getType() == ExpandableRowType.HTML) {
				TextView view = new TextView(context);
				view.setLines(1);
				view.setTextColor(row.getTextColour());
				view.setBackgroundColor(row.getBackgroundColour());
				view.setText((Spanned)row.getValue());
				view.setTypeface(view.getTypeface(), row.getTextStyle());
				view.setSingleLine(!row.isMultiLine());
				this.addView(view, params);
			}
		}
	}
}
