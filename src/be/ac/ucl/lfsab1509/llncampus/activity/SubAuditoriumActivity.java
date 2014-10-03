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
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier

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
 * 
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
	    	showContent.putExtra("ID_PARENT", subAuditorium.getParentId());
	    	showContent.putExtra("ID", subAuditorium.getId());
			showContent.putExtra("NAME",subAuditorium.getName());
			showContent.putExtra("NBPLACES",  subAuditorium.getPlaces());
			showContent.putExtra("MOBILIER", subAuditorium.getFurniture());
			showContent.putExtra("CABINE", subAuditorium.hasCabin());
			showContent.putExtra("ECRAN", subAuditorium.hasScreen());
			showContent.putExtra("RETRO", subAuditorium.hasRetro());
			showContent.putExtra("SONO", subAuditorium.hasSound());
			showContent.putExtra("DIA", subAuditorium.hasSlide());
			showContent.putExtra("VIDEO", subAuditorium.getVideo());
			showContent.putExtra("NETWORK", subAuditorium.hasNetwork());
			showContent.putExtra("ACCESS", subAuditorium.hasAccess());
			
			startActivity(showContent);
	    } else { // Don't have to create an activity, query the fragment to update the layout
	        viewer.updateSubAuditorium(subAuditorium);
	       
	    }
	}

	
}
