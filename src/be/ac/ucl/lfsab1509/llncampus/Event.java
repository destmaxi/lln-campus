package be.ac.ucl.lfsab1509.llncampus;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.text.format.Time;

/**
 * Decrit un evenement (de ADE, ou ...).
 * @author damien
 *
 */
public class Event {
	private Time begin;
	private Time end;
	private HashMap<String, String> details;
	/**
	 * Constructeur.
	 * @param date Date of begin
	 * @param beginTime Hour of begin
	 * @param duration Duration
	 */
	public Event(final String date, final String beginTime, final String duration) {
		this.begin = new Time();
		this.end = new Time();
		try {
			setBegin(date, beginTime);
			setDuration(duration);
		} catch (NumberFormatException e) {
			e.printStackTrace(); //TODO
		}
		details = new HashMap<String, String>();
	}
	/**
	 * Constructeur.
	 * @param l (en miliseconde depuis epoch [1 jan 1970])
	 * @param m (en miliseconde depuis epoch [1 jan 1970])
	 */
	public Event(final long l, final long m){
		this.begin = new Time();
		this.begin.set(l);
		this.end = new Time();
		this.end.set(m);
		details = new HashMap<String, String>();
	}
	
	/**
	 * Ajouter des details a l'evenement
	 * @param key
	 * @param value
	 */
	public final void addDetail(final String key, final String value) {
		details.put(key, value);
	}
	/**
	 * 
	 * @param date Date
	 * @param beginT Time
	 */
	private void setBegin(final String date, final String beginT) {
		int day = Integer.valueOf(date.substring(3, 5));
		int month = Integer.valueOf(date.substring(0, 2)) - 1;
		int year = Integer.valueOf(date.substring(6, 10));
		
		int beginHour = Integer.valueOf(beginT.substring(0, 2));
		int beginMin = Integer.valueOf(beginT.substring(3, 5));
		
		begin.set(0, beginMin, beginHour, day, month, year);
	}
	/**
	 * Define the duration
	 * @param duration Duration
	 */
	private void setDuration(final String duration) {
		int i;
		String d = duration;
		int endHour = this.begin.hour;
		int endMin = this.begin.minute;
		i = d.indexOf('h');
		if (i != -1) {
			endHour += Integer.valueOf(d.substring(0, i));
			d = d.substring(i + 1);
		}
		i = d.indexOf("min");
		if (i != -1) {
			endMin += Integer.valueOf(d.substring(0, i));
		}
		if (endMin >= 60) {
			endHour++;
			endMin-= 60;
		}
		
		end.set(0, endMin, endHour, this.begin.monthDay, this.begin.month, this.begin.year);
	}
	/**
	 * Retourne le detail demande ou null si le detail n'existe pas .
	 * @param key Key of the detail
	 * @return le detail demande ou null si le detail n'existe pas
	 */
	public final String getDetail(final String key) { return details.get(key); }
	
	/**
	 * Retourne la date/heure de début.
	 * @return The time of begin
	 */
	public final Time getBeginTime() {
		return begin;
	}
	
	/**
	 * Retourne la date/heure de fin.
	 * @return The time of end
	 */
	public final Time getEndTime() {
		return end;
	}
	
	/**
	 * To string @see Object.toString
	 */
	public final String toString() {
		String detailsTxt = "";
		for (String key : details.keySet()) {
			detailsTxt += key + " : " + details.get(key) + "\n";
		}
		return "Date : " + 
					begin.format("Le %d/%m/%Y de %H:%M") + 
					" à " + end.format("%H:%M")+"\n"
				+ detailsTxt;
	}

	/**
	 * Pour l'insertion dans la table Horaire. 
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public final ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put("TIME_BEGIN", begin.toMillis(false));
		cv.put("TIME_END", end.toMillis(false));
		
		for ( String key : this.details.keySet()) {
			cv.put(key.toUpperCase(), this.details.get(key));
		}
		return cv;
	}
}