package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.llncampus.ADE;
import be.ac.ucl.lfsab1509.llncampus.Event;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.activity.adapter.EventListAdapter;
import android.app.AlertDialog;
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
	//private TextView txtview;
	private CalendarView calendarView;
	
	//create an handler
	private final Handler handler = new Handler();
	final Runnable updateRunnable = new Runnable() {
		public void run() {
			//call the activity method that updates the UI
			updateInfos();
		}
	};

	private ArrayList<Event> events;
	private ArrayList<Event> currentEvents;
	private Time currentDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.horaire);
		//txtview = (TextView) this.findViewById(R.id.horaire_txt);
		     
        calendarView=(CalendarView) findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener(this);
       	this.currentDate = new Time();
       	currentDate.setToNow();

       	updateInfos();
	}

	public void updateInfos(){
		this.events = new ArrayList<Event>();
		/*Cursor c = 	db.select(
						"Horaire", 
						new String[]{"COURSE", "TIME_BEGIN", "TIME_END", "TRAINEES", "TRAINERS", "ROOM", "ACTIVITY_NAME"}, 
						null, null, null, null, "TIME_BEGIN ASC", null);*/
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
		while(c.moveToNext()){
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
		/*
		String infos =  "+-------------------------------------+\n"
					  + "|         COURS POUR LE " 
					  		+ this.actualDate.format("%d/%m/%Y") 
					  							   + "         |\n"
					  + "+-------------------------------------+\n";
		for (Event e : this.events) {
			if (e.getBeginTime().monthDay == this.actualDate.monthDay 
					&& e.getBeginTime().month == this.actualDate.month
					&& e.getBeginTime().year == this.actualDate.year) {
				infos += "Cours : "+e.getDetail("course") + "\n"
						+ "Heure : " 
							+ "De " + e.getBeginTime().format("%H:%M") 
							+ " à " + e.getEndTime().format("%H:%M") + "\n"
						+ "Prof : " + e.getDetail("trainers") + "\n"
						+ "Lieu : " + e.getDetail("room") + "\n"
						+ "Etudiants concernes : " + e.getDetail("trainees") + "\n"
						+ "------------ \n";
			}
		}
		//txtview.setText(infos);*/
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
				Intent intent = new Intent(this, CoursListEditActivity.class);
				startActivity(intent);
		}
		return true;
	}
	@Override
	public void onResume() {
		updateInfos();
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