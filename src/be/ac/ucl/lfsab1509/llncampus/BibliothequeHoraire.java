package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;

public class BibliothequeHoraire {
	private final int bibID;
	private ArrayList<PlageHoraire> plagesHoraires = null;

	/**
	 * Constructeur.
	 * 
	 * @param bibID
	 *            Id de la bibliothèque.
	 */
	public BibliothequeHoraire(int bibID) {
		this.bibID = bibID;
	}

	/**
	 * Fournit le jour de la semaine sous forme d'un string (lundi, mardi,...).
	 * 
	 * @param i
	 *            Jour de la semaine sous forme numérique (0 = lundi, 1 = mardi,
	 *            ...)
	 * @return Jour de la semaine
	 * @todo Mettre les string dans strings.xml
	 */
	private String getJour(int i) {
		Resources r = LLNCampus.getContext().getResources();
		switch (i) {
		case 0:
			return r.getString(R.string.lundi);
		case 1:
			return r.getString(R.string.mardi);
		case 2:
			return r.getString(R.string.mercredi);
		case 3:
			return r.getString(R.string.jeudi);
		case 4:
			return r.getString(R.string.vendredi);
		case 5:
			return r.getString(R.string.samedi);
		case 6:
			return r.getString(R.string.dimanche);
		default:
			Log.e("BibliothequeHoraire.java", "Numero de jour inconnu : " + i);
			return "Unknown day";
		}
	}

	/**
	 * Indique si on se trouve dans une plage horaire d'ouverture.
	 * 
	 * @return true si la bibliotheque est ouverte, false sinon.
	 */
	public final boolean isOpen() {
		if (plagesHoraires == null) {
			loadPlagesHoraires();
		}

		Calendar now = Calendar.getInstance();
		int actualWeekday = (now.get(Calendar.DAY_OF_WEEK) - 1 + 6) % 7;
		int actualHourMin = (now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE));
		for (PlageHoraire ph : plagesHoraires) {
			if (ph.getDay() == actualWeekday
					&& ph.getBeginTime() <= actualHourMin
					&& ph.getEndTime() > actualHourMin) {
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
	public final String toString() {
		if (plagesHoraires == null) {
			loadPlagesHoraires();
		}
		String str = "";
		int precDay = -1;
		for (PlageHoraire ph : plagesHoraires) {
			if (ph.getDay() != precDay) {
				str += getJour(ph.getDay()) + " : ";
			} else {
				str += "\t\t";
			}
			str += ph.getBeginTimeStr() + " - " + ph.getEndTimeStr() + "\n";
			precDay = ph.getDay();
		}

		return str;
	}

	/**
	 * Charge les plages horaires de la bibliothèque considérée.
	 */
	private void loadPlagesHoraires() {
		plagesHoraires = new ArrayList<PlageHoraire>();
		Cursor c = LLNCampus.getDatabase().select("Bibliotheque_Horaire",
				new String[] {"DAY", "BEGIN_TIME", "END_TIME"},
				"BUILDING_ID = ?", new String[] {String.valueOf(bibID)},
				null, null, "DAY ASC", null);
		while (c.moveToNext()) {
			plagesHoraires.add(new PlageHoraire(c.getInt(0), c.getInt(1), c
					.getInt(2)));
		}

	}

	private static class PlageHoraire {
		private int day;
		private int beginTime;
		private int endTime;

		/**
		 * Constructeur.
		 * 
		 * @param day
		 *            Jour de la semaine (0=lundi)
		 * @param beginTime
		 *            Heure d'ouverture sous la forme : Heure*60 + Minutes
		 * @param endTime
		 *            Heure de fermeture sous la forme Heure*60 + Minutes
		 */
		public PlageHoraire(int day, int beginTime, int endTime) {
			this.day = day;
			this.beginTime = beginTime;
			this.endTime = endTime;
		}

		/**
		 * Fournit le jour de la semaine (0=lundi).
		 * 
		 * @return Jour de la semaine.
		 */
		public int getDay() {
			return day;
		}

		/**
		 * Fournit l'heure d'ouverture.
		 * 
		 * @return Heure sous la forme : heure*60+minutes.
		 */
		public int getBeginTime() {
			return beginTime;
		}

		/**
		 * Fournit l'heure de fermeture.
		 * 
		 * @return Heure sous la forme : heure*60+minutes.
		 */
		public int getEndTime() {
			return endTime;
		}

		/**
		 * Fournit l'heure d'ouverture.
		 * 
		 * @return Heure sous la forme d'un string HHhMM ou HHh.
		 */
		public String getBeginTimeStr() {
			return getStringTime(beginTime);
		}

		/**
		 * Fournit l'heure de fermeture.
		 * 
		 * @return Heure sous la forme d'un string HHhMM ou HHh.
		 */
		public String getEndTimeStr() {
			return getStringTime(endTime);
		}

		/**
		 * Convertit une heure sous la forme (Heure*60+Minutes) en String sous
		 * la forme HHhMM ou HHh.
		 * 
		 * @param time
		 *            (Heure*60+minutes) à convertir
		 * @return String sous la forme HHhMM ou HHh.
		 */
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
