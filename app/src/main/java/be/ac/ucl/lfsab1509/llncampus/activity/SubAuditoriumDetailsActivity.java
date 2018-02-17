package be.ac.ucl.lfsab1509.llncampus.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.SubAuditorium;
import be.ac.ucl.lfsab1509.llncampus.fragment.SubAuditoriumDetailsFragment;
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
 * This class is intended to shows information about an ISubAuditorium.
 * Related with the XML file subauditorium_details_fragment.xml.
 * Caution: this class is called only if the device is NOT in landscape!
 * 			Check in the XML file (landscape and normal) and SubAuditoriumActivity for details.
 *
 */
public class SubAuditoriumDetailsActivity extends LLNCampusActivity {
	private ISubAuditorium subauditorium;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Finish the activity if the device orientation is landscape.
		int orientation = this.getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			finish();
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.subauditorium_details_fragment);

		// Fetch all given information useful for the layout.
		int parentId = getIntent().getIntExtra(EXTRA_PARENT_ID, 0);
		int id = getIntent().getIntExtra(EXTRA_ID, 0);
		String name = getIntent().getStringExtra(EXTRA_NAME);
		int places = getIntent().getIntExtra(EXTRA_PLACES, 0);
		String furniture = getIntent().getStringExtra(EXTRA_FURNITURE);
		boolean cabin = getIntent().getBooleanExtra(EXTRA_CABIN, false);
		boolean screen = getIntent().getBooleanExtra(EXTRA_SCREEN, false);
		boolean retro = getIntent().getBooleanExtra(EXTRA_RETRO, false);
		boolean sound = getIntent().getBooleanExtra(EXTRA_SOUND, false);
		boolean slide = getIntent().getBooleanExtra(EXTRA_SLIDE, false);
		String video = getIntent().getStringExtra(EXTRA_VIDEO);
		boolean network = getIntent().getBooleanExtra(EXTRA_NETWORK, false);
		boolean access = getIntent().getBooleanExtra(EXTRA_ACCESS, false);

		// Create a new instance of ISubAuditorium.
		subauditorium = new SubAuditorium(parentId, id, name, places, furniture, cabin, screen,
				sound, retro, slide, video, network, access);

		// Delegate the update operation to the fragment.
		SubAuditoriumDetailsFragment viewer = (SubAuditoriumDetailsFragment) getFragmentManager()
				.findFragmentById(R.id.subauditorium_details_fragment);
		viewer.updateSubAuditorium(subauditorium);
	}
}
