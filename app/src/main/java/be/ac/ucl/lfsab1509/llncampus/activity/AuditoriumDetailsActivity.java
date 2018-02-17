package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.Auditorium;
import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.fragment.AuditoriumDetailsFragment;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

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
 * This class is intended to shows information about an IAuditorium.
 * Related with the XML file auditorium_details.xml.
 * Caution: this class is called only if the device is NOT in landscape!
 * Check in the XML file (landscape and normal) and in AuditoriumActivity for details.
 *
 */
public class AuditoriumDetailsActivity extends LLNCampusActivity implements OnClickListener {
	private IAuditorium auditorium;

	/*
	 * Create an local IAuditorium useful for the buttons 
	 * (fetch information given by AuditoriumListActivity).
	 * Also manage the update query to the fragment.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Finish the activity if the device orientation is landscape.
		int orientation = this.getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			finish();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auditorium_details_fragment);

		String name = getIntent().getStringExtra(EXTRA_NAME);
		String address = getIntent().getStringExtra(EXTRA_ADDRESS);
		double[] coord = getIntent().getDoubleArrayExtra(EXTRA_COORDINATES_ARRAY);
		int id = getIntent().getIntExtra(EXTRA_ID, 0);
		auditorium = new Auditorium (id, name, coord[0], coord[1], address);

		AuditoriumDetailsFragment viewer = (AuditoriumDetailsFragment) getFragmentManager()
				.findFragmentById(R.id.auditorium_details_fragment);
		viewer.updateAuditorium(auditorium);

		setListeners();
	}

	/** 
	 * Set the listeners on buttons that are shown by the Activity.
	 */
	private void setListeners() {
		View GPSButton = findViewById(R.id.button_auditorium_gps);
		GPSButton.setOnClickListener(this);
		View subButton = findViewById(R.id.button_subauditorium);
		subButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button_auditorium_gps:
			ExternalAppUtility.startNavigation(auditorium.getLatitude(), 
					auditorium.getLongitude(), this);
			break;
		case R.id.button_subauditorium:
			intent = new Intent(this, SubAuditoriumActivity.class);
			intent.putExtra(EXTRA_PARENT_ID, auditorium.getID());
			startActivity(intent);
			break;			
		}
	}


}
