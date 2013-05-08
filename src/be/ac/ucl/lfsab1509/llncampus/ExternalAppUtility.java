package be.ac.ucl.lfsab1509.llncampus;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;


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
 * 
 * Utility class for invoking other Android applications
 * 
 */
public class ExternalAppUtility {
	private static CookieStore cookieStore = null;
	/**
	 * Constructor
	 */
	private ExternalAppUtility() {
		// Nothing to do ... 
	}

	/** 
	 * Open a browser on the URL specified.
	 * @param context Contexte de l'application. 
	 * @param url The url...
	 */
	public static void openBrowser(Context context, String url) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(intent);
	}
	
	public static void openBrowser(Context context, Uri uri) {
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(intent);
	}
	
	/**
	 * Cree un nouveau client HTTP avec toujours la meme session de cookiees.
	 * (Utile pour les connexions a ADE par exemple)
	 * @return HttpClient 
	 */
	public static synchronized HttpClient getHttpClient() {
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		if (cookieStore == null) {
			cookieStore = httpClient.getCookieStore();
		} else {
			httpClient.setCookieStore(cookieStore);
		}
		HttpProtocolParams.setUserAgent(httpClient.getParams(), "Mozilla 5/0");
		return httpClient;
	}

	public static void startGPS(float lat, float lon, Context c) {
		try{
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
					Uri.parse("http://maps.google.com/maps?daddr="+lat+","+lon+ "&dirflg=w"));
			intent.setComponent(new ComponentName("com.google.android.apps.maps", 
					"com.google.android.maps.MapsActivity"));          
			c.startActivity(intent);

		} catch (ActivityNotFoundException e) { // If we don't have Google Maps
			Log.e("ExternalAppUtility",c.getString(R.string.no_google_maps));
			Toast t = Toast.makeText(LLNCampus.getContext().getApplicationContext(), R.string.no_google_maps,
					Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		}
	}
	public static void startGPS(double lat, double lon, Context c){
		startGPS((float) lat, (float) lon, c);
	}
	
}
