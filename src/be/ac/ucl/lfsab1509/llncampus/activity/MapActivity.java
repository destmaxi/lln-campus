package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.R;
import android.os.Bundle;
import android.webkit.WebView;


/**
 * Cette classe ne sera appelée que si l'appareil ne possède pas de lecteur PDF
 * Elle consiste simplement à utiliser le GoogleDocs avec le PDF (requiert une connection Internet!)
 * Related with map.xml
 *
 */
public class MapActivity extends LLNCampusActivity {
	
		// URL ou est stockee l'image (avec lecteur de PDF integre puisque Google Docs)
		final String URL = "https://docs.google.com/file/d/0B2KSzm3Kdk4LQmcwRjg3TXdFWDA/edit?usp=sharing";
		
		@Override
		protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
	        setContentView(R.layout.map);
	        
	        WebView webview = (WebView) findViewById(R.id.map_webview);
	        
	        
	        webview.loadUrl(URL);
	        
	        finish();
		}
}