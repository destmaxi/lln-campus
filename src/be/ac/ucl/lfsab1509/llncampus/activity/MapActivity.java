package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.R;
import android.os.Bundle;
import android.webkit.WebView;


/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier
    Copyright (C) 2014 Quentin De Coninck

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
 * This Activity is called only if the device does not have PDF reader. 
 * It only uses GoogleDocs to read the PDF stored on the drive (need an Internet connection).
 * Related with map.xml.
 *
 */
public class MapActivity extends LLNCampusActivity {
	
		/** URL where the PDF file is stored. */
		public static final String URL_MAP_PDF = 
				"https://docs.google.com/file/d/0B2KSzm3Kdk4LSHZRMWNyTkJnbTA/edit?usp=sharing";
		
		@Override
		protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
	        setContentView(R.layout.map);
	        WebView webview = (WebView) findViewById(R.id.map_webview);
	        webview.loadUrl(URL_MAP_PDF);  
	        finish();
		}
}