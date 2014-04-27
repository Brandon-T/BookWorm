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

import java.util.ArrayList;
import java.util.List;

import com.example.test.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TableAdapter extends ArrayAdapter<TableRow> implements Filterable {
	
	private Filter mFilter = null;
	private List<TableRow> dataList = null;
	private List<TableRow> mOriginalValues = null;
	
	public TableAdapter(Context context, List<TableRow> table) {
		super(context, R.layout.activity_main, table);
		this.dataList = table;
	}

	@Override
	public int getCount() {
		return this.dataList.size();
	}

	@Override
	public TableRow getItem(int position) {
		return this.dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return new ViewHolder(getContext(), this.dataList.get(position));
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private class ViewHolder extends LinearLayout {

		public ViewHolder(Context context, TableRow row) {
			super(context);
			for (int i = 0; i < row.getCount(); ++i) {
				TableCell cell = row.getCell(i);
				this.setBackgroundColor(row.getBackgroundColour());
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cell.getWidth(), cell.getHeight(), cell.getWeight());
				params.setMargins(cell.getMargins()[0], cell.getMargins()[1], cell.getMargins()[2], cell.getMargins()[3]);
				
				if (cell.getType() == TableCellTypes.TEXT) {
					TextView view = new TextView(context);
					view.setLines(1);
					view.setGravity(cell.getGravity());
					view.setTextColor(cell.getTextColour());
					view.setBackgroundColor(cell.getBackgroundColour());
					view.setText(String.valueOf(cell.getValue()));
					view.setTypeface(view.getTypeface(), cell.getTextStyle());
					this.addView(view, params);
				} else if (cell.getType() == TableCellTypes.IMAGE) {
					ImageView view = new ImageView(context);
					view.setBackgroundColor(Color.WHITE);
					view.setImageResource((Integer)cell.getValue());
					this.addView(view, params);
				} else if (cell.getType() == TableCellTypes.VIEW) {
					this.addView((View)cell.getValue(), params);
				}
			}
		}
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
	}
	
	private class ArrayFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			List<TableRow> FilteredList = new ArrayList<TableRow>();
			
			if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<TableRow>(dataList);
            }
			
			if (constraint == null || constraint.length() == 0) {
                results.values = mOriginalValues;
                results.count = mOriginalValues.size();
            } else {
            	constraint = constraint.toString().toLowerCase().trim();
            	for (int i = 0; i < mOriginalValues.size(); ++i) {
                    String data = mOriginalValues.get(i).toString();
                    if (data.toLowerCase().contains(constraint.toString())) {
                        FilteredList.add(mOriginalValues.get(i));
                        System.out.println(data.toLowerCase());
                    }
                }
            	
            	results.count = FilteredList.size();
                results.values = FilteredList;
            }

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			dataList = (List<TableRow>) results.values;
            notifyDataSetChanged();
		}
		
	}
}
