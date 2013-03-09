package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.R;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

public class MapActivity extends LLNCampusActivity {
	
		final String FILE_PATH = "file:///android_asset/plan_2007recto2.png";
		
		@Override
		protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
	        setContentView(R.layout.map);
	        
	        Uri uri=Uri.parse(FILE_PATH);
	        
	        //WebView myMap = (WebView) findViewById(R.id.map_view);
	        //myMap.setImageURI(uri);
		}
}

		
		