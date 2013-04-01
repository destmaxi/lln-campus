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

/**
 * Classe abstraite d'activite permettant d'avoir un menu commun et des methodes
 * utiles pour les differentes activites
 * 
 * @author Damien
 */
public abstract class LLNCampusActivity extends Activity {
	/**
	 * Variable referencant la base de donnee de l'application
	 */
	protected Database db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LLNCampus.setContext(this);
		this.db = LLNCampus.getDatabase();
		super.onCreate(savedInstanceState);
		db.open();
		editActionBar();
	}
	protected void editActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onResume() {
		if (!db.isOpen()) {
			db.open();
		}
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
		case R.id.menu_resetDB: // FIXME : Dispo en mode developpement
								// uniquement
			db.close();
			db.reset();
			db.open();
			notify("La base de donnée a été rechargée");
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
