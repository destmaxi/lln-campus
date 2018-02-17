package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.Database;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
 * Abstract class for Activities in order to share common menu and useful variables and 
 * methods.
 */
public abstract class LLNCampusActivity extends Activity {

	/** Name of the access item stored in extras. */
	public static final String EXTRA_ACCESS = "ACCESS";
	/** Name of the activity name item stored in extras. */
	public static final String EXTRA_ACTIVITY_NAME = "ACTIVITY_NAME";
	/** Name of the activity address item stored in extras. */
	public static final String EXTRA_ADDRESS = "ADDRESS";
	/** Name of the begin time item stored in extras. */
	public static final String EXTRA_BEGIN_TIME = "BEGIN_TIME";
	/** Name of the cabin item stored in extras. */
	public static final String EXTRA_CABIN = "CABIN";
	/** Name of the coordinates item stored in extras. */
	public static final String EXTRA_COORDINATES = "COORDINATES";
	/** Name of the coordinates array item stored in extras. */
	public static final String EXTRA_COORDINATES_ARRAY = "COORDINATES_ARRAY";
	/** Name of the css item stored in extras, for the web view activity. */
	public static final String EXTRA_CSS = "CSS";
	/** Name of the details item stored in extras. */
	public static final String EXTRA_DETAILS = "DETAILS";
	/** Name of the encoding item stored in extras. */
	public static final String EXTRA_ENCODING = "ENCODING";
	/** Name of the end time item stored in extras. */
	public static final String EXTRA_END_TIME = "END_TIME";
	/** Name of the furniture item stored in extras. */
	public static final String EXTRA_FURNITURE = "FURNITURE";
	/** Name of the id item stored in extras. */
	public static final String EXTRA_ID = "ID";
	/** Name of the latitude item stored in extras. */
	public static final String EXTRA_LATITUDE = "LATITUDE";
	/** Name of the longitude item stored in extras. */
	public static final String EXTRA_LONGITUDE = "LONGITUDE";
	/** Name of the name item stored in extras. */
	public static final String EXTRA_NAME = "NAME";
	/** Name of the network item stored in extras. */
	public static final String EXTRA_NETWORK = "NETWORK";
	/** Name of the parent id item stored in extras. */
	public static final String EXTRA_PARENT_ID = "PARENT_ID";
	/** Name of the places item stored in extras. */
	public static final String EXTRA_PLACES = "PLACES";
	/** Name of the retro item stored in extras. */
	public static final String EXTRA_RETRO = "RETRO";
	/** Name of the screen item stored in extras. */
	public static final String EXTRA_SCREEN = "SCREEN";
	/** Name of the slide item stored in extras. */
	public static final String EXTRA_SLIDE = "SLIDE";
	/** Name of the sound item stored in extras. */
	public static final String EXTRA_SOUND = "SOUND";
	/** Name of the start UCLouvain item stored in extras, used to automate the fetching of
	 * courses from UCLouvain when updating the ADE schedule. */
	public static final String EXTRA_START_UCLOUVAIN = "START_UCLOUVAIN";
	/** Name of the title item stored in extras, for the web view activity. */
	public static final String EXTRA_TITLE = "TITLE";
	/** Name of the url item stored in extras, for the web view activity. */
	public static final String EXTRA_URL = "URL";
	/** Name of the video item stored in extras. */
	public static final String EXTRA_VIDEO = "VIDEO";

	/** References the application database. */
	protected Database db;
	/** An Handler. */
	protected Handler mHandler = new Handler();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LLNCampus.setContext(this);
		LLNCampus.startAlarmService();
		this.db = LLNCampus.getDatabase();
		super.onCreate(savedInstanceState);

		// If small device, prevent rotation.
		LLNCampus.blockRotationOnSmallDevices(this);
		enableActionBar();
	}

	/** 
	 * Enable the action bar to return on the parent screen.
	 */
	protected void enableActionBar() {
		if(getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public void onResume() {
		if (!db.isOpen())
		{
			db.open();
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();	
	}

	/**
	 * Show an alert to the user.
	 * 
	 * @param title
	 *            Alert title.
	 * @param msg
	 *            Alert message.
	 */
	public final void showAlert(String title, String msg) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(title);
		adb.setMessage(msg);
		adb.setPositiveButton(android.R.string.ok, null);
		adb.show();
	}

	/**
	 * Show an alert to the user.
	 * 
	 * @param titleRes
	 *            Resource integer to the alert title.
	 * @param msgRes
	 *            Resource integer to the alert message.
	 */
	public void showAlert(int titleRes, int msgRes) {
		showAlert(getString(titleRes), getString(msgRes));
	}

	/**
	 * Notify to the user a message.
	 * 
	 * @param text
	 *            Notification text.
	 */
	public final void notify(String text) {
		Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}

	/**
	 * Affiche une notification a l'utilisateur
	 * 
	 * @param text
	 *            Integer referencant le texte de la notification.
	 */
	public final void notify(int text) {
		Toast t = Toast.makeText(getApplicationContext(), text,
				Toast.LENGTH_LONG);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		super.onOptionsItemSelected(menuItem);
		Intent intent;
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				// Application icon in action bar clicked; go home.
				intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			case R.id.menu_settings:
				intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				break;
			case R.id.menu_reset_database: 
				final LLNCampusActivity context = this;
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getString(R.string.reset_DB)).setMessage(
						getString(R.string.confirm_reset_DB));
				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						db.reset();
						db.open();
						context.notify(getString(R.string.reloaded_db));
					}
				});
				builder.setNegativeButton(android.R.string.cancel, null);
				AlertDialog dialog = builder.create();
				dialog.show();
				break;
			case R.id.menu_about:
				intent = new Intent(this, AboutActivity.class);
				startActivity(intent);
				break;
			default:
				return super.onOptionsItemSelected(menuItem);
		}
		return true;
	}

}
