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
 * Class that allows the user to make research about
 * the UCL staff
 * Related with repertoire.xml
 */
public class RepertoireActivity extends LLNCampusActivity implements
		OnClickListener {
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.repertoire);
		findViewById(R.id.repertoire_searchbtn).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.repertoire_searchbtn:
			Intent intent = new Intent(this, WebviewActivity.class);
			String nom = (((EditText) findViewById(R.id.repertoire_nom))
					.getText().toString().replace(' ', '+'));
			String prenom = (((EditText) findViewById(R.id.repertoire_prenom))
					.getText().toString().replace(' ', '+'));
			String entite = (((EditText) findViewById(R.id.repertoire_entite))
					.getText().toString().replace(' ', '+'));
			intent.putExtra("URL",
					"http://www.uclouvain.be/repertoire-personnel.html"
							+ "?nom=" + nom + "&prenom=" + prenom
							+ "&output=detail&sigle=" + entite
							+ "&Envoi=Effectuer+la+recherche");
			intent.putExtra("TITLE", getTitle());
			intent.putExtra("CSS", "#menu, #header{display:none;}");
			startActivity(intent);
			break;
		}
	}
	
}
