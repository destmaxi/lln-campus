package be.ac.ucl.lfsab1509.llncampus.fragment;

import java.util.ArrayList;
import be.ac.ucl.lfsab1509.llncampus.SubAuditorium;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ISubAuditorium;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
 * This class is intended to manage information shown on the list of
 * subauditorium when the SubAuditoriumActivity is called.
 * Related with the xml file subauditorium_list_fragment.xml
 * Note: a fragment is called by the xml file!
 *
 */
public class SubAuditoriumListFragment extends LLNCampusListFragment {

	ArrayList<String> subAuditoriumsName = null;
	final String NAME = "NAME";
	private ArrayAdapter<String> adapter;
	private ISubAuditorium subauditorium;
	private OnSubAuditoriumSelectedListener subAudSelectedListener;
	private int idParent;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Get the id of the parent to know at which IAuditorium we are
		idParent = getActivity().getIntent().getIntExtra("IDPARENT", 0);

		// Query the DB about the ISubAudtorium in the IAuditorium with the id idParent
		String[] cols = {"AUDITORIUM_NAME"};
		Cursor c = db.select("Auditorium", cols,"BUILDING_ID = "+idParent,null, null, null, "AUDITORIUM_NAME ASC", null);

		// Create an ArrayList of the subauditorium's names to make the list 
		this.subAuditoriumsName = new ArrayList<String>();
		while(c.moveToNext()){
			subAuditoriumsName.add(c.getString(0));
		}
		// Don't forget to close the Cursor!
		c.close();


		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data

		adapter=new ArrayAdapter<String>(
				this.getActivity(),android.R.layout.simple_list_item_1, subAuditoriumsName){

			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {
				View view =super.getView(position, convertView, parent);

				TextView textView=(TextView) view.findViewById(android.R.id.text1);

				//YOUR CHOICE OF COLOR
				textView.setTextColor(Color.BLACK);

				return view;
			}
		};
		//SET THE ADAPTER TO LISTVIEW

		setListAdapter(adapter);
		setHasOptionsMenu(true);
	}

	// When the junction between the activity and the fragment is done, then define the listener
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			subAudSelectedListener = (OnSubAuditoriumSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnSubAuditoriumSelectedListener");
		}
	}

	// When an item is clicked, then delegate the action to the listener attached
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		String content = subAuditoriumsName.get(position);

		if(content == null)
		{
			// There is an error; return
			return;
		}
		
		String nameSubAuditorium = content;
		// Query the DB about the subauditorium selected
		String[] cols = {"AUDITORIUM_ID","AUDITORIUM_NAME","BUILDING_ID", "PLACE", "MOBILIER", "CABINE", "ECRAN", "SONO", "RETRO", "DIA", "VIDEO", "RESEAU", "ACCESS"};
		Cursor c = super.db.select("Auditorium", cols, "AUDITORIUM_NAME = "+ "'"+nameSubAuditorium+"'", null, null, null, null, null);
		c.moveToFirst();
		subauditorium = new SubAuditorium(idParent , c.getInt(0), c.getString(1), c.getInt(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7), c.getString(8), c.getString(9), c.getString(10), c.getString(11), c.getString(12));

		c.close();
		
		subAudSelectedListener.onSubAuditoriumSelected(subauditorium);
	}

	// Interface for the listener of ISubAuditorium defined here
	public interface OnSubAuditoriumSelectedListener {
		/**
		 * Shows information about the sudAuditorium selected on the layout
		 * The behavior can be different if the device is in landscape or not
		 * @pre subAuditorium != null
		 * @post Shows information about the sudAud selected on the layout, subAuditorium not changed
		 * @param subAuditorium
		 */
		public void onSubAuditoriumSelected(ISubAuditorium subAuditorium);
	}

}
