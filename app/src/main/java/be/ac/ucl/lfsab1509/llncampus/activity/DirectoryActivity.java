package be.ac.ucl.lfsab1509.llncampus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import be.ac.ucl.lfsab1509.llncampus.R;

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
 * Class that allows the user to do a search on the UCL staff.
 * Related with directory.xml.
 */
public class DirectoryActivity extends LLNCampusActivity implements OnClickListener {
	
	/** URL for the UCL directories. */
	public static final String URL_UCL_DIRECTORIES = 
			"http://www.uclouvain.be/repertoire-personnel.html";
	
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.directory);
		findViewById(R.id.button_directory_search).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_directory_search:
			Intent intent = new Intent(this, WebviewActivity.class);
			String lastName = (((EditText) findViewById(R.id.directory_last_name))
					.getText().toString().replace(' ', '+'));
			String firstName = (((EditText) findViewById(R.id.directory_first_name))
					.getText().toString().replace(' ', '+'));
			String entity = (((EditText) findViewById(R.id.directory_entity))
					.getText().toString().replace(' ', '+'));
			intent.putExtra(EXTRA_URL,
					URL_UCL_DIRECTORIES + "?nom=" + lastName + "&prenom=" + firstName
							+ "&output=detail&sigle=" + entity
							+ "&Envoi=Effectuer+la+recherche");
			intent.putExtra(EXTRA_TITLE, getTitle());
			intent.putExtra(EXTRA_CSS, "#menu, #header{display:none;}");
			startActivity(intent);
			break;
		}
	}
	
}
