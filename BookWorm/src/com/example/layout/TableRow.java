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

import android.graphics.Color;

public class TableRow {
	private TableCell[] cells = null;
	private int bgColour = Color.WHITE;
	
	public TableRow(TableCell[] cells) {
		this.cells = cells;
	}
	
	public TableCell getCell(int position) {
		return cells[position];
	}
	
	public int getCount() {
		return this.cells.length;
	}
	
	public void setBackgroundColour(int colour) {
		this.bgColour = colour;
	}
	
	public int getBackgroundColour() {
		return this.bgColour;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < cells.length; ++i) {
			builder.append(String.valueOf(cells[i].getValue())).append(' ');
		}
		return builder.toString().trim();
	}
}
