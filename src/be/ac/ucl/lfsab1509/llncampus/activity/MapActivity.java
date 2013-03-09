package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.R;
import android.os.Bundle;
import android.webkit.WebView;


/**
 * Cette classe ne sera appelée que si l'appareil ne possède pas de lecteur PDF
 * Elle consiste simplement à utiliser le GoogleDocs avec le PDF (requiert une connection Internet!)
 * @author Quentin
 *
 */
public class MapActivity extends LLNCampusActivity {
	
		// final String FILE_PATH = "file:///android_asset/plan_2007recto2.png";
		final String URL = "https://docs.google.com/file/d/0B2KSzm3Kdk4LQmcwRjg3TXdFWDA/edit?usp=sharing";
		
		@Override
		protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
	        setContentView(R.layout.map);
	        
	        WebView webview = (WebView) findViewById(R.id.map_webview);
	        
	        // TODO
	        webview.loadUrl(URL);
	        
	        /*
	        webview.getSettings().setBuiltInZoomControls(true);
	        webview.getSettings().setDisplayZoomControls(false);
	        webview.getSettings().setLoadWithOverviewMode(true);
	        webview.getSettings().setUseWideViewPort(true);
	        webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	        webview.loadUrl("file:///sdcard/plan_2007recto.png");
	        */
	        //Uri uri=Uri.parse(FILE_PATH);
	        
	        //WebView myMap = (WebView) findViewById(R.id.map_view);
	        //myMap.setImageURI(uri);
		}
}

		
		