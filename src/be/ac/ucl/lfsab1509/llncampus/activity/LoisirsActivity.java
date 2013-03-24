package be.ac.ucl.lfsab1509.llncampus.activity;



import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.fragment.LoisirListFragment;
import be.ac.ucl.lfsab1509.llncampus.fragment.LoisirsDetailsFragment;


public class LoisirsActivity extends LLNCampusActivity implements LoisirListFragment.OnCategorySelectedListener, OnClickListener {
	ArrayList<String> values = null;
	private String current_category;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loisirs_list_fragment);
				 
		View vue = findViewById(R.id.loisirs_list_fragment);
		vue.setBackgroundColor(getResources().getColor(R.color.Blue)); 
	}
	

	protected void onListItemClick(ListView l, View v, int position, long id) {
		// effect of the buttons is defined in the fragment and onCategorySelected
    }
	
	public void onCategorySelected(String cat){
		Intent intent;
		current_category = cat;
		
		if (current_category == "Cinema") {
			LoisirsDetailsFragment viewer = (LoisirsDetailsFragment) getFragmentManager()
					.findFragmentById(R.id.loisirs_details_fragment);
			if (viewer == null || !viewer.isInLayout()) {
				intent = new Intent(getApplicationContext(), LoisirsDetails.class);
				intent.putExtra("NAME", current_category);
				startActivity(intent);
			} else {
				viewer.updateLoisir(current_category);
				setListeners();
			}
		}
		
		else {
			intent = new Intent(this, TodoActivity.class);
			intent.putExtra("NAME", current_category);
			startActivity(intent);
		}

	}
	
	/*
	 * Methods used when the tablet is in landscape.
	 */
	private void setListeners() {
		if (current_category == "Cinema") {
			View webButton = findViewById(R.id.website);
		    webButton.setOnClickListener(this);
		}
	}

	public void onClick(View v) {
		final String URL = "http://www.cinescope.be/fr/louvain-la-neuve/accueil/";
		switch (v.getId()) {
			case R.id.website:
				ExternalAppUtility.openBrowser(LoisirsActivity.this, URL);
				break;
		}
	} 
}
