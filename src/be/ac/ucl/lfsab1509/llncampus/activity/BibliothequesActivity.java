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

public class BibliothequesActivity extends LLNCampusActivity implements
		OnItemClickListener, OnClickListener {
	private GridView bibliothequesListView;
	private BibliothequesListAdapter bibliothequesListAdapter;
	private ArrayList<Bibliotheque> bibliothequesList;

	private int currentBibliothequePos = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("currentBibPos", currentBibliothequePos);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (bibliothequesList == null) {
			bibliothequesList = Bibliotheque.getBibliothequesList();
		}
		currentBibliothequePos = savedInstanceState.getInt("currentBibPos");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		currentBibliothequePos = position;
		updateDisplay();

	}

	private void updateDisplay() {
		int orientation = this.getResources().getConfiguration().orientation;
		Log.d("BibActi", "currentBib : " + getCurrentBibliotheque()
				+ " - Orientation : " + orientation);

		View detailsView = (View) findViewById(R.id.bibliotheque_details);
		bibliothequesListView.setVisibility(View.VISIBLE);
		if (getCurrentBibliotheque() != null) {
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

			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			if (orientation != Configuration.ORIENTATION_LANDSCAPE) {
				bibliothequesListView.setVisibility(View.GONE);
				detailsView.getLayoutParams().width = width;
			} else {
				bibliothequesListView.getLayoutParams().width = (int) (0.35 * width);
				detailsView.getLayoutParams().width = (int) (0.65 * width);
			}
			detailsView.setVisibility(View.VISIBLE);
		} else {
			detailsView.setVisibility(View.GONE);
		}

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		updateDisplay();
	}

	private Bibliotheque getCurrentBibliotheque() {
		if (currentBibliothequePos == -1) {
			return null;
		}
		return bibliothequesList.get(currentBibliothequePos);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.bibliotheque_details_button_navigation:
			intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?daddr="
							+ getCurrentBibliotheque().getLatitude() + ","
							+ getCurrentBibliotheque().getLongitude() + "&dirflg=w"));
			intent.setComponent(new ComponentName(
					"com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity"));
			startActivity(intent);
			break;
		case R.id.bibliotheque_details_button_horaire:
			ExternalAppUtility.openBrowser(this,
					getCurrentBibliotheque().getScheduleUrl());
			break;
		}
	}

}
