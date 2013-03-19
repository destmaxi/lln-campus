package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.fragment.LibraryListFragment;
import be.ac.ucl.lfsab1509.llncampus.fragment.LibraryDetailsFragment;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ILibrary;

/**
 * This class is intended to create a list of libraries in order to make a clickable list for the user.
 *
 */
public class LibraryActivity extends LLNCampusActivity implements LibraryListFragment.OnLibrarySelectedListener, OnClickListener {
	ArrayList<String> values = null;
	private ILibrary current_library;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_list_fragment);
				 
		View vue = findViewById(R.id.library_list_fragment);
		vue.setBackgroundColor(getResources().getColor(R.color.Blue)); 
	}
	

    /*
     * Adding an item click listener to the list
     */

	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(this, DetailsLibrary.class); //the intent is used to start a new activity
    	/*
		 * before starting the new activity, we serialize the selected Poi from the list and add it as a parameter to the intent with the method 'putExtra'
		 * Note that non-serializable fields of the Poi (marked as 'transient') will not be included in the serialized object.
		 * This is the case for the data source of the Poi. Then, when de-serialized, the Poi should get another data source
		 */
    	// Here, give to the next activity some informations about what you did before (e.g. on which button you pressed)
    	/*
    	intent.putExtra(VISITABLE_ENTITY, cities.get(position));
    	intent.putExtra("Index", position);
    	*/
    	
    	intent.putExtra("NAME", values.get(position));
		startActivity(intent); //starts the activity denoted by this intent. 
    }
	
	
	public void onLibrarySelected(ILibrary library){
		
		current_library = library;
		LibraryDetailsFragment viewer = (LibraryDetailsFragment) getFragmentManager()
	            .findFragmentById(R.id.library_details_fragment);
	    if (viewer == null || !viewer.isInLayout()) {
	    	Intent showContent = new Intent(getApplicationContext(),
					DetailsLibrary.class);
			showContent.putExtra("NAME",library.getName());
			showContent.putExtra("ADDRESS", library.getAddress());
			double[] coord={ library.getLatitude(), library.getLongitude() };
			showContent.putExtra("COORD", coord);
			showContent.putExtra("ID", library.getID());
			showContent.putExtra("SCHEDULE", library.getSchedule());
			startActivity(showContent);
	    } else {
	        viewer.updateLibrary(library);
	        setListeners();
	    }
	}
	
	/*
	 * Les deux methodes qui suivent ne seront utilisees quand dans le cas ou la tablette serait en
	 * paysage; gerer ce qu'aurait du faire DetailsLibrary.
	 */
	
	 private void setListeners() {
	        View GPSButton = findViewById(R.id.button_library_gps);
	        GPSButton.setOnClickListener(this);
	    }
	    
	    // Permet de définir l'action effectuée grâce à l'appui sur un bouton
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.button_library_gps:
				intent = new Intent(android.content.Intent.ACTION_VIEW, 
							Uri.parse("http://maps.google.com/maps?daddr="+current_library.getLatitude()+","+current_library.getLongitude()+ "&dirflg=w"));
			    intent.setComponent(new ComponentName("com.google.android.apps.maps", 
					            "com.google.android.maps.MapsActivity"));          
				startActivity(intent);
				//ExternalAppUtility.openBrowser(DetailsLibrary.this, "google.navigation:dirflg=w&q="+library.getLatitude()+","+library.getLongitude());
				break;		
			}
		}

}
