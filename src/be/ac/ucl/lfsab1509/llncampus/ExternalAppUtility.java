package be.ac.ucl.lfsab1509.llncampus;

import java.net.URI;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;

import be.ac.ucl.lfsab1509.llncampus.activity.LLNCampusActivity;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
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
			//FIXME : Afficher le message à l'utilisateur
		}
	}
	public static void startGPS(double lat, double lon, Context c){
		startGPS((float) lat, (float) lon, c);
	}
	
}
