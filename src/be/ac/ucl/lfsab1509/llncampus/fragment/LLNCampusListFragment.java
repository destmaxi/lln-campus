package be.ac.ucl.lfsab1509.llncampus.fragment;

import be.ac.ucl.lfsab1509.llncampus.Database;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import android.app.ListFragment;
import android.os.Bundle;

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
 * Class that offers some useful functionnalities for ListFragment
 */
public class LLNCampusListFragment extends ListFragment{
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
