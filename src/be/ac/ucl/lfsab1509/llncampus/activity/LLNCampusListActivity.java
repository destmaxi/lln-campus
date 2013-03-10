package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.Database;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * FIXME Code duplique avec LLNCampusActivity, voir comment eviter ca
 * @author Damien
 */
public abstract class LLNCampusListActivity extends ListActivity {
	protected Database db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        LLNCampus.setContext(this);
		this.db = LLNCampus.getDatabase();
		super.onCreate(savedInstanceState);
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
    	db.close(); 
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
