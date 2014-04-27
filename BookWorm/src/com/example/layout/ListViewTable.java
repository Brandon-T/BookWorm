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

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class ListViewTable {
	private int columnCount = 0;
	private ListView view = null;
	private final ArrayList<TableRow> table = new ArrayList<TableRow>();
	
	public ListViewTable(ListView view, int columnCount) {
		this.view = view;
		this.columnCount = columnCount;
		view.setAdapter(new TableAdapter(view.getContext(), this.table));
	}
	
	public void notifyDataSetChanged() {
		((ArrayAdapter<TableRow>) view.getAdapter()).notifyDataSetChanged();
	}
	
	public void add(TableRow row) {
		table.add(row);
	}
	
	public void add(TableCell cells[]) {
		table.add(new TableRow(cells));
	}
	
	public TableRow getRow(int index) {
		return table.get(index);
	}
	
	public void alignColumns() {
		int widths[] = new int[columnCount];
		int heights[] = new int[columnCount];
		
		for (TableRow row : table) {
			for (int i = 0, j = 0; i < columnCount; ++i) {
				j = i % columnCount;
				TableCell cell = row.getCell(i);
				widths[j] = cell.getWidth() >= widths[j] ? cell.getWidth() : widths[j];
				heights[j] = cell.getHeight() >= heights[j] ? cell.getHeight() : heights[j];
			}
		}
		
		for (int i = 0; i < widths.length; ++i) {
			if (widths[i] == 0) {
				widths[i] = LayoutParams.WRAP_CONTENT;
			}
			
			if (heights[i] == 0) {
				heights[i] = LayoutParams.WRAP_CONTENT;
			}
		}
		
		for (TableRow row : table) {
			for (int i = 0, j = 0; i < columnCount; ++i) {
				j = i % columnCount;
				TableCell cell = row.getCell(i);
				cell.setWidth(widths[j]);
				cell.setHeight(heights[j]);
			}
		}
	}
}
