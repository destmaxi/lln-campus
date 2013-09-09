package be.ac.ucl.lfsab1509.llncampus.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
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
 * Class intended to show some buttons to redirect to
 * leisures activities
 * Related with loisirs.xml
 * */
public class LoisirsActivity extends LLNCampusActivity implements
		OnClickListener {

	private static final String URL_SPORT = "http://fmserver2.sipr.ucl.ac.be/Ucl_Sport/recordlist.php";
	private static final String URL_CINE = "http://www.cinescope.be/fr/louvain-la-neuve/accueil/";
	private static final String URL_CARPESTUDENTEM = "http://www.carpestudentem.org/pub/agenda.php";
	private static final String URL_SABLON = "http://www.uclouvain.be/278074.html";
	private static final String URL_GALILEE = "http://www.uclouvain.be/278075.html";
	private static final String URL_DUN_PAIN_A_LAUTRE = "http://www.uclouvain.be/276940.html";	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loisirs);
		// Resources r = this.getResources();

		setThisOnClickListener(R.id.button_cinema);
		setThisOnClickListener(R.id.button_carpestudentem);
		setThisOnClickListener(R.id.button_sports);
		setThisOnClickListener(R.id.button_sablon);
		setThisOnClickListener(R.id.button_galilee);
		setThisOnClickListener(R.id.button_dun_pain_a_lautre);
		setThisOnClickListener(R.id.button_solidaire);
		setThisOnClickListener(R.id.button_culture);
	}

	private void setThisOnClickListener(int btnId) {
		Button tmp = (Button) findViewById(btnId);
		tmp.setOnClickListener(this);
	}

	/**
	 * Au chargement du layout et au changement d'orientation : Redéfinit les
	 * dimensions des boutons et des images pour occuper toute la place de
	 * manière constante.
	 */
	@Override
	public final void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		/** Afin d'avoir une largeur ou une hauteur optimale **/
		GridLayout gl = (GridLayout) findViewById(R.id.loisirs_list);
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

	@Override
	public void onClick(View v) {
		Resources r = getResources();
		Intent intent = new Intent(this, WebviewActivity.class);
		switch (v.getId()) {
		case R.id.button_cinema:
			intent.putExtra("TITLE", r.getString(R.string.cinema));
			intent.putExtra("URL", URL_CINE);
			intent.putExtra(
					"CSS",
					"#NewsLetter, #BandTitel, #StaticSocials, #HeaderWrapper, #HeaderLogo, #ContainerFooter, #ContentTeaserAndPubWrapper, #MovieScroller, #ContentTeaserAndPubBg {display:none;} body{background:#FFF;} #fullVisualBg{ background:transparent; padding:0;}");
			startActivity(intent);
			break;
		case R.id.button_sports:
			intent.putExtra("TITLE", r.getString(R.string.sports));
			intent.putExtra("URL", URL_SPORT);
			startActivity(intent);
			break;
		case R.id.button_carpestudentem:
			intent.putExtra("TITLE", r.getString(R.string.carpestudentem));
			intent.putExtra("URL", URL_CARPESTUDENTEM);
			intent.putExtra("CSS", "#account_bar, #header, #menu, #nav, #footer, #unique_column h4{ display:none; }");
			startActivity(intent);
			break;
		case R.id.button_sablon:
			intent.putExtra("TITLE", r.getString(R.string.sablon));
			intent.putExtra("URL", URL_SABLON);
			intent.putExtra(
					"CSS",
					"#menu, #header{display:none;}");
			startActivity(intent);
			break;
		case R.id.button_galilee:
			intent.putExtra("TITLE", r.getString(R.string.galilee));
			intent.putExtra("URL", URL_GALILEE);
			intent.putExtra(
					"CSS",
					"#menu, #header{display:none;}");
			startActivity(intent);
			break;					
		case R.id.button_dun_pain_a_lautre:
				intent.putExtra("TITLE", r.getString(R.string.dun_pain_a_lautre));
				intent.putExtra("URL", URL_DUN_PAIN_A_LAUTRE);
				intent.putExtra(
						"CSS",
						"#menu, #header{display:none;}");
				startActivity(intent);
				break;	
		case R.id.button_solidaire:
			intent = new Intent(this, SolidaireActivity.class);
			startActivity(intent);
			break;
		case R.id.button_culture:
			intent = new Intent(this, CultureActivity.class);
			startActivity(intent);
		default:
			notify(r.getString(R.string.todo));
		}
	}
}
