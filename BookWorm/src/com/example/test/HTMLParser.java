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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser {
	
	public static String between(String Str, String Start, String End) {
	    int i = Str.indexOf(Start);
	    if (i != -1) {
	        int j = Str.indexOf(End, i + Start.length());
	        return j != -1 ? Str.substring(i, j + End.length()) : null;
	    }
	    return null;
	}

	public static String betweenEx(String Str, String Start, String End) {
	    final Pattern m = Pattern.compile(Start + "(.*)" + End);
	    Matcher matcher = m.matcher(Str);
	    return matcher.find() ? matcher.group(1) : null;
	}
	
	public static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}
	
	public String trimOptions(String data) {
		int start = data.indexOf("<td><select");
		start = start != -1 ? data.indexOf("<option", start) : -1;
		int end = start != -1 ? data.indexOf("</select>", start) : -1;
		
		if (end != -1) {
			data = data.substring(start, end).trim();
			return data;
		}
		return null;
	}
}
