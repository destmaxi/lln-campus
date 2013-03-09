package be.ac.ucl.lfsab1509.llncampus.activity;

import be.ac.ucl.lfsab1509.llncampus.ADE;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class HoraireActivity extends LLNCampusActivity implements OnClickListener {
	TextView txtview; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.horaire);
		txtview = (TextView) this.findViewById(R.id.horaire_txt);
		View updateButton = findViewById(R.id.updateADE);
        updateButton.setOnClickListener(this);
        showInfos();
	}

	public void showInfos(){
		String infos = "";
		Cursor c = 	db.select(
						"Horaire", 
						new String[]{"COURSE", "DATE_BEGIN", "DATE_END", "TRAINEES", "TRAINERS", "ROOM", "ACTIVITY_NAME"}, 
						null, null, null, null, null, null);
		while(c.moveToNext()){
			infos = infos + "Nom du cours : " + c.getString(0) + "\n"
					+ "Date de début : " + c.getString(1) + "\n"
					+ "Date de fin : " + c.getString(2) + "\n"
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
			ADE.runUpdateADE(this);
			break;
		}
	}
}