package be.ac.ucl.lfsab1509.llncampus.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import be.ac.ucl.lfsab1509.llncampus.Library;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.fragment.LibraryDetailsFragment;

/**
 * Class intended for showing some information about an Library
 *
 */
public class DetailsLibrary extends LLNCampusActivity implements OnClickListener {
	private Library library;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.library_details_fragment);
	        
	        String name = getIntent().getStringExtra("NAME");
	        String address = getIntent().getStringExtra("ADDRESS");
	        double []coord = getIntent().getDoubleArrayExtra("COORD");
	        int id = getIntent().getIntExtra("ID", 0);
	        String schedule = getIntent().getStringExtra("SCHEDULE");
	        library = new Library (id, name, coord[0], coord[1], address, schedule);
	         
	        
	        LibraryDetailsFragment viewer = (LibraryDetailsFragment) getFragmentManager().findFragmentById(R.id.library_details_fragment);
	        viewer.updateLibrary(library);
	        
			setListeners();
	    }
	 
	 
	 private void setListeners() {
	        View GPSButton = findViewById(R.id.button_library_gps);
	        GPSButton.setOnClickListener(this);
	    }
	    
	    // Permet de definir l'action effectuee grace a l'appui sur un bouton
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.button_library_gps:
				intent = new Intent(android.content.Intent.ACTION_VIEW, 
							Uri.parse("http://maps.google.com/maps?daddr="+library.getLatitude()+","+library.getLongitude()+ "&dirflg=w"));
			    intent.setComponent(new ComponentName("com.google.android.apps.maps", 
					            "com.google.android.maps.MapsActivity"));          
				startActivity(intent);
				//ExternalAppUtility.openBrowser(DetailsLibrary.this, "google.navigation:dirflg=w&q="+.getLatitude()+","+.getLongitude());
				break;	
			}
		}
	 
}
