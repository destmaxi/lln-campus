package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.fragment.SubAuditoriumDetailsFragment;
import be.ac.ucl.lfsab1509.llncampus.fragment.SubAuditoriumListFragment;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ISubAuditorium;

/**
 * This class is intended to create a list of subauditoriums in order to make a
 * clickable list for the user.
 * Related with the xml file subauditorium_list_fragment.xml
 * (This file is different if we are in landscape or not)
 *
 */
public class SubAuditoriumActivity extends LLNCampusActivity implements SubAuditoriumListFragment.OnSubAuditoriumSelectedListener {
	ArrayList<String> values = null;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subauditorium_list_fragment);
				 
		//View vue = findViewById(R.id.subauditorium_list_fragment);
		//vue.setBackgroundColor(getResources().getColor(R.color.Blue)); 
	}
	

    /*
     * Adding an item click listener to the list
     */
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(this, DetailsSubAuditorium.class); //the intent is used to start a new activity
    	/*
		 * before starting the new activity, we serialize the selected Poi from the list and add it as a parameter to the intent with the method 'putExtra'
		 * Note that non-serializable fields of the Poi (marked as 'transient') will not be included in the serialized object.
		 * This is the case for the data source of the Poi. Then, when de-serialized, the Poi should get another data source
		 */    	
    	intent.putExtra("NAME", values.get(position));
		startActivity(intent); //starts the activity denoted by this intent. 
    }
	
	
	public void onSubAuditoriumSelected(ISubAuditorium subAuditorium){
		
		SubAuditoriumDetailsFragment viewer = (SubAuditoriumDetailsFragment) getFragmentManager()
	            .findFragmentById(R.id.subauditorium_details_fragment);
		// If we are not in landscape, then create an activitty and give it all useful information
	    if (viewer == null || !viewer.isInLayout()) {
	    	Intent showContent = new Intent(getApplicationContext(),
					DetailsSubAuditorium.class);
	    	showContent.putExtra("ID_PARENT", subAuditorium.getIDParent());
	    	showContent.putExtra("ID", subAuditorium.getID());
			showContent.putExtra("NAME",subAuditorium.getName());
			showContent.putExtra("NBPLACES",  subAuditorium.getNbPlaces());
			showContent.putExtra("MOBILIER", subAuditorium.getMobilier());
			showContent.putExtra("CABINE", subAuditorium.hasCabine());
			showContent.putExtra("ECRAN", subAuditorium.hasEcran());
			showContent.putExtra("RETRO", subAuditorium.hasRetro());
			showContent.putExtra("SONO", subAuditorium.hasSono());
			showContent.putExtra("DIA", subAuditorium.hasDia());
			showContent.putExtra("VIDEO", subAuditorium.getVideo());
			showContent.putExtra("NETWORK", subAuditorium.hasNetwork());
			showContent.putExtra("ACCESS", subAuditorium.hasAccess());
			
			startActivity(showContent);
	    } else { // Don't have to create an activity, query the fragment to update the layout
	        viewer.updateSubAuditorium(subAuditorium);
	       
	    }
	}

	
}
