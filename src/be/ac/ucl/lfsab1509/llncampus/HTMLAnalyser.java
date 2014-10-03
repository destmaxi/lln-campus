package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;

/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Class to analyze and extract information from HTML.
 */
public final class HTMLAnalyser {
	
	static int count = 0;

	/**
	 * Default constructor.
	 */
	private HTMLAnalyser() {
		
	}

	/**
	 * Extract parts between < tag(...) > and < /tag > of an HTML code.
	 * 
	 * @param html
	 *            HTML Code.
	 * @param tag
	 *            Tag name.
	 * @return List of content of the tags.
	 */
	public static ArrayList<String> getTagsContent(String html, String tag) {
		ArrayList<String> tagsContent = new ArrayList<String>();
		int start, stop, endStart, i, nextStart;
		String theString = html;
		while ((start = theString.indexOf("<" + tag)) != -1
				&& (endStart = theString.indexOf('>', start)) != -1
				&& (stop = theString.indexOf("</" + tag + ">", endStart)) != -1) {
			i = endStart;

			/* To cope with nested tags of same type */
			while ((nextStart = theString.indexOf("<" + tag, i)) < stop
					&& nextStart > 0) {
				i = nextStart + tag.length() + 2;
				stop = theString.indexOf("</" + tag + ">", stop);
			}
			tagsContent.add(theString.substring(endStart + 1, stop));

			count++;
			theString = theString.substring(stop + 3 + tag.length());
		}
		return tagsContent;
	}

	/**
	 * Remove HTML tag to return only its content.
	 * 
	 * @param html
	 *            HTML code. 
	 * @return html without the wrapping (x)HTML tag.
	 */
	public static String removeHTMLTag(String html) {
		int start, stop;
		// Looks for an opening of tag "<" and a closing one ">"
		if ((start = html.indexOf('<')) != -1
				&& (stop = html.indexOf('>', start)) != -1) {
			String toReturn = "";
			if (start > 0) {
				toReturn += html.substring(0, start);
			}
			toReturn += html.substring(stop + 1);
			return removeHTMLTag(toReturn);
		}
		// No remaining tag, return the parameter
		return html;
	}
}