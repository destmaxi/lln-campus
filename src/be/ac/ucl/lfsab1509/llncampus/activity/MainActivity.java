package be.ac.ucl.lfsab1509.llncampus.activity;

import java.io.File;

import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;

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
 * The first activity class that interacts with the user. 
 * Related with the XML main.xml.
 * 
 */
public class MainActivity extends LLNCampusActivity implements OnClickListener {

	/** URL fot the iCampus web site. */
	private static final String ICAMPUS_URL = "https://www.uclouvain.be/cnx_icampus.html";
	/** URL fot the Moodle web site. */
	private static final String MOODLE_URL = "https://www.uclouvain.be/cnx_moodle.html";
	/** URL fot the UCL virtual office. */
	private static final String UCL_OFFICE_URL = "http://www.uclouvain.be/onglet_bureau.html?";
	/** Name of the Louvain-la-Neuve map file. */
	private static final String MAP_NAME = "plan_lln.pdf";

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		setListeners();
		getActionBar().setDisplayHomeAsUpEnabled(false);

		new Thread(new Runnable() {
			public void run() {
				LLNCampus.copyAssets();
			}
		}).start();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				// Don't return to parent Activity because it is the root Activity!
				return true;
			default:
				return super.onOptionsItemSelected(menuItem);
		}
	}
		
	// FIXME Little fix for display bug, but should be improved.
	@Override
	public void onUserInteraction()
	{
		super.onUserInteraction();
		onWindowFocusChanged(true);
	}

	/**
	 * When the layout is loading or when the orientation changes, redefine the dimensions of
	 * buttons and pictures to take the whole available space in a constant way.
	 * 
	 * @param hasFocus
	 * 			Whether the window of this activity has focus.
	 */
	@Override
	public final void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// In order to get an optimal height and width.
		GridLayout gl = (GridLayout) findViewById(R.id.main_grid_layout);
		Button resizedButton;

		// Stretch buttons.
		int idealChildWidth = (int) (gl.getWidth() / gl.getColumnCount());
		int idealChildHeight = (int) ((gl.getHeight()) / gl.getRowCount());
		for (int i = 0; i < gl.getChildCount(); i++) {
			resizedButton = (Button) gl.getChildAt(i);
			resizedButton.setWidth(idealChildWidth);
			resizedButton.setHeight(idealChildHeight);
			Drawable[] drawables = resizedButton.getCompoundDrawables();
			Drawable d;
			for (int j = 0; j < drawables.length; j++) {
				if ((d = drawables[j]) != null) {
					d.setBounds(0, 0, (int) (d.getIntrinsicWidth() * 0.9),
							(int) (d.getIntrinsicHeight() * 0.9));
					ScaleDrawable sd = new ScaleDrawable(d, 0, (float) 1, (float) 1);
					switch (j) {
					case 0:
						resizedButton.setCompoundDrawables(sd.getDrawable(), null, null, null);
						break;
					case 1:
						resizedButton.setCompoundDrawables(null, sd.getDrawable(), null, null);
						break;
					case 2:
						resizedButton.setCompoundDrawables(null, null, sd.getDrawable(), null);
						break;
					case 3:
						resizedButton.setCompoundDrawables(null, null, null, sd.getDrawable());
						break;
					}
				}
			}
		}
	}

	/**
	 * Define this Activity as the listener of the buttons it shows.
	 */
	private void setListeners() {
		findViewById(R.id.button_leisure).setOnClickListener(this);
		findViewById(R.id.button_schedule).setOnClickListener(this);
		findViewById(R.id.button_auditorium).setOnClickListener(this);
		findViewById(R.id.button_library).setOnClickListener(this);
		findViewById(R.id.button_map).setOnClickListener(this);
		findViewById(R.id.button_directory).setOnClickListener(this);
		findViewById(R.id.button_icampus).setOnClickListener(this);
		findViewById(R.id.button_moodle).setOnClickListener(this);
		findViewById(R.id.button_office).setOnClickListener(this);
	}
	
	@Override
	public void onStop()
	{
		db.close();
		super.onStop();
	}
	
	@Override
	public final void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button_leisure:
			intent = new Intent(this, LeisureActivity.class);
			startActivity(intent);
			break;
		case R.id.button_schedule:
			intent = new Intent(this, ScheduleActivity.class);
			startActivity(intent);
			break;
		case R.id.button_auditorium:
			intent = new Intent(this, AuditoriumActivity.class);
			startActivity(intent);
			break;
		case R.id.button_library:
			intent = new Intent(this, LibraryActivity.class);
			startActivity(intent);
			break;
		case R.id.button_map:
			// Uses the tablet's default PDF reader. Starts MapActivity if none is found.
			try {
				Intent intentUrl = new Intent(Intent.ACTION_VIEW);

				Uri url = Uri.fromFile(new File("/"
						+ Environment.getExternalStorageDirectory().getPath()
						+ "/" + LLNCampus.LLNREPOSITORY + "/" + MAP_NAME));
				intentUrl.setDataAndType(url, "application/pdf");
				intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentUrl);
			} catch (ActivityNotFoundException e) {
				notify(this.getString(R.string.no_pdf_reader));
				intent = new Intent(this, MapActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.button_directory:
			intent = new Intent(this, DirectoryActivity.class);
			startActivity(intent);
			break;
		case R.id.button_icampus:
			ExternalAppUtility.openBrowser(this, ICAMPUS_URL);
			break;
		case R.id.button_moodle:
			ExternalAppUtility.openBrowser(this, MOODLE_URL);
			break;
		case R.id.button_office:
			ExternalAppUtility.openBrowser(this, UCL_OFFICE_URL);
			break;
		}
	}
}
