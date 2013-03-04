package be.ac.ucl.lfsab1509.llncampus.activity;



import java.util.ArrayList;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
import be.ac.ucl.lfsab1509.llncampus.fragment.AuditoriumDetailsFragment;
import be.ac.ucl.lfsab1509.llncampus.fragment.AuditoriumListFragment;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This class is intended to create a list of auditoriums in order to make a clickable list for the user.
 * @author Quentin & Anh Tuan
 * @version 19/02/2013
 *
 */
public class AuditoriumActivity extends LLNCampusActivity implements AuditoriumListFragment.OnAuditoriumSelectedListener, OnClickListener {
	ArrayList<String> values = null;
	private IAuditorium current_auditorium;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auditorium_list_fragment);
				 
		View vue = findViewById(R.id.auditorium_list_fragment);
		vue.setBackgroundColor(getResources().getColor(R.color.Blue)); 
	}
	

    /*
     * Adding an item click listener to the list
     */

	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(this, DetailsAuditorium.class); //the intent is used to start a new activity
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
	
	public void onAuditoriumSelected(IAuditorium auditorium){
		
		current_auditorium = auditorium;
		AuditoriumDetailsFragment viewer = (AuditoriumDetailsFragment) getFragmentManager()
	            .findFragmentById(R.id.auditorium_details_fragment);
	    if (viewer == null || !viewer.isInLayout()) {
	    	Intent showContent = new Intent(getApplicationContext(),
					DetailsAuditorium.class);
			showContent.putExtra("NAME",auditorium.getName());
			showContent.putExtra("ADDRESS", auditorium.getAddress());
			double[] coord={ auditorium.getLatitude(), auditorium.getLongitude() };
			showContent.putExtra("COORD", coord);
			showContent.putExtra("ID", auditorium.getID());
			startActivity(showContent);
	    } else {
	        viewer.updateAuditorium(auditorium);
	        setListeners();
	    }
	}
	
	
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
							Uri.parse("http://maps.google.com/maps?daddr="+current_auditorium.getLatitude()+","+current_auditorium.getLongitude()+ "&dirflg=w"));
			    intent.setComponent(new ComponentName("com.google.android.apps.maps", 
					            "com.google.android.maps.MapsActivity"));          
				startActivity(intent);
				//ExternalAppUtility.openBrowser(DetailsAuditorium.this, "google.navigation:dirflg=w&q="+auditorium.getLatitude()+","+auditorium.getLongitude());
				break;
			case R.id.button_subauditorium:
				intent = new Intent(this, SubAuditoriumActivity.class);
				startActivity(intent);
				break;			
			}
		}
	 
}
