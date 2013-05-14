package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.Database;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
 * Classe avec le meme objectif que LLNCampusActivity, mais pour les ListActivity
 */
public abstract class LLNCampusListActivity extends ListActivity {
	protected Database db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        LLNCampus.setContext(this);
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
		db.open();
		
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
    	//db.close(); 
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
		switch(menuItem.getItemId()) {
			case R.id.menu_settings : 
				intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				break;
			default:
				return super.onOptionsItemSelected(menuItem);
		}
		return true;
	}
}
