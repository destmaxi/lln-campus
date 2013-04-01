package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import be.ac.ucl.lfsab1509.llncampus.Bibliotheque;
import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.activity.adapter.BibliothequesListAdapter;

/**
 * Activité pour l'affichage de la liste et des détails pour les bibliothèques.
 */
public class BibliothequesActivity extends LLNCampusActivity implements
		OnItemClickListener, OnClickListener {
	private GridView bibliothequesListView;
	private BibliothequesListAdapter bibliothequesListAdapter;
	private ArrayList<Bibliotheque> bibliothequesList;

	private int currentBibliothequePos = -1;

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bibliotheques);
		if (bibliothequesList == null) {
			bibliothequesList = Bibliotheque.getBibliothequesList();
		}
		bibliothequesListView = (GridView) findViewById(R.id.bibliotheque_list_view);
		bibliothequesListAdapter = new BibliothequesListAdapter(this,
				bibliothequesList);
		bibliothequesListView.setAdapter(bibliothequesListAdapter);
		bibliothequesListView.setOnItemClickListener(this);

		Button gpsBtn = (Button) findViewById(R.id.bibliotheque_details_button_navigation);
		gpsBtn.setOnClickListener(this);
		Button horaireBtn = (Button) findViewById(R.id.bibliotheque_details_button_horaire);
		horaireBtn.setOnClickListener(this);
	}

	@Override
	public final void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("currentBibPos", currentBibliothequePos);
	}

	@Override
	public final void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (bibliothequesList == null) {
			bibliothequesList = Bibliotheque.getBibliothequesList();
		}
		currentBibliothequePos = savedInstanceState.getInt("currentBibPos");
	}

	@Override
	public final void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		currentBibliothequePos = position;
		updateDisplay();
	}

	/**
	 * Met à jour l'affichage.
	 */
	private void updateDisplay() {
		// Vue pour le détails
		View detailsView = (View) findViewById(R.id.bibliotheque_details);

		// On met la liste des bibliothèque visible par défaut.
		bibliothequesListView.setVisibility(View.VISIBLE);

		if (getCurrentBibliotheque() != null) {
			// Si l'utilisateur a choisis une bibliothèque

			// ... on rend la vue de détail visible
			detailsView.setVisibility(View.VISIBLE);

			// ... et on complète les infos.
			TextView bibName = (TextView) findViewById(R.id.bibliotheque_details_name);
			bibName.setText(getCurrentBibliotheque().getName() + " ("
					+ getCurrentBibliotheque().getSigle() + ")");
			ImageView bibImg = (ImageView) findViewById(R.id.bibliotheque_details_img);
			bibImg.setImageResource(getCurrentBibliotheque().getImg());
			TextView bibAddress = (TextView) findViewById(R.id.bibliotheque_details_address);
			bibAddress.setText(getCurrentBibliotheque().getAddress());
			TextView bibHoraire = (TextView) findViewById(R.id.bibliotheque_details_horaire);
			bibHoraire
					.setText(getCurrentBibliotheque().getHoraire().toString());

			// Orientation de l'écran
			int orientation = this.getResources().getConfiguration().orientation;

			// On récupère la largeur de l'écran.
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;

			if (orientation != Configuration.ORIENTATION_LANDSCAPE) {
				// Si on est pas en mode paysage, alors on masque la liste et on
				// affiche les détails sur toute la largeur.
				bibliothequesListView.setVisibility(View.GONE);
				detailsView.getLayoutParams().width = width;
			} else {
				// Si on est en mode paysage, on affiche la liste sur 35% de
				// l'écran.
				bibliothequesListView.getLayoutParams().width = (int) (0.35 * width);
				detailsView.getLayoutParams().width = (int) (0.65 * width);
			}
		} else {
			// Sinon on masque la vue des détails.
			detailsView.setVisibility(View.GONE);
		}

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// Dès que l'affichage est changé (changement d'orientation / lancement
		// de l'activité) on met à jour l'affichage.
		updateDisplay();
	}

	/**
	 * Fournit la bibliothèque courante.
	 * 
	 * @return Bibliothèque courante ou null si on est dans la liste.
	 */
	private Bibliotheque getCurrentBibliotheque() {
		if (currentBibliothequePos == -1) {
			return null;
		}
		return bibliothequesList.get(currentBibliothequePos);
	}

	@Override
	public final void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.bibliotheque_details_button_navigation:
			intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?daddr="
							+ getCurrentBibliotheque().getLatitude() + ","
							+ getCurrentBibliotheque().getLongitude()
							+ "&dirflg=w"));
			intent.setComponent(new ComponentName(
					"com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity"));
			startActivity(intent);
			break;
		case R.id.bibliotheque_details_button_horaire:
			ExternalAppUtility.openBrowser(this, getCurrentBibliotheque()
					.getScheduleUrl());
			break;
		}
	}

	@Override
	public final void onBackPressed() {
		if (currentBibliothequePos != -1
				&& this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
			currentBibliothequePos = -1;
			updateDisplay();
		} else {
			super.onBackPressed();
		}
	}

}
