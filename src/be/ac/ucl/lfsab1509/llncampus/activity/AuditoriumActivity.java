package be.ac.ucl.lfsab1509.llncampus.activity;



import java.util.ArrayList;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
import be.ac.ucl.lfsab1509.llncampus.fragment.AuditoriumDetailsFragment;
import be.ac.ucl.lfsab1509.llncampus.fragment.AuditoriumListFragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

/**
 * This class is intended to create a list of auditoriums in order to make a
 * clickable list for the user.
 * Related with the xml file auditorium_list_fragment.xml
 * (This file is different if we are in landscape or not)
 *
 */
public class AuditoriumActivity extends LLNCampusActivity implements AuditoriumListFragment.OnAuditoriumSelectedListener, OnClickListener {
	ArrayList<String> values = null;
	private IAuditorium current_auditorium;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auditorium_list_fragment);

		// View vue = findViewById(R.id.auditorium_list_fragment);
		// vue.setBackgroundColor(getResources().getColor(R.color.Blue));  // If we want to change color of background
	}

	// On remet en mode portrait pour les petits devices
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

    /*
     * Adding an item click listener to the list
     */

	protected void onListItemClick(ListView l, View v, int position, long id) {
		//the intent is used to start a new activity
    	Intent intent = new Intent(this, DetailsAuditorium.class);
    	/*
		 * before starting the new activity, we serialize the selected Poi from the list and add it as a parameter to the intent with the method 'putExtra'
		 * Note that non-serializable fields of the Poi (marked as 'transient') will not be included in the serialized object.
		 * This is the case for the data source of the Poi. Then, when de-serialized, the Poi should get another data source
		 */

    	intent.putExtra("NAME", values.get(position));
		startActivity(intent); //starts the activity denoted by this intent.
    }


	public void onAuditoriumSelected(IAuditorium auditorium) {

		current_auditorium = auditorium;
		// Find the fragment related to the "subfragment" in the xml file
		AuditoriumDetailsFragment viewer = (AuditoriumDetailsFragment) getFragmentManager()
	            .findFragmentById(R.id.auditorium_details_fragment);
		// Check if the viewer is in the layout (i.e., if we are in landscape)
	    if (viewer == null || !viewer.isInLayout()) {
	    	// Create a new intent and give all the useful data for it
	    	Intent showContent = new Intent(getApplicationContext(),
					DetailsAuditorium.class);
			showContent.putExtra("NAME", auditorium.getName());
			showContent.putExtra("ADDRESS", auditorium.getAddress());
			double[] coord = {auditorium.getLatitude(), auditorium.getLongitude() };
			showContent.putExtra("COORD", coord);
			showContent.putExtra("ID", auditorium.getID());
			startActivity(showContent);
	    } else { // We are in landscape, so the activity manage directly information
	        viewer.updateAuditorium(auditorium);
	        setListeners();
	    }
	}

	/*
	 * Les deux methodes qui suivent ne seront utilisees quand dans le cas ou la tablette serait en
	 * paysage; gerer ce qu'aurait du faire DetailsAuditorium.
	 */

	// Set the listener
	private void setListeners() {
		View GPSButton = findViewById(R.id.button_auditorium_gps);
		GPSButton.setOnClickListener(this);
		View subButton = findViewById(R.id.button_subauditorium);
		subButton.setOnClickListener(this);
	}

	// Permet de définir l'action effectuée grâce à l'appui sur un bouton
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button_auditorium_gps:
			intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?daddr=" + current_auditorium.getLatitude() + "," + current_auditorium.getLongitude() + "&dirflg=w"));
			intent.setComponent(new ComponentName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity"));
			startActivity(intent);
			break;
		case R.id.button_subauditorium:
			intent = new Intent(this, SubAuditoriumActivity.class);
			intent.putExtra("IDPARENT", current_auditorium.getID());
			startActivity(intent);
			break;
		}
	}
}