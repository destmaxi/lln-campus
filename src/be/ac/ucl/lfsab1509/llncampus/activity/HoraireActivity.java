package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.llncampus.ADE;
import be.ac.ucl.lfsab1509.llncampus.Cours;
import be.ac.ucl.lfsab1509.llncampus.Event;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.activity.adapter.EventListAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

public class HoraireActivity extends LLNCampusActivity implements OnDateChangeListener, OnItemClickListener {
	private CalendarView calendarView;
	
	//create an handler
	private final Handler handler = new Handler();
	final Runnable updateRunnable = new Runnable() {
		public void run() {
			//call the activity method that updates the UI
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
	

	private void updateInfos() {
		coursList = Cours.getList();
		this.events = new ArrayList<Event>();
		Cursor c = db.sqlRawQuery(
				"SELECT " +
						"h.COURSE, " +
						"h.TIME_BEGIN, " +
						"h.TIME_END, " +
						"h.TRAINEES, " +
						"h.TRAINERS, " +
						"h.ROOM, " +
						"h.ACTIVITY_NAME, " +
						"c.NAME " +
				"FROM " +
					"Horaire as h, Courses as c " +
				"WHERE " +
					"h.COURSE = c.CODE " +
				"ORDER BY " +
					"TIME_BEGIN ASC");
		while (c.moveToNext()) {
			Event e = new Event(c.getLong(1), c.getLong(2));
			e.addDetail("course", c.getString(0));			
			e.addDetail("trainees", c.getString(3));
			e.addDetail("trainers", c.getString(4));
			e.addDetail("room", c.getString(5));
			e.addDetail("activity_name", c.getString(6));
			e.addDetail("title", c.getString(7));
			this.events.add(e);
		}
		c.close();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informations").setMessage(e.toString());
	    builder.setPositiveButton(android.R.string.ok, null);
	    AlertDialog dialog = builder.create();
        dialog.show();	        
		
	}
}