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
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout.LayoutParams;

public class TableCell {
	private Object value;
	private int weight = 0;
	private int bgColour = 0xFFCCCCCC;
	private int textColour = Color.BLACK;
	private int gravity = Gravity.LEFT;
	private int[] margins = { 1, 1, 1, 1 };
	private int textStyle = Typeface.NORMAL;
	private int width = LayoutParams.WRAP_CONTENT;
	private int height = LayoutParams.WRAP_CONTENT;
	private TableCellTypes type = TableCellTypes.TEXT;

	public TableCell(Object value) {
		this.value = value;
	}

	public void setGravity(int gravity) {
		this.gravity = gravity;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public void setTextColour(int colour) {
		this.textColour = colour;
	}

	public void setBackgroundColour(int colour) {
		this.bgColour = colour;
	}

	public void setTextStyle(int style) {
		this.textStyle = style;
	}
	
	public void setStyle(TableCellTypes type) {
		this.type = type;
	}

	public void setMargins(int left, int top, int right, int bottom) {
		this.margins = new int[] { left, top, right, bottom };
	}

	public Object getValue() {
		return this.value;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	public int getBackgroundColour() {
		return this.bgColour;
	}
	
	public int getTextColour() {
		return this.textColour;
	}
	
	public int getGravity() {
		return this.gravity;
	}
	
	public int[] getMargins() {
		return this.margins;
	}
	
	public int getTextStyle() {
		return this.textStyle;
	}
	
	public TableCellTypes getType() {
		return this.type;
	}
	
	public int getTextWidth(int size) {
		Paint paint = new Paint();
	    paint.setTypeface(null);
	    paint.setTextSize(size);   
	    return (int)paint.measureText(String.valueOf(this.value));
	}
}
