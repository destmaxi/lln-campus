package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import android.content.ActivityNotFoundException;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import be.ac.ucl.lfsab1509.llncampus.Library;
import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.activity.adapter.LibraryListAdapter;

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
 * Activity to show libraries list and their details.
 */
public class LibraryActivity extends LLNCampusActivity implements
		OnItemClickListener, OnClickListener {
	private GridView libraryListView;
	private LibraryListAdapter libraryListAdapter;
	private ArrayList<Library> libraryList;

	private int currentLibraryPosition = -1;

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library);
		if (libraryList == null) {
			libraryList = Library.getLibraryList();
		}
		libraryListView = (GridView) findViewById(R.id.library_list_view);
		libraryListAdapter = new LibraryListAdapter(this, libraryList);
		libraryListView.setAdapter(libraryListAdapter);
		libraryListView.setOnItemClickListener(this);

		Button gpsButton = (Button) findViewById(R.id.library_details_button_gps);
		gpsButton.setOnClickListener(this);
		Button detailsButton = (Button) findViewById(R.id.library_details_button_details);
		detailsButton.setOnClickListener(this);
	}

	@Override
	public final void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("currentLibraryPosition", currentLibraryPosition);
	}

	@Override
	public final void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (libraryList == null) {
			libraryList = Library.getLibraryList();
		}
		currentLibraryPosition = savedInstanceState.getInt("currentLibraryPosition");
	}

	@Override
	public final void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		currentLibraryPosition = position;
		updateDisplay();
	}

	/**
	 * Update the display of the Activity.
	 */
	private void updateDisplay() {
		// Details view.
		View detailsView = (View) findViewById(R.id.library_details);

		// By default, set the libraries list visible.
		libraryListView.setVisibility(View.VISIBLE);

		if (getCurrentLibrary() != null) { // User chose a library.
			// Set the details view visible.
			detailsView.setVisibility(View.VISIBLE);
			// Set information into details view.
			TextView libraryName = (TextView) findViewById(R.id.library_details_name);
			libraryName.setText(getCurrentLibrary().getName() + " ("
					+ getCurrentLibrary().getAcronym() + ")");
			ImageView libraryPicture = (ImageView) findViewById(R.id.library_details_picture);
			libraryPicture.setImageResource(getCurrentLibrary().getPicture());
			TextView libraryAddress = (TextView) findViewById(R.id.library_details_address);
			libraryAddress.setText(getCurrentLibrary().getAddress());
			TextView librarySchedule = (TextView) findViewById(R.id.library_details_schedule);
			librarySchedule
					.setText(getCurrentLibrary().getSchedule().toString());

			// Screen orientation.
			int orientation = this.getResources().getConfiguration().orientation;

			// Get the width of the screen.
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;

			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				// If landscape orientation, show the list on 35% of the width of the screen.
				libraryListView.getLayoutParams().width = (int) (0.35 * width);
				detailsView.getLayoutParams().width = (int) (0.65 * width);
			} else {
				// If portrait orientation, don't show the list (only details on the whole width).
				libraryListView.setVisibility(View.GONE);
				detailsView.getLayoutParams().width = width;
			}
		} else {
			// Else hide the details view.
			detailsView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// Upon a display change, update it.
		updateDisplay();
	}

	/**
	 * Get the current library.
	 * 
	 * @return Current library or null if no one.
	 */
	private Library getCurrentLibrary() {
		if (currentLibraryPosition < 0 || currentLibraryPosition >= libraryList.size()) {
			return null;
		}
		return libraryList.get(currentLibraryPosition);
	}

	@Override
	public final void onClick(View v) {
		switch (v.getId()) {
		case R.id.library_details_button_gps:
			try{
				ExternalAppUtility.startNavigation(getCurrentLibrary().getLatitude(),
						getCurrentLibrary().getLongitude(), this);

			} catch (ActivityNotFoundException e) { // If we don't have Google Maps
				notify(R.string.no_google_maps);
			}
			break;
		case R.id.library_details_button_details:
			ExternalAppUtility.openBrowser(this, getCurrentLibrary().getScheduleUrl());
			break;
		}
	}

	@Override
	public final void onBackPressed() {
		if (currentLibraryPosition != -1 && this.getResources().getConfiguration()
				.orientation != Configuration.ORIENTATION_LANDSCAPE) {
			currentLibraryPosition = -1;
			updateDisplay();
		} else {
			super.onBackPressed();
		}
	}
}