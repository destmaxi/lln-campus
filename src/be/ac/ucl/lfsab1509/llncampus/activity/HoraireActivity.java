package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.llncampus.ADE;
import be.ac.ucl.lfsab1509.llncampus.Coordinates;
import be.ac.ucl.lfsab1509.llncampus.Cours;
import be.ac.ucl.lfsab1509.llncampus.Event;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.activity.adapter.EventListAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.CalendarView.OnDateChangeListener;

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
 * Activity intended to show the schedule of a student
 * Related with horaire.xml
 */
public class HoraireActivity extends LLNCampusActivity implements OnDateChangeListener, OnItemClickListener {
	private CalendarView calendarView;
	
	private final Handler handler = new Handler();
	final Runnable updateRunnable = new Runnable() {
		public void run() {
			updateInfos();
		}
	};

	private ArrayList<Cours> coursList;
	private ArrayList<Event> events;
	private ArrayList<Event> currentEvents;
	private Time currentDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.horaire);
		     
        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener(this);
       	this.currentDate = new Time();
       	currentDate.setToNow();
       	coursList = Cours.getList();
       	
       	/*
       	 * Si la liste des cours est vide, affiche un message d'explication et propose de la completer.
       	 */
       	if (coursList.isEmpty()) {
       		AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.emptyCoursListDialogTitle)).setMessage(getString(R.string.emptyCoursListDialogText));
            builder.setNeutralButton(getString(R.string.course_list_edit),
            		new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							handler.post(new Runnable(){
								public void run(){
									startCoursListEditActivity();
								}
							});
						}
					});
            builder.setPositiveButton(getString(R.string.update_from_uclouvain),
            		new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							handler.post(new Runnable(){
								public void run(){
									startUpdateFromUCLouvainActivity();
								}
							});
						}
					});
            builder.setCancelable(true);
            AlertDialog dialog = builder.create();
            dialog.show();
       	}

       	updateInfos();
	}
	

	/**
	 * Met à jour la liste des évènements depuis la base de données.
	 */
	private void updateInfos() {
		coursList = Cours.getList();
		this.events = Event.getList();
		updateViewInfos();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onSelectedDayChange(CalendarView cv, int y, int m,
			int d) {
		this.currentDate.set(d,m,y);
		
		calendarView = cv;
		updateViewInfos();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.findItem(R.id.ade_update).setVisible(true);
		menu.findItem(R.id.course_list_edit).setVisible(true);
		return true;
	}
	

	private void updateViewInfos() {
		//eventList = Cours.getList();
		ListView eventListView = (ListView) findViewById(R.id.event_list);
		this.currentEvents = new ArrayList<Event>();
		for (Event e : events) {
			if (e.getBeginTime().monthDay == this.currentDate.monthDay 
					&& e.getBeginTime().month == this.currentDate.month
					&& e.getBeginTime().year == this.currentDate.year) {
				currentEvents.add(e);
			}
		}
		EventListAdapter eventListAdapter = new EventListAdapter(this, currentEvents);
		eventListView.setAdapter(eventListAdapter);
		eventListView.setOnItemClickListener(this);

        calendarView.setDate(currentDate.toMillis(false));

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		super.onOptionsItemSelected(menuItem);
		switch(menuItem.getItemId()) {
			case R.id.ade_update:
				ADE.runUpdateADE(this, handler, updateRunnable);
				break;
			case R.id.course_list_edit:
				startCoursListEditActivity();
				break;
		}
		return true;
	}
	
	private void startCoursListEditActivity() {
		Intent intent = new Intent(this, CoursListEditActivity.class);
		startActivity(intent);
	}
	private void startUpdateFromUCLouvainActivity() {
		Intent intent = new Intent(this, CoursListEditActivity.class);
		intent.putExtra("startUCLouvain", true);
		startActivity(intent);
	}
	@Override
	public void onResume() {
		/** 
		 * Si la liste des cours a été mise à jour, on lance la mise a jour
		 * depuis ADE de manière automatique. 
		 */
		if (Cours.listChanged()) {
			Cours.setListChangeSeen();
			ADE.runUpdateADE(this, handler, updateRunnable);
		} else {
			updateInfos();
		}
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		final Event e = currentEvents.get(position);
		Intent i = new Intent(this, CourseDetailsActivity.class);
		// Put data if we want to erase
		i.putExtra("ACTIVITY_NAME", e.getDetail("activity_name"));
		i.putExtra("BEGIN_TIME", e.getBeginTime().toMillis(false));
		i.putExtra("END_TIME", e.getEndTime().toMillis(false));
		// Put data for GPS
		i.putExtra("DETAILS", e.toString());
		Coordinates c = e.getCoordinates();
		Log.d("HoraireActivity", "Coordonnées : "+c);
		if (c != null) {
			i.putExtra("COORDINATES", true);
			i.putExtra("LATITUDE", c.getLat());
			i.putExtra("LONGITUDE", c.getLon());
		}
		startActivity(i);
	}
}