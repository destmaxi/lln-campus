package be.ac.ucl.lfsab1509.llncampus.activity;

import java.io.File;

import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
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
 * Class that will be executed when starting the application. Related with the
 * XML main_title.xml
 * 
 */
public class MainActivity extends LLNCampusActivity implements OnClickListener {

	// Defining the URL used in the code
	private static final String ICAMPUS_URL = "https://www.uclouvain.be/cnx_icampus.html";
	private static final String MOODLE_URL = "https://www.uclouvain.be/cnx_moodle.html";
	private static final String BUREAU_UCL_URL = "http://www.uclouvain.be/onglet_bureau.html?";
	private static final String MAP_NAME = "plan_lln.pdf";

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		setListeners();
		getActionBar().setDisplayHomeAsUpEnabled(false);

		new Thread(new Runnable() {
			public void run() {
				LLNCampus.copyAssets();
			}
		}).start();
	}

	/**
	 * Au chargement du layout et au changement d'orientation : Redéfinit les
	 * dimensions des boutons et des images pour occuper toute la place de
	 * manière constante.
	 */
	@Override
	public final void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// get/set the size here.
		/** Afin d'avoir une largeur ou une hauteur optimale **/

		GridLayout gl = (GridLayout) findViewById(R.id.maingridlayout);
		Button buttontmp;

		// Stretch buttons
		int idealChildWidth = (int) (gl.getWidth() / gl.getColumnCount());
		int idealChildHeight = (int) ((gl.getHeight()) / gl.getRowCount());
		for (int i = 0; i < gl.getChildCount(); i++) {
			buttontmp = (Button) gl.getChildAt(i);
			buttontmp.setWidth(idealChildWidth);
			buttontmp.setHeight(idealChildHeight);
			Drawable[] drawables = buttontmp.getCompoundDrawables();
			Drawable d;
			for (int j = 0; j < drawables.length; j++) {
				if ((d = drawables[j]) != null) {
					d.setBounds(0, 0, (int) (d.getIntrinsicWidth() * 0.9),
							(int) (d.getIntrinsicHeight() * 0.9));
					ScaleDrawable sd = new ScaleDrawable(d, 0, (float) 1,
							(float) 1);
					switch (j) {
					case 0:
						buttontmp.setCompoundDrawables(sd.getDrawable(), null,
								null, null);
						break;
					case 1:
						buttontmp.setCompoundDrawables(null, sd.getDrawable(),
								null, null);
						break;
					case 2:
						buttontmp.setCompoundDrawables(null, null,
								sd.getDrawable(), null);
						break;
					case 3:
						buttontmp.setCompoundDrawables(null, null, null,
								sd.getDrawable());
						break;
					}

				}
			}
		}
	}

	/**
	 * Définit les listeners sur les boutons.
	 */
	private void setListeners() {
		findViewById(R.id.button_loisirs).setOnClickListener(this);
		findViewById(R.id.button_horaire).setOnClickListener(this);
		findViewById(R.id.button_auditoire).setOnClickListener(this);
		findViewById(R.id.button_bibliotheque).setOnClickListener(this);
		findViewById(R.id.button_map).setOnClickListener(this);
		findViewById(R.id.button_repertoire).setOnClickListener(this);
		findViewById(R.id.button_icampus).setOnClickListener(this);
		findViewById(R.id.button_moodle).setOnClickListener(this);
		findViewById(R.id.button_bureau).setOnClickListener(this);
	}
	
	@Override
	public final void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button_loisirs:
			intent = new Intent(this, LoisirsActivity.class);
			startActivity(intent);
			break;
		case R.id.button_horaire:
			intent = new Intent(this, HoraireActivity.class);
			startActivity(intent);
			break;
		case R.id.button_auditoire:
			intent = new Intent(this, AuditoriumActivity.class);
			startActivity(intent);
			break;
		case R.id.button_bibliotheque:
			intent = new Intent(this, BibliothequesActivity.class);
			startActivity(intent);
			break;
		case R.id.button_map:

			// Uses the tablet's default PDF reader. Starts MapActivity if none
			// is found.
			
			try {
				Intent intentUrl = new Intent(Intent.ACTION_VIEW);

				Uri url = Uri.fromFile(new File("/"
						+ Environment.getExternalStorageDirectory().getPath()
						+ "/" + LLNCampus.LLNREPOSITORY + "/" + MAP_NAME));
				intentUrl.setDataAndType(url, "application/pdf");
				intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentUrl);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(
						this,
						this.getString(R.string.no_pdf_player),
						Toast.LENGTH_LONG).show();
				intent = new Intent(this, MapActivity.class);
				startActivity(intent);
			}
			

			break;
		case R.id.button_repertoire:
			intent = new Intent(this, RepertoireActivity.class);
			startActivity(intent);
			break;
		case R.id.button_icampus:
			ExternalAppUtility.openBrowser(MainActivity.this, ICAMPUS_URL);
			break;
		case R.id.button_moodle:
			ExternalAppUtility.openBrowser(MainActivity.this, MOODLE_URL);
			break;
		case R.id.button_bureau:
			ExternalAppUtility.openBrowser(MainActivity.this, BUREAU_UCL_URL);
			break;
		}

	}
}
