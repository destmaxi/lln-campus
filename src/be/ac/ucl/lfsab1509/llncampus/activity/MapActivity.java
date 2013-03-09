package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.R;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;


/**
 * Cette classe ne sera appelée que si l'appareil ne possède pas de lecteur PDF
 * Elle consiste simplement à utiliser le GoogleDocs avec le PDF (requiert une connection Internet!)
 * @author Quentin
 *
 */
public class MapActivity extends LLNCampusActivity {
	
		final String FILE_PATH = "file:///android_asset/plan_2007recto2.png";
		
		@Override
		protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
	        setContentView(R.layout.map);
	        
	        WebView webview = (WebView) findViewById(R.id.map_webview);
	        
	        // TODO
	        webview.loadUrl("http://docs.google.com/gview?embedded=true&url=http://myurl.com/demo.pdf");
	        
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

		
		