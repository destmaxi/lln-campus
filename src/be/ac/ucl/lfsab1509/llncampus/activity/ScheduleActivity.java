package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.llncampus.ADE;
import be.ac.ucl.lfsab1509.llncampus.Coordinates;
import be.ac.ucl.lfsab1509.llncampus.Course;
import be.ac.ucl.lfsab1509.llncampus.Event;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.activity.adapter.EventListAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
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
 * Activity intended to show the schedule of the user.
 * Related with schedule.xml.
 */
public class ScheduleActivity extends LLNCampusActivity implements OnDateChangeListener, 
	OnItemClickListener {
	
	private CalendarView calendarView;
	
	private final Handler handler = new Handler();
	final Runnable updateRunnable = new Runnable() {
		public void run() {
			updateEventsAndCourses();
		}
	};

	private ArrayList<Course> coursesList;
	private ArrayList<Event> allEvents;
	private ArrayList<Event> selectedDateEvents;
	private Time currentDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);
		     
        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener(this);
       	this.currentDate = new Time();
       	currentDate.setToNow();
       	coursesList = Course.getList();
       	
       	// If the courses list is empty, show a explanation message and propose to fill it.
       	if (coursesList.isEmpty()) {
       		AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.empty_course_list_dialog_title))
            	.setMessage(getString(R.string.empty_course_list_dialog_text));
            builder.setNeutralButton(getString(R.string.course_list_edit),
            		new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							handler.post(new Runnable(){
								public void run(){
									startCourseListEditActivity();
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
       	updateEventsAndCourses();
	}
	

	/**
	 * Update the events and the courses lists from the database.
	 */
	private void updateEventsAndCourses() {
		coursesList = Course.getList();
		this.allEvents = Event.getList();
		updateViewInfo();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onSelectedDayChange(CalendarView cv, int y, int m, int d) {
		this.currentDate.set(d, m, y);
		calendarView = cv;
		updateViewInfo();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.findItem(R.id.ade_update).setVisible(true);
		menu.findItem(R.id.course_list_edit).setVisible(true);
		return true;
	}
	

	/**
	 * Update view information, and more especially the events of the selected date. 
	 */
	private void updateViewInfo() {
		ListView eventListView = (ListView) findViewById(R.id.event_list);
		this.selectedDateEvents = new ArrayList<Event>();
		for (Event e : allEvents) {
			if (e.getBeginTime().monthDay == this.currentDate.monthDay 
					&& e.getBeginTime().month == this.currentDate.month
					&& e.getBeginTime().year == this.currentDate.year) {
				selectedDateEvents.add(e);
			}
		}
		EventListAdapter eventListAdapter = new EventListAdapter(this, selectedDateEvents);
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
				startCourseListEditActivity();
				break;
		}
		return true;
	}
	
	/**
	 * Start the course list edit activity.
	 */
	private void startCourseListEditActivity() {
		Intent intent = new Intent(this, CourseListEditActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Start the course list edit activity and automatically update the user courses list
	 * from UCLouvain.
	 */
	private void startUpdateFromUCLouvainActivity() {
		Intent intent = new Intent(this, CourseListEditActivity.class);
		intent.putExtra(EXTRA_START_UCLOUVAIN, true);
		startActivity(intent);
	}
	
	@Override
	public void onResume() {
		// If the courses list was updated, the ADE update will be launched automatically.
		if (Course.listChanged() && Course.getList().size() > 0) {
			Course.setListChangeSeen();
			ADE.runUpdateADE(this, handler, updateRunnable);
		} else {
			updateEventsAndCourses();
		}
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		final Event event = selectedDateEvents.get(position);
		Intent intent = new Intent(this, EventDetailsActivity.class);
		// Put data if we want to erase.
		intent.putExtra(EXTRA_ACTIVITY_NAME, event.getDetail(Event.ACTIVITY_NAME));
		intent.putExtra(EXTRA_BEGIN_TIME, event.getBeginTime().toMillis(false));
		intent.putExtra(EXTRA_END_TIME, event.getEndTime().toMillis(false));
		// Put data for GPS.
		intent.putExtra(EXTRA_DETAILS, event.toString());
		Coordinates coordinates = event.getCoordinates();
		if (coordinates != null) {
			intent.putExtra(EXTRA_COORDINATES, true);
			intent.putExtra(EXTRA_LATITUDE, coordinates.getLatitude());
			intent.putExtra(EXTRA_LONGITUDE, coordinates.getLongitude());
		}
		startActivity(intent);
	}
}