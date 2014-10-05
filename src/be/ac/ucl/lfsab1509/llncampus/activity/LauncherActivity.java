package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.external.SecurePreferences;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

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
 * First activity class that will be executed when starting the application.
 * It will manage starting operations like application updates.
 * Related with launcher.xml.
 */

public class LauncherActivity extends LLNCampusActivity {

	protected boolean update = false;
	protected boolean firstUse = false;
	private ProgressDialog progressDialog;

	/** Static variable for the static Handler. */
	private static Intent intent;
	/** Static variable for the static Handler. */
	private static LLNCampusActivity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		
		setContentView(R.layout.launcher);
		update = false;
		firstUse = false;
		progressDialog = new ProgressDialog(this);

		/* Compare the version number of the new version with this one; 
		 * if different, then reload the database. 
		 */
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			int newCode = info.versionCode;
			String newName = info.versionName;

			// Compare with stored values on the device to see if there is a new version.
			SharedPreferences prefs = this.getSharedPreferences(
					this.getPackageName(), Context.MODE_PRIVATE);

			String codeKey = this.getPackageName() + ".code";
			String nameKey = this.getPackageName() + ".name";

			// Get current code and name.
			int oldCode = prefs.getInt(codeKey, -1);
			String oldName = prefs.getString(nameKey, null);

			// First use, no update.
			if (oldCode == -1 && oldName == null) {
				firstUse = true;
				// No need to reload the database, just store the values.
				prefs.edit().putInt(codeKey, newCode).commit();
				prefs.edit().putString(nameKey, newName).commit();
			}
			else {
				// If there is update, then reload the database and store the values.
				if (oldCode != newCode || oldName.compareTo(newName) != 0) {
					update = true;
					prefs.edit().putInt(codeKey, newCode).commit();
					prefs.edit().putString(nameKey, newName).commit();
				}
			}
			// If this version is equal to the other, then no need to reload the database.
		} catch (NameNotFoundException nnf) {
			nnf.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		lauchApp();
	}
	
	public void onStop() {
		if (progressDialog != null)
			progressDialog.dismiss();
		super.onStop();
		
	}

	/** This handler actually starts the main activity */
	private static Handler handler = new Handler() {
		/** 
		 * Catch messages sent to the handler.
		 * @param msg
		 * 			The message sent to the Handler.
		 */
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				// Start the main activity.
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(intent);
				activity.finish();
			}
		};
	};

	/**
	 * Launch the application. More specifically, it first process the application update
	 * (if there is) and prepare the intent to the main activity.
	 */
	private void lauchApp() {
		// Add a message to the process dialog.
		if (update) {
			progressDialog.setMessage(getString(R.string.update_app));
		}
		else if (firstUse) {
			progressDialog.setMessage(getString(R.string.first_use));
		}
		else {
			progressDialog.setMessage(getString(R.string.loading_app));
		}

		progressDialog.show();
		final Context thisContext = this;
		new Thread(new Runnable() {
			@Override
			public void run() {
				db.open();
				if (update) {
					// Encrypt unencrypted preferences.
					SharedPreferences mInsecurePrefs = PreferenceManager
							.getDefaultSharedPreferences(thisContext);
			        SharedPreferences mSecurePrefs = new SecurePreferences(thisContext);
			    	LLNCampus.convertInsecureToSecurePreferences(mInsecurePrefs, mSecurePrefs);
					// Reset the database.
					db.reset();
					db.open();
				}
				// Now start the application!
				handler.sendEmptyMessage(0);
				// When the process ends, the message is removed by the onStop() method.
			}
		}).start();
		intent = new Intent(this, MainActivity.class);
	}
}
