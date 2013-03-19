package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.llncampus.ADE;
import be.ac.ucl.lfsab1509.llncampus.Event;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;

public class HoraireActivity extends LLNCampusActivity implements OnDateChangeListener {
	private TextView txtview;
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
	private Time actualDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.horaire);
		txtview = (TextView) this.findViewById(R.id.horaire_txt);
		     
        calendarView=(CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
       	this.actualDate = new Time();
       	actualDate.setToNow();

        updateInfos();
        
	}

	public void updateInfos(){
		this.events = new ArrayList<Event>();
		Cursor c = 	db.select(
						"Horaire", 
						new String[]{"COURSE", "TIME_BEGIN", "TIME_END", "TRAINEES", "TRAINERS", "ROOM", "ACTIVITY_NAME"}, 
						null, null, null, null, "TIME_BEGIN ASC", null);
		while(c.moveToNext()){
			Event e = new Event(c.getLong(1), c.getLong(2));
			e.addDetail("course", c.getString(0));			
			e.addDetail("trainees", c.getString(3));
			e.addDetail("trainers", c.getString(4));
			e.addDetail("room", c.getString(5));
			e.addDetail("activity_name", c.getString(6));
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
		this.actualDate.set(d,m,y);
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
		txtview.setText(infos);
        calendarView.setDate(actualDate.toMillis(false));

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
}