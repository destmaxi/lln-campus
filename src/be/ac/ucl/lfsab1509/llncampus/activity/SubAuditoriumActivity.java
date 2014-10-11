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
 * This class is intended to create a list of subauditoriums in order to make a
 * clickable list for the user.
 * Related with the xml file subauditorium_list_fragment.xml
 * (This file is different if we are in landscape or not).
 *
 */
public class SubAuditoriumActivity extends LLNCampusActivity implements 
	SubAuditoriumListFragment.OnSubAuditoriumSelectedListener {
	ArrayList<String> values = null;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subauditorium_list_fragment);
	}

	/**
     * This method will be called when an item in the list is selected.
     * 
     * @param l
     * 		The ListView where the click happened.
     * @param v
     * 		The view that was clicked within the ListView.
     * @param position
     * 		The position of the view in the list.
     * @param id
     * 		The row id of the item that was clicked. 
     */
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(this, SubAuditoriumDetailsActivity.class);
    	/*
		 * Before starting the new activity, we serialize the selected Poi from the list and add
		 * it as a parameter to the intent with the method 'putExtra'.
		 * Note that non-serializable fields of the Poi (marked as 'transient') will not be 
		 * included in the serialized object.
		 * This is the case for the data source of the Poi. 
		 * Then, when de-serialized, the Poi should get another data source.
		 */    	
    	intent.putExtra(EXTRA_NAME, values.get(position));
		startActivity(intent); 
    }
	
	
	public void onSubAuditoriumSelected(ISubAuditorium subAuditorium){
		
		SubAuditoriumDetailsFragment viewer = (SubAuditoriumDetailsFragment) getFragmentManager()
	            .findFragmentById(R.id.subauditorium_details_fragment);
		// If we are not in landscape, then create an activity and give it all useful information.
	    if (viewer == null || !viewer.isInLayout()) {
	    	Intent showContent = new Intent(getApplicationContext(),
					SubAuditoriumDetailsActivity.class);
	    	showContent.putExtra(EXTRA_PARENT_ID, subAuditorium.getParentId());
	    	showContent.putExtra(EXTRA_ID, subAuditorium.getId());
			showContent.putExtra(EXTRA_NAME,subAuditorium.getName());
			showContent.putExtra(EXTRA_PLACES,  subAuditorium.getPlaces());
			showContent.putExtra(EXTRA_FURNITURE, subAuditorium.getFurniture());
			showContent.putExtra(EXTRA_CABIN, subAuditorium.hasCabin());
			showContent.putExtra(EXTRA_SCREEN, subAuditorium.hasScreen());
			showContent.putExtra(EXTRA_RETRO, subAuditorium.hasRetro());
			showContent.putExtra(EXTRA_SOUND, subAuditorium.hasSound());
			showContent.putExtra(EXTRA_SLIDE, subAuditorium.hasSlide());
			showContent.putExtra(EXTRA_VIDEO, subAuditorium.getVideo());
			showContent.putExtra(EXTRA_NETWORK, subAuditorium.hasNetwork());
			showContent.putExtra(EXTRA_ACCESS, subAuditorium.hasAccess());
			
			startActivity(showContent);
	    } else { // Don't have to create an activity, query the fragment to update the layout.
	        viewer.updateSubAuditorium(subAuditorium);
	    }
	}
}
