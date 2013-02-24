package be.ac.ucl.lfsab1509.llncampus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/*
 * Utility class for invoking other Android applications
 * 
 */
public class ExternalAppUtility {
	
	/** Open a browser on the URL specified */
	public static void openBrowser(Context context, String url) {
	Uri uri = Uri.parse(url);
	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	context.startActivity(intent);
	}
}
