package be.ac.ucl.lfsab1509.llncampus.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
 * Class intended to show some information about the culture card.
 * Related with culture.xml.
 * */
public class CultureActivity extends LLNCampusActivity implements OnClickListener {
	/** The URL for the culture card. */
	private static final String URL_CULTURE = "http://www.carteculture.be/";	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.culture);
		setThisOnClickListener(R.id.button_culture_site);
	}
	
	private void setThisOnClickListener(int btnId) {
		Button button = (Button) findViewById(btnId);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Resources resources = getResources();
		Intent intent = new Intent(this, WebviewActivity.class);
		switch (v.getId()) {
		case R.id.button_culture_site:
				intent.putExtra(EXTRA_TITLE, resources.getString(R.string.culture));
				intent.putExtra(EXTRA_URL, URL_CULTURE);
				startActivity(intent);
				break;	
		default:
			notify(resources.getString(R.string.todo));
		}
	}

}
