package be.ac.ucl.lfsab1509.llncampus.activity;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.llncampus.ADE;
import be.ac.ucl.lfsab1509.llncampus.Event;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class HoraireActivity extends LLNCampusActivity implements OnClickListener {
	private static final int notifyID = 1;
	TextView txtview; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.horaire);
		txtview = (TextView) this.findViewById(R.id.horaire_txt);
		View updateButton = findViewById(R.id.updateADE);
        updateButton.setOnClickListener(this);
	}

	protected void updateADE() {
		final NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		final Builder nb = new NotificationCompat.Builder(this)
			.setContentTitle("Mise a jour de ADE")
			.setContentText("Mise a jour en cours")
			.setSmallIcon(android.R.drawable.stat_notify_sync);		
		nm.notify(notifyID, nb.build());
		super.onStart();
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<Event> events;
				if ((events = ADE.getInfos("LFSAB1509")) != null) {
					nb.setContentText("Termine");
					txtview.setText(events.toString());
				} else {
					nb.setContentText("Une erreur s'est produite");
				}
				nm.notify(notifyID, nb.build());
			}
		}).start();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.updateADE:
			updateADE();
			break;
		// TODO Auto-generated method stub
		}
	}
}