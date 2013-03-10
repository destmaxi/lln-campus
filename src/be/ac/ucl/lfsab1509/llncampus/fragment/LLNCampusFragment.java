package be.ac.ucl.lfsab1509.llncampus.fragment;

import be.ac.ucl.lfsab1509.llncampus.Database;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import android.app.Fragment;
import android.os.Bundle;


public class LLNCampusFragment extends Fragment{
	protected Database db;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
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


}
