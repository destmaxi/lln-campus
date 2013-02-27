package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.Database;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class LLNCampusActivity extends Activity{
	protected Database db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        LLNCampus.setContext(this);
		this.db = LLNCampus.getDatabase();
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void onResume() {
		db.open();
		super.onResume();
	}
	@Override
    public void onPause() {
    	super.onPause();
    	db.close(); 
    }

	/*
	 * Affiche une alerte à l'utilisateur.
	 */
	public void alert(String title, String msg){
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(title);
		adb.setMessage(msg);
		adb.setPositiveButton(android.R.string.ok, null);
		adb.show();
	}
	public void alert(int title_res, int msg_res){
		alert(getString(title_res), getString(msg_res));
	}
	
    /*
     * Affiche une notification à l'utilisateur.
     */
    public void notify(String text){
    	Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
    	t.setGravity(Gravity.CENTER,0,0);
    	t.show(); 	
    }
    public void notify(int text){
    	Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
    	t.setGravity(Gravity.CENTER,0,0);
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
		switch(menuItem.getItemId()) {
			case R.id.menu_settings : 
				intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				break;
			case R.id.menu_resetDB: // FIXME : Dispo en mode developpement uniquement
				db.close();
				db.reset();
				db.open();
				notify("La base de donnée a été rechargée");
				break;
			case R.id.menu_about : 
				intent = new Intent(this, AboutActivity.class);
				startActivity(intent);
				break;
			default:
				return super.onOptionsItemSelected(menuItem);
		}
		return true;
	}

}
