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

public class LoisirsActivity extends LLNCampusActivity implements
		OnClickListener {

	private static final String URL_SPORT = "http://fmserver2.sipr.ucl.ac.be/Ucl_Sport/recordlist.php";
	private static final String URL_CINE = "http://www.cinescope.be/fr/louvain-la-neuve/accueil/";
	private static final String URL_CARPESTUDENTEM = "http://www.carpestudentem.org/pub/agenda.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loisirs);
		Resources r = this.getResources();

		setThisOnClickListener(R.id.button_cinema);
		setThisOnClickListener(R.id.button_carpestudentem);
		setThisOnClickListener(R.id.button_sports);
		setThisOnClickListener(R.id.button_restaurants);
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
			startActivity(intent);
			break;
		default:
			notify(r.getString(R.string.todo));
		}
	}
}
