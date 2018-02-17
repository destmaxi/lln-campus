package be.ac.ucl.lfsab1509.llncampus.fragment;

import java.util.ArrayList;
import be.ac.ucl.lfsab1509.llncampus.Auditorium;
import be.ac.ucl.lfsab1509.llncampus.activity.adapter.AuditoriumListAdapter;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
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
 * This class is intended to manage information shown on the list of auditorium when the 
 * AuditoriumActivity is called.
 * Related with the XML file auditorium_list_fragment.xml.
 * Note: a fragment is called by the XML file!
 *
 */
public class AuditoriumListFragment extends LLNCampusListFragment {
	
	private ArrayList<String> auditoriumsName = null;
	private IAuditorium auditorium;
	private OnIAuditoriumSelectedListener audSelectedListener;
	
	private AuditoriumListAdapter auditoriumListAdapter;
	private ArrayList<IAuditorium> auditoriumList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// If we don't have the auditorium's names yet, then go to the database and fetch them.
		if (auditoriumsName == null) {
			// Prepare the query to fetch the auditoriums.
			String[] cols = {"NAME"};
			// Result of the query.
			Cursor c = db.select("Poi", cols,"TYPE = 'auditoire'",null, null, null, 
					"NAME ASC", null);
			// Set the result in an ArrayList.
			this.auditoriumsName = new ArrayList<String>();
			while(c.moveToNext()){
				auditoriumsName.add(c.getString(0));
			}
			// Then create an ArrayList of IAuditorium with the ArrayList of auditorium's names.
			this.auditoriumList = new ArrayList<IAuditorium>();
			for (int i=0; i < auditoriumsName.size(); i++)
			{
				String[] cols1 = {"ID", "NAME", "LATITUDE", "LONGITUDE", "ADDRESS"};
				Cursor c1 = super.db.select("Poi", cols1, "NAME = " + "'" + auditoriumsName.get(i)
						+ "'", null, null, null, null, null);
				c1.moveToFirst();
				auditoriumList.add(new Auditorium(c1.getInt(0), c1.getString(1), c1.getDouble(2),
						c1.getDouble(3), c1.getString(4)));
			}
			// Don't forget to close the Cursor!
			c.close();
		}
		// Create an instance of AuditoriumListAdapter to create the list with pictures.
		auditoriumListAdapter = new AuditoriumListAdapter(getActivity(), auditoriumList);
		// Set the list.
		setListAdapter(auditoriumListAdapter);
		// Set the options' menu.
        setHasOptionsMenu(true);
	}
	
	// When the junction between activity and fragment is done, set the listener for.
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            audSelectedListener = (OnIAuditoriumSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() 
            		+ " must implement OnAuditoriumSelectedListener");
        }
    }

	// When an item is clicked, then delegate the action to the listener attached.
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	    auditorium = auditoriumList.get(position);
	    audSelectedListener.onAuditoriumSelected(auditorium);
	}
	
	/** Interface defined for the list of IAuditoriums. */
	public interface OnIAuditoriumSelectedListener {
		/**
		 * Shows information about auditorium on the layout.
		 * The behavior can differ if it's in landscape or not.
		 * 
		 * @param auditorium
		 * 			The auditorium to show information about.
		 */
	    public void onAuditoriumSelected(IAuditorium auditorium);
	}
	
}
