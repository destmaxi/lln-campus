package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
import be.ac.ucl.lfsab1509.llncampus.fragment.AuditoriumDetailsFragment;
import be.ac.ucl.lfsab1509.llncampus.fragment.AuditoriumListFragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier
	Copyright (C) 2014 Quentin De Coninck

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * This Activity class is intended to create a list of auditoriums in order to make a
 * click-able list for the user.
 * Related with the xml file auditorium_list_fragment.xml
 * (this file is different if we are in landscape or not).
 *
 */
public class AuditoriumActivity extends LLNCampusActivity implements 
	AuditoriumListFragment.OnIAuditoriumSelectedListener, OnClickListener {
	ArrayList<String> values = null;
	private IAuditorium selectedAuditorium;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auditorium_list_fragment);
	}

	// For small devices, set the screen orientation to portrait.
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

    /**
     * This method will be called when an item in the list is selected.
     * 
     * @param l
     * 			The ListView where the click happened.
     * @param v
     * 			The view that was clicked within the ListView.
     * @param position
     * 			The position of the view in the list.
     * @param id
     * 			The row id of the item that was clicked.
     */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// The intent is used to start a new activity.
    	Intent intent = new Intent(this, AuditoriumDetailsActivity.class);
    	/*
		 * Before starting the new activity, we serialize the selected Poi from the list and add it
		 * as a parameter to the intent with the method 'putExtra'.
		 * Note that non-serializable fields of the Poi (marked as 'transient') will not be
		 * included in the serialized object. This is the case for the data source of the Poi.
		 * Then, when de-serialized, the Poi should get another data source.
		 */

    	intent.putExtra("NAME", values.get(position));
		startActivity(intent); // Starts the activity denoted by this intent.
    }

	// Specified in the OnAuditoriumSelectedListener interface.
	public void onAuditoriumSelected(IAuditorium auditorium) {
		selectedAuditorium = auditorium;
		// Find the fragment related to the "subfragment" in the XML file.
		AuditoriumDetailsFragment viewer = (AuditoriumDetailsFragment) getFragmentManager()
	            .findFragmentById(R.id.auditorium_details_fragment);
		// Check if the viewer is in the layout (i.e., if we are in landscape)
	    if (viewer == null || !viewer.isInLayout()) {
	    	// Create a new intent and give all the useful data for it.
	    	Intent showContent = new Intent(getApplicationContext(),
					AuditoriumDetailsActivity.class);
			showContent.putExtra(EXTRA_NAME, auditorium.getName());
			showContent.putExtra(EXTRA_ADDRESS, auditorium.getAddress());
			double[] coord = {auditorium.getLatitude(), auditorium.getLongitude() };
			showContent.putExtra(EXTRA_COORDINATES_ARRAY, coord);
			showContent.putExtra(EXTRA_ID, auditorium.getID());
			startActivity(showContent);
	    } else { // We are in landscape, so the activity manage directly information.
	        viewer.updateAuditorium(auditorium);
	        setListeners();
	    }
	}

	/*
	 * The two following methods will be used only when the device is in landscape orientation;
	 * manage then what AuditoriumDetails should have done.
	 */

	/**
	 * Set the listeners (GPS and SubAuditorium buttons).
	 */
	private void setListeners() {
		View GPSButton = findViewById(R.id.button_auditorium_gps);
		GPSButton.setOnClickListener(this);
		View subButton = findViewById(R.id.button_subauditorium);
		subButton.setOnClickListener(this);
	}

	// Specified by the OnClickListener interface.
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button_auditorium_gps:
			ExternalAppUtility.startNavigation(selectedAuditorium.getLatitude(), 
					selectedAuditorium.getLongitude(), this);
			break;
		case R.id.button_subauditorium:
			intent = new Intent(this, SubAuditoriumActivity.class);
			intent.putExtra(EXTRA_PARENT_ID, selectedAuditorium.getID());
			startActivity(intent);
			break;
		}
	}
}