package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.ADE;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class HoraireActivity extends LLNCampusActivity implements OnClickListener {
	TextView txtview; 
	
	//create an handler
	private final Handler handler = new Handler();
	final Runnable updateRunnable = new Runnable() {
		public void run() {
			//call the activity method that updates the UI
			updateInfos();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.horaire);
		txtview = (TextView) this.findViewById(R.id.horaire_txt);
		View updateButton = findViewById(R.id.updateADE);
        updateButton.setOnClickListener(this);
        updateInfos();
	}

	public void updateInfos(){
		String infos = "";
		Cursor c = 	db.select(
						"Horaire", 
						new String[]{"COURSE", "TIME_BEGIN", "TIME_END", "TRAINEES", "TRAINERS", "ROOM", "ACTIVITY_NAME"}, 
						null, null, null, null, "TIME_BEGIN ASC", null);
		while(c.moveToNext()){
			Time begin = new Time();
			begin.set(c.getLong(1));
			Time end = new Time();
			end.set(c.getLong(2));
			
			infos = infos + "Code du cours : " + c.getString(0) + "\n"
					+ "Date  : " + begin.format("%d/%m/%Y de %H:%M") + " à " + end.format("%H:%M") + "\n"
					+ "Trainees : " + c.getString(3) + "\n"
					+ "Trainers : " + c.getString(4) + "\n"
					+ "Room : " + c.getString(5) + "\n"
					+ "Activity name : " + c.getString(6) + "\n"
					+ "------------ \n";
		}
		c.close();
		txtview.setText(infos);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.updateADE:
			ADE.runUpdateADE(this, handler, updateRunnable);
			break;
		}
	}
}