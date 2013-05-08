package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.Database;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
 * Classe abstraite d'activite permettant d'avoir un menu commun et des methodes
 * utiles pour les differentes activites
 * 
 */
public abstract class LLNCampusActivity extends Activity {
	/**
	 * Variable referencant la base de donnee de l'application
	 */
	protected Database db;
	protected Handler mHandler = new Handler();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LLNCampus.setContext(this);
		LLNCampus.startAlarmService();
		this.db = LLNCampus.getDatabase();
		super.onCreate(savedInstanceState);
		
		// Si on a un device petit (ou normal) (ecran < 4.5 pouces)
		// On bloque la rotation 
		if ((getResources().getConfiguration().screenLayout & 
			    Configuration.SCREENLAYOUT_SIZE_MASK) == 
			        Configuration.SCREENLAYOUT_SIZE_NORMAL ||
			(getResources().getConfiguration().screenLayout & 
				Configuration.SCREENLAYOUT_SIZE_MASK) == 
					Configuration.SCREENLAYOUT_SIZE_SMALL)
		{
			this.setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		//db.open();
		editActionBar();
	}
	protected void editActionBar() {
		if( getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public void onResume() {
		/*if (!db.isOpen()) {
			db.open();
		}*/
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		db.close();
	}

	/**
	 * Affiche une alerte à l'utilisateur.
	 * 
	 * @param title
	 *            Titre de l'alerte
	 * @param msg
	 *            Message de l'alerte
	 */
	public final void alert(String title, String msg) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(title);
		adb.setMessage(msg);
		adb.setPositiveButton(android.R.string.ok, null);
		adb.show();
	}

	/**
	 * Affiche une alerte à l'utilisateur.
	 * 
	 * @param titleRes
	 *            Integer referencant le titre de l'alerte
	 * @param msgRes
	 *            Integer referencant le message de l'alerte
	 */
	public void alert(int titleRes, int msgRes) {
		alert(getString(titleRes), getString(msgRes));
	}

	/**
	 * Affiche une notification a l'utilisateur.
	 * 
	 * @param text
	 *            Texte de la notification
	 */
	public final void notify(String text) {
		Toast t = Toast.makeText(getApplicationContext(), text,
				Toast.LENGTH_LONG);
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
			// app icon in action bar clicked; go home
			intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_settings:
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
			
		case R.id.menu_resetDB: 
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
