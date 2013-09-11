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
 * culture activity
 * Related with culture.xml
 * */
public class CultureActivity extends LLNCampusActivity implements OnClickListener{
	private static final String CULTURE = "http://www.carteculture.be/";	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.culture);
		
		setThisOnClickListener(R.id.button_culture_site);
	}
	
	private void setThisOnClickListener(int btnId) {
		Button tmp = (Button) findViewById(btnId);
		tmp.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Resources r = getResources();
		Intent intent = new Intent(this, WebviewActivity.class);
		switch (v.getId()) {
		case R.id.button_culture_site:
				intent.putExtra("TITLE", r.getString(R.string.culture));
				intent.putExtra("URL", CULTURE);
				//intent.putExtra("CSS", "#menu, #header{display:none;}");
				startActivity(intent);
				break;	
		default:
			notify(r.getString(R.string.todo));
		}
	}

}
