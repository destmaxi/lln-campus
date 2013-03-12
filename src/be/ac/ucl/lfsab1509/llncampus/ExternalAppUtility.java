package be.ac.ucl.lfsab1509.llncampus;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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
	
}
