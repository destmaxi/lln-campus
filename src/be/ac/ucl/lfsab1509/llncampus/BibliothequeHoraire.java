package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.database.Cursor;
import android.text.format.Time;
import android.util.Log;

public class BibliothequeHoraire {

	private final int bibID;
	private ArrayList<PlageHoraire> plagesHoraires = null;

	public BibliothequeHoraire(int bibID) {
		this.bibID = bibID;
	}

	private String getJour(int i){
		switch (i) {
		case 0 : return "Lundi";
		case 1 : return "Mardi";
		case 2 : return "Mercredi";
		case 3 : return "Jeudi";
		case 4: return "Vendredi";
		case 5 : return "Samedi";
		case 6 : return "Dimanche";
		default:
			return "Unknown day";
		}
	}

	/**
	 * Indique si on se trouve dans une plage horaire d'ouverture.
	 * 
	 * @return true si la bibliotheque est ouverte, false sinon.
	 */
	public boolean isOpen() {
		if (plagesHoraires == null) {
			loadPlagesHoraires();
		}
		
		Calendar now = Calendar.getInstance();
		int actualWeekday = (now.get(Calendar.DAY_OF_WEEK) - 1 + 5)%6;
		int actualHourMin = (now.get(Calendar.HOUR) * 60 + now.get(Calendar.MINUTE));
		for (PlageHoraire ph : plagesHoraires) {
			if (ph.day == actualWeekday && ph.beginTime <= actualHourMin && ph.endTime > actualHourMin) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Retourne l'horaire complet de la bibliotheque.
	 * 
	 * @return L'horaire complet.
	 */
	public String toString() {
		if (plagesHoraires == null) {
			loadPlagesHoraires();
		}
		String str = "";
		int precDay = -1;
		for (PlageHoraire ph : plagesHoraires) {
			if (ph.day != precDay) {
				str += getJour(ph.day) + " : ";
			} else {
				str += "\t\t";
			}
			str += ph.getBeginTime() + " - " + ph.getEndTime() + "\n";
			precDay = ph.day;
		}
				
		return str;
	}


	private void loadPlagesHoraires() {
		plagesHoraires = new ArrayList<PlageHoraire>();
		Cursor c = LLNCampus.getDatabase().select("Bibliotheque_Horaire",
				new String[] { "DAY", "BEGIN_TIME", "END_TIME" },
				"BUILDING_ID = ?", new String[] { String.valueOf(bibID) },
				null, null, "DAY ASC", null);
		while (c.moveToNext()) {
			plagesHoraires.add(new PlageHoraire(c.getInt(0), c.getInt(1), c
					.getInt(2)));
		}

	}

	private static class PlageHoraire {
		public int day;
		public int beginTime;
		public int endTime;

		public PlageHoraire(int day, int beginTime, int endTime) {
			this.day = day;
			this.beginTime = beginTime;
			this.endTime = endTime;
		}

		public String getBeginTime() {
			return getStringTime(beginTime);
		}

		public String getEndTime() {
			return getStringTime(endTime);
		}

		private static String getStringTime(int time) {
			int hour = (int) Math.floor(time / 60);
			int min = time - hour * 60;
			if (min == 0) {
				return hour + "h"; 
			} 
			if (min < 10) {
				return hour + "h0" + min;
			}
			return hour + "h" + min;
		}

	}
}
